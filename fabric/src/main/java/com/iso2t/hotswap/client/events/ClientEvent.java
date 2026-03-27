package com.iso2t.hotswap.client.events;

import com.iso2t.hotswap.callbacks.InputPreCallback;
import com.iso2t.hotswap.events.HotSwapEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public class ClientEvent {

    public static void initCallbacks() {
        AttackBlockCallback.EVENT.register((((player, world, hand, pos, direction) -> {
            HotSwapEvents.onBeginBreak(player, world.getBlockState(pos), world.getBlockState(pos));
            return InteractionResult.PASS;
        })));
        AttackEntityCallback.EVENT.register((((player, world, hand, entity, hitResult) -> {
            HotSwapEvents.attackEntity(player, entity);
            return InteractionResult.PASS;
        })));
        InputPreCallback.EVENT.register(((type, code, action, modifiers) -> {
            var mousePosX = Minecraft.getInstance().mouseHandler.xpos();
            var mousePosY = Minecraft.getInstance().mouseHandler.ypos();
            HotSwapEvents.finishBlockBreak(new MouseButtonEvent(mousePosX, mousePosY, new MouseButtonInfo(code, modifiers)), action);
            return false;
        }));
        InputPreCallback.EVENT.register(((type, code, action, modifiers) -> {
            HotSwapEvents.toggleOnOff(new KeyEvent(code, code, modifiers), action);
            return false;
        }));
        ClientTickEvents.START_CLIENT_TICK.register(client -> HotSwapEvents.holdOff());
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            HotSwapEvents.playerJoin(player);
        });
    }

}
