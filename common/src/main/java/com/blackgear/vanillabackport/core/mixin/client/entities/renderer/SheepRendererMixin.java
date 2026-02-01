package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.client.level.entities.layer.SheepWoolUndercoatLayer;
import com.blackgear.vanillabackport.client.level.entities.renderer.ageable.AbstractAgeableRenderer;
import com.blackgear.vanillabackport.client.level.entities.renderer.ageable.SheepAgeableRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(SheepRenderer.class)
public abstract class SheepRendererMixin extends MobRendererMixin<Sheep, SheepModel<Sheep>> {
    @Unique private Supplier<SheepAgeableRenderer> renderer;

    public SheepRendererMixin(EntityRendererProvider.Context context, SheepModel<Sheep> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new SheepWoolUndercoatLayer(this, context.getModelSet()));
        this.renderer = AbstractAgeableRenderer.create(context, SheepAgeableRenderer::new);
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Sheep;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void onGetTextureLocation(Sheep entity, CallbackInfoReturnable<ResourceLocation> cir) {
        this.renderer.get().getTexture(entity).ifPresent(cir::setReturnValue);
    }

    @Override
    public void render(Sheep entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.renderer.get().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}