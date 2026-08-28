package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantSpawner;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.pig.PigVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.pig.PigVariants;
import com.blackgear.vanillabackport.common.registries.entities.ModSyncedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Pig.class)
public abstract class PigMixin extends Animal implements VariantDataHolder<PigVariant>, EntityDataHolder, MobBehaviorAccess {
    protected PigMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setVariantData(PigVariant variant) {
        VariantUtils.setVariant(this, variant, PigVariants.REGISTRIES, ModSyncedEntityData.PIG_VARIANTS);
    }

    @Override
    public Optional<PigVariant> getVariantData() {
        return Optional.ofNullable(VariantUtils.getVariant(this, PigVariants.REGISTRIES, ModSyncedEntityData.PIG_VARIANTS));
    }

    @Override
    public void vb$addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.addVariantSaveData(this, tag, PigVariants.REGISTRIES);
    }

    @Override
    public void vb$readAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, PigVariants.REGISTRIES);
    }

    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), PigVariants.REGISTRIES, VariantSpawner.FARM_ANIMALS)
            .ifPresent(this::setVariantData);
    }
    
    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Pig;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Pig> cir) {
        Pig child = cir.getReturnValue();
        if (child != null && otherParent instanceof Pig mate) {
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }
}