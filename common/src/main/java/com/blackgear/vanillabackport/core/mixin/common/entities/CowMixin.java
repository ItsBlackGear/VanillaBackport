package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.variant.VariantSpawner;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariants;
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
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Cow.class)
public abstract class CowMixin extends MobMixin implements VariantDataHolder<CowVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Cow.class, EntityDataSerializers.STRING);

    protected CowMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Cow;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Cow> cir) {
        Cow child = cir.getReturnValue();
        if (child != null && otherParent instanceof Cow mate) {
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    protected void vb$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_VARIANT_ID, "minecraft:temperate");
    }

    @Override
    public void setVariantData(CowVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(CowVariants.REGISTRIES, variant));
    }

    @Override
    public Optional<CowVariant> getVariantData() {
        return VariantUtils.getOrDefault(CowVariants.REGISTRIES, this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, CowVariants.REGISTRIES);
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, CowVariants.REGISTRIES);
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), CowVariants.REGISTRIES, VariantSpawner.FARM_ANIMALS)
            .ifPresent(this::setVariantData);
    }
}