package com.blackgear.vanillabackport.client.level.renderer.block_entity;

import com.blackgear.vanillabackport.client.level.renderer.item.ModelBoundingBoxManager;
import com.blackgear.vanillabackport.common.level.blocks.ShelfBlock;
import com.blackgear.vanillabackport.common.level.block_entities.ShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
    private static final float BASE_ITEM_SCALE = 0.25F;
    private static final float TARGET_EXTRA_SCALE = 2.0F;
    private static final float SLOT_WIDTH = 4.0F / 16.0F;
    private static final float SLOT_HEIGHT = 8.0F / 16.0F;
    private static final float ITEM_OFFSET_X = 0.3125F;
    
    private final Map<Item, ItemScaleData> cache = new HashMap<>();
    private final ItemRenderer itemRenderer;
    
    public ShelfRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }
    
    @Override
    public void render(ShelfBlockEntity shelf, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction direction = shelf.getBlockState().getValue(ShelfBlock.FACING);
        float yRot = direction.getAxis().isHorizontal() ? -direction.toYRot() : 180.0F;
        
        NonNullList<ItemStack> items = shelf.getItems();
        int seed = HashCommon.long2int(shelf.getBlockPos().asLong());
        boolean alignToBottom = shelf.getAlignItemsToBottom();
        
        for (int slot = 0; slot < items.size(); slot++) {
            ItemStack stack = items.get(slot);
            if (stack.isEmpty()) continue;
            
            float slotOffsetX = (slot - 1) * ITEM_OFFSET_X;
            
            pose.pushPose();
            pose.translate(0.5F, 0.5F, 0.5F);
            pose.mulPose(Axis.YP.rotationDegrees(yRot));
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(-slotOffsetX, alignToBottom ? -0.25F : 0.0F, 0.27F);
            pose.scale(BASE_ITEM_SCALE, BASE_ITEM_SCALE, BASE_ITEM_SCALE);
            
            Level level = shelf.getLevel();
            BakedModel model = this.itemRenderer.getModel(stack, level, null, seed + slot);
            ItemScaleData scaleData = this.getItemScaleData(stack, model, level, seed + slot);
            AABB bounds = scaleData.bounds();
            
            pose.scale(scaleData.scale(), scaleData.scale(), scaleData.scale());
            
            double offsetY = -bounds.minY;
            if (!alignToBottom) offsetY += -(bounds.maxY - bounds.minY) / 2.0;
            pose.translate(0.0, offsetY, 0.0);
            
            boolean isCustom = model.isCustomRenderer();
            if (isCustom) {
                Vector3f scale = model.getTransforms().getTransform(ItemDisplayContext.FIXED).scale;
                pose.pushPose();
                pose.scale(0.9F / normalizeScale(scale.x()), 0.9F / normalizeScale(scale.y()), 0.9F / normalizeScale(scale.z()));
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, pose, buffer, level, seed + slot);
                pose.popPose();
            } else {
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, pose, buffer, level, seed + slot);
            }
            
            pose.popPose();
        }
    }
    
    private static float normalizeScale(float value) {
        return Math.abs(value) > Mth.EPSILON ? value : 1.0F;
    }
    
    private ItemScaleData getItemScaleData(ItemStack stack, BakedModel model, Level level, int seed) {
        return this.cache.computeIfAbsent(stack.getItem(), key -> computeItemScale(stack, model, level, seed));
    }
    
    private ItemScaleData computeItemScale(ItemStack stack, BakedModel model, Level level, int seed) {
        AABB bounds = ModelBoundingBoxManager.INSTANCE.getModelBoundingBox(model, stack, this.itemRenderer, level, seed);
        double width = bounds.maxX - bounds.minX;
        double height = bounds.maxY - bounds.minY;
        
        boolean isCustom = model.isCustomRenderer();
        if (!isCustom && isFlatModel(bounds)) {
            return new ItemScaleData(bounds, 1.0F);
        }
        
        float scale = calculateOptimalScale(width, height, isCustom);
        return new ItemScaleData(bounds, scale);
    }
    
    private static float calculateOptimalScale(double width, double height, boolean customRenderer) {
        float scale = TARGET_EXTRA_SCALE;
        double projectedWidth = width * BASE_ITEM_SCALE * TARGET_EXTRA_SCALE;
        
        if (projectedWidth > SLOT_WIDTH && width > Mth.EPSILON) {
            scale = (float) (SLOT_WIDTH / (width * BASE_ITEM_SCALE));
        }
        
        double projectedHeight = height * BASE_ITEM_SCALE * scale;
        float allowedHeight = customRenderer ? SLOT_HEIGHT : SLOT_WIDTH;
        
        if (projectedHeight > allowedHeight && height > Mth.EPSILON) {
            float heightScale = (float) (allowedHeight / (height * BASE_ITEM_SCALE));
            scale = Math.min(scale, heightScale);
        }
        
        return scale;
    }
    
    private static boolean isFlatModel(AABB box) {
        double smallestDimension = Math.min(box.maxX - box.minX, Math.min(box.maxY - box.minY, box.maxZ - box.minZ));
        return smallestDimension < 0.25;
    }
    
    private record ItemScaleData(AABB bounds, float scale) {}
}