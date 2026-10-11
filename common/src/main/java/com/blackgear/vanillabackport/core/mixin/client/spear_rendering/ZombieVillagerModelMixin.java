package com.blackgear.vanillabackport.core.mixin.client.spear_rendering;

import com.blackgear.vanillabackport.common.level.components.SwingAnimation;
import com.blackgear.vanillabackport.common.level.components.SwingAnimationType;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ZombieVillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ZombieVillagerModel.class)
public abstract class ZombieVillagerModelMixin<T extends Zombie> {
    @WrapOperation(
        method = "setupAnim(Lnet/minecraft/world/entity/monster/Zombie;FFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/AnimationUtils;animateZombieArms(Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;ZFF)V"
        )
    )
    private void wrapAnimateArms(
        ModelPart leftArm,
        ModelPart rightArm,
        boolean isAggressive,
        float attackTime,
        float ageInTicks,
        Operation<Void> original,
        T entity
    ) {
        SwingAnimation animation = SwingAnimation.get(entity.getMainHandItem());
        boolean animateAttack = animation != null && animation.type() == SwingAnimationType.STAB;
        if (!animateAttack) {
            original.call(leftArm, rightArm, isAggressive, attackTime, ageInTicks);
        } else {
            AnimationUtils.bobArms(rightArm, leftArm, ageInTicks);
        }
    }
}