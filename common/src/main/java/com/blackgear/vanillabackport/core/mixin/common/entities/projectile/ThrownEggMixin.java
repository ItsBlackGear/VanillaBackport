package com.blackgear.vanillabackport.core.mixin.common.entities.projectile;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin extends ThrowableItemProjectileMixin implements VariantDataHolder<ChickenVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(ThrownEgg.class, EntityDataSerializers.STRING);

    public ThrownEggMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void vb$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_VARIANT_ID, "minecraft:temperate");
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
    protected void addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, ChickenVariants.REGISTRIES);
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, ChickenVariants.REGISTRIES);
    }

    @Inject(
        method = "onHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setChickenVariant(HitResult result, CallbackInfo ci, int i, int j, Chicken chicken) {
        this.getVariantData().ifPresent(variant -> VariantDataHolder.<ChickenVariant>getHolder(chicken).setVariantData(variant));
    }
}