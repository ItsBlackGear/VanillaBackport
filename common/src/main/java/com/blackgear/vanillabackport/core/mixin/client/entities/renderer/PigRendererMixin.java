package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.client.api.modules.mob_variants.PigVariantRenderer;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.RenderConditions;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.SpecialMobRenderer;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(PigRenderer.class)
public abstract class PigRendererMixin extends MobRendererMixin<Pig, PigModel<Pig>> {
    @Unique private Optional<Supplier<PigVariantRenderer>> renderer;

    public PigRendererMixin(EntityRendererProvider.Context context, PigModel<Pig> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.renderer = SpecialMobRenderer.create(context, PigVariantRenderer::new, RenderConditions.FARM_ANIMALS);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Pig;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Pig entity, CallbackInfoReturnable<ResourceLocation> cir) {
        this.renderer.flatMap(renderer -> Optional.ofNullable(renderer.get()))
            .flatMap(renderer -> renderer.getTexture(entity))
            .ifPresent(cir::setReturnValue);
    }

    @Override
    public void render(Pig entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.renderer.flatMap(renderer -> Optional.ofNullable(renderer.get()))
            .ifPresent(renderer -> this.model = renderer.getModel(entity).orElseGet(() -> this.defaultModel));
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}