package com.blackgear.vanillabackport.core.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.projectile.Projectile;
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

    public static boolean mayBreak(Projectile pProjectile, ServerLevel pLevel) {
        // Need game rule registry. - Echo2craft.
        return pProjectile.getType().is(EntityTypeTags.IMPACT_PROJECTILES) /*&& pLevel.getGameRules().getBoolean(TTGameRules.RULE_PROJECTILESCANBREAKBLOCKS)*/;
    }
}