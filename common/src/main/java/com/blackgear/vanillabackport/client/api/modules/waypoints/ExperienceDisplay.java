package com.blackgear.vanillabackport.client.api.modules.waypoints;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
public interface ExperienceDisplay {
    static ExperienceDisplay of(LocalPlayer player) {
        return (ExperienceDisplay) player;
    }
    
    int getExperienceDisplayStartTick();
    
    void setExperienceDisplayStartTick(int ticks);
}