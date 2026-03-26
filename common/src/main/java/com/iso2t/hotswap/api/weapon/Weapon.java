package com.iso2t.hotswap.api.weapon;

import net.minecraft.world.item.ItemStack;

public record Weapon(ItemStack stack, float attackDamage, int index, int itemDamage) {
}