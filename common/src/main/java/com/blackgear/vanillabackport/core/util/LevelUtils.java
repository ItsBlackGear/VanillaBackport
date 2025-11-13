package com.blackgear.vanillabackport.core.util;

import net.minecraft.world.level.Level;

public class LevelUtils {
    public static boolean isMoonVisible(Level level) {
        if (!level.dimensionType().natural()) {
            return false;
        } else {
            int ticks = (int) (level.getDayTime() % 24000L);
            return ticks >= 12600 && ticks <= 23400;
        }
    }
}