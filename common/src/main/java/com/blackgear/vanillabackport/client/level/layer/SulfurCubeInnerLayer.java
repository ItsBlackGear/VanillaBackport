package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SmallSulfurCubeModel;
import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SulfurCubeModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.sulfur_cube.SulfurCube;
import com.blackgear.vanillabackport.core.mixin.client.access.BlockRenderDispatcherAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class SulfurCubeInnerLayer extends RenderLayer<SulfurCube, SulfurCubeModel> {
    private static final ResourceLocation SULFUR_CUBE_INNER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner.png");
    private static final ResourceLocation SULFUR_CUBE_INNER_SMALL_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner_small.png");

    private final SulfurCubeModel normalModel;
    private final SmallSulfurCubeModel smallModel;
    private final BlockRenderDispatcher blockRenderer;

    public SulfurCubeInnerLayer(RenderLayerParent<SulfurCube, SulfurCubeModel> renderer, EntityModelSet modelSet, BlockRenderDispatcher blockRenderer) {
        super(renderer);
        this.normalModel = new SulfurCubeModel(modelSet.bakeLayer(ModModelLayers.SULFUR_CUBE_INNER));
        this.smallModel = new SmallSulfurCubeModel(modelSet.bakeLayer(ModModelLayers.SULFUR_CUBE_SMALL_INNER));
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(
        PoseStack pose,
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
        float fuse = cube.getFuseRemainingTicks(partialTick);
        int overlayCoords = fuse > 0.0F && isLit(fuse)
            ? OverlayTexture.pack(OverlayTexture.u(1.0F), 10)
            : LivingEntityRenderer.getOverlayCoords(cube, 0.0F);

        ItemStack containedBlock = cube.getContainedBlock();
        if (!containedBlock.isEmpty()) {
            pose.pushPose();
            pose.mulPose(Axis.XP.rotationDegrees(180.0F));
            if (cube.isBaby()) pose.scale(0.5F, 0.5F, 0.5F);

            pose.translate(-0.5F, -0.518F, -0.5F);
            BlockState state = Block.byItem(containedBlock.getItem()).defaultBlockState(); //TODO: apply matching properties from item
            renderSingleBlock(state, pose, buffer, packedLight, overlayCoords);
            pose.popPose();
        } else if (!cube.isInvisible()) {
            ResourceLocation texture = cube.isBaby() ? SULFUR_CUBE_INNER_SMALL_LOCATION : SULFUR_CUBE_INNER_LOCATION;
            EntityModel<SulfurCube> model = cube.isBaby() ? this.smallModel : this.normalModel;
            RenderType renderType = RenderType.entitySolid(texture);
            model.renderToBuffer(pose, buffer.getBuffer(renderType), packedLight, overlayCoords, -1);
        }
    }

    private void renderSingleBlock(BlockState state, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderShape renderShape = state.getRenderShape();
        if (renderShape != RenderShape.INVISIBLE) {
            switch (renderShape) {
                case MODEL:
                    BakedModel bakedModel = this.blockRenderer.getBlockModel(state);
                    int colors = ((BlockRenderDispatcherAccessor) this.blockRenderer).getBlockColors().getColor(state, null, null, 0);
                    float red = (float)(colors >> 16 & 255) / 255.0F;
                    float green = (float)(colors >> 8 & 255) / 255.0F;
                    float blue = (float)(colors & 255) / 255.0F;
                    this.blockRenderer.getModelRenderer().renderModel(pose.last(), buffer.getBuffer(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS)), state, bakedModel, red, green, blue, packedLight, packedOverlay);
                    break;
                case ENTITYBLOCK_ANIMATED:
                    ((BlockRenderDispatcherAccessor) this.blockRenderer).getBlockEntityRenderer().renderByItem(new ItemStack(state.getBlock()), ItemDisplayContext.NONE, pose, buffer, packedLight, packedOverlay);
            }
        }
    }

    public boolean isLit(float fuse) {
        return !(fuse < 0.0F) && (int) (fuse / 5.0F) % 2 == 0;
    }
}