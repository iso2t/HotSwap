package com.iso2t.hotswap.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import com.iso2t.hotswap.HotSwap;
import com.iso2t.hotswap.api.armor.ArmorHelper;
import com.iso2t.hotswap.api.tool.ToolHelper;
import com.iso2t.hotswap.config.ModConfig;
import com.iso2t.hotswap.keybinding.Keybindings;

import static com.iso2t.hotswap.HotSwap.IS_ALPHA;

/**
 * This class handles the events for HotSwap.
 * It is used to swap the item in the player's hand when breaking blocks or attacking entities.
 */
public class HotSwapEvents {

	private static final ModConfig CONFIG = HotSwap.CONFIG;

	private static boolean enabled = true;
	private static int currentSelected = -1;
	private static int newSelected = -1;
	private static boolean modified = false;
	private static boolean heldKey = false;
	private static boolean allow = true;
	private static Block oldBlock = null;

	/**
	 * Called when the player starts breaking a block.
	 * @param player The player breaking the block
	 * @param old The old block state
	 * @param targetBlock The target block state
	 */
	public static void onBeginBreak (Player player, BlockState old, BlockState targetBlock) {
		if (!allow || !enabled || !CONFIG.ACTIONS.MINE.ENABLED.get()) return;
		if ((player.isCreative() == CONFIG.BASIC.ALLOW_IN_CREATIVE.get()) || CONFIG.BASIC.ALLOW_IN_SURVIVAL.get() || CONFIG.BASIC.ALLOW_IN_ADVENTURE.get()) {
			oldBlock = old.getBlock();
			if (!modified || oldBlock == targetBlock.getBlock() || oldBlock != null) {
				modified = true;
				if (oldBlock != targetBlock.getBlock() && !heldKey)
					currentSelected = player.getInventory().getSelectedSlot();
				newSelected = ToolHelper.getBestToolFor(targetBlock, player);
				player.getInventory().setSelectedSlot(newSelected);
			}
		}
	}

	/**
	 * Called when the player attacks an entity.
	 * @param player The player attacking the entity
	 * @param target The target entity
	 */
	public static void attackEntity (Player player, Entity target) {
		if (!allow || !CONFIG.BASIC.ALLOW_FOR_ATTACKING.get() || !CONFIG.ACTIONS.ATTACK.ENABLED.get() || !enabled) return;
		currentSelected = player.getInventory().getSelectedSlot();
		newSelected = ToolHelper.getBestWeaponFor(player, target, CONFIG.ACTIONS.ATTACK.ALLOW_AXES_FOR_ATTACKING.get());
		player.getInventory().setSelectedSlot(newSelected);
	}

	/**
	 * Called when the player finishes breaking a block.
	 * @param button The button pressed
	 * @param action The action performed
	 */
	public static void finishBlockBreak (int button, int action) {
		if (!allow || !enabled) return;
		if (Minecraft.getInstance().screen != null || Minecraft.getInstance().player == null) return;
		Player player = Minecraft.getInstance().player;

		var options = Minecraft.getInstance().options;
		var mouse = options.keyAttack.getDefaultKey().getValue() == button && options.keyAttack.getDefaultKey().getType() == InputConstants.Type.MOUSE;

		if (mouse && action == InputConstants.PRESS) {
			heldKey = true;
			currentSelected = player.getInventory().getSelectedSlot();
		}

		if (CONFIG.BASIC.KEEP_LAST.get()) return;
		if (mouse && action == InputConstants.RELEASE) {
			player.getInventory().setSelectedSlot(currentSelected);
			modified = false;
			heldKey = false;
		}
	}

	/**
	 * Called when the player toggles the mod on or off.
	 * @param button The button pressed
	 * @param action The action performed
	 */
	public static void toggleOnOff (int button, int action) {
		if (!allow) return;
		if (Minecraft.getInstance().screen != null || Minecraft.getInstance().player == null) return;

		Player player = Minecraft.getInstance().player;
		if (Keybindings.INSTANCE.toggle.getDefaultKey().getValue() == button && Keybindings.INSTANCE.toggle.getDefaultKey().getType() == InputConstants.Type.KEYSYM && action == InputConstants.PRESS) {
			enabled = !enabled;
			player.sendSystemMessage(Component.translatable("chat.hotswap.toggleEnable", getEnabled(enabled)));
		}
	}

	/**
	 * Called when the player holds off the key.
	 */
	public static void holdOff () {
		allow = !Keybindings.INSTANCE.preventSwitch.isDown();
	}

	/**
	 * Called when the player joins the game.
	 * @param entity The player entity
	 */
	public static void playerJoin (Entity entity) {
		if (!(entity instanceof Player player)) return;
		currentSelected = player.getInventory().getSelectedSlot();

		if (IS_ALPHA && !CONFIG.DEBUG.IGNORE_ALPHA_MESSAGE.get()) {
			player.sendSystemMessage(Component.literal("§l§4WARNING: §rHotSwap is in alpha. Bugs should be expected."));
		}
	}

	/**
	 * Called when the player breaks an item.
	 * @param player The player breaking the item
	 */
	public static void armorBreak (Player player, ItemStack stack, float damageToApply) {
		if (!allow || !enabled) return;
		/*System.out.println("Armor break event");

		if (ArmorHelper.isArmorItem(broken)) {
			System.out.println("Is armor item");
			ArmorHelper.doArmorSwap(player, slot, broken);
		}*/

		int inventorySlot;

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			inventorySlot = slot.getIndex(Inventory.INVENTORY_SIZE);
			if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
			if (stack.isEmpty()) continue;                  // no piece there
			int remainingDur   = stack.getMaxDamage() - stack.getDamageValue();

			if (damageToApply >= remainingDur) {
				ArmorHelper.doArmorSwap(player, inventorySlot, stack);
				return;
			}
		}
	}

	/**
	 * Overlay chat component for toggling the mod on or off.
	 * @param bool The boolean value to display
	 * @return The chat component
	 */
	public static Component getEnabled (boolean bool) {
		return Component.translatable("chat.hotswap.toggleEnable." + bool);
	}

}
