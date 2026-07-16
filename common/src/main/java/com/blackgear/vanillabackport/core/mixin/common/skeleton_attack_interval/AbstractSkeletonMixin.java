package com.blackgear.vanillabackport.core.mixin.common.skeleton_attack_interval;

import com.blackgear.vanillabackport.common.api.extensions.entity.modifiers.SkeletonAttackIntervalModifier;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonMixin {
    @ModifyExpressionValue(method = "reassessWeaponGoal", at = @At(value = "CONSTANT", args = "intValue=20"))
    private int vb$modifyHardAttackInterval(int original) {
        return (AbstractSkeleton)(Object) this instanceof SkeletonAttackIntervalModifier modifier
            ? modifier.getHardAttackInterval()
            : original;
    }
    
    @ModifyExpressionValue(method = "reassessWeaponGoal", at = @At(value = "CONSTANT", args = "intValue=40"))
    private int vb$modifyAttackInterval(int original) {
        return (AbstractSkeleton)(Object) this instanceof SkeletonAttackIntervalModifier modifier
            ? modifier.getAttackInterval()
            : original;
    }
}