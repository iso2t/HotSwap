package com.iso2t.hotswap.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import com.iso2t.hotswap.HotSwap;
import com.iso2t.hotswap.api.ConfigChecks;
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
	private static long lastMineSwapMs = 0L;
	private static long lastAttackSwapMs = 0L;
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
		if (ConfigChecks.BlockHelper.configBlacklistMine(targetBlock.getBlock())) {
			if (heldKey && currentSelected >= 0) {
				player.getInventory().setSelectedSlot(currentSelected);
			}
			modified = false;
			oldBlock = null;
			return;
		}
		long cooldownMs = CONFIG.ACTIONS.MINE.COOLDOWN.get();
		if (cooldownMs > 0 && (System.currentTimeMillis() - lastMineSwapMs) < cooldownMs) return;
		if ((player.isCreative() == CONFIG.BASIC.ALLOW_IN_CREATIVE.get()) || CONFIG.BASIC.ALLOW_IN_SURVIVAL.get() || CONFIG.BASIC.ALLOW_IN_ADVENTURE.get()) {
			oldBlock = old.getBlock();
			if (!modified || oldBlock == targetBlock.getBlock() || oldBlock != null) {
				modified = true;
				if (oldBlock != targetBlock.getBlock() && !heldKey)
					currentSelected = player.getInventory().getSelectedSlot();
				newSelected = ToolHelper.getBestToolFor(targetBlock, player);
				player.getInventory().setSelectedSlot(newSelected);
				lastMineSwapMs = System.currentTimeMillis();
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
		long cooldownMs = CONFIG.ACTIONS.ATTACK.COOLDOWN.get();
		if (cooldownMs > 0 && (System.currentTimeMillis() - lastAttackSwapMs) < cooldownMs) return;
		currentSelected = player.getInventory().getSelectedSlot();
		newSelected = ToolHelper.getBestWeaponFor(player, target, CONFIG.ACTIONS.ATTACK.ALLOW_AXES_FOR_ATTACKING.get());
		player.getInventory().setSelectedSlot(newSelected);
		lastAttackSwapMs = System.currentTimeMillis();
	}

    /**
     * Handles the action of finishing a block-breaking event based on mouse input.
     *
     * @param event The mouse button event that triggered the method. Contains details about the mouse position and button state.
     * @param action Integer representing the modifier keys (e.g., Shift, Ctrl) pressed during the event.
     */
	public static void finishBlockBreak (MouseButtonEvent event, int action) {
		if (!allow || !enabled) return;
		if (Minecraft.getInstance().screen != null || Minecraft.getInstance().player == null) return;
		Player player = Minecraft.getInstance().player;

		var options = Minecraft.getInstance().options;
		var mouse = options.keyAttack.matchesMouse(event);

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
     * Toggles the mod's enabled state based on a key event and action.
     * This method evaluates the key event to determine if the toggle keybinding is triggered and changes the enabled state accordingly.
     * A system message is then sent to the player to reflect the current enabled/disabled status.
     *
     * @param event The key event that triggered this method. Contains information about the key code and modifiers.
     * @param action The action associated with the key event (e.g., press, release). Used to determine the context of the input.
     */
	public static void toggleOnOff (KeyEvent event, int action) {
		if (!allow) return;
		if (Minecraft.getInstance().screen != null || Minecraft.getInstance().player == null) return;

		Player player = Minecraft.getInstance().player;
		if (Keybindings.INSTANCE.toggle.matches(event) && action == InputConstants.PRESS) {
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
