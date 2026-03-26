package com.iso2t.hotswap.api.armor;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.iso2t.hotswap.HotSwap;
import com.iso2t.hotswap.api.ConfigChecks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArmorHelper {

	public static final List<TagKey<Item>> ARMOR_TAGS = ConfigChecks.TagHelper.convertToTags(HotSwap.CONFIG.ARMOR.TAGS.get());

	public static boolean isArmorItem (ItemStack stack) {
		// first check to prevent unneeded compute.
		if (!HotSwap.CONFIG.ARMOR.ENABLED.get()) return false;
		for (var tag : ARMOR_TAGS) {
			if (stack.is(tag)) return true;
		}
		return false;
	}

	public static void doArmorSwap (Player player, int slot, ItemStack broken) {
		if (!HotSwap.CONFIG.ARMOR.ENABLED.get()) return;
		if (ConfigChecks.ItemHelper.configBlacklistArmorPiece(broken.getItem())) return;

		if (broken.getItem() == Items.ELYTRA) {
			elytra(player, slot, broken);
			return;
		}

		ArrayList<Armor> validItems = new ArrayList<>();

		// find best armor piece for the slot
		for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
			var stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && stack != ItemStack.EMPTY) {
				if (ConfigChecks.TagHelper.getArmorType(stack) == ConfigChecks.TagHelper.getArmorType(broken)) {
					validItems.add(new Armor(stack, ConfigChecks.TagHelper.getArmorType(stack), stack.getMaxDamage() - stack.getDamageValue()));
				}
			}
		}

		System.out.println("Valid items: " + validItems);

		if (validItems.isEmpty()) return;

		System.out.println("Valid items: " + validItems.size());

		// sort the list by protection and durability
		sort(validItems);
		var bestItem = validItems.getFirst();

		if (bestItem == null) return;

		// swap the item
		if (bestItem.armorType() == ItemTags.HEAD_ARMOR) {
			player.getInventory().setItem(HotSwap.CONFIG.ARMOR.HEAD_SLOT.get(), bestItem.stack());
		} else if (bestItem.armorType() == ItemTags.CHEST_ARMOR) {
			player.getInventory().setItem(HotSwap.CONFIG.ARMOR.CHEST_SLOT.get(), bestItem.stack());
		} else if (bestItem.armorType() == ItemTags.LEG_ARMOR) {
			player.getInventory().setItem(HotSwap.CONFIG.ARMOR.LEGS_SLOT.get(), bestItem.stack());
		} else if (bestItem.armorType() == ItemTags.FOOT_ARMOR) {
			player.getInventory().setItem(HotSwap.CONFIG.ARMOR.FEET_SLOT.get(), bestItem.stack());
		}
	}

	private static void elytra (Player player, int slot, ItemStack broken) {
		if (!HotSwap.CONFIG.ARMOR.ALLOW_ELYTRA.get()) return;
		if (broken.isEmpty() || broken == ItemStack.EMPTY) return;
		if (ConfigChecks.ItemHelper.configBlacklistArmorPiece(broken.getItem())) return;

		/*ArrayList<Armor> validItems = new ArrayList<>();

		// find best armor piece for the slot
		for (int i = 0; i < 36; i++) {
			var stack = inventory.getItem(i);
			if (stack.isEmpty() || stack == ItemStack.EMPTY) return;
			if (ConfigChecks.TagHelper.getArmorType(stack) != ConfigChecks.TagHelper.getArmorType(broken)) return;
			validItems.add(new Armor(stack, ConfigChecks.TagHelper.getArmorType(stack), stack.getDamageValue(), HotSwap.CONFIG.ARMOR.CHEST_SLOT.get()));
		}

		if (validItems.isEmpty()) return;
		// sort the list by protection and durability
		sort(validItems);
		player.getInventory().setItem(HotSwap.CONFIG.ARMOR.CHEST_SLOT.get(), validItems.getFirst().stack());*/
	}

	private static void sort (ArrayList<Armor> list) {
		Comparator<Armor> comparator = Comparator.comparing(Armor::getProtection).thenComparing(Armor::durability).reversed();
		list.sort(comparator);
	}

}
