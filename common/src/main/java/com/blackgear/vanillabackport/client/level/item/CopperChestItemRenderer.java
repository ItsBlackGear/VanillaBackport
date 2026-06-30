package com.blackgear.vanillabackport.client.level.item;

import com.blackgear.platform.client.v2.render.BuiltinItemRendererRegistry;
import com.blackgear.vanillabackport.common.level.block.CopperChestBlock;
import com.blackgear.vanillabackport.common.level.block_entity.CopperChestBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CopperChestItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private final CopperChestBlockEntity chest;
    
    public CopperChestItemRenderer(CopperChestBlock chest) {
        this.chest = new CopperChestBlockEntity(BlockPos.ZERO, chest.defaultBlockState());
    }
    
    @Override
    public void render(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        BlockEntityRenderer<CopperChestBlockEntity> renderer = dispatcher.getRenderer(this.chest);
        if (renderer != null)
            renderer.render(this.chest, 0.0F, pose, buffer, light, overlay);
    }
}