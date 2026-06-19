package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.core.mixin.access.BooleanValueAccessor;
import com.blackgear.vanillabackport.core.mixin.access.GameRulesAccessor;
import net.minecraft.world.level.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> RULE_ENDER_PEARLS_VANISH_ON_DEATH = GameRulesAccessor.callRegister("enderPearlsVanishOnDeath", GameRules.Category.PLAYER, BooleanValueAccessor.callCreate(false));

    public static void bootstrap() {}
}