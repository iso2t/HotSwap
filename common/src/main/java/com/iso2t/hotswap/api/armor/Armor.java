package com.iso2t.hotswap.api.armor;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record Armor(ItemStack stack, TagKey<Item> armorType, int durability) {

	public double getProtection () {
		double[] protection = { 0.0 };
		stack().forEachModifier(getArmorType(), (holder, modifier) -> {
			if (holder.value() == Attributes.ARMOR.value()) {
				protection[0] = modifier.amount();
			}
		});
		return protection[0];
	}

	private EquipmentSlot getArmorType () {
		return switch (armorType().location().getPath()) {
			case "helmet" -> EquipmentSlot.HEAD;
			case "chestplate" -> EquipmentSlot.CHEST;
			case "leggings" -> EquipmentSlot.LEGS;
			case "boots" -> EquipmentSlot.FEET;
			default -> EquipmentSlot.MAINHAND;
		};
	}

}
