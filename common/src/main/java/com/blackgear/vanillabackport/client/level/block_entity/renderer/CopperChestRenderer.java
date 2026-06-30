package com.blackgear.vanillabackport.client.level.block_entity.renderer;

import com.blackgear.vanillabackport.common.level.block.CopperChestBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import static net.minecraft.client.renderer.Sheets.CHEST_SHEET;

@Environment(EnvType.CLIENT)
public class CopperChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;
    private final ModelPart doubleLeftLid;
    private final ModelPart doubleLeftBottom;
    private final ModelPart doubleLeftLock;
    private final ModelPart doubleRightLid;
    private final ModelPart doubleRightBottom;
    private final ModelPart doubleRightLock;
    
    public CopperChestRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart chest = context.bakeLayer(ModelLayers.CHEST);
        this.bottom = chest.getChild("bottom");
        this.lid = chest.getChild("lid");
        this.lock = chest.getChild("lock");
        ModelPart left = context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT);
        this.doubleLeftBottom = left.getChild("bottom");
        this.doubleLeftLid = left.getChild("lid");
        this.doubleLeftLock = left.getChild("lock");
        ModelPart right = context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT);
        this.doubleRightBottom = right.getChild("bottom");
        this.doubleRightLid = right.getChild("lid");
        this.doubleRightLock = right.getChild("lock");
    }
    
    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        boolean hasLevel = level != null;
        BlockState blockState = hasLevel ? blockEntity.getBlockState() : blockEntity.getBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        if (blockState.getBlock() instanceof CopperChestBlock chest) {
            poseStack.pushPose();
            float angle = blockState.getValue(ChestBlock.FACING).toYRot();
            poseStack.translate(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-angle));
            poseStack.translate(-0.5F, -0.5F, -0.5F);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combineResult;
            if (hasLevel) {
                combineResult = chest.combine(blockState, level, blockEntity.getBlockPos(), true);
            } else {
                combineResult = DoubleBlockCombiner.Combiner::acceptNone;
            }
            
            float open = combineResult.apply(ChestBlock.opennessCombiner(blockEntity)).get(partialTick);
            open = 1.0F - open;
            open = 1.0F - open * open * open;
            int light = combineResult.apply(new BrightnessCombiner<>()).applyAsInt(packedLight);
            Material material = getChestMaterialType(chest, chestType);
            VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityCutout);
            if (chestType != ChestType.SINGLE) {
                if (chestType == ChestType.LEFT) {
                    this.render(poseStack, vertexConsumer, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, open, light, packedOverlay);
                } else {
                    this.render(poseStack, vertexConsumer, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, open, light, packedOverlay);
                }
            } else {
                this.render(poseStack, vertexConsumer, this.lid, this.lock, this.bottom, open, light, packedOverlay);
            }
            
            poseStack.popPose();
        }
    }
    
    private static Material getChestMaterialType(CopperChestBlock chest, ChestType type) {
        return switch (chest.getState()) {
            case EXPOSED -> getChestType(type, "copper_exposed");
            case WEATHERED -> getChestType(type, "copper_weathered");
            case OXIDIZED -> getChestType(type, "copper_oxidized");
            default -> getChestType(type, "copper");
        };
    }
    
    private static Material getChestType(ChestType type, String chest) {
        return switch (type) {
            case LEFT -> chestMaterial(chest + "_left");
            case RIGHT -> chestMaterial(chest + "_right");
            default -> chestMaterial(chest);
        };
    }
    
    private static Material chestMaterial(String chestName) {
        return new Material(CHEST_SHEET, ResourceLocation.withDefaultNamespace("entity/chest/" + chestName));
    }
    
    private void render(PoseStack poseStack, VertexConsumer consumer, ModelPart lidPart, ModelPart lockPart, ModelPart bottomPart, float lidAngle, int packedLight, int packedOverlay) {
        lidPart.xRot = -(lidAngle * Mth.HALF_PI);
        lockPart.xRot = lidPart.xRot;
        lidPart.render(poseStack, consumer, packedLight, packedOverlay);
        lockPart.render(poseStack, consumer, packedLight, packedOverlay);
        bottomPart.render(poseStack, consumer, packedLight, packedOverlay);
    }
}