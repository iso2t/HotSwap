package com.iso2t.hotswap.config.subs;

import com.iso2t.easyconfig.api.annotations.Comment;
import com.iso2t.easyconfig.api.value.wrappers.BooleanValue;
import com.iso2t.easyconfig.api.value.wrappers.EnumValue;
import com.iso2t.easyconfig.api.value.wrappers.IntegerValue;
import com.iso2t.easyconfig.api.value.wrappers.ListValue;
import com.iso2t.hotswap.config.defs.ArmorSwap;

import java.util.List;

public class ArmorSettings {

	@Comment("Enable armor swapping.")
	public BooleanValue ENABLED = BooleanValue.of(true);

	@Comment({"Enable swapping to elytra.",
			"This behavior is only valid if an elytra is worn and the player has an elytra in their inventory."})
	public BooleanValue ALLOW_ELYTRA = BooleanValue.of(true);

	@Comment({"Swap armor when it breaks/low durability.",
			"If set to LOW_DURABILITY, it will swap to the best first match with a higher durability."})
	public EnumValue<ArmorSwap> ARMOR_SWAP_BEHAVIOR = EnumValue.of(ArmorSwap.class, ArmorSwap.BREAK);

	@Comment({"Which tag groups to consider when swapping armor.",
			"Order does not matter."})
	public ListValue<String> TAGS = ListValue.of(List.of(
			"#minecraft:chest_armor",
			"#minecraft:leg_armor",
			"#minecraft:foot_armor",
			"#minecraft:head_armor"
	                                                       ));

	@Comment("Any item IDs to blacklist even if their tags match. IE: \"minecraft:diamond_leggings\" will never be swapped to if enabled in this list.")
	public ListValue<String> BLACKLIST = ListValue.of(List.of());

	@Comment("The slot IDs for the armor slots.")
	public IntegerValue HEAD_SLOT  = IntegerValue.of(39);
	public IntegerValue CHEST_SLOT = IntegerValue.of(38);
	public IntegerValue LEGS_SLOT = IntegerValue.of(37);
	public IntegerValue FEET_SLOT = IntegerValue.of(36);
}
