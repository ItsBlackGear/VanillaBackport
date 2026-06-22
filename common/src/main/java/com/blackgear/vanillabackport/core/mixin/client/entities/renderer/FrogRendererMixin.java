package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.frog.FrogDataVariant;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FrogRenderer;
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
        VariantDataHolder.<FrogDataVariant>getHolder(entity).getVariantData().ifPresent(variant -> cir.setReturnValue(variant.assetInfo().path()));
    }
}