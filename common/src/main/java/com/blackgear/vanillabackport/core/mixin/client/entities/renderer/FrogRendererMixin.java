package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.common.api.variant.EnhancedVariants;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.level.entities.animal.EnhancedFrogVariant;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FrogRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FrogRenderer.class)
public abstract class FrogRendererMixin extends MobRendererMixin<Frog, FrogModel<Frog>> {
    public FrogRendererMixin(EntityRendererProvider.Context context, FrogModel<Frog> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/frog/Frog;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Frog entity, CallbackInfoReturnable<ResourceLocation> cir) {
        EnhancedFrogVariant variant = VariantHolder.<EnhancedFrogVariant>vb$getVariantHolder(entity).vb$getVariant();

        if (EnhancedVariants.hasVariantExclusive(ModBuiltinRegistries.FROG_VARIANTS, variant, BuiltInRegistries.FROG_VARIANT)) {
            cir.setReturnValue(variant.assetInfo().path());
        }
    }
}