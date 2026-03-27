package com.iso2t.hotswap.config.subs;

import com.iso2t.configmanager.annotations.Comment;
import com.iso2t.configmanager.annotations.Config;
import com.iso2t.configmanager.value.wrappers.BooleanValue;
import com.iso2t.configmanager.value.wrappers.EnumValue;
import com.iso2t.configmanager.value.wrappers.IntegerValue;
import com.iso2t.configmanager.value.wrappers.ListValue;
import com.iso2t.hotswap.config.defs.ArmorSwap;

import java.util.List;

@Config(name = "armor_settings")
public class ArmorSettings {

	@Comment("Enable armor swapping.")
	public BooleanValue ENABLED = new BooleanValue(true);

	@Comment({"Enable swapping to elytra.",
			"This behavior is only valid if an elytra is worn and the player has an elytra in their inventory."})
	public BooleanValue ALLOW_ELYTRA = new BooleanValue(true);

	@Comment({"Swap armor when it breaks/low durability.",
			"If set to LOW_DURABILITY, it will swap to the best first match with a higher durability.",
			"Possible values: \"BREAK\" | \"LOW_DURABILITY\""})
	public EnumValue<ArmorSwap> ARMOR_SWAP_BEHAVIOR = new EnumValue<>(ArmorSwap.class, ArmorSwap.BREAK);

	@Comment({"Which tag groups to consider when swapping armor.",
			"Order does not matter."})
	public ListValue<String> TAGS = new ListValue<>(List.of(
			"#minecraft:chest_armor",
			"#minecraft:leg_armor",
			"#minecraft:foot_armor",
			"#minecraft:head_armor"
	));

	@Comment("Any item IDs to blacklist even if their tags match. IE: \"minecraft:diamond_leggings\" will never be swapped to if enabled in this list.")
	public ListValue<String> BLACKLIST = new ListValue<>(List.of());

	@Comment("The slot IDs for the armor slots.")
	public IntegerValue HEAD_SLOT = new IntegerValue(39);
	public IntegerValue CHEST_SLOT = new IntegerValue(38);
	public IntegerValue LEGS_SLOT = new IntegerValue(37);
	public IntegerValue FEET_SLOT = new IntegerValue(36);
}
