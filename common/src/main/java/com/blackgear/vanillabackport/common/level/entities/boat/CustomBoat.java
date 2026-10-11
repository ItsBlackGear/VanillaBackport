package com.blackgear.vanillabackport.common.level.entities.boat;

import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CustomBoat extends Boat implements CustomBoatBehavior {
    private static final EntityDataAccessor<String> BOAT_TYPE_ID = SynchedEntityData.defineId(CustomBoat.class, EntityDataSerializers.STRING);
    
    public CustomBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public CustomBoat(Level level, double x, double y, double z, ResourceLocation type) {
        this(ModEntityTypes.CUSTOM_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setBoatType(type);
    }
    
    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (onGround) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Status.ON_LAND) {
                        this.resetFallDistance();
                        return;
                    }
                    
                    this.causeFallDamage(this.fallDistance, 1.0F, this.damageSources().fall());
                    if (!this.level().isClientSide() && !this.isRemoved()) {
                        this.kill();
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int i = 0; i < 3; i++) {
                                this.spawnAtLocation(this.getBoatType().getPlanks());
                            }
                            
                            for (int i = 0; i < 3; i++) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }
                
                this.resetFallDistance();
            } else if (!this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0) {
                this.fallDistance -= (float) y;
            }
        }
    }
    
    @Override
    public Item getDropItem() {
        return this.getBoatType().getBoat();
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOAT_TYPE_ID, BoatRegistry.DEFAULT_TYPE.toString());
    }
    
    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("BoatType", this.getBoatType().id().toString());
    }
    
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BoatType", 8)) {
            ResourceLocation id = ResourceLocation.tryParse(compound.getString("BoatType"));
            if (id != null) this.setBoatType(id);
        }
    }
    
    public void setBoatType(ResourceLocation variant) {
        this.entityData.set(BOAT_TYPE_ID, variant.toString());
    }
    
    @Override
    public BoatRegistry.BoatType getBoatType() {
        ResourceLocation id = ResourceLocation.tryParse(this.entityData.get(BOAT_TYPE_ID));
        return BoatRegistry.getType(id != null ? id : BoatRegistry.DEFAULT_TYPE);
    }
}