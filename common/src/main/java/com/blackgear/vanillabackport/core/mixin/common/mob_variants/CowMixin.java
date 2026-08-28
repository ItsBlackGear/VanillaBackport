package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantSpawner;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cow.CowVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cow.CowVariants;
import com.blackgear.vanillabackport.common.registries.entities.ModSyncedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Cow.class)
public abstract class CowMixin extends Animal implements VariantDataHolder<CowVariant>, EntityDataHolder, MobBehaviorAccess {
    protected CowMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public void setVariantData(CowVariant variant) {
        VariantUtils.setVariant(this, variant, CowVariants.REGISTRIES, ModSyncedEntityData.COW_VARIANTS);
    }
    
    @Override
    public Optional<CowVariant> getVariantData() {
        return Optional.ofNullable(VariantUtils.getVariant(this, CowVariants.REGISTRIES, ModSyncedEntityData.COW_VARIANTS));
    }
    
    @Override
    public void vb$addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.addVariantSaveData(this, tag, CowVariants.REGISTRIES);
    }
    
    @Override
    public void vb$readAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, CowVariants.REGISTRIES);
    }
    
    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), CowVariants.REGISTRIES, VariantSpawner.FARM_ANIMALS)
            .ifPresent(this::setVariantData);
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
}