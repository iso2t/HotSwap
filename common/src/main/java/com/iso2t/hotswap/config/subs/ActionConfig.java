package com.iso2t.hotswap.config.subs;

import com.iso2t.easyconfig.api.annotations.Comment;
import com.iso2t.easyconfig.api.annotations.Config;
import com.iso2t.easyconfig.api.value.wrappers.BooleanValue;
import com.iso2t.easyconfig.api.value.wrappers.IntegerValue;
import com.iso2t.easyconfig.api.value.wrappers.ListValue;

import java.util.List;

public class ActionConfig {

	@Config(name = "mine")
	public static class Mine extends ActionConfig {
		@Comment("Enable swapping to a mining tool.")
		public BooleanValue ENABLED = BooleanValue.of(true);

		@Comment("Which tag groups to consider when performing this action")
		public ListValue<String> TAGS = ListValue.of(
				List.of("#minecraft:pickaxes",
						"#minecraft:shovels",
						"#minecraft:axes")
		);

		@Comment({"In what order to prefer matches (first match wins)", "This will still use the best tool for breaking the block, but it indicates the fallback if a tool/match does not exist."})
		public ListValue<String> PRIORITY_ORDER = ListValue.of(
				List.of("#minecraft:pickaxes",
						"#minecraft:shovels",
						"#minecraft:axes")
		);

		@Comment("Any item IDs to blacklist even if their tags match. IE: \"minecraft:diamond_pickaxe\" will never be swapped to if enabled in this list.")
		public ListValue<String> TOOL_BLACKLIST = ListValue.of(List.of());

		@Comment("If the block is on this list, the correct tool will not be swapped to. IE: \"block.minecraft.diamond_ore\" will never be swapped to if it is in this list.")
		public ListValue<String> BLACKLIST = ListValue.of(List.of());

		@Comment("Cooldown in ms between swaps for this action.")
		public IntegerValue COOLDOWN = IntegerValue.of(0);
	}

	@Config(name = "attack")
	public static class Attack extends ActionConfig {
		@Comment("Enable swapping to a weapon.")
		public BooleanValue ENABLED = BooleanValue.of(true);

		@Comment("Allow swapping to an axe when looking for a viable attacking entity.")
		public BooleanValue ALLOW_AXES_FOR_ATTACKING = BooleanValue.of(true);

		@Comment("Which tag groups to consider when performing this action")
		public ListValue<String> TAGS = ListValue.of(
				List.of("#minecraft:axes",
						"#minecraft:swords",
						"#minecraft:pickaxes",
						"#minecraft:shovels",
						"#minecraft:hoes")
		);

		@Comment("In what order to prefer matches (first match wins)")
		public ListValue<String> PRIORITY_ORDER = ListValue.of(
				List.of("#minecraft:axes",
						"#minecraft:swords",
						"#minecraft:pickaxes",
						"#minecraft:shovels",
						"#minecraft:hoes")
		);

		@Comment("Any item IDs to blacklist even if their tags match")
		public ListValue<String> BLACKLIST = ListValue.of(List.of());

		@Comment("Cooldown in ms between swaps for this action")
		public IntegerValue COOLDOWN = IntegerValue.of(0);
	}

}
