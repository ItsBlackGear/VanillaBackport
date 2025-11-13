package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobRenderer.class)
public abstract class MobRendererMixin<T extends Mob, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    public MobRendererMixin(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique protected M defaultModel;
    @Unique protected EntityRendererProvider.Context context;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, M model, float shadowRadius, CallbackInfo ci) {
        this.context = context;
        this.defaultModel = model;
    }
}