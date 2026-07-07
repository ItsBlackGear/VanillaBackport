package com.blackgear.vanillabackport.client.level.renderer.block_entity;

import com.blackgear.vanillabackport.client.level.model.block_entity.CopperGolemStatueModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.block.CopperGolemStatueBlock;
import com.blackgear.vanillabackport.common.level.block.CopperGolemStatueBlock.Pose;
import com.blackgear.vanillabackport.common.level.block_entity.CopperGolemStatueBlockEntity;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolemOxidationLevels;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockStateProperties;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CopperGolemStatueRenderer implements BlockEntityRenderer<CopperGolemStatueBlockEntity> {
    public final Map<Pose, CopperGolemStatueModel> models = new HashMap<>();
    
    public CopperGolemStatueRenderer(BlockEntityRendererProvider.Context context) {
        EntityModelSet models = context.getModelSet();
        this.models.put(Pose.STANDING, new CopperGolemStatueModel(models.bakeLayer(ModModelLayers.COPPER_GOLEM)));
        this.models.put(Pose.RUNNING, new CopperGolemStatueModel(models.bakeLayer(ModModelLayers.COPPER_GOLEM_RUNNING)));
        this.models.put(Pose.SITTING, new CopperGolemStatueModel(models.bakeLayer(ModModelLayers.COPPER_GOLEM_SITTING)));
        this.models.put(Pose.STAR, new CopperGolemStatueModel(models.bakeLayer(ModModelLayers.COPPER_GOLEM_STAR)));
    }
    
    @Override
    public void render(
        CopperGolemStatueBlockEntity statue,
        float partialTick,
        PoseStack pose,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay
    ) {
        if (statue.getBlockState().getBlock() instanceof CopperGolemStatueBlock block) {
            pose.pushPose();
            pose.translate(0.5, 0.0, 0.5);
            CopperGolemStatueModel model = this.models.get(statue.getBlockState().getValue(ModBlockStateProperties.COPPER_GOLEM_POSE));
            Direction direction = statue.getBlockState().getValue(CopperGolemStatueBlock.FACING);
            RenderType renderType = RenderType.entityCutoutNoCull(CopperGolemOxidationLevels.getOxidationLevel(block.getWeatheringState()).texture());
            model.setupAnim(direction);
            VertexConsumer consumer = buffer.getBuffer(renderType);
            model.renderToBuffer(pose, consumer, packedLight, packedOverlay, -1);
            pose.popPose();
        }
    }
}