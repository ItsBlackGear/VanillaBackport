package com.blackgear.vanillabackport.core.mixin.leashable.client;

import com.blackgear.vanillabackport.common.api.leash.LeashRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatRenderer.class)
public abstract class BoatRendererMixin extends EntityRenderer<Boat> {
    @Unique private final LeashRenderer<Boat> leashRenderer = new LeashRenderer<>(this.entityRenderDispatcher);

    protected BoatRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    private void onRenderLeash(Boat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        this.leashRenderer.render(entity, partialTicks, poseStack, buffer);
    }

    @Override
    public boolean shouldRender(Boat boat, Frustum camera, double camX, double camY, double camZ) {
        return this.leashRenderer.shouldRender(boat, camera, super.shouldRender(boat, camera, camX, camY, camZ));
    }
}