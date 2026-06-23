package com.blackgear.vanillabackport.core.mixin.common.attributes;

import com.blackgear.vanillabackport.common.api.extensions.entity.TravelAwareEntity;
import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.core.mixin.common.access.EntityAccessor;
import com.blackgear.vanillabackport.core.mixin.common.entity_movement.EntityMixin;
import com.blackgear.vanillabackport.core.util.WorldUtilities.*;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Shadow protected abstract boolean isAffectedByFluids();

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createLivingAttributes(AttributeSupplier.Builder original) {
        original.add(ModAttributes.AIR_DRAG_MODIFIER);
        original.add(ModAttributes.BOUNCINESS);
        original.add(ModAttributes.FRICTION_MODIFIER);

        return original;
    }

    @Inject(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;calculateEntityAnimation(Z)V"
        )
    )
    private void vb$handlePostTravelInFluid(Vec3 input, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof TravelAwareEntity traveller && self.isControlledByLocalInstance()) {
            FluidState fluidState = self.level().getFluidState(self.blockPosition());
            if (EntityUtils.isInLiquid(self) && this.isAffectedByFluids() && !self.canStandOnFluid(fluidState)) {
                traveller.postTravelInFluid();
            }
        }
    }

    @ModifyArg(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;handleRelativeFrictionAndCalculateMovement(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"
        ),
        index = 1
    )
    private float vb$handleMovementFriction(float rawFriction) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!self.onGround()) return 1.0F;
        var inst = self.getAttribute(ModAttributes.FRICTION_MODIFIER);
        if (inst == null) return rawFriction;
        return vb$computeModifiedFriction(rawFriction, (float) inst.getValue());
    }

    @WrapOperation(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(DDD)V",
            ordinal = 3
        )
    )
    private void vb$applyAirDrag(LivingEntity self, double argX, double argY, double argZ, Operation<Void> original) {
        BlockPos posBelow = ((EntityAccessor) self).callGetBlockPosBelowThatAffectsMyMovement();
        boolean isFlyingAnimal = self instanceof FlyingAnimal;
        float rawFriction = self.level().getBlockState(posBelow).getBlock().getFriction();

        float xzFriction = self.onGround() ? rawFriction * 0.91F : 0.91F;
        float yFriction = isFlyingAnimal ? xzFriction : 0.98F;

        double xMovement = xzFriction != 0.0F ? argX / xzFriction : argX;
        double zMovement = xzFriction != 0.0F ? argZ / xzFriction : argZ;
        double yMovement = yFriction != 0.0F ? argY / yFriction : argY;

        float frictionModifier = (float) vb$getOrDefault(self, ModAttributes.FRICTION_MODIFIER);
        float airDragModifier = (float) vb$getOrDefault(self, ModAttributes.AIR_DRAG_MODIFIER);

        float blockFriction = self.onGround()
            ? vb$computeModifiedFriction(rawFriction, frictionModifier)
            : 1.0F;
        float airDrag = vb$computeModifiedFriction(0.91F, airDragModifier);
        float friction = blockFriction * airDrag;

        boolean omnidirectional = self instanceof TravelAwareEntity traveller && traveller.omnidirectionalAirMover();
        float verticalFriction = vb$computeModifiedFriction(0.98F, airDragModifier);

        if (omnidirectional || isFlyingAnimal) {
            verticalFriction = friction;
        }

        original.call(self, xMovement * friction, yMovement * verticalFriction, zMovement * friction);
    }

    @Unique
    private static float vb$computeModifiedFriction(float friction, float modifier) {
        return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
    }

    @Unique
    private static double vb$getOrDefault(Entity entity, Attribute attribute) {
        if (!(entity instanceof LivingEntity living)) return 1.0;

        AttributeInstance instance = living.getAttribute(attribute);
        return instance != null ? instance.getValue() : 1.0;
    }
}