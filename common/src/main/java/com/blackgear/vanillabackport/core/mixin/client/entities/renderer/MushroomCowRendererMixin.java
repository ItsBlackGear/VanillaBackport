package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.client.level.entities.renderer.ageable.AbstractAgeableRenderer;
import com.blackgear.vanillabackport.client.level.entities.renderer.ageable.MushroomCowAgeableRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends MobRendererMixin<MushroomCow, CowModel<MushroomCow>> {
    @Unique private Supplier<MushroomCowAgeableRenderer> renderer;

    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel<MushroomCow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.renderer = AbstractAgeableRenderer.create(context, MushroomCowAgeableRenderer::new);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/MushroomCow;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(MushroomCow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        this.renderer.get().getTexture(entity).ifPresent(cir::setReturnValue);
    }

    @Override
    public void render(MushroomCow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.renderer.get().getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}