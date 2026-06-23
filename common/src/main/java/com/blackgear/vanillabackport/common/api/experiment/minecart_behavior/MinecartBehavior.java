package com.blackgear.vanillabackport.common.api.experiment.minecart_behavior;

import com.blackgear.vanillabackport.common.api.modules.leash_behavior.InterpolationHandler;
import com.blackgear.vanillabackport.core.mixin.common.access.AbstractMinecartAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

public abstract class MinecartBehavior {
    protected final AbstractMinecart minecart;
    private boolean flipped;
    
    protected MinecartBehavior(final AbstractMinecart minecart) {
        this.minecart = minecart;
    }
    
    public InterpolationHandler getInterpolation() {
        return null;
    }
    
    public void lerpMotion(final Vec3 movement) {
        this.setDeltaMovement(movement);
    }
    
    public abstract void tick();
    
    public Level level() {
        return this.minecart.level();
    }
    
    public abstract void moveAlongTrack(ServerLevel level);
    
    public abstract double stepAlongTrack(final BlockPos pos, final RailShape shape, final double movementLeft);
    
    public abstract boolean pushAndPickupEntities();
    
    public Vec3 getDeltaMovement() {
        return this.minecart.getDeltaMovement();
    }
    
    public void setDeltaMovement(final Vec3 deltaMovement) {
        this.minecart.setDeltaMovement(deltaMovement);
    }
    
    public void setDeltaMovement(final double x, final double y, final double z) {
        this.minecart.setDeltaMovement(x, y, z);
    }
    
    public Vec3 position() {
        return this.minecart.position();
    }
    
    public double getX() {
        return this.minecart.getX();
    }
    
    public double getY() {
        return this.minecart.getY();
    }
    
    public double getZ() {
        return this.minecart.getZ();
    }
    
    public void setPos(final Vec3 pos) {
        this.minecart.setPos(pos);
    }
    
    public void setPos(final double x, final double y, final double z) {
        this.minecart.setPos(x, y, z);
    }
    
    public float getXRot() {
        return this.minecart.getXRot();
    }
    
    public void setXRot(final float rot) {
        this.minecart.setXRot(rot);
    }
    
    public float getYRot() {
        return this.minecart.getYRot();
    }
    
    public void setYRot(final float rot) {
        this.minecart.setYRot(rot);
    }
    
    public Direction getMotionDirection() {
        return this.minecart.getDirection();
    }
    
    public Vec3 getKnownMovement(final Vec3 knownMovement) {
        return knownMovement;
    }
    
    public abstract double getMaxSpeed(ServerLevel level);
    
    public abstract double getSlowdownFactor();
    
    // HELPER METHODS
    
    public boolean isFlipped() {
        return this.flipped;
    }
    
    public void setFlipped(final boolean flipped) {
        this.flipped = flipped;
    }
    
    public Vec3 getRedstoneDirection(BlockPos pos) {
        BlockState state = this.minecart.level().getBlockState(pos);
        if (state.is(Blocks.POWERED_RAIL) && state.getValue(PoweredRailBlock.POWERED)) {
            RailShape shape = state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty());
            if (shape == RailShape.EAST_WEST) {
                if (((AbstractMinecartAccessor) this.minecart).callIsRedstoneConductor(pos.west())) {
                    return new Vec3(1.0, 0.0, 0.0);
                }
                
                if (((AbstractMinecartAccessor) this.minecart).callIsRedstoneConductor(pos.east())) {
                    return new Vec3(-1.0, 0.0, 0.0);
                }
            } else if (shape == RailShape.NORTH_SOUTH) {
                if (((AbstractMinecartAccessor) this.minecart).callIsRedstoneConductor(pos.north())) {
                    return new Vec3(0.0, 0.0, 1.0);
                }
                
                if (((AbstractMinecartAccessor) this.minecart).callIsRedstoneConductor(pos.south())) {
                    return new Vec3(0.0, 0.0, -1.0);
                }
            }
        }
        
        return Vec3.ZERO;
    }
    
    protected Vec3 applyNaturalSlowdown(Vec3 movement) {
        double slowdownFactor = this.getSlowdownFactor();
        Vec3 newMovement = movement.multiply(slowdownFactor, 0.0F, slowdownFactor);
        if (this.minecart.isInWater()) {
            newMovement = newMovement.scale(0.95F);
        }
        
        return newMovement;
    }
    
    public BlockPos getCurrentBlockPosOrRailBelow() {
        int xt = Mth.floor(this.getX());
        int yt = Mth.floor(this.getY());
        int zt = Mth.floor(this.getZ());
        if (useExperimentalMovement(this.level())) {
            double y = this.getY() - 0.1 - Mth.EPSILON;
            if (this.level().getBlockState(BlockPos.containing(xt, y, zt)).is(BlockTags.RAILS)) {
                yt = Mth.floor(y);
            }
        } else if (this.level().getBlockState(new BlockPos(xt, yt - 1, zt)).is(BlockTags.RAILS)) {
            yt--;
        }
        
        return new BlockPos(xt, yt, zt);
    }
    
    protected void comeOffTrack(ServerLevel level) {
        double maxSpeed = this.getMaxSpeed(level);
        Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(Mth.clamp(movement.x, -maxSpeed, maxSpeed), movement.y, Mth.clamp(movement.z, -maxSpeed, maxSpeed));
        if (this.minecart.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
        }
        
        this.minecart.move(MoverType.SELF, this.getDeltaMovement());
        if (!this.minecart.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95F));
        }
    }
    
    public static boolean useExperimentalMovement(final Level level) {
//        return level.enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS);
        return true;
    }
}