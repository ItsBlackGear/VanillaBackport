package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.variant.EnhancedVariants;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.level.entities.animal.EnhancedCatVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.EnhancedCatVariants;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.blackgear.vanillabackport.core.util.ColorUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimalMixin implements VariantHolder<EnhancedCatVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.STRING);

    @Shadow public abstract Holder<CatVariant> getVariant();

    @Shadow public abstract DyeColor getCollarColor();

    @Shadow @Final private static EntityDataAccessor<Integer> DATA_COLLAR_COLOR;

    protected CatMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_VARIANT_ID, "minecraft:tabby");
    }

    @Override
    public void vb$setVariant(EnhancedCatVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(ModBuiltinRegistries.CAT_VARIANTS, variant));
    }

    @Override
    public EnhancedCatVariant vb$getVariant() {
        return VariantUtils.getOrDefault(ModBuiltinRegistries.CAT_VARIANTS, this.entityData.get(DATA_VARIANT_ID), EnhancedCatVariants.TABBY);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void vb$addAdditionalData(CompoundTag tag, CallbackInfo ci) {
        EnhancedVariants.addVariantSaveData(this, tag, ModBuiltinRegistries.CAT_VARIANTS);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void vb$readAdditionalData(CompoundTag tag, CallbackInfo ci) {
        EnhancedVariants.readVariantSaveData(this, tag, ModBuiltinRegistries.CAT_VARIANTS);
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (EnhancedVariants.hasVariantInclusive(BuiltInRegistries.CAT_VARIANT, this.getVariant().value(), ModBuiltinRegistries.CAT_VARIANTS)) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ModBuiltinRegistries.CAT_VARIANTS)
                .ifPresent(this::vb$setVariant);
        }
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Cat;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Cat> cir) {
        Cat child = cir.getReturnValue();
        if (child != null && otherParent instanceof Cat mate) {
            if (this.isTame()) {
                DyeColor fatherColor = this.getCollarColor();
                DyeColor motherColor = mate.getCollarColor();
                child.getEntityData().set(DATA_COLLAR_COLOR, ColorUtils.getMixedColor(level, fatherColor, motherColor).getId());
            }

            if (EnhancedVariants.hasVariantInclusive(BuiltInRegistries.CAT_VARIANT, this.getVariant().value(), ModBuiltinRegistries.CAT_VARIANTS)
                && EnhancedVariants.hasVariantInclusive(BuiltInRegistries.CAT_VARIANT, mate.getVariant().value(), ModBuiltinRegistries.CAT_VARIANTS)) {
                VariantHolder.vb$trySetOffspringVariant(child, this, mate);
            }
        }
    }
}