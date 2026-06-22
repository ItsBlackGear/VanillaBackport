package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantSpawner;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.core.data.ModBuiltInLootTables;
import com.blackgear.vanillabackport.core.util.WorldUtilities.LootUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Chicken.class)
public abstract class ChickenMixin extends MobMixin implements VariantDataHolder<ChickenVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.STRING);

    protected ChickenMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Chicken;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Chicken> cir) {
        Chicken child = cir.getReturnValue();
        if (child != null && otherParent instanceof Chicken mate) {
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_VARIANT_ID, "minecraft:temperate");
    }

    @Override
    public void setVariantData(ChickenVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(ChickenVariants.REGISTRIES, variant));
    }

    @Override
    public Optional<ChickenVariant> getVariantData() {
        return VariantUtils.getOrDefault(ChickenVariants.REGISTRIES, this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, ChickenVariants.REGISTRIES);
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, ChickenVariants.REGISTRIES);
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ChickenVariants.REGISTRIES, VariantSpawner.FARM_ANIMALS)
            .ifPresent(this::setVariantData);
    }

    @ModifyArg(
        method = "aiStep",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Chicken;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"),
        index = 0
    )
    private ItemLike vb$modifyEggDrop(ItemLike originalItem) {
        Optional<ChickenVariant> variant = this.getVariantData();

        if (variant.isPresent() && !VariantUtils.matches(ChickenVariants.REGISTRIES, variant.get(), ChickenVariants.TEMPERATE)) {
            if (LootUtils.dropFromGiftLootTable(this, (ServerLevel) this.level(), ModBuiltInLootTables.CHICKEN_LAY, (level, stack) -> this.spawnAtLocation(stack))) {
                return Items.AIR;
            }
        }

        return originalItem;
    }
}