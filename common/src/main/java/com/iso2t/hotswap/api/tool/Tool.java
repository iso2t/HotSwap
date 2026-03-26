package com.iso2t.hotswap.api.tool;

import net.minecraft.world.item.ItemStack;

public record Tool(ItemStack stack, float destroySpeed, int index, int itemDamage) {
}
