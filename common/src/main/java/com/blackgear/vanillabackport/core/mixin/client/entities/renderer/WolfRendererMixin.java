package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.client.api.renderer.SpecialMobRenderer;
import com.blackgear.vanillabackport.client.level.entities.layer.WolfArmorCrackinessLayer;
import com.blackgear.vanillabackport.client.level.entities.layer.WolfArmorLayer;
import com.blackgear.vanillabackport.common.api.variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfVariant;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfRenderer.class)
public abstract class WolfRendererMixin extends MobRendererMixin<Wolf, WolfModel<Wolf>> {
    private WolfArmorLayer armorLayer;

    public WolfRendererMixin(EntityRendererProvider.Context context, WolfModel<Wolf> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$addLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
        SpecialMobRenderer.create(context, ctx -> new WolfArmorLayer(this, ctx.getModelSet()))
                .ifPresent(layer -> {
                    this.armorLayer = layer.get();
                    this.addLayer(this.armorLayer);
                });
        SpecialMobRenderer.create(context, ctx ->
                new WolfArmorCrackinessLayer((WolfRenderer)(Object)this, this.armorLayer.getArmorModel())
        ).ifPresent(layer -> this.addLayer(layer.get()));
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Wolf entity, CallbackInfoReturnable<ResourceLocation> cir) {
        VariantDataHolder.<WolfVariant>getHolder(entity).getVariantData().ifPresent(variant -> {
            if (entity.isTame()) {
                cir.setReturnValue(variant.assetInfo().tame().path());
            } else {
                cir.setReturnValue(entity.isAngry() ? variant.assetInfo().angry().path() : variant.assetInfo().wild().path());
            }
        });
    }
}