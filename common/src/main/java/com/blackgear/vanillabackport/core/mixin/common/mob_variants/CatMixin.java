package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cat.CatDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cat.CatDataVariants;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal implements EntityDataHolder, MobBehaviorAccess, VariantDataHolder<CatDataVariant> {
    @Shadow @Final private static EntityDataAccessor<Integer> DATA_COLLAR_COLOR;
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.STRING);
    
    @Shadow public abstract DyeColor getCollarColor();
    
    protected CatMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void vb$defineSynchedData() {
        this.entityData.define(DATA_VARIANT_ID, "minecraft:tabby");
    }

    @Override
    public void setVariantData(CatDataVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(CatDataVariants.REGISTRIES, variant));
    }

    @Override
    public Optional<CatDataVariant> getVariantData() {
        return VariantUtils.getOrDefault(CatDataVariants.REGISTRIES, this.entityData.get(DATA_VARIANT_ID));
    }
    
    @Override
    public void vb$addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.addVariantSaveData(this, tag, CatDataVariants.REGISTRIES);
    }
    
    @Override
    public void vb$readAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, CatDataVariants.REGISTRIES);
    }

    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), CatDataVariants.REGISTRIES)
            .ifPresent(this::setVariantData);
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

            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }
}
