package com.blackgear.vanillabackport.common.level.entity.decoration;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public abstract class BlockAttachedEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int checkInterval;
    protected BlockPos pos;
    
    protected BlockAttachedEntity(EntityType<? extends BlockAttachedEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    protected BlockAttachedEntity(EntityType<? extends BlockAttachedEntity> entityType, Level level, BlockPos pos) {
        this(entityType, level);
        this.pos = pos;
    }
    
    protected abstract void recalculateBoundingBox();
    
    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            this.checkBelowWorld();
            if (this.checkInterval++ == 100) {
                this.checkInterval = 0;
                if (!this.isRemoved() && !this.survives()) {
                    this.discard();
                    this.dropItem(null);
                }
            }
        }
    }
    
    public abstract boolean survives();
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    public boolean skipAttackInteraction(Entity entity) {
        if (entity instanceof Player player) {
            return !this.level().mayInteract(player, this.pos) ? true : this.hurt(this.damageSources().playerAttack(player), 0.0F);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.isRemoved() && !this.level().isClientSide) {
                this.kill();
                this.markHurt();
                this.dropItem(source.getEntity());
            }
            
            return true;
        }
    }
    
    @Override
    public void move(MoverType type, Vec3 pos) {
        if (!this.level().isClientSide && !this.isRemoved() && pos.lengthSqr() > 0.0) {
            this.kill();
            this.dropItem(null);
        }
    }
    
    @Override
    public void push(double x, double y, double z) {
        if (!this.level().isClientSide && !this.isRemoved() && x * x + y * y + z * z > 0.0) {
            this.kill();
            this.dropItem(null);
        }
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        BlockPos blockPos = this.getPos();
        compound.putInt("TileX", blockPos.getX());
        compound.putInt("TileY", blockPos.getY());
        compound.putInt("TileZ", blockPos.getZ());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        BlockPos blockPos = new BlockPos(compound.getInt("TileX"), compound.getInt("TileY"), compound.getInt("TileZ"));
        if (!blockPos.closerThan(this.blockPosition(), 16.0)) {
            LOGGER.error("Block-attached entity at invalid position: {}", blockPos);
        } else {
            this.pos = blockPos;
        }
    }
    
    public abstract void dropItem(@Nullable Entity entity);
    
    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }
    
    @Override
    public void setPos(double x, double y, double z) {
        this.pos = BlockPos.containing(x, y, z);
        this.recalculateBoundingBox();
        this.hasImpulse = true;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
    }
    
    @Override
    public void refreshDimensions() {
    }
}