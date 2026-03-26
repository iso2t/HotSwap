package com.iso2t.hotswap.config;

import com.iso2t.hotswap.config.subs.*;
import com.iso2t.superconfig.annotations.Comment;
import com.iso2t.superconfig.annotations.Config;

@Config(name = "hotswap")
public class ModConfig {

	@Comment({
			"###################################",
			"# Basic configuration for HotSwap #",
			"###################################"})
	public BasicSettings BASIC = new BasicSettings();

	@Comment({
			"####################################",
			"# Action configuration for HotSwap #",
			"####################################"})
	public ActionSettings ACTIONS = new ActionSettings();

	@Comment({
			"####################################",
			"# Armor configuration for HotSwap #",
			"####################################"})
	public ArmorSettings ARMOR = new ArmorSettings();

	@Comment({
			"####################################",
			"# Tool configuration for HotSwap #",
			"####################################"})
	public ToolSettings TOOLS = new ToolSettings();

	@Comment({
			"###################################",
			"# Debug configuration for HotSwap #",
			"###################################"})
	public DebugSettings DEBUG = new DebugSettings();

}
