package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;

public enum PlayerActions {
    STAB;
    
    public ServerboundPlayerActionPacket.Action get() {
        return ServerboundPlayerActionPacket.Action.valueOf(this.name());
    }
}