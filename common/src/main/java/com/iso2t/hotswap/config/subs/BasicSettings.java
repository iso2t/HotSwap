package com.iso2t.hotswap.config.subs;

import com.iso2t.easyconfig.api.annotations.Comment;
import com.iso2t.easyconfig.api.value.wrappers.BooleanValue;

public class BasicSettings {
	@Comment("Allow swapping while in creative mode.")
	public BooleanValue ALLOW_IN_CREATIVE = BooleanValue.of(false);

	@Comment("Allow swapping while in adventure mode.")
	public BooleanValue ALLOW_IN_ADVENTURE = BooleanValue.of(false);

	@Comment("Allow swapping while in survival mode.")
	public BooleanValue ALLOW_IN_SURVIVAL = BooleanValue.of(true);

	@Comment("Allow auto swapping for attacking.")
	public BooleanValue ALLOW_FOR_ATTACKING = BooleanValue.of(true);

	@Comment("Stay on selected tool.")
	public BooleanValue KEEP_LAST = BooleanValue.of(false);

}
