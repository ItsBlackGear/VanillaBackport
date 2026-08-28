package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.frog.FrogDataVariants;
import com.blackgear.vanillabackport.common.registries.entities.ModSyncedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Frog.class)
public abstract class FrogMixin extends Animal implements VariantDataHolder<FrogDataVariant>, EntityDataHolder, MobBehaviorAccess {
    protected FrogMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public void setVariantData(FrogDataVariant variant) {
        VariantUtils.setVariant(this, variant, FrogDataVariants.REGISTRIES, ModSyncedEntityData.FROG_VARIANTS);
    }

    @Override
    public Optional<FrogDataVariant> getVariantData() {
        return Optional.ofNullable(VariantUtils.getVariant(this, FrogDataVariants.REGISTRIES, ModSyncedEntityData.FROG_VARIANTS));
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, FrogDataVariants.REGISTRIES);
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, FrogDataVariants.REGISTRIES);
    }

    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), FrogDataVariants.REGISTRIES)
            .ifPresent(this::setVariantData);
    }
}