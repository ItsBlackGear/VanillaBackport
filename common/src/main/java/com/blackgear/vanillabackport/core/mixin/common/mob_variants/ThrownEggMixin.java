package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.registries.entities.ModSyncedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin extends ThrowableItemProjectile implements EntityDataHolder, VariantDataHolder<ChickenVariant> {
    public ThrownEggMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public void setVariantData(ChickenVariant variant) {
        VariantUtils.setVariant(this, variant, ChickenVariants.REGISTRIES, ModSyncedEntityData.CHICKEN_VARIANTS);
    }

    @Override
    public Optional<ChickenVariant> getVariantData() {
        return Optional.ofNullable(VariantUtils.getVariant(this, ChickenVariants.REGISTRIES, ModSyncedEntityData.CHICKEN_VARIANTS));
    }
    
    @Override
    public void vb$addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, ChickenVariants.REGISTRIES);
    }
    
    @Override
    public void vb$readAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, ChickenVariants.REGISTRIES);
    }

    @Inject(
        method = "onHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        require = 0
    )
    private void setChickenVariant(HitResult result, CallbackInfo ci, int i, int j, Chicken chicken) {
        this.getVariantData().ifPresent(variant -> VariantDataHolder.<ChickenVariant>getHolder(chicken).ifPresent(holder -> holder.setVariantData(variant)));
    }
}