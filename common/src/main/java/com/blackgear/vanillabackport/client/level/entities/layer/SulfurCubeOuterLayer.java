package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.level.entities.model.SulfurCubeModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.sulfurcube.SulfurCube;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class SulfurCubeOuterLayer extends RenderLayer<SulfurCube, SulfurCubeModel<SulfurCube>> {
    private static final ResourceLocation SULFUR_CUBE_OUTER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_outer.png");

    private final EntityModel<SulfurCube> model;

    public SulfurCubeOuterLayer(RenderLayerParent<SulfurCube, SulfurCubeModel<SulfurCube>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new SulfurCubeModel<>(modelSet.bakeLayer(ModModelLayers.SULFUR_CUBE));
    }

    @Override
    public void render(
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        SulfurCube cube,
        float limbSwing,
        float limbSwingAmount,
        float partialTick,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean glowing = minecraft.shouldEntityAppearGlowing(cube) && cube.isInvisible();
        if (!cube.isInvisible() || glowing) {
            VertexConsumer vertexConsumer;
            if (glowing) {
                vertexConsumer = buffer.getBuffer(RenderType.outline(this.getTextureLocation(cube)));
            } else {
                vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(SULFUR_CUBE_OUTER_LOCATION));
            }

            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(cube, limbSwing, limbSwingAmount, partialTick);
            this.model.setupAnim(cube, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(cube, 0.0F), -1);
        }
    }
}
