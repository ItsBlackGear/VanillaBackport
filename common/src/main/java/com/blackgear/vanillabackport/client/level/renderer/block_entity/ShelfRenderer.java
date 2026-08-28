package com.blackgear.vanillabackport.client.level.renderer.block_entity;

import com.blackgear.vanillabackport.common.level.blocks.ShelfBlock;
import com.blackgear.vanillabackport.common.level.block_entities.ShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ShelfRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ShelfBlockEntity shelf, float partialTick, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction direction = shelf.getBlockState().getValue(ShelfBlock.FACING);
        float yRot = direction.getAxis().isHorizontal() ? -direction.toYRot() : 180.0F;
        NonNullList<ItemStack> items = shelf.getItems();
        int seed = (int) shelf.getBlockPos().asLong();

        for (int slot = 0; slot < items.size(); slot++) {
            ItemStack stack = items.get(slot);
            if (!stack.isEmpty()) {
                float itemSlotPosition = (slot - 1) * 0.3125F;
                Vec3 itemOffset = new Vec3(itemSlotPosition, shelf.getAlignItemsToBottom() ? -0.25 : 0.0, -0.25);
                pose.pushPose();
                pose.translate(0.5F, 0.5F, 0.5F);
                pose.mulPose(Axis.YP.rotationDegrees(yRot));
                pose.translate(itemOffset.x, itemOffset.y, itemOffset.z);
                pose.mulPose(Axis.YP.rotationDegrees(180.0F));
                pose.scale(0.25F, 0.25F, 0.25F);
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, pose, bufferSource, shelf.getLevel(), seed + slot);
                pose.popPose();
            }
        }
    }
}
