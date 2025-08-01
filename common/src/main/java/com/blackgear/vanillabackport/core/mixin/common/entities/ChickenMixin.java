package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.variant.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariants;
import com.blackgear.vanillabackport.common.registries.ModEntityDataSerializers;
import com.blackgear.vanillabackport.common.registries.ModItems;
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
import net.minecraft.world.entity.animal.Chicken;
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

@Mixin(Chicken.class)
public abstract class ChickenMixin extends MobMixin implements VariantHolder<ChickenVariant> {
    @Unique private static final EntityDataAccessor<ChickenVariant> DATA_VARIANT_ID = SynchedEntityData.defineId(Chicken.class, ModEntityDataSerializers.CHICKEN_VARIANT);

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
            VariantHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    public ChickenVariant getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public void setVariant(ChickenVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    @Override
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, ModBuiltinRegistries.CHICKEN_VARIANTS);
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, ModBuiltinRegistries.CHICKEN_VARIANTS);
    }

    @Override
    protected void vb$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_VARIANT_ID, VariantUtils.getDefault(ModBuiltinRegistries.CHICKEN_VARIANTS, ChickenVariants.TEMPERATE));
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ModBuiltinRegistries.CHICKEN_VARIANTS, ChickenVariants.TEMPERATE)
            .ifPresent(this::setVariant);
    }

    @ModifyArg(
        method = "aiStep",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Chicken;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"),
        index = 0
    )
    private ItemLike vb$modifyEggDrop(ItemLike originalItem) {
        Chicken chicken = (Chicken) (Object) this;
        if (chicken instanceof VariantHolder<?> holder) {
            if (holder.getVariant() instanceof ChickenVariant variant) {
                if (variant == ModBuiltinRegistries.CHICKEN_VARIANTS.get(ChickenVariants.COLD)) {
                    return ModItems.BLUE_EGG.get();
                }

                if (variant == ModBuiltinRegistries.CHICKEN_VARIANTS.get(ChickenVariants.WARM)) {
                    return ModItems.BROWN_EGG.get();
                }
            }
        }

        return originalItem;
    }
}