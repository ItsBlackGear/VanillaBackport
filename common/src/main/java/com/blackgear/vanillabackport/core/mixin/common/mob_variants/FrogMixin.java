package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.frog.FrogDataVariants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(Frog.class)
public abstract class FrogMixin extends Animal implements EntityDataHolder, MobBehaviorAccess, VariantDataHolder<FrogDataVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.STRING);

    protected FrogMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void vb$defineSynchedData() {
        this.entityData.define(DATA_VARIANT_ID, "minecraft:temperate");
    }

    @Override
    public void setVariantData(FrogDataVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(FrogDataVariants.REGISTRIES, variant));
    }

    @Override
    public Optional<FrogDataVariant> getVariantData() {
        return VariantUtils.getOrDefault(FrogDataVariants.REGISTRIES, this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public void vb$addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.addVariantSaveData(this, tag, FrogDataVariants.REGISTRIES);
    }

    @Override
    public void vb$readAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, FrogDataVariants.REGISTRIES);
    }

    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), FrogDataVariants.REGISTRIES)
            .ifPresent(this::setVariantData);
    }
}