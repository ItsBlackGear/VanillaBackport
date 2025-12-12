package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.common.api.variant.EnhancedVariants;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.level.entities.animal.EnhancedWolfVariant;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.WolfVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfRenderer.class)
public abstract class WolfRendererMixin extends MobRendererMixin<Wolf, WolfModel<Wolf>> {
    public WolfRendererMixin(EntityRendererProvider.Context context, WolfModel<Wolf> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Wolf entity, CallbackInfoReturnable<ResourceLocation> cir) {
        EnhancedWolfVariant variant = VariantHolder.<EnhancedWolfVariant>vb$getVariantHolder(entity).vb$getVariant();
        Registry<WolfVariant> registry = entity.level().registryAccess().registryOrThrow(Registries.WOLF_VARIANT);

        if (EnhancedVariants.hasVariantExclusive(ModBuiltinRegistries.WOLF_VARIANTS, variant, registry)) {
            if (entity.isTame()) {
                cir.setReturnValue(variant.assetInfo().tame().path());
            } else {
                cir.setReturnValue(entity.isAngry() ? variant.assetInfo().angry().path() : variant.assetInfo().wild().path());
            }
        }
    }
}
