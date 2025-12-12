package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.variant.EnhancedVariants;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.animal.EnhancedFrogVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.EnhancedFrogVariants;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frog.class)
public abstract class FrogMixin extends MobMixin implements VariantHolder<EnhancedFrogVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.STRING);

    @Shadow public abstract Holder<FrogVariant> getVariant();

    protected FrogMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_VARIANT_ID, "minecraft:temperate");
    }

    @Override
    public void vb$setVariant(EnhancedFrogVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(ModBuiltinRegistries.FROG_VARIANTS, variant));
    }

    @Override
    public EnhancedFrogVariant vb$getVariant() {
        return VariantUtils.getOrDefault(ModBuiltinRegistries.FROG_VARIANTS, this.entityData.get(DATA_VARIANT_ID), EnhancedFrogVariants.TEMPERATE);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void vb$addAdditionalData(CompoundTag tag, CallbackInfo ci) {
        EnhancedVariants.addVariantSaveData(this, tag, ModBuiltinRegistries.FROG_VARIANTS);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void vb$readAdditionalData(CompoundTag tag, CallbackInfo ci) {
        EnhancedVariants.readVariantSaveData(this, tag, ModBuiltinRegistries.FROG_VARIANTS);
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (EnhancedVariants.hasVariantInclusive(BuiltInRegistries.FROG_VARIANT, this.getVariant().value(), ModBuiltinRegistries.FROG_VARIANTS)) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ModBuiltinRegistries.FROG_VARIANTS)
                .ifPresent(this::vb$setVariant);
        }
    }
}