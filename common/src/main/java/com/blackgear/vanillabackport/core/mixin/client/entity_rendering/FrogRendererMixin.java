package com.blackgear.vanillabackport.core.mixin.client.entity_rendering;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.core.compat.ClientCompat;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FrogRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FrogRenderer.class)
public abstract class FrogRendererMixin extends MobRenderer<Frog, FrogModel<Frog>> {
    public FrogRendererMixin(EntityRendererProvider.Context context, FrogModel<Frog> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/frog/Frog;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Frog entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (ClientCompat.hasQuarkFrogTexture(entity)) return;
        if (ClientCompat.getNMLActiveRemodel(entity)) return;
        VariantDataHolder.<FrogDataVariant>getHolder(entity).flatMap(VariantDataHolder::getVariantData).ifPresent(variant -> cir.setReturnValue(variant.assetInfo().path()));
    }
}