package com.blackgear.vanillabackport.core.mixin.client.entities;

import com.blackgear.vanillabackport.client.level.entities.variant.CowVariantRenderer;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowRenderer.class)
public abstract class CowRendererMixin extends MobRendererMixin<Cow, CowModel<Cow>> {
    @Unique private CowVariantRenderer renderer;

    public CowRendererMixin(EntityRendererProvider.Context context, CowModel<Cow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private CowVariantRenderer renderer() {
        if (this.renderer == null) {
            this.renderer = new CowVariantRenderer(this.context);
        }

        return this.renderer;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cow;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Cow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.renderer().getTexture(entity) != null) {
            cir.setReturnValue(this.renderer().getTexture(entity));
        }
    }

    @Override
    public void render(Cow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.renderer().getModel(entity).orElseGet(() -> this.defaultModel);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}