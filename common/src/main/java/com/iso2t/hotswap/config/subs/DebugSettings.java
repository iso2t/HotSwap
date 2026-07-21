package com.iso2t.hotswap.config.subs;

import com.iso2t.easyconfig.api.annotations.Comment;
import com.iso2t.easyconfig.api.value.wrappers.BooleanValue;
import com.iso2t.easyconfig.api.value.wrappers.EnumValue;
import com.iso2t.hotswap.config.defs.LogLevel;

public class DebugSettings {

	@Comment("Enable or disable debug logging.")
	public BooleanValue DEBUG_LOGGING = BooleanValue.of(false);

	@Comment({"Enable or disable logging of HotSwap's actions."})
	public EnumValue<LogLevel> LOG_LEVEL = EnumValue.of(LogLevel.class, LogLevel.INFO);

	@Comment("Show the alpha message when the mod is loaded if the version is alpha.")
	public BooleanValue IGNORE_ALPHA_MESSAGE = BooleanValue.of(false);
}
