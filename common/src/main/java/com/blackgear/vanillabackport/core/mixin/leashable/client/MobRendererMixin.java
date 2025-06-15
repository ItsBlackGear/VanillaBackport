package com.blackgear.vanillabackport.core.mixin.leashable.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobRenderer.class)
public abstract class MobRendererMixin<T extends Mob> extends EntityRendererMixin<T> {
    @Inject(method = "renderLeash", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void onRenderLeash(T entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, E leashHolder, CallbackInfo ci) {
        ci.cancel();
    }
}