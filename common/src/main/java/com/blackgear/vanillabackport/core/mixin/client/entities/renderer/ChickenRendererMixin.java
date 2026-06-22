package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.client.api.modules.mob_variants.SpecialMobRenderer;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.ChickenVariantRenderer;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.RenderConditions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(ChickenRenderer.class)
public abstract class ChickenRendererMixin extends MobRendererMixin<Chicken, ChickenModel<Chicken>> {
    @Unique private Optional<Supplier<ChickenVariantRenderer>> renderer;

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
        this.renderer.flatMap(renderer -> Optional.ofNullable(renderer.get()))
            .flatMap(renderer -> renderer.getTexture(entity))
            .ifPresent(cir::setReturnValue);
    }

    @Override
    public void render(Chicken entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
        this.renderer.flatMap(renderer -> Optional.ofNullable(renderer.get()))
            .ifPresent(renderer -> this.model = renderer.getModel(entity).orElseGet(() -> this.defaultModel));
        super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
    }
}