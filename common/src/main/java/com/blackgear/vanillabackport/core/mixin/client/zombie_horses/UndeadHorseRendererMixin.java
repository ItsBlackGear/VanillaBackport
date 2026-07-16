package com.blackgear.vanillabackport.core.mixin.client.zombie_horses;

import com.blackgear.vanillabackport.client.api.modules.mob_variants.RenderConditions;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.SpecialMobRenderer;
import com.blackgear.vanillabackport.client.level.layer.UndeadHorseArmorLayer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererMixin extends AbstractHorseRenderer<AbstractHorse, HorseModel<AbstractHorse>> {
    public UndeadHorseRendererMixin(EntityRendererProvider.Context context, HorseModel<AbstractHorse> model, float scale) {
        super(context, model, scale);
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$appendArmorLayer(EntityRendererProvider.Context context, ModelLayerLocation layer, CallbackInfo ci) {
        SpecialMobRenderer.addLayer(
            RenderConditions.DEFAULT,
            () -> new UndeadHorseArmorLayer(this, context.getModelSet()),
            this::addLayer
        );
    }
}