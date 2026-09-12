package com.blackgear.vanillabackport.client.level.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

public class ModelBoundingBoxManager {
    public static final ModelBoundingBoxManager INSTANCE = new ModelBoundingBoxManager();
    private static final Direction[] CULL_FACES = { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, null };
    private final Map<Item, AABB> cache = new HashMap<>();
    
    public AABB getModelBoundingBox(BakedModel model, ItemStack stack, ItemRenderer renderer, Level level, int seed) {
        return this.cache.computeIfAbsent(stack.getItem(), i -> compute(model, stack, renderer, level, seed));
    }
    
    private static AABB compute(BakedModel model, ItemStack stack, ItemRenderer renderer, Level level, int seed) {
        if (model.isCustomRenderer()) {
            return computeForCustomRenderer(stack, renderer, level, seed);
        }

        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;

        ItemTransform transform = model.getTransforms().getTransform(ItemDisplayContext.FIXED);
        PoseStack poseStack = new PoseStack();
        transform.apply(false, poseStack);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        Matrix4f pose = poseStack.last().pose();
        Vector4f pos = new Vector4f();

        for (Direction direction : CULL_FACES) {
            for (BakedQuad quad : model.getQuads(null, direction, level.getRandom())) {
                Direction quadDir = quad.getDirection();
                if (quadDir == Direction.SOUTH || quadDir == Direction.NORTH) continue;

                int[] vertices = quad.getVertices();
                for (int i = 0; i < 4; i++) {
                    int offset = i * 8;
                    float x = Float.intBitsToFloat(vertices[offset]);
                    float y = Float.intBitsToFloat(vertices[offset + 1]);
                    float z = Float.intBitsToFloat(vertices[offset + 2]);
                    pose.transform(x, y, z, 1.0F, pos);

                    minX = Math.min(minX, pos.x);
                    minY = Math.min(minY, pos.y);
                    minZ = Math.min(minZ, pos.z);
                    maxX = Math.max(maxX, pos.x);
                    maxY = Math.max(maxY, pos.y);
                    maxZ = Math.max(maxZ, pos.z);
                }
            }
        }

        if (minX == Float.POSITIVE_INFINITY) {
            return fallback();
        }

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    private static AABB computeForCustomRenderer(ItemStack stack, ItemRenderer renderer, Level level, int seed) {
        PoseStack pose = new PoseStack();
        CustomRendererBoundManager manager = new CustomRendererBoundManager();
        
        renderer.renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, pose, renderType -> manager, level, seed);
    
        if (!manager.hasData()) {
            return fallback();
        }
        
        AABB box = manager.toAABB();
        BakedModel model = renderer.getModel(stack, level, null, seed);
        ItemTransform transform = model.getTransforms().getTransform(ItemDisplayContext.FIXED);
        Vector3f scale = transform.scale;

        float scaleX = Math.abs(scale.x()) > 1.0E-4F ? scale.x() : 1.0F;
        float scaleY = Math.abs(scale.y()) > 1.0E-4F ? scale.y() : 1.0F;
        float scaleZ = Math.abs(scale.z()) > 1.0E-4F ? scale.z() : 1.0F;

        return new AABB(
            box.minX / scaleX, box.minY / scaleY, box.minZ / scaleZ,
            box.maxX / scaleX, box.maxY / scaleY, box.maxZ / scaleZ
        );
    }
    
    private static AABB fallback() {
        return new AABB(-0.25, -0.25, -0.25, 0.25, 0.25, 0.25);
    }
    
    private static final class CustomRendererBoundManager implements VertexConsumer {
        private double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        private double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
        private boolean hasData = false;
        
        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            hasData = true;
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
            return this;
        }
        
        @Override public VertexConsumer setColor(int r, int g, int b, int a) { return this; }
        @Override public VertexConsumer setUv(float u, float v) { return this; }
        @Override public VertexConsumer setUv1(int u, int v) { return this; }
        @Override public VertexConsumer setUv2(int u, int v) { return this; }
        @Override public VertexConsumer setNormal(float x, float y, float z) { return this; }
        
        boolean hasData() { return hasData; }
        AABB toAABB() { return new AABB(minX, minY, minZ, maxX, maxY, maxZ); }
    }
}