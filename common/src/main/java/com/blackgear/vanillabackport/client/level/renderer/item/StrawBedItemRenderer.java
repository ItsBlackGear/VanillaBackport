package com.blackgear.vanillabackport.client.level.renderer.item;

import com.blackgear.platform.client.v2.render.DynamicItemRenderer;
import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.Collections;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class StrawBedItemRenderer implements DynamicItemRenderer.Renderer {
    public static final StrawBedItemRenderer INSTANCE = new StrawBedItemRenderer();
    
    private static final BlockState HEAD_STATE = ModBlocks.STRAW_BED.get().defaultBlockState()
        .setValue(BedBlock.FACING, Direction.SOUTH)
        .setValue(BedBlock.PART, BedPart.HEAD);
    
    private static final BlockState FOOT_STATE = ModBlocks.STRAW_BED.get().defaultBlockState()
        .setValue(BedBlock.FACING, Direction.SOUTH)
        .setValue(BedBlock.PART, BedPart.FOOT);
    
    @Override
    public void renderFirstPerson(
        ItemStack stack,
        ItemDisplayContext context,
        boolean leftHand,
        PoseStack pose,
        MultiBufferSource buffer,
        int light,
        int overlay,
        BakedModel model,
        ItemModelShaper shaper,
        ItemColors colors
    ) {
        if (stack.is(ModBlocks.STRAW_BED.get().asItem())) {
            BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
            
            BakedModel headModel = renderer.getBlockModel(HEAD_STATE);
            BakedModel footModel = renderer.getBlockModel(FOOT_STATE);
            
            RenderType renderType = ItemBlockRenderTypes.getRenderType(stack, true);
            VertexConsumer vertices = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, stack.hasFoil());
            
            pose.pushPose();
            
            model.getTransforms().getTransform(context).apply(leftHand, pose);
            
            pose.translate(-0.5F, -0.5F, -0.5F);
            
            pose.pushPose();
            pose.translate(0.0F, 0.0F, -0.3F);
            this.renderModelLists(headModel, stack, light, overlay, pose, vertices, colors);
            pose.popPose();
            
            pose.pushPose();
            pose.translate(0.0F, 0.0F, -1.3F);
            this.renderModelLists(footModel, stack, light, overlay, pose, vertices, colors);
            pose.popPose();
            
            pose.popPose();
        }
    }
    
    @Override
    public ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper) {
        return ResultHolder.pass();
    }
    
    @Override
    public Set<ModelResourceLocation> registerModels() {
        return Collections.emptySet();
    }
}