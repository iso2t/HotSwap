package com.iso2t.hotswap.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import com.iso2t.hotswap.HotSwap;

import java.util.List;
import java.util.Optional;

/**
 * For converting tags from the Config into actual TagKeys.
 */
public class ConfigChecks {

	public static class TagHelper {
		public static boolean anyMatchMine (ItemStack stack) {
			return anyMatch(stack, convertToTags(HotSwap.CONFIG.ACTIONS.MINE.TAGS.get()));
		}

		public static boolean anyMatchAttack (ItemStack stack) {
			return anyMatch(stack, convertToTags(HotSwap.CONFIG.ACTIONS.ATTACK.TAGS.get()));
		}

		@SafeVarargs
		public static boolean anyMatch (ItemStack stack, TagKey<Item>... entry) {
			return anyMatch(stack, List.of(entry));
		}

		@SafeVarargs
		public static boolean anyMatch (BlockState block, TagKey<Block>... entry) {
			return anyMatch(block, List.of(entry));
		}

		public static boolean anyMatch (ItemStack stack, List<TagKey<Item>> entry) {
			for (var tag : entry) {
				if (stack.is(tag)) {
					return true;
				}
			}
			return false;
		}

		public static boolean anyMatch (BlockState block, List<TagKey<Block>> entry) {
			for (var tag : entry) {
				if (block.is(tag)) {
					return true;
				}
			}
			return false;
		}

		public static List<TagKey<Item>> convertToTags (List<String> tags) {
			return tags.stream().map(ConfigChecks.TagHelper::convertToItemTag).toList();
		}

		public static List<TagKey<Block>> convertToBlockTags (List<String> tags) {
			return tags.stream().map(ConfigChecks.TagHelper::convertToBlockTag).toList();
		}

		private static TagKey<Item> convertToItemTag (String tag) {
			if (tag.startsWith("#")) {
				tag = tag.substring(1);
			}
			String[] parts = tag.split(":");
			return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(parts[0], parts[1]));
		}

		private static TagKey<Block> convertToBlockTag (String tag) {
			if (tag.startsWith("#")) {
				tag = tag.substring(1);
			}
			String[] parts = tag.split(":");
			return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(parts[0], parts[1]));
		}

		public static TagKey<Item> getArmorType (ItemStack stack) {
			if (stack.isEmpty()) return null;
			if (stack.is(ItemTags.HEAD_ARMOR)) return ItemTags.HEAD_ARMOR;
			if (stack.is(ItemTags.CHEST_ARMOR)) return ItemTags.CHEST_ARMOR;
			if (stack.is(ItemTags.LEG_ARMOR)) return ItemTags.LEG_ARMOR;
			if (stack.is(ItemTags.FOOT_ARMOR)) return ItemTags.FOOT_ARMOR;
			return null;
		}

	}

	public static class ItemHelper {
		public static Optional<Item> getItem (String id) {
			if (id.startsWith("#")) {
				id = id.substring(1);
			}
			String[] parts = id.split(":");
			return BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath(parts[0], parts[1]));
		}

		public static boolean configBlacklistMine (Item item) {
			if (item == null) return false;
			return HotSwap.CONFIG.ACTIONS.MINE.TOOL_BLACKLIST.get().contains(item.getDescriptionId());
		}

		public static boolean configAllowedAttack (Item item) {
			if (item == null) return false;
			return !HotSwap.CONFIG.ACTIONS.ATTACK.BLACKLIST.get().contains(item.getDescriptionId());
		}

		public static boolean configBlacklistArmorPiece (Item item) {
			if (item == null) return false;
			return HotSwap.CONFIG.ARMOR.BLACKLIST.get().contains(item.getDescriptionId());
		}

		public static boolean configToolBlacklist (Item item) {
			if (item == null) return false;
			return HotSwap.CONFIG.ACTIONS.MINE.TOOL_BLACKLIST.get().contains(item.getDescriptionId());
		}

		public static boolean configWeaponBlacklist (Item item) {
			if (item == null) return false;
			return HotSwap.CONFIG.ACTIONS.ATTACK.BLACKLIST.get().contains(item.getDescriptionId());
		}
	}

	public static class BlockHelper {
		public static Optional<Block> getBlock (String id) {
			if (id.startsWith("#")) {
				id = id.substring(1);
			}
			String[] parts = id.split(":");
			return BuiltInRegistries.BLOCK.getOptional(Identifier.fromNamespaceAndPath(parts[0], parts[1]));
		}

		public static BlockState getBlockState (String id) {
			if (id.startsWith("#")) {
				id = id.substring(1);
			}
			String[] parts = id.split(":");
			return BuiltInRegistries.BLOCK.getOptional(Identifier.fromNamespaceAndPath(parts[0], parts[1])).map(Block::defaultBlockState).orElse(null);
		}

		public static boolean configBlacklistMine (Block block) {
			if (block == null) return false;
			var b = HotSwap.CONFIG.ACTIONS.MINE.BLACKLIST.get().contains(block.toString());
			HotSwap.logAction("Checking block: " + block.getDescriptionId());
			HotSwap.logAction("Is blacklisted: " + b);
			return b;
		}
	}

}
