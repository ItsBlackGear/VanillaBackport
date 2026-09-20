package com.blackgear.vanillabackport.core.mixin.common.attributes;

import com.blackgear.vanillabackport.common.api.extensions.entity.movement.TravelAwareEntity;
import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Vec3 getDeltaMovement();
    @Shadow public abstract void gameEvent(Holder<GameEvent> gameEvent);
    @Shadow public abstract void setDeltaMovement(Vec3 deltaMovement);
    @Shadow public abstract double getGravity();
    
    @Shadow public boolean verticalCollision;
    @Shadow public boolean horizontalCollision;
    @Shadow public boolean verticalCollisionBelow;
    @Shadow private Level level;
    
    @Unique private boolean vb$canBounce;
    @Unique private Vec3 vb$movement;
    @Unique private Vec3 vb$velocityBeforeCollide;
    @Unique private boolean vb$xCollision;
    @Unique private boolean vb$zCollision;
    
    @Inject(method = "move", at = @At("HEAD"))
    private void vb$earlyCapture(MoverType type, Vec3 delta, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        this.vb$canBounce = self instanceof LivingEntity && vb$getOrDefault(self, ModAttributes.BOUNCINESS, 0.0) > 0.0;
    }
    
    @WrapOperation(
        method = "move",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 vb$captureMovementAndVelocity(Entity self, Vec3 movement, Operation<Vec3> original) {
        if (!this.vb$canBounce) {
            return original.call(self, movement);
        }

        this.vb$velocityBeforeCollide = this.getDeltaMovement();
        Vec3 collide = original.call(self, movement);
        
        this.vb$movement = collide;
        this.vb$xCollision = !Mth.equal(movement.x, collide.x);
        this.vb$zCollision = !Mth.equal(movement.z, collide.z);
        return collide;
    }
    
    @WrapOperation(
        method = "move",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V")
    )
    private void vb$preventDefaultHorizontalReset(Entity instance, double x, double y, double z, Operation<Void> original) {
        if (!this.vb$canBounce) {
            original.call(instance, x, y, z);
        }
    }
    
    @Inject(
        method = "move",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tryCheckInsideBlocks()V")
    )
    private void vb$restituteMovementAfterCollisions(MoverType type, Vec3 delta, CallbackInfo ci) {
        if (!this.vb$canBounce || this.vb$movement == null || (!this.verticalCollision && !this.horizontalCollision)) {
            return;
        }

        Entity self = (Entity) (Object) this;
        BlockState effectState = this.level.getBlockState(self.getOnPosLegacy());

        boolean suppressEntity = self instanceof LivingEntity living && living.isSuppressingBounce();
        boolean suppressBlock = effectState.is(ModBlockTags.SUPPRESSES_BOUNCE);
        
        if (suppressEntity || suppressBlock) return;

        double restitution = vb$getOrDefault(self, ModAttributes.BOUNCINESS, 0.0);
        if (restitution <= 0.0) return;

        Vec3 currentMovement = this.vb$velocityBeforeCollide != null ? this.vb$velocityBeforeCollide : this.getDeltaMovement();
        double newX = currentMovement.x;
        double newY = currentMovement.y;
        double newZ = currentMovement.z;
        boolean bounced = false;

        // Rebote Horizontal (Ejes X / Z)
        if (this.vb$xCollision) {
            newX = -currentMovement.x * restitution;
            bounced = true;
        }
        if (this.vb$zCollision) {
            newZ = -currentMovement.z * restitution;
            bounced = true;
        }

        // Rebote Vertical (Eje Y)
        if (this.verticalCollision) {
            boolean validImpact = !this.verticalCollisionBelow || (-currentMovement.y > this.getEffectiveGravity());
            
            if (validImpact) {
                double portionWithMovement = currentMovement.y != 0.0 ? this.vb$movement.y / currentMovement.y : 0.0;
                double gravityCompensation = portionWithMovement * this.getEffectiveGravity();
                double effectiveDrag = Mth.lerp(portionWithMovement, 1.0, vb$getAirDrag(self));
                
                newY = (gravityCompensation - currentMovement.y) * effectiveDrag * restitution;
                bounced = true;
            }
        }

        if (bounced) {
            this.setDeltaMovement(new Vec3(newX, newY, newZ));
            this.gameEvent(GameEvent.HIT_GROUND);
        }
    }
    
    @Inject(method = "move", at = @At("RETURN"))
    private void vb$cleanup(MoverType type, Vec3 delta, CallbackInfo ci) {
        this.vb$movement = null;
        this.vb$velocityBeforeCollide = null;
    }

    // --- MÉTODOS AUXILIARES OPTIMIZADOS ---

    @Unique
    private static double vb$getAirDrag(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return 0.98;

        float airDragModifier = (float) vb$getOrDefault(living, ModAttributes.AIR_DRAG_MODIFIER, 1.0);
        if (!(entity instanceof TravelAwareEntity airborne) || !airborne.omnidirectionalAirMover()) {
            return vb$computeFriction(0.98F, airDragModifier);
        }

        float friction = living.onGround()
            ? vb$computeFriction(living.level().getBlockState(living.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction(), (float) vb$getOrDefault(living, ModAttributes.FRICTION_MODIFIER, 1.0))
            : 1.0F;

        return friction * vb$computeFriction(0.91F, airDragModifier);
    }
    
    @Unique
    private static float vb$computeFriction(float friction, float modifier) {
        return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
    }
    
    @Unique
    private static double vb$getOrDefault(Entity entity, Holder<Attribute> attribute, double fallback) {
        if (!(entity instanceof LivingEntity living)) return fallback;
        AttributeInstance instance = living.getAttribute(attribute);
        return instance != null ? instance.getValue() : fallback;
    }
    
    @Unique
    protected double getEffectiveGravity() {
        if ((Entity) (Object) this instanceof LivingEntity living && this.getDeltaMovement().y <= 0.0) {
            if (living.hasEffect(MobEffects.SLOW_FALLING)) {
                return Math.min(this.getGravity(), 0.01);
            }
        }
        
        return this.getGravity();
    }
}