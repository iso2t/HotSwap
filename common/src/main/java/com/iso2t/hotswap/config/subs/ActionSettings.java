package com.iso2t.hotswap.config.subs;

import com.iso2t.easyconfig.api.annotations.Comment;

public class ActionSettings {

	@Comment("Mining Settings")
	public ActionConfig.Mine MINE = new ActionConfig.Mine();

	@Comment("Attacking Settings")
	public ActionConfig.Attack ATTACK = new ActionConfig.Attack();

}
