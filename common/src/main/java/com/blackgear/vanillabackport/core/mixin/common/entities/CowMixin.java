package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.variant.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariants;
import com.blackgear.vanillabackport.common.registries.ModEntityDataSerializers;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
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

@Mixin(Cow.class)
public abstract class CowMixin extends MobMixin implements VariantHolder<CowVariant> {
    @Unique private static final EntityDataAccessor<CowVariant> DATA_VARIANT_ID = SynchedEntityData.defineId(Cow.class, ModEntityDataSerializers.COW_VARIANT);

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
            VariantHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    public CowVariant getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public void setVariant(CowVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    @Override
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, ModBuiltinRegistries.COW_VARIANTS);
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, ModBuiltinRegistries.COW_VARIANTS);
    }

    @Override
    protected void vb$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_VARIANT_ID, VariantUtils.getDefault(ModBuiltinRegistries.COW_VARIANTS, CowVariants.TEMPERATE));
    }

    @Override
    protected void vb$finalizeSpawn(
        ServerLevelAccessor level,
        DifficultyInstance difficulty,
        MobSpawnType reason,
        SpawnGroupData spawnData,
        CompoundTag dataTag,
        CallbackInfoReturnable<SpawnGroupData> cir
    ) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ModBuiltinRegistries.COW_VARIANTS).ifPresent(this::setVariant);
    }
}