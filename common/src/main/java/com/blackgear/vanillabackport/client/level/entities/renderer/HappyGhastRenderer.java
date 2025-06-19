package com.blackgear.vanillabackport.client.level.entities.renderer;

import com.blackgear.vanillabackport.client.level.entities.layer.GhastHarnessLayer;
import com.blackgear.vanillabackport.client.level.entities.layer.RopesLayer;
import com.blackgear.vanillabackport.client.level.entities.model.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.entities.model.HappyGhastModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HappyGhastRenderer extends MobRenderer<HappyGhast, HappyGhastModel<HappyGhast>> {
    private static final ResourceLocation GHAST_LOCATION = VanillaBackport.resource("textures/entity/ghast/happy_ghast.png");
    private static final ResourceLocation GHAST_BABY_LOCATION = VanillaBackport.resource("textures/entity/ghast/happy_ghast_baby.png");
    private static final ResourceLocation GHAST_ROPES = VanillaBackport.resource("textures/entity/ghast/happy_ghast_ropes.png");
    private final HappyGhastModel<HappyGhast> babyModel;

    public HappyGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new HappyGhastModel<>(context.bakeLayer(ModModelLayers.HAPPY_GHAST)), 1.5F);
        this.babyModel = new HappyGhastModel<>(context.bakeLayer(ModModelLayers.HAPPY_GHAST_BABY));
        this.addLayer(new GhastHarnessLayer<>(this, new HappyGhastHarnessModel<>(context.bakeLayer(ModModelLayers.HAPPY_GHAST_HARNESS))));
        this.addLayer(new RopesLayer<>(this, context.getModelSet(), GHAST_ROPES));
    }

    @Override
    public ResourceLocation getTextureLocation(HappyGhast entity) {
        return entity.isBaby() ? GHAST_BABY_LOCATION : GHAST_LOCATION;
    }

    @Override
    public void render(HappyGhast entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isBaby()) this.model = this.babyModel;
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(HappyGhast entity, PoseStack matrices, float partialTicks) {
        float scale = entity.isBaby() ? (4.0F * 0.2375F) : 4.0F;
        matrices.scale(scale, scale, scale);
    }
}