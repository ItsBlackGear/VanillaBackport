package com.blackgear.vanillabackport.common.level.entities.bat;

import net.minecraft.world.entity.AnimationState;

public interface BatAnimator {
    AnimationState flyAnimationState();
    AnimationState restAnimationState();
}