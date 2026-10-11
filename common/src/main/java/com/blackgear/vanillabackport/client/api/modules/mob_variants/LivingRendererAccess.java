package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingRendererAccess<T extends LivingEntity, M extends EntityModel<T>> {
    M getDefaultModel();
    
    default void onRender(T entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) { }
}