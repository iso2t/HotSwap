package com.iso2t.hotswap.api.tool;

import com.iso2t.hotswap.HotSwap;
import com.iso2t.hotswap.api.ConfigChecks;
import com.iso2t.hotswap.api.weapon.Weapon;
import com.iso2t.hotswap.api.weapon.WeaponItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ToolHelper {

	public static int getBestToolFor (BlockState state, Player player) {
		String primaryGroup = getPrimaryMineGroup(state);
		var groups = HotSwap.CONFIG.ACTIONS.MINE.PRIORITY_ORDER.get();

		int selected = player.getInventory().getSelectedSlot();
		Integer best = findBestMineToolInGroups(player, state, selected, primaryGroup, groups);
		return best == null ? selected : best;
	}

	public static int getBestWeaponFor (Player player, Entity target, Boolean allowAxe) {
		var groups = HotSwap.CONFIG.ACTIONS.ATTACK.PRIORITY_ORDER.get();
		int selected = player.getInventory().getSelectedSlot();
		Integer best = findBestAttackWeaponInGroups(player, target, allowAxe, selected, groups);
		return best == null ? selected : best;
	}

	private static int bestTool (List<Tool> tools, int current) {
		Comparator<Tool> comparator = Comparator.comparing(Tool::destroySpeed).thenComparing(Tool::itemDamage).reversed();
		tools.sort(comparator);
		if (tools.getFirst() != null) {
			int best = tools.getFirst().index();
			tools.clear();
			return best;
		}
		tools.clear();
		return current;
	}

	private static int bestWeapon (List<Weapon> weapons, int current) {
		Comparator<Weapon> comparator = Comparator.comparing(Weapon::attackDamage).thenComparing(Weapon::itemDamage).reversed();
		weapons.sort(comparator);
		if (weapons.getFirst() != null) {
			int best = weapons.getFirst().index();
			weapons.clear();
			return best;
		}
		weapons.clear();
		return current;
	}

	public static float getDestroySpeed (ItemStack stack, BlockState state) {
		return stack.getDestroySpeed(state);
	}

	public static float getAttackDamage (ItemStack stack, Player player, Entity target) {
		WeaponItem weaponItem = new WeaponItem(stack, player, player.level().damageSources().playerAttack(player), target);
		return Math.max(weaponItem.getDamageValue(), 2.f);
	}

	private static boolean isViableWeapon (ItemStack stack, @Nullable Boolean allowAxe) {
		boolean axe = allowAxe != null && allowAxe;

		if (!ConfigChecks.ItemHelper.configAllowedAttack(stack.getItem())) return false;
		if (!ConfigChecks.TagHelper.anyMatchAttack(stack)) return false;

		return stack.is(getSwordItemTag()) || (stack.is(getAxeItemTag()) == axe) || (stack.is(ItemTags.WEAPON_ENCHANTABLE) && stack.getItem() instanceof AxeItem == axe);
	}

	private static String getPrimaryMineGroup (BlockState state) {
		if (state.is(getPickaxeBlockTag())) return "#minecraft:pickaxes";
		if (state.is(getAxeBlockTag())) return "#minecraft:axes";
		if (state.is(getShovelBlockTag())) return "#minecraft:shovels";
		if (state.is(getHoeBlockTag())) return "#minecraft:hoes";
		if (state.is(getSwordBlockTag()) || state.getBlock() == Blocks.COBWEB) return "#minecraft:swords";
		return null;
	}

	private static Integer findBestMineToolInGroups (Player player, BlockState state, int currentSelected, String primaryGroup, List<String> priorityOrder) {
		ArrayList<String> checkOrder = new ArrayList<>();
		if (primaryGroup != null && !primaryGroup.isBlank()) checkOrder.add(primaryGroup);
		if (priorityOrder != null) {
			for (String g : priorityOrder) {
				if (g == null || g.isBlank()) continue;
				if (primaryGroup != null && primaryGroup.equals(g)) continue;
				checkOrder.add(g);
			}
		}

		for (String group : checkOrder) {
			List<Tool> tools = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (ConfigChecks.ItemHelper.configToolBlacklist(stack.getItem())) continue;
				if (!isViable(stack, state)) continue;
				if (!matchesGroup(stack, group)) continue;
				tools.add(new Tool(stack, getDestroySpeed(stack, state), i, stack.getDamageValue()));
			}
			if (!tools.isEmpty()) return bestTool(tools, currentSelected);
		}
		return null;
	}

	private static Integer findBestAttackWeaponInGroups (Player player, Entity target, @Nullable Boolean allowAxe, int currentSelected, List<String> priorityOrder) {
		if (priorityOrder == null || priorityOrder.isEmpty()) {
			ArrayList<Weapon> weapons = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (ConfigChecks.ItemHelper.configWeaponBlacklist(stack.getItem())) continue;
				if (!isViableWeapon(stack, allowAxe)) continue;
				weapons.add(new Weapon(stack, getAttackDamage(stack, player, target), i, stack.getDamageValue()));
			}
			return weapons.isEmpty() ? null : bestWeapon(weapons, currentSelected);
		}

		for (String group : priorityOrder) {
			if (group == null || group.isBlank()) continue;
			ArrayList<Weapon> weapons = new ArrayList<>();
			for (int i = 0; i < 9; i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (ConfigChecks.ItemHelper.configWeaponBlacklist(stack.getItem())) continue;
				if (!isViableWeapon(stack, allowAxe)) continue;
				if (!matchesGroup(stack, group)) continue;
				weapons.add(new Weapon(stack, getAttackDamage(stack, player, target), i, stack.getDamageValue()));
			}
			if (!weapons.isEmpty()) return bestWeapon(weapons, currentSelected);
		}
		return null;
	}

	private static boolean matchesGroup (ItemStack stack, String group) {
		if (group == null || group.isBlank()) return true;
		if (group.startsWith("#")) {
			try {
				var tag = ConfigChecks.TagHelper.convertToTags(List.of(group)).getFirst();
				return stack.is(tag);
			} catch (Exception ignored) {
				return false;
			}
		}
		return group.equals(stack.getItem().getDescriptionId());
	}

	private static boolean isViable (ItemStack stack, BlockState state) {
		if (ConfigChecks.ItemHelper.configBlacklistMine(stack.getItem())) return false;
		if (ConfigChecks.BlockHelper.configBlacklistMine(state.getBlock())) return false;

		if (state.is(getPickaxeBlockTag())) {
			return ConfigChecks.TagHelper.anyMatchMine(stack);
		} else if (state.is(getAxeBlockTag())) {
			return ConfigChecks.TagHelper.anyMatchMine(stack);
		} else if (state.is(getShovelBlockTag())) {
			return ConfigChecks.TagHelper.anyMatchMine(stack);
		} else if (state.is(getHoeBlockTag())) {
			return ConfigChecks.TagHelper.anyMatchMine(stack);
		} else if (state.is(getSwordBlockTag()) || state.getBlock() == Blocks.COBWEB) {
			return stack.is(getSwordItemTag());
		}
		return false;
	}

	public static double attackSpeed (ItemStack stack, Player player, Entity target) {
		WeaponItem item = new WeaponItem(stack, player, player.level().damageSources().playerAttack(player), target);
		return item.getSpeedValue();
	}

	public static TagKey<Block> getPickaxeBlockTag () {
		return BlockTags.MINEABLE_WITH_PICKAXE;
	}

	public static TagKey<Item> getPickaxeItemTag () {
		return ItemTags.PICKAXES;
	}

	public static TagKey<Block> getAxeBlockTag () {
		return BlockTags.MINEABLE_WITH_AXE;
	}

	public static TagKey<Item> getAxeItemTag () {
		return ItemTags.AXES;
	}

	public static TagKey<Block> getShovelBlockTag () {
		return BlockTags.MINEABLE_WITH_SHOVEL;
	}

	public static TagKey<Item> getShovelItemTag () {
		return ItemTags.SHOVELS;
	}

	public static TagKey<Block> getHoeBlockTag () {
		return BlockTags.MINEABLE_WITH_HOE;
	}

	public static TagKey<Item> getHoeItemTag () {
		return ItemTags.HOES;
	}

	public static TagKey<Block> getSwordBlockTag () {
		return BlockTags.SWORD_EFFICIENT;
	}

	public static TagKey<Item> getSwordItemTag () {
		return ItemTags.SWORDS;
	}

}
