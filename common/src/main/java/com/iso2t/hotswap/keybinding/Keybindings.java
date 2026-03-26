package com.iso2t.hotswap.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public final class Keybindings {

	public static final Keybindings INSTANCE = new Keybindings();

	private Keybindings () {
		// Private constructor to prevent instantiation
	}

	public final KeyMapping preventSwitch = new KeyMapping(
			"key.hotswap.preventSwitch",
			InputConstants.KEY_LALT,
            KeyMapping.Category.GAMEPLAY
	);

	public final KeyMapping toggle = new KeyMapping(
			"key.hotswap.toggle",
			InputConstants.KEY_SEMICOLON,
            KeyMapping.Category.GAMEPLAY
	);

}
