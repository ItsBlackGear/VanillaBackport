package com.blackgear.vanillabackport.core.mixin.client.entities;

import com.blackgear.vanillabackport.client.level.entities.variant.PigVariantRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigRenderer.class)
public abstract class PigRendererMixin extends MobRendererMixin<Pig, PigModel<Pig>> {
    @Unique private PigVariantRenderer renderer;

    public PigRendererMixin(EntityRendererProvider.Context context, PigModel<Pig> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private PigVariantRenderer renderer() {
        if (this.renderer == null) {
            this.renderer = new PigVariantRenderer(this.context);
        }

        return this.renderer;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Pig;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Pig entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.renderer().getTexture(entity) != null) {
            cir.setReturnValue(this.renderer().getTexture(entity));
        }
    }

    @Override
    public void render(Pig entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.renderer().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}