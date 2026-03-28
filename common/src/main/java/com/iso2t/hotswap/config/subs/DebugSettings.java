package com.iso2t.hotswap.config.subs;

import com.iso2t.configmanager.annotations.Comment;
import com.iso2t.configmanager.annotations.CommentValues;
import com.iso2t.configmanager.annotations.Config;
import com.iso2t.configmanager.value.wrappers.BooleanValue;
import com.iso2t.configmanager.value.wrappers.EnumValue;
import com.iso2t.hotswap.config.defs.LogLevel;

@Config(name = "Debug")
public class DebugSettings {

	@Comment("Enable or disable debug logging.")
	public BooleanValue DEBUG_LOGGING = new BooleanValue(false);

	@Comment({"Enable or disable logging of HotSwap's actions."})
    @CommentValues
	public EnumValue<LogLevel> LOG_LEVEL = new EnumValue<>(LogLevel.class, LogLevel.INFO);

	@Comment("Show the alpha message when the mod is loaded if the version is alpha.")
	public BooleanValue IGNORE_ALPHA_MESSAGE = new BooleanValue(false);
}
