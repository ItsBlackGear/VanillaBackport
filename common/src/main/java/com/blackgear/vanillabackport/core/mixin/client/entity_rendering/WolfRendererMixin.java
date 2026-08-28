package com.blackgear.vanillabackport.core.mixin.client.entity_rendering;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfDataVariant;
import com.blackgear.vanillabackport.core.compat.ClientCompat;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfRenderer.class)
public abstract class WolfRendererMixin extends MobRenderer<Wolf, WolfModel<Wolf>> {
    public WolfRendererMixin(EntityRendererProvider.Context context, WolfModel<Wolf> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Wolf entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (ClientCompat.getNMLActiveRemodel(entity)) return;
        VariantDataHolder.<WolfDataVariant>getHolder(entity).flatMap(VariantDataHolder::getVariantData).ifPresent(variant -> {
            var assets = variant.assetInfo();
            var texture = entity.isTame() ? assets.tame() : (entity.isAngry() ? assets.angry() : assets.wild());
            cir.setReturnValue(texture.path());
        });
    }
}