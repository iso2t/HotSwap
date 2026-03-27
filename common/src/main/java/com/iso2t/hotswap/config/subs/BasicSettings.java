package com.iso2t.hotswap.config.subs;

import com.iso2t.configmanager.annotations.Comment;
import com.iso2t.configmanager.annotations.Config;
import com.iso2t.configmanager.value.wrappers.BooleanValue;

@Config(name = "basic_settings")
public class BasicSettings {
	@Comment("Allow swapping while in creative mode.")
	public BooleanValue ALLOW_IN_CREATIVE = new BooleanValue(false);

	@Comment("Allow swapping while in adventure mode.")
	public BooleanValue ALLOW_IN_ADVENTURE = new BooleanValue(false);

	@Comment("Allow swapping while in survival mode.")
	public BooleanValue ALLOW_IN_SURVIVAL = new BooleanValue(true);

	@Comment("Allow auto swapping for attacking.")
	public BooleanValue ALLOW_FOR_ATTACKING = new BooleanValue(true);

	@Comment("Stay on selected tool.")
	public BooleanValue KEEP_LAST = new BooleanValue(false);

}
