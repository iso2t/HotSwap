package com.iso2t.hotswap.config.subs;

import com.iso2t.superconfig.annotations.Comment;
import com.iso2t.superconfig.annotations.Config;

@Config(name = "action_settings")
public class ActionSettings {

	@Comment("Mining Settings")
	public ActionConfig.Mine MINE = new ActionConfig.Mine();

	@Comment("Attacking Settings")
	public ActionConfig.Attack ATTACK = new ActionConfig.Attack();

}
