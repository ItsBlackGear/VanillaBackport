package com.blackgear.vanillabackport.core.mixin.client.entity_rendering;

import com.blackgear.vanillabackport.client.api.modules.mob_variants.LivingRendererAccess;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.SpecialMobRenderer;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.ChickenVariantRenderer;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.RenderConditions;
import com.blackgear.vanillabackport.core.compat.ClientCompat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChickenRenderer.class)
public abstract class ChickenRendererMixin extends MobRenderer<Chicken, ChickenModel<Chicken>> implements LivingRendererAccess<Chicken, ChickenModel<Chicken>> {
    @Unique private SpecialMobRenderer<Chicken, ChickenModel<Chicken>> renderer;

    public ChickenRendererMixin(EntityRendererProvider.Context context, ChickenModel<Chicken> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.renderer = SpecialMobRenderer.create(context, ChickenVariantRenderer::new, RenderConditions.FARM_ANIMALS);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Chicken;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Chicken entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (ClientCompat.hasQuarkChickenTexture(entity)) return;
        this.renderer.getTexture(entity).ifPresent(cir::setReturnValue);
    }

    @Override
    public void onRender(Chicken entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
        if (ClientCompat.hasQuarkChickenTexture(entity)) return;
        this.model = this.renderer.getModel(entity).orElseGet(this::getDefaultModel);
    }
}