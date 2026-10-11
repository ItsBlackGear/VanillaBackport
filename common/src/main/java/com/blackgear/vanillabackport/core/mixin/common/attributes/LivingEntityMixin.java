package com.blackgear.vanillabackport.core.mixin.common.attributes;

import com.blackgear.vanillabackport.common.api.extensions.entity.movement.TravelAwareEntity;
import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.core.mixin.common.access.EntityAccessor;
import com.blackgear.vanillabackport.core.mixin.common.entity_movement.EntityMixin;
import com.blackgear.vanillabackport.core.util.WorldUtilities.*;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
    private static AttributeSupplier.Builder vb$registerAttributes(AttributeSupplier.Builder original) {
        return original.add(ModAttributes.AIR_DRAG_MODIFIER)
            .add(ModAttributes.BOUNCINESS)
            .add(ModAttributes.FRICTION_MODIFIER);
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
    private float vb$modifyGroundFriction(float rawFriction) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!self.onGround()) return rawFriction;
        
        double modifier = vb$getOrDefault(self, ModAttributes.FRICTION_MODIFIER, 1.0);
        return modifier == 1.0 ? rawFriction : vb$computeFriction(rawFriction, (float) modifier);
    }
    
    @WrapOperation(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(DDD)V",
            ordinal = 3
        )
    )
    private void vb$applyAirAndBlockDrag(LivingEntity self, double rawX, double rawY, double rawZ, Operation<Void> original) {
        double frictionModifier = vb$getOrDefault(self, ModAttributes.FRICTION_MODIFIER, 1.0);
        double airDragModifier = vb$getOrDefault(self, ModAttributes.AIR_DRAG_MODIFIER, 1.0);
        
        if (frictionModifier == 1.0 && airDragModifier == 1.0) {
            original.call(self, rawX, rawY, rawZ);
            return;
        }
        
        BlockPos posBelow = ((EntityAccessor) self).callGetBlockPosBelowThatAffectsMyMovement();
        float rawFriction = self.level().getBlockState(posBelow).getBlock().getFriction();
        
        boolean isFlyingAnimal = self instanceof FlyingAnimal;
        float vanillaFriction = self.onGround() ? rawFriction * 0.91F : 0.91F;
        float vanillaVertical = isFlyingAnimal ? vanillaFriction : 0.98F;
        
        double cleanX = vanillaFriction != 0.0F ? rawX / vanillaFriction : rawX;
        double cleanY = vanillaVertical != 0.0F ? rawY / vanillaVertical : rawY;
        double cleanZ = vanillaFriction != 0.0F ? rawZ / vanillaFriction : rawZ;
        
        float blockFriction = self.onGround() ? vb$computeFriction(rawFriction, (float) frictionModifier) : 1.0F;
        float airDrag = vb$computeFriction(0.91F, (float) airDragModifier);
        float horizontalFriction = blockFriction * airDrag;
        
        boolean isOmnidirectional = self instanceof TravelAwareEntity traveller && traveller.omnidirectionalAirMover();
        float verticalFriction = (isFlyingAnimal || isOmnidirectional)
            ? airDrag
            : vb$computeFriction(0.98F, (float) airDragModifier);
        
        original.call(self, cleanX * horizontalFriction, cleanY * verticalFriction, cleanZ * horizontalFriction);
    }
    
    @Unique
    private static float vb$computeFriction(float friction, float modifier) {
        return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
    }
    
    @Unique
    private static double vb$getOrDefault(Entity entity, Attribute attribute, double fallback) {
        if (!(entity instanceof LivingEntity living)) return fallback;
        AttributeInstance instance = living.getAttribute(attribute);
        return instance != null ? instance.getValue() : fallback;
    }
}