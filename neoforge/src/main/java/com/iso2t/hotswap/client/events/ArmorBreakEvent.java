package com.iso2t.hotswap.client.events;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class ArmorBreakEvent extends Event {
    private final Player player;
    private final EquipmentSlot slot;

    public ArmorBreakEvent(Player player, EquipmentSlot slot) {
        this.player = player;
        this.slot = slot;
    }

    public Player getPlayer() {
        return player;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }
}
