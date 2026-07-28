package com.blackgear.vanillabackport.common.level.entities.happyghast;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * added for retro-compatibility with Happy Ghast Boost, highly recommended for the dev to migrate!
 */
@Deprecated(forRemoval = true)
public class HappyGhast extends com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast {
    public HappyGhast(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
    
    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        return super.getRiddenInput(player, travelVector);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }
    
    @Override
    public void tick() {
        super.tick();
    }
}
