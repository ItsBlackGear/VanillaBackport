package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.level.entities.model.happy_ghast.HappyGhastModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RopesLayer<T extends HappyGhast> extends RenderLayer<T, HappyGhastModel<T>> {
    private final RenderType ropes;
    private final HappyGhastModel<T> model;

    public RopesLayer(RenderLayerParent<T, HappyGhastModel<T>> renderer, EntityModelSet modelSet, ResourceLocation texture) {
        super(renderer);
        this.ropes = RenderType.entityCutoutNoCull(texture);
        this.model = new HappyGhastModel<>(modelSet.bakeLayer(ModModelLayers.HAPPY_GHAST_ROPES));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isLeashHolder() && entity.isHarnessed()) {
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.model.renderToBuffer(poseStack, buffer.getBuffer(this.ropes), packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}