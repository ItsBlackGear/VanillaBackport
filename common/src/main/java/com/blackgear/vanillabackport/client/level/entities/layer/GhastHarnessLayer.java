package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.level.entities.model.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.entities.model.HappyGhastModel;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class GhastHarnessLayer<T extends HappyGhast> extends RenderLayer<T, HappyGhastModel<T>> {
    private static final Map<ItemStack, ResourceLocation> TEXTURE_BY_ITEM = new ImmutableMap.Builder<ItemStack, ResourceLocation>()
        .put(new ItemStack(ModItems.WHITE_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/white_harness.png"))
        .put(new ItemStack(ModItems.ORANGE_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/orange_harness.png"))
        .put(new ItemStack(ModItems.MAGENTA_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/magenta_harness.png"))
        .put(new ItemStack(ModItems.LIGHT_BLUE_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/light_blue_harness.png"))
        .put(new ItemStack(ModItems.YELLOW_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/yellow_harness.png"))
        .put(new ItemStack(ModItems.LIME_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/lime_harness.png"))
        .put(new ItemStack(ModItems.PINK_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/pink_harness.png"))
        .put(new ItemStack(ModItems.GRAY_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/gray_harness.png"))
        .put(new ItemStack(ModItems.LIGHT_GRAY_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/light_gray_harness.png"))
        .put(new ItemStack(ModItems.CYAN_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/cyan_harness.png"))
        .put(new ItemStack(ModItems.PURPLE_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/purple_harness.png"))
        .put(new ItemStack(ModItems.BLUE_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/blue_harness.png"))
        .put(new ItemStack(ModItems.BROWN_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/brown_harness.png"))
        .put(new ItemStack(ModItems.GREEN_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/green_harness.png"))
        .put(new ItemStack(ModItems.RED_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/red_harness.png"))
        .put(new ItemStack(ModItems.BLACK_HARNESS.get()), VanillaBackport.resource("textures/entity/ghast/harness/black_harness.png"))
        .build();
    private final HappyGhastHarnessModel<T> model;

    public GhastHarnessLayer(RenderLayerParent<T, HappyGhastModel<T>> renderer, HappyGhastHarnessModel<T> model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        Optional<ResourceLocation> texture = TEXTURE_BY_ITEM.entrySet().stream()
            .filter(entry -> entity.getItemBySlot(EquipmentSlot.CHEST).is(entry.getKey().getItem()))
            .map(Map.Entry::getValue)
            .findFirst();
        if (texture.isPresent() && entity.isSaddled()) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertices = buffer.getBuffer(RenderType.entityCutoutNoCull(texture.get()));
            this.model.renderToBuffer(poseStack, vertices, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}