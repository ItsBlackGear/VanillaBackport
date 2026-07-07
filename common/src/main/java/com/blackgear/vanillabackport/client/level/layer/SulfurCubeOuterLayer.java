package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SmallSulfurCubeModel;
import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SulfurCubeModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
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
public class SulfurCubeOuterLayer extends RenderLayer<SulfurCube, SulfurCubeModel> {
    private static final ResourceLocation SULFUR_CUBE_OUTER_LOCATION = new ResourceLocation("textures/entity/sulfur_cube/sulfur_cube_outer.png");
    private static final ResourceLocation SULFUR_CUBE_OUTER_SMALL_LOCATION = new ResourceLocation("textures/entity/sulfur_cube/sulfur_cube_outer_small.png");

    private final SulfurCubeModel normalModel;
    private final SmallSulfurCubeModel smallModel;

    public SulfurCubeOuterLayer(RenderLayerParent<SulfurCube, SulfurCubeModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.normalModel = new SulfurCubeModel(modelSet.bakeLayer(ModModelLayers.SULFUR_CUBE));
        this.smallModel = new SmallSulfurCubeModel(modelSet.bakeLayer(ModModelLayers.SULFUR_CUBE_SMALL));
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
            EntityModel<SulfurCube> model = cube.isBaby() ? this.smallModel : this.normalModel;
            ResourceLocation texture = cube.isBaby() ? SULFUR_CUBE_OUTER_SMALL_LOCATION : SULFUR_CUBE_OUTER_LOCATION;
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));

            if (glowing) {
                vertexConsumer = buffer.getBuffer(RenderType.outline(this.getTextureLocation(cube)));
            }

            this.getParentModel().copyPropertiesTo(model);
            model.prepareMobModel(cube, limbSwing, limbSwingAmount, partialTick);
            model.setupAnim(cube, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(cube, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
