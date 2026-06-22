package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cat.CatDataVariant;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatRenderer.class)
public abstract class CatRendererMixin extends MobRendererMixin<Cat, CatModel<Cat>> {
    public CatRendererMixin(EntityRendererProvider.Context context, CatModel<Cat> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cat;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Cat entity, CallbackInfoReturnable<ResourceLocation> cir) {
        VariantDataHolder.<CatDataVariant>getHolder(entity).getVariantData().ifPresent(variant -> cir.setReturnValue(variant.assetInfo().path()));
    }
}