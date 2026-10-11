package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.llamalad7.mixinextras.sugar.Local;
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

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin extends ThrowableItemProjectile {
    public ThrownEggMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "onHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Chicken;moveTo(DDDFF)V",
            shift = At.Shift.AFTER
        ),
        require = 0
    )
    private void setChickenVariant(HitResult result, CallbackInfo ci, @Local Chicken chicken) {
        RegistryKey<ChickenVariant> key = this.getItem().get(ModDataComponents.CHICKEN_VARIANT.get());
        if (key != null) {
            ChickenVariant variant = VariantUtils.getDefault(ChickenVariants.REGISTRIES, key);
            VariantDataHolder.<ChickenVariant>getHolder(chicken).ifPresent(holder -> holder.setVariantData(variant));
        }
    }
}