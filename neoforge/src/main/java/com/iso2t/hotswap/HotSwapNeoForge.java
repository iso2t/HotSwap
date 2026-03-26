package com.iso2t.hotswap;

import com.iso2t.hotswap.client.events.ClientEvent;
import com.iso2t.hotswap.keybinding.Keybindings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

import static com.iso2t.hotswap.Constants.MOD_ID;

@Mod(MOD_ID)
public class HotSwapNeoForge {

    public HotSwapNeoForge(IEventBus eventBus) {
        HotSwap.init();

        if (FMLEnvironment.getDist() == Dist.DEDICATED_SERVER) {
            Constants.LOG.warn("[{}] {} is not supported on Server configurations.", Constants.MOD_NAME, Constants.MOD_NAME);
        } else {
            NeoForge.EVENT_BUS.addListener(ClientEvent::onBeginBreak);
            NeoForge.EVENT_BUS.addListener(ClientEvent::attackEntity);
            NeoForge.EVENT_BUS.addListener(ClientEvent::finishBlockBreak);
            NeoForge.EVENT_BUS.addListener(ClientEvent::toggleOnOff);
            NeoForge.EVENT_BUS.addListener(ClientEvent::holdOff);
            NeoForge.EVENT_BUS.addListener(ClientEvent::playerJoin);
            NeoForge.EVENT_BUS.addListener(ClientEvent::itemBreak);
            NeoForge.EVENT_BUS.addListener(ClientEvent::armorBreak);
        }

    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModHandler {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(Keybindings.INSTANCE.preventSwitch);
            event.register(Keybindings.INSTANCE.toggle);
        }
    }

}