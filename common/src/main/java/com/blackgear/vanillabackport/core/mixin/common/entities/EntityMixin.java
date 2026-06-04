package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.extensions.TravelAwareEntity;
import com.blackgear.vanillabackport.common.api.extensions.MotionAwareEntity;
import com.blackgear.vanillabackport.common.api.leash.LeashableCallback;
import com.blackgear.vanillabackport.common.registries.ModAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements MotionAwareEntity, LeashableCallback {
    @Shadow public abstract Vec3 position();
    @Shadow @Nullable public abstract LivingEntity getControllingPassenger();
    @Shadow public abstract boolean isAlive();
    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();
    @Shadow public abstract Level level();
    @Shadow @Deprecated public abstract BlockPos getOnPosLegacy();
    @Shadow public abstract Vec3 getDeltaMovement();
    @Shadow public abstract void gameEvent(Holder<GameEvent> gameEvent);
    @Shadow public abstract void setDeltaMovement(Vec3 deltaMovement);
    @Shadow public abstract boolean onGround();
    @Shadow public abstract double getGravity();

    @Shadow public boolean verticalCollision;
    @Shadow public boolean horizontalCollision;
    @Shadow public boolean verticalCollisionBelow;

    // Motion Handler

    @Unique private Vec3 lastKnownSpeed = Vec3.ZERO;
    @Unique @Nullable private Vec3 lastKnownPosition;

    @Inject(
        method = "reapplyPosition",
        at = @At("TAIL")
    )
    private void vb$reapplyPosition(CallbackInfo ci) {
        this.lastKnownPosition = null;
    }

    @Inject(
        method = "baseTick",
        at = @At("TAIL")
    )
    private void vb$baseTick(CallbackInfo ci) {
        this.computeSpeed();
    }

    @Override
    public void computeSpeed() {
        if (this.lastKnownPosition == null) {
            this.lastKnownPosition = this.position();
        }

        this.lastKnownSpeed = this.position().subtract(this.lastKnownPosition);
        this.lastKnownPosition = this.position();
    }

    @Override
    public Vec3 getKnownSpeed() {
        return this.getControllingPassenger() instanceof Player player && this.isAlive()
            ? ((MotionAwareEntity) player).getKnownSpeed()
            : this.lastKnownSpeed;
    }

    // Bounciness Handler

    @Unique private boolean vb$canBounce;

    @Unique private Vec3 vb$movement;
    @Unique private Vec3 vb$velocity;
    @Unique private boolean vb$xCollision;
    @Unique private boolean vb$zCollision;

    @Inject(
        method = "move",
        at = @At("HEAD")
    )
    private void vb$earlyCapture(MoverType type, Vec3 delta, CallbackInfo ci) {
        this.vb$canBounce = vb$getOrDefault((Entity) (Object) this, ModAttributes.BOUNCINESS, 0.0) > 0.0;
        if (this.vb$canBounce) {
            this.vb$velocity = this.getDeltaMovement();
            this.vb$movement = null;
            this.vb$xCollision = false;
            this.vb$zCollision = false;
        }
    }

    @WrapOperation(
        method = "move",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"
        )
    )
    private Vec3 vb$capture(Entity self, Vec3 movement, Operation<Vec3> original) {
        Vec3 collide = original.call(self, movement);
        if (this.vb$canBounce) {
            this.vb$movement = collide;
            this.vb$xCollision = Math.abs(collide.x) < Math.abs(movement.x) && !Mth.equal(movement.x, collide.x);
            this.vb$zCollision = Math.abs(collide.z) < Math.abs(movement.z) && !Mth.equal(movement.z, collide.z);
        }

        return collide;
    }

    @Inject(
        method = "move",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;tryCheckInsideBlocks()V"
        )
    )
    private void vb$RestituteMovementAfterCollisions(MoverType type, Vec3 delta, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        if (!this.vb$canBounce || this.vb$movement == null) return;
        if (!this.verticalCollision && !this.horizontalCollision) return;

        boolean suppress = self instanceof LivingEntity living && living.isSuppressingBounce();
        double restitution = suppress ? 0.0 : vb$getOrDefault(self, ModAttributes.BOUNCINESS, 0.0);
        Vec3 currentMovement = this.vb$velocity != null ? this.vb$velocity : this.getDeltaMovement();
        Vec3 movementAfterBounce = currentMovement;

        if (this.vb$xCollision) {
            movementAfterBounce = movementAfterBounce.with(Direction.Axis.X, -currentMovement.x * restitution);
        }
        if (this.vb$zCollision) {
            movementAfterBounce = movementAfterBounce.with(Direction.Axis.Z, -currentMovement.z * restitution);
        }

        boolean bounced = restitution > 0.0 && (this.vb$xCollision || this.vb$zCollision);

        if (this.verticalCollision) {
            if (this.verticalCollisionBelow) {
                restitution = (!(-currentMovement.y < this.getEffectiveGravity()) && !suppress)
                    ? restitution
                    : 0.0;
            }

            double gravityCompensation = 0.0;
            double effectiveDrag = 1.0;
            if (restitution > 0.0) {
                double portionWithMovement = currentMovement.y != 0.0 ? this.vb$movement.y / currentMovement.y : 0.0;
                gravityCompensation = portionWithMovement * this.getEffectiveGravity();
                effectiveDrag = Mth.lerp(portionWithMovement, 1.0, vb$getAirDrag(self));
                bounced = true;
            }

            movementAfterBounce = movementAfterBounce.with(Direction.Axis.Y, (gravityCompensation - currentMovement.y) * effectiveDrag * restitution);
        }

        if (bounced) {
            this.gameEvent(GameEvent.HIT_GROUND);
        }

        this.setDeltaMovement(movementAfterBounce);
    }

    @Unique
    private static double vb$getAirDrag(Entity entity) {
        if (entity instanceof LivingEntity living) {
            float airDragModifier = (float) vb$getOrDefault(living, ModAttributes.AIR_DRAG_MODIFIER, 1.0);
            if (!(entity instanceof TravelAwareEntity airborne) || !airborne.omnidirectionalAirMover()) {
                return computeModifiedFriction(0.98F, airDragModifier);
            } else {
                BlockPos posBelow = living.getBlockPosBelowThatAffectsMyMovement();
                float friction = living.onGround()
                    ? computeModifiedFriction(living.level().getBlockState(posBelow).getBlock().getFriction(), (float) vb$getOrDefault(living, ModAttributes.FRICTION_MODIFIER, 1.0))
                    : 1.0F;
                float airDrag = computeModifiedFriction(0.91F, airDragModifier);
                return friction * airDrag;
            }
        }

        return 0.98;
    }

    @Unique
    private static float computeModifiedFriction(float friction, float modifier) {
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
        if ((Entity) (Object) this instanceof LivingEntity living) {
            boolean isFalling = this.getDeltaMovement().y <= 0.0;
            return isFalling && living.hasEffect(MobEffects.SLOW_FALLING) ? Math.min(this.getGravity(), 0.01) : this.getGravity();
        }

        return this.getGravity();
    }
}