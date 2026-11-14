package com.blackgear.vanillabackport.core.mixin.common.frozen_mobs;

import com.blackgear.platform.common.data.entity.SyncedDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.entity.conversion.FrostbiteConversion;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModSyncedEntityData;
import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements FrostbiteConversion {
    @Shadow private int conversionTime;
    @Unique private int vb$inPowderSnowTime;
    
    @Shadow protected abstract void convertToZombieType(EntityType<? extends Zombie> entityType);
    @Shadow protected abstract void doUnderWaterConversion();
    
    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void vb$tick(CallbackInfo ci) {
        if (!this.level().isClientSide && this.isAlive() && !this.isNoAi()) {
            if (this.isInPowderSnow && this.getType().is(ModEntityTypeTags.ZOMBIES_THAT_CONVERT_ON_POWDER_SNOW)) {
                if (this.vb$isFreezeConverting()) {
                    this.conversionTime--;
                    if (this.conversionTime < 0) {
                        this.vb$doFreezeConversion();
                    }
                } else {
                    this.vb$inPowderSnowTime++;
                    if (this.vb$inPowderSnowTime >= 140) {
                        this.vb$startFreezingConversion(300);
                    }
                }
            } else {
                this.vb$inPowderSnowTime = -1;
                this.vb$setFreezeConverting(false);
            }
        }
    }
    
    @Unique
    protected void vb$doFreezeConversion() {
        if (this.getType() == EntityType.ZOMBIE) {
            this.convertToZombieType(ModEntityTypes.FROSTBITE.get());
            if (!this.isSilent()) {
                LevelEvents.CONVERT_ZOMBIE_TO_FROSTBITE.trigger(this.level(), this.blockPosition(), 0);
            }
        } else if (this.getType() == EntityType.DROWNED) {
            this.convertToZombieType(EntityType.ZOMBIE);
            if (!this.isSilent()) {
                LevelEvents.CONVERT_DROWNED_TO_ZOMBIE.trigger(this.level(), this.blockPosition(), 0);
            }
        } else {
            this.doUnderWaterConversion();
        }
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void vb$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("FrostbiteConversionTime", this.vb$isFreezeConverting() ? this.conversionTime : -1);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void vb$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("FrostbiteConversionTime", 99) && compound.getInt("FrostbiteConversionTime") > -1) {
            this.vb$startFreezingConversion(compound.getInt("FrostbiteConversionTime"));
        }
    }
    
    @Unique
    private void vb$startFreezingConversion(int conversionTime) {
        this.conversionTime = conversionTime;
        this.vb$setFreezeConverting(true);
    }
    
    @Override
    public boolean vb$isFreezeConverting() {
        return SyncedDataHolder.get(this).get(ModSyncedEntityData.FROSTBITE_CONVERSION);
    }
    
    @Unique
    public void vb$setFreezeConverting(boolean isFrozen) {
        SyncedDataHolder.get(this).set(ModSyncedEntityData.FROSTBITE_CONVERSION, isFrozen);
    }
}