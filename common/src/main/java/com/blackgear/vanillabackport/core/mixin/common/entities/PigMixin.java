package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariantHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pig.class)
public abstract class PigMixin extends MobMixin implements AnimalVariantHolder {
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.STRING);

    protected PigMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Pig;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Pig> cir) {
        Pig child = cir.getReturnValue();
        if (child != null && otherParent instanceof Pig mate) {
            AnimalVariantHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    public AnimalVariant getVariant() {
        return AnimalVariant.getByName(this.getVariantName());
    }

    @Unique
    private String getVariantName() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public void setVariant(AnimalVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.getName());
    }

    @Override
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putString("Variant", this.getVariantName());
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.entityData.set(DATA_VARIANT_ID, tag.getString("Variant"));
    }

    @Override
    protected void vb$defineSynchedData(CallbackInfo ci) {
        super.vb$defineSynchedData(ci);
        this.entityData.define(DATA_VARIANT_ID, AnimalVariant.DEFAULT.getName());
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        AnimalVariant.selectVariantToSpawn(level, this, reason);
    }
}