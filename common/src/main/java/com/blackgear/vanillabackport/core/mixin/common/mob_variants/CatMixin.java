package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cat.CatDataVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cat.CatDataVariants;
import com.blackgear.vanillabackport.common.registries.entities.ModSyncedEntityData;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal implements VariantDataHolder<CatDataVariant>, EntityDataHolder, MobBehaviorAccess {
    @Shadow @Final private static EntityDataAccessor<Integer> DATA_COLLAR_COLOR;
    @Shadow public abstract DyeColor getCollarColor();
    
    protected CatMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setVariantData(CatDataVariant variant) {
        VariantUtils.setVariant(this, variant, CatDataVariants.REGISTRIES, ModSyncedEntityData.CAT_VARIANTS);
    }

    @Override
    public Optional<CatDataVariant> getVariantData() {
        return Optional.ofNullable(VariantUtils.getVariant(this, CatDataVariants.REGISTRIES, ModSyncedEntityData.CAT_VARIANTS));
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, CatDataVariants.REGISTRIES);
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
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