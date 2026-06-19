package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.client.api.renderer.SpecialMobRenderer;
import com.blackgear.vanillabackport.client.api.renderer.renderers.CowVariantRenderer;
import com.blackgear.vanillabackport.client.api.renderer.RenderConditions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(CowRenderer.class)
public abstract class CowRendererMixin extends MobRendererMixin<Cow, CowModel<Cow>> {
    @Unique private Optional<Supplier<CowVariantRenderer>> renderer;

    public CowRendererMixin(EntityRendererProvider.Context context, CowModel<Cow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.renderer = SpecialMobRenderer.create(context, CowVariantRenderer::new, RenderConditions.FARM_ANIMALS);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cow;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Cow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        this.renderer.flatMap(renderer -> Optional.ofNullable(renderer.get()))
            .flatMap(renderer -> renderer.getTexture(entity))
            .ifPresent(cir::setReturnValue);
    }

    @Override
    public void render(Cow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.renderer.flatMap(renderer -> Optional.ofNullable(renderer.get()))
            .ifPresent(renderer -> this.model = renderer.getModel(entity).orElseGet(() -> this.defaultModel));
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}