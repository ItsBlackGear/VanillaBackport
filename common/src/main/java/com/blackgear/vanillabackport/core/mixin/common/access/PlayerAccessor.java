package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerAccessor {
    @Accessor int getCurrentImpulseContextResetGraceTime();
    
    @Accessor void setCurrentImpulseContextResetGraceTime(int currentImpulseContextResetGraceTime);
}
