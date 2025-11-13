package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.variant.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariants;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
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
            VariantHolder.vb$trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    public ChickenVariant vb$getVariant() {
        return VariantUtils.getVariant(ModBuiltinRegistries.CHICKEN_VARIANTS, this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public void vb$setVariant(ChickenVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(ModBuiltinRegistries.CHICKEN_VARIANTS, variant));
    }

    @Override
    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_VARIANT_ID, VariantUtils.getDefaultID(ModBuiltinRegistries.CHICKEN_VARIANTS, ChickenVariants.TEMPERATE));
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
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ModBuiltinRegistries.CHICKEN_VARIANTS, ChickenVariants.TEMPERATE)
            .ifPresent(this::vb$setVariant);
    }

    @ModifyArg(
        method = "aiStep",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Chicken;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"),
        index = 0
    )
    private ItemLike vb$modifyEggDrop(ItemLike originalItem) {
        Chicken chicken = (Chicken) (Object) this;
        if (chicken instanceof VariantHolder<?> holder) {
            if (holder.vb$getVariant() instanceof ChickenVariant variant) {
                if (VariantUtils.matches(ModBuiltinRegistries.CHICKEN_VARIANTS, variant, ChickenVariants.COLD)) {
                    return ModItems.BLUE_EGG.get();
                }

                if (VariantUtils.matches(ModBuiltinRegistries.CHICKEN_VARIANTS, variant, ChickenVariants.WARM)) {
                    return ModItems.BROWN_EGG.get();
                }
            }
        }

        return originalItem;
    }
}