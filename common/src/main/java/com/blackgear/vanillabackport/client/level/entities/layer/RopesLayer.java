package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.level.entities.model.HappyGhastModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
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
    private final HappyGhastModel<T> adultModel;
    private final HappyGhastModel<T> babyModel;

    public RopesLayer(RenderLayerParent<T, HappyGhastModel<T>> renderer, EntityModelSet modelSet, ResourceLocation texture) {
        super(renderer);
        this.ropes = RenderType.entityCutoutNoCull(texture);
        this.adultModel = new HappyGhastModel<>(modelSet.bakeLayer(ModModelLayers.HAPPY_GHAST_ROPES));
        this.babyModel = new HappyGhastModel<>(modelSet.bakeLayer(ModModelLayers.HAPPY_GHAST_BABY_ROPES));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isLeashHolder() && entity.isHarnessed()) {
            HappyGhastModel<T> model = entity.isBaby() ? this.babyModel : this.adultModel;
            model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            model.renderToBuffer(poseStack, buffer.getBuffer(this.ropes), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}