package com.iso2t.hotswap.config.subs;

import com.iso2t.configmanager.annotations.Comment;
import com.iso2t.configmanager.annotations.Config;
import com.iso2t.configmanager.value.wrappers.BooleanValue;
import com.iso2t.configmanager.value.wrappers.EnumValue;
import com.iso2t.configmanager.value.wrappers.ListValue;
import com.iso2t.hotswap.config.defs.ToolSwap;

import java.util.List;

@Config(name = "tool_settings")
public class ToolSettings {

	@Comment("Enable replacing a broken tool with the next best candidate.")
	public BooleanValue ENABLED = new BooleanValue(true);

	@Comment({"Swap tool when it breaks/low durability.",
			"If set to LOW_DURABILITY, it will swap to the best first match with a higher durability.",
			"Possible values: \"BREAK\" | \"LOW_DURABILITY\""})
	public EnumValue<ToolSwap> TOOL_SWAP_BEHAVIOR = new EnumValue<>(ToolSwap.class, ToolSwap.BREAK);

	@Comment({"Which tag groups to consider when swapping tools.",
			"Order does not matter."})
	public ListValue<String> TAGS = new ListValue<>(List.of(
			"#minecraft:axes",
			"#minecraft:shovels",
			"#minecraft:pickaxes",
			"#minecraft:swords",
			"#minecraft:hoes",
			"#minecraft:shields"
	));

	@Comment("Any item IDs to blacklist even if their tags match. IE: \"minecraft:diamond_shovel\" will never be swapped to if enabled in this list.")
	public ListValue<String> BLACKLIST = new ListValue<>(List.of());

}
