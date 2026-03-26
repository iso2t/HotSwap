package com.iso2t.hotswap;

import com.iso2t.hotswap.client.events.ClientEvent;
import com.iso2t.hotswap.keybinding.Keybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;

public class HotSwapFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        HotSwap.init();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            Constants.LOG.warn("[{}] {} is not supported on Server configurations.", Constants.MOD_NAME, Constants.MOD_NAME);
        } else {
            registerKeyBindings();
            ClientEvent.initCallbacks();
        }
    }

    private void registerKeyBindings() {
        var keybindings = Keybindings.INSTANCE;
        KeyMappingHelper.registerKeyMapping(keybindings.preventSwitch);
        KeyMappingHelper.registerKeyMapping(keybindings.toggle);
    }
}
