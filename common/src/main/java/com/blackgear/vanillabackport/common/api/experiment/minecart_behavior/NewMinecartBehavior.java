package com.blackgear.vanillabackport.common.api.experiment.minecart_behavior;

import com.blackgear.vanillabackport.core.mixin.access.AbstractMinecartAccessor;
import com.blackgear.vanillabackport.core.mixin.access.EntityAccessor;
import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import com.blackgear.vanillabackport.core.util.WorldUtilities.*;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class NewMinecartBehavior extends MinecartBehavior {
    private @Nullable StepPartialTicks cacheIndexAlpha;
    private int cachedLerpDelay;
    private float cachedPartialTick;
    private int lerpDelay = 0;
    public final List<MinecartStep> lerpSteps = new LinkedList<>();
    public final List<MinecartStep> currentLerpSteps = new LinkedList<>();
    public double currentLerpStepsTotalWeight = 0.0;
    public MinecartStep oldLerp = MinecartStep.ZERO;
    
    public NewMinecartBehavior(AbstractMinecart minecart) {
        super(minecart);
    }
    
    @Override
    public void tick() {
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockPos var5 = this.getCurrentBlockPosOrRailBelow();
            BlockState state = this.level().getBlockState(var5);
            if (((EntityAccessor) this.minecart).isFirstTick()) {
                ((AbstractMinecartAccessor) this.minecart).setOnRails(BaseRailBlock.isRail(state));
                this.adjustToRails(var5, state, true);
            }
            
            ((EntityAccessor) this.minecart).callApplyGravity();
            this.moveAlongTrack(serverLevel);
        } else {
            this.lerpClientPositionAndRotation();
            boolean onRails = BaseRailBlock.isRail(this.level().getBlockState(this.getCurrentBlockPosOrRailBelow()));
            ((AbstractMinecartAccessor) this.minecart).setOnRails(onRails);
        }
    }
    
    private void lerpClientPositionAndRotation() {
        if (--this.lerpDelay <= 0) {
            this.setOldLerpValues();
            this.currentLerpSteps.clear();
            if (!this.lerpSteps.isEmpty()) {
                this.currentLerpSteps.addAll(this.lerpSteps);
                this.lerpSteps.clear();
                this.currentLerpStepsTotalWeight = 0.0;
                
                for (NewMinecartBehavior.MinecartStep minecartStep : this.currentLerpSteps) {
                    this.currentLerpStepsTotalWeight = this.currentLerpStepsTotalWeight + minecartStep.weight;
                }
                
                this.lerpDelay = this.currentLerpStepsTotalWeight == 0.0 ? 0 : 3;
            }
        }
        
        if (this.cartHasPosRotLerp()) {
            this.setPos(this.getCartLerpPosition(1.0F));
            this.setDeltaMovement(this.getCartLerpMovements(1.0F));
            this.setXRot(this.getCartLerpXRot(1.0F));
            this.setYRot(this.getCartLerpYRot(1.0F));
        }
    }
    
    public void setOldLerpValues() {
        this.oldLerp = new NewMinecartBehavior.MinecartStep(this.position(), this.getDeltaMovement(), this.getYRot(), this.getXRot(), 0.0F);
    }
    
    public boolean cartHasPosRotLerp() {
        return !this.currentLerpSteps.isEmpty();
    }
    
    public float getCartLerpXRot(final float partialTicks) {
        NewMinecartBehavior.StepPartialTicks currentStepPartialTicks = this.getCurrentLerpStep(partialTicks);
        return Mth.rotLerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.xRot, currentStepPartialTicks.currentStep.xRot);
    }
    
    public float getCartLerpYRot(final float partialTicks) {
        NewMinecartBehavior.StepPartialTicks currentStepPartialTicks = this.getCurrentLerpStep(partialTicks);
        return Mth.rotLerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.yRot, currentStepPartialTicks.currentStep.yRot);
    }
    
    public Vec3 getCartLerpPosition(final float partialTicks) {
        NewMinecartBehavior.StepPartialTicks currentStepPartialTicks = this.getCurrentLerpStep(partialTicks);
        return MthUtils.lerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.position, currentStepPartialTicks.currentStep.position);
    }
    
    public Vec3 getCartLerpMovements(final float partialTicks) {
        NewMinecartBehavior.StepPartialTicks currentStepPartialTicks = this.getCurrentLerpStep(partialTicks);
        return MthUtils.lerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.movement, currentStepPartialTicks.currentStep.movement);
    }
    
    private NewMinecartBehavior.StepPartialTicks getCurrentLerpStep(final float partialTick) {
        if (partialTick != this.cachedPartialTick || this.lerpDelay != this.cachedLerpDelay || this.cacheIndexAlpha == null) {
            float alpha = (3 - this.lerpDelay + partialTick) / 3.0F;
            float countUp = 0.0F;
            float indexedPartialTick = 1.0F;
            boolean foundIndex = false;
            
            int index;
            for (index = 0; index < this.currentLerpSteps.size(); index++) {
                float weight = this.currentLerpSteps.get(index).weight;
                if (!(weight <= 0.0F)) {
                    countUp += weight;
                    if (countUp >= this.currentLerpStepsTotalWeight * alpha) {
                        float current = countUp - weight;
                        indexedPartialTick = (float) ((alpha * this.currentLerpStepsTotalWeight - current) / weight);
                        foundIndex = true;
                        break;
                    }
                }
            }
            
            if (!foundIndex) {
                index = this.currentLerpSteps.size() - 1;
            }
            
            MinecartStep currentStep = this.currentLerpSteps.get(index);
            MinecartStep previousStep = index > 0 ? this.currentLerpSteps.get(index - 1) : this.oldLerp;
            this.cacheIndexAlpha = new StepPartialTicks(indexedPartialTick, currentStep, previousStep);
            this.cachedLerpDelay = this.lerpDelay;
            this.cachedPartialTick = partialTick;
        }
        
        return this.cacheIndexAlpha;
    }
    
    public void adjustToRails(final BlockPos targetBlockPos, final BlockState currentState, final boolean instant) {
        if (BaseRailBlock.isRail(currentState)) {
            RailShape shape = currentState.getValue(((BaseRailBlock)currentState.getBlock()).getShapeProperty());
            Pair<Vec3i, Vec3i> exits = AbstractMinecartAccessor.callExits(shape);
            Vec3 exit0 = VectorUtils.create(exits.getFirst()).scale(0.5);
            Vec3 exit1 = VectorUtils.create(exits.getSecond()).scale(0.5);
            Vec3 horizontalOutDirection = VectorUtils.horizontal(exit0);
            Vec3 horizontalInDirection = VectorUtils.horizontal(exit1);
            if (this.getDeltaMovement().length() > 1.0E-5F && this.getDeltaMovement().dot(horizontalOutDirection) < this.getDeltaMovement().dot(horizontalInDirection)
                || this.isDecending(horizontalInDirection, shape)) {
                Vec3 swap = horizontalOutDirection;
                horizontalOutDirection = horizontalInDirection;
                horizontalInDirection = swap;
            }
            
            float yRot = 180.0F - (float)(Math.atan2(horizontalOutDirection.z, horizontalOutDirection.x) * 180.0 / Math.PI);
            yRot += this.isFlipped() ? 180.0F : 0.0F;
            Vec3 previousPosition = this.position();
            boolean inCorner = exit0.x() != exit1.x() && exit0.z() != exit1.z();
            Vec3 targetPosition;
            if (inCorner) {
                Vec3 from0to1 = exit1.subtract(exit0);
                Vec3 from0toPos = previousPosition.subtract(Vec3.atBottomCenterOf(targetBlockPos)).subtract(exit0);
                Vec3 travelVectorFrom0 = from0to1.scale(from0to1.dot(from0toPos) / from0to1.dot(from0to1));
                targetPosition = Vec3.atBottomCenterOf(targetBlockPos).add(exit0).add(travelVectorFrom0);
                yRot = 180.0F - (float)(Math.atan2(travelVectorFrom0.z, travelVectorFrom0.x) * 180.0 / Math.PI);
                yRot += this.isFlipped() ? 180.0F : 0.0F;
            } else {
                boolean zSnap = exit0.subtract(exit1).x != 0.0;
                boolean xSnap = exit0.subtract(exit1).z != 0.0;
                targetPosition = new Vec3(
                    xSnap ? Vec3.atCenterOf(targetBlockPos).x : previousPosition.x, targetBlockPos.getY(), zSnap ? Vec3.atCenterOf(targetBlockPos).z : previousPosition.z
                );
            }
            
            Vec3 diffFromBlock = targetPosition.subtract(previousPosition);
            this.setPos(previousPosition.add(diffFromBlock));
            float xRot = 0.0F;
            boolean inHill = exit0.y() != exit1.y();
            if (inHill) {
                Vec3 inPosition = Vec3.atBottomCenterOf(targetBlockPos).add(horizontalInDirection);
                double horizontalDistanceFromIn = inPosition.distanceTo(this.position());
                this.setPos(this.position().add(0.0, horizontalDistanceFromIn + 0.1, 0.0));
                xRot = this.isFlipped() ? 45.0F : -45.0F;
            } else {
                this.setPos(this.position().add(0.0, 0.1, 0.0));
            }
            
            this.setRotation(yRot, xRot);
            double adjustDistance = previousPosition.distanceTo(this.position());
            if (adjustDistance > 0.0) {
                this.lerpSteps
                    .add(
                        new MinecartStep(this.position(), this.getDeltaMovement(), this.getYRot(), this.getXRot(), instant ? 0.0F : (float)adjustDistance)
                    );
            }
        }
    }
    
    private void setRotation(float yRot, float xRot) {
        double yRotDiff = Math.abs(yRot - this.getYRot());
        if (yRotDiff >= 175.0 && yRotDiff <= 185.0) {
            this.setFlipped(!this.isFlipped());
            yRot -= 180.0F;
            xRot *= -1.0F;
        }
        
        xRot = Math.clamp(xRot, -45.0F, 45.0F);
        this.setXRot(xRot % 360.0F);
        this.setYRot(yRot % 360.0F);
    }
    
    @Override
    public void moveAlongTrack(ServerLevel level) {
        for (TrackIteration trackIteration = new TrackIteration();
             trackIteration.shouldIterate() && this.minecart.isAlive();
             trackIteration.firstIteration = false
        ) {
            Vec3 initialStepDeltaMovement = this.getDeltaMovement();
            BlockPos currentPos = this.getCurrentBlockPosOrRailBelow();
            BlockState currentState = this.level().getBlockState(currentPos);
            boolean onRails = BaseRailBlock.isRail(currentState);
            if (this.minecart.isOnRails() != onRails) {
                ((AbstractMinecartAccessor) this.minecart).setOnRails(onRails);
                this.adjustToRails(currentPos, currentState, false);
            }
            
            if (onRails) {
                this.minecart.resetFallDistance();
                this.minecart.setOldPosAndRot();
                if (currentState.is(Blocks.ACTIVATOR_RAIL)) {
                    this.minecart.activateMinecart(currentPos.getX(), currentPos.getY(), currentPos.getZ(), currentState.getValue(PoweredRailBlock.POWERED));
                }
                
                RailShape shape = currentState.getValue(((BaseRailBlock) currentState.getBlock()).getShapeProperty());
                Vec3 newDeltaMovement = this.calculateTrackSpeed(level, VectorUtils.horizontal(initialStepDeltaMovement), trackIteration, currentPos, currentState, shape);
                if (trackIteration.firstIteration) {
                    trackIteration.movementLeft = newDeltaMovement.horizontalDistance();
                } else {
                    trackIteration.movementLeft = trackIteration.movementLeft + (newDeltaMovement.horizontalDistance() - initialStepDeltaMovement.horizontalDistance());
                }
                
                this.setDeltaMovement(newDeltaMovement);
                trackIteration.movementLeft = this.stepAlongTrack(currentPos, shape, trackIteration.movementLeft);
            } else {
                this.comeOffTrack(level);
                trackIteration.movementLeft = 0.0;
            }
            
            Vec3 stepPosition = this.position();
            Vec3 stepDelta = stepPosition.subtract(EntityUtils.oldPosition(this.minecart));
            double stepLength = stepDelta.length();
            if (stepLength > Mth.EPSILON) {
                if (!(stepDelta.horizontalDistanceSqr() > Mth.EPSILON)) {
                    if (!this.minecart.isOnRails()) {
                        this.setXRot(this.minecart.onGround() ? 0.0F : Mth.rotLerp(0.2F, this.getXRot(), 0.0F));
                    }
                } else {
                    float yRot = 180.0F - (float)(Math.atan2(stepDelta.z, stepDelta.x) * Mth.RAD_TO_DEG);
                    float xRot = this.minecart.onGround() && !this.minecart.isOnRails()
                        ? 0.0F
                        : 90.0F - (float) (Math.atan2(stepDelta.horizontalDistance(), stepDelta.y) * Mth.RAD_TO_DEG);
                    yRot += this.isFlipped() ? 180.0F : 0.0F;
                    xRot *= this.isFlipped() ? -1.0F : 1.0F;
                    this.setRotation(yRot, xRot);
                }
                
                this.lerpSteps
                    .add(
                        new MinecartStep(
                            stepPosition, this.getDeltaMovement(), this.getYRot(), this.getXRot(), (float)Math.min(stepLength, this.getMaxSpeed(level))
                        )
                    );
            } else if (initialStepDeltaMovement.horizontalDistanceSqr() > 0.0) {
                this.lerpSteps.add(new MinecartStep(stepPosition, this.getDeltaMovement(), this.getYRot(), this.getXRot(), 1.0F));
            }
            
            if (stepLength > Mth.EPSILON || trackIteration.firstIteration) {
                ((EntityAccessor) this.minecart).callCheckInsideBlocks();
                ((EntityAccessor) this.minecart).callCheckInsideBlocks();
            }
        }
    }
    
    private Vec3 calculateTrackSpeed(
        ServerLevel level,
        Vec3 deltaMovement,
        TrackIteration trackIteration,
        BlockPos currentPos,
        BlockState currentState,
        RailShape shape
    ) {
        Vec3 newDeltaMovement = deltaMovement;
        if (!trackIteration.hasGainedSlopeSpeed) {
            Vec3 slopedDeltaMovement = this.calculateSlopeSpeed(deltaMovement, shape);
            if (slopedDeltaMovement.horizontalDistanceSqr() != deltaMovement.horizontalDistanceSqr()) {
                trackIteration.hasGainedSlopeSpeed = true;
                newDeltaMovement = slopedDeltaMovement;
            }
        }
        
        if (trackIteration.firstIteration) {
            Vec3 playerInputMovement = this.calculatePlayerInputSpeed(newDeltaMovement);
            if (playerInputMovement.horizontalDistanceSqr() != newDeltaMovement.horizontalDistanceSqr()) {
                trackIteration.hasHalted = true;
                newDeltaMovement = playerInputMovement;
            }
        }
        
        if (!trackIteration.hasHalted) {
            Vec3 haltedDeltaMovement = this.calculateHaltTrackSpeed(newDeltaMovement, currentState);
            if (haltedDeltaMovement.horizontalDistanceSqr() != newDeltaMovement.horizontalDistanceSqr()) {
                trackIteration.hasHalted = true;
                newDeltaMovement = haltedDeltaMovement;
            }
        }
        
        if (trackIteration.firstIteration) {
            newDeltaMovement = this.applyNaturalSlowdown(newDeltaMovement);
            if (newDeltaMovement.lengthSqr() > 0.0) {
                double speed = Math.min(newDeltaMovement.length(), this.getMaxSpeed(level));
                newDeltaMovement = newDeltaMovement.normalize().scale(speed);
            }
        }
        
        if (!trackIteration.hasBoosted) {
            Vec3 boostedDeltaMovement = this.calculateBoostTrackSpeed(newDeltaMovement, currentPos, currentState);
            if (boostedDeltaMovement.horizontalDistanceSqr() != newDeltaMovement.horizontalDistanceSqr()) {
                trackIteration.hasBoosted = true;
                newDeltaMovement = boostedDeltaMovement;
            }
        }
        
        return newDeltaMovement;
    }
    
    private Vec3 calculateSlopeSpeed(Vec3 deltaMovement, RailShape shape) {
        double slideSpeed = Math.max(0.0078125, deltaMovement.horizontalDistance() * 0.02);
        if (this.minecart.isInWater()) {
            slideSpeed *= 0.2;
        }
        return switch (shape) {
            case ASCENDING_EAST -> deltaMovement.add(-slideSpeed, 0.0, 0.0);
            case ASCENDING_WEST -> deltaMovement.add(slideSpeed, 0.0, 0.0);
            case ASCENDING_NORTH -> deltaMovement.add(0.0, 0.0, slideSpeed);
            case ASCENDING_SOUTH -> deltaMovement.add(0.0, 0.0, -slideSpeed);
            default -> deltaMovement;
        };
    }
    
    private Vec3 calculatePlayerInputSpeed(Vec3 deltaMovement) {
        if (this.minecart.getFirstPassenger() instanceof ServerPlayer player) {
            Vec3 moveIntent = player.getDeltaMovement();
            if (moveIntent.lengthSqr() > 0.0) {
                Vec3 riderMovement = moveIntent.normalize();
                double ownDist = deltaMovement.horizontalDistanceSqr();
                if (riderMovement.lengthSqr() > 0.0 && ownDist < 0.01) {
                    return deltaMovement.add(new Vec3(riderMovement.x, 0.0, riderMovement.z).normalize().scale(0.001));
                }
            }
        }
        
        return deltaMovement;
    }
    
    private Vec3 calculateHaltTrackSpeed(Vec3 deltaMovement, BlockState state) {
        if (state.is(Blocks.POWERED_RAIL) && !state.getValue(PoweredRailBlock.POWERED)) {
            return deltaMovement.length() < 0.03 ? Vec3.ZERO : deltaMovement.scale(0.5);
        } else {
            return deltaMovement;
        }
    }
    
    private Vec3 calculateBoostTrackSpeed(Vec3 deltaMovement, BlockPos pos, BlockState state) {
        if (state.is(Blocks.POWERED_RAIL) && state.getValue(PoweredRailBlock.POWERED)) {
            if (deltaMovement.length() > 0.01) {
                return deltaMovement.normalize().scale(deltaMovement.length() + 0.06);
            } else {
                Vec3 powerDirection = this.getRedstoneDirection(pos);
                return powerDirection.lengthSqr() <= 0.0 ? deltaMovement : powerDirection.scale(deltaMovement.length() + 0.2);
            }
        } else {
            return deltaMovement;
        }
    }
    
    @Override
    public double stepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) {
        if (movementLeft < Mth.EPSILON) {
            return 0.0;
        } else {
            Vec3 oldPosition = this.position();
            Pair<Vec3i, Vec3i> exits = AbstractMinecartAccessor.callExits(shape);
            Vec3i exit0 = exits.getFirst();
            Vec3i exit1 = exits.getSecond();
            Vec3 movement = VectorUtils.horizontal(this.getDeltaMovement());
            if (movement.length() < Mth.EPSILON) {
                this.setDeltaMovement(Vec3.ZERO);
                return 0;
            } else {
                boolean inHill = exit0.getY() != exit1.getY();
                Vec3 horizontalInDirection = VectorUtils.horizontal(VectorUtils.create(exit1).scale(0.5));
                Vec3 horizontalOutDirection = VectorUtils.horizontal(VectorUtils.create(exit0).scale(0.5));
                if (movement.dot(horizontalOutDirection) < movement.dot(horizontalInDirection)) {
                    horizontalOutDirection = horizontalInDirection;
                }
                
                Vec3 outPosition = Vec3.atBottomCenterOf(pos).add(horizontalOutDirection).add(0.0, 0.1, 0.0).add(horizontalOutDirection.normalize().scale(Mth.EPSILON));
                if (inHill && !this.isDecending(movement, shape)) {
                    outPosition = outPosition.add(0.0, 1.0, 0.0);
                }
                
                Vec3 towardsOut = outPosition.subtract(this.position()).normalize();
                movement = towardsOut.scale(movement.length() / towardsOut.horizontalDistance());
                Vec3 newPosition = oldPosition.add(movement.normalize().scale(movementLeft * (inHill ? Mth.SQRT_OF_TWO : 1.0F)));
                if (oldPosition.distanceToSqr(outPosition) <= oldPosition.distanceToSqr(newPosition)) {
                    movementLeft = outPosition.subtract(newPosition).horizontalDistance();
                    newPosition = outPosition;
                } else {
                    movementLeft = 0.0;
                }
                
                this.minecart.move(MoverType.SELF, newPosition.subtract(oldPosition));
                BlockState newBlockState = this.level().getBlockState(BlockPos.containing(newPosition));
                if (inHill) {
                    if (BaseRailBlock.isRail(newBlockState)) {
                        RailShape newRailShape = newBlockState.getValue(((BaseRailBlock) newBlockState.getBlock()).getShapeProperty());
                        if (this.restAtVShape(shape, newRailShape)) {
                            return 0.0;
                        }
                    }
                    
                    double horizontalDistanceFromOut = VectorUtils.horizontal(outPosition).distanceTo(VectorUtils.horizontal(this.position()));
                    double projectYPos = outPosition.y + (this.isDecending(movement, shape) ? horizontalDistanceFromOut : -horizontalDistanceFromOut);
                    if (this.position().y < projectYPos) {
                        this.setPos(this.position().x, projectYPos, this.position().z);
                    }
                }
                
                if (this.position().distanceTo(oldPosition) < Mth.EPSILON && newPosition.distanceTo(oldPosition) > Mth.EPSILON) {
                    this.setDeltaMovement(Vec3.ZERO);
                    return 0.0;
                } else {
                    this.setDeltaMovement(movement);
                    return movementLeft;
                }
            }
        }
    }
    
    private boolean restAtVShape(RailShape currentRailShape, RailShape newRailShape) {
        if (this.getDeltaMovement().lengthSqr() < 0.005
            && newRailShape.isAscending()
            && this.isDecending(this.getDeltaMovement(), currentRailShape)
            && !this.isDecending(this.getDeltaMovement(), newRailShape)) {
            this.setDeltaMovement(Vec3.ZERO);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public double getMaxSpeed(ServerLevel level) {
        return 8 * (this.minecart.isInWater() ? 0.5 : 1.0) / 20.0; //TODO: add gamerule
    }
    
    private boolean isDecending(Vec3 movement, RailShape shape) {
        return switch (shape) {
            case ASCENDING_EAST -> movement.x < 0.0;
            case ASCENDING_WEST -> movement.x > 0.0;
            case ASCENDING_NORTH -> movement.z > 0.0;
            case ASCENDING_SOUTH -> movement.z < 0.0;
            default -> false;
        };
    }
    
    @Override
    public double getSlowdownFactor() {
        return this.minecart.isVehicle() ? 0.997 : 0.975;
    }
    
    @Override
    public boolean pushAndPickupEntities() {
        boolean pickedUp = this.pickupEntities(this.minecart.getBoundingBox().inflate(0.2, 0.0, 0.2));
        if (!this.minecart.horizontalCollision && !this.minecart.verticalCollision) {
            return false;
        } else {
            boolean pushed = this.pushEntities(this.minecart.getBoundingBox().inflate(MthUtils.EPSILON));
            return pickedUp && !pushed;
        }
    }
    
    public boolean pickupEntities(AABB hitbox) {
        if (this.minecart instanceof Minecart && !this.minecart.isVehicle()) {
            List<Entity> entities = this.level().getEntities(this.minecart, hitbox, EntitySelector.pushableBy(this.minecart));
            if (!entities.isEmpty()) {
                for (Entity entity : entities) {
                    if (!(entity instanceof Player)
                        && !(entity instanceof IronGolem)
                        && !(entity instanceof AbstractMinecart)
                        && !this.minecart.isVehicle()
                        && !entity.isPassenger()) {
                        boolean pickedUp = entity.startRiding(this.minecart);
                        if (pickedUp) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean pushEntities(AABB hitbox) {
        boolean pushed = false;
        if (this.minecart instanceof Minecart) {
            List<Entity> entities = this.level().getEntities(this.minecart, hitbox, EntitySelector.pushableBy(this.minecart));
            if (!entities.isEmpty()) {
                for (Entity entity : entities) {
                    if (entity instanceof Player || entity instanceof IronGolem || entity instanceof AbstractMinecart || this.minecart.isVehicle() || entity.isPassenger()) {
                        entity.push(this.minecart);
                        pushed = true;
                    }
                }
            }
        } else {
            for (Entity entity : this.level().getEntities(this.minecart, hitbox)) {
                if (!this.minecart.hasPassenger(entity) && entity.isPushable() && entity instanceof AbstractMinecart) {
                    entity.push(this.minecart);
                    pushed = true;
                }
            }
        }
        
        return pushed;
    }
    
    public record MinecartStep(Vec3 position, Vec3 movement, float yRot, float xRot, float weight) {
        public static final StreamCodec<ByteBuf, NewMinecartBehavior.MinecartStep> STREAM_CODEC = StreamCodec.composite(
            AdditionalCodecs.VEC3_STREAM_CODEC, MinecartStep::position,
            AdditionalCodecs.VEC3_STREAM_CODEC, MinecartStep::movement,
            AdditionalCodecs.ROTATION_BYTE, MinecartStep::yRot,
            AdditionalCodecs.ROTATION_BYTE, MinecartStep::xRot,
            ByteBufCodecs.FLOAT, MinecartStep::weight,
            MinecartStep::new
        );
        
        public static final MinecartStep ZERO = new MinecartStep(Vec3.ZERO, Vec3.ZERO, 0.0F, 0.0F, 0.0F);
    }
    
    private record StepPartialTicks(float partialTicksInStep, MinecartStep currentStep, MinecartStep previousStep) { /* NO-OP */}
    
    private static class TrackIteration {
        public double movementLeft = 0.0;
        public boolean firstIteration = true;
        public boolean hasGainedSlopeSpeed = false;
        public boolean hasHalted = false;
        public boolean hasBoosted = false;
        
        public boolean shouldIterate() {
            return this.firstIteration || this.movementLeft > Mth.EPSILON;
        }
    }
}