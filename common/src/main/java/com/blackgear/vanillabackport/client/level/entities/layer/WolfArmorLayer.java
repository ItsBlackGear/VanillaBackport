package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.wolf.ModCrackiness;
import com.blackgear.vanillabackport.common.level.entities.wolf.ModCrackiness.Level;
import com.blackgear.vanillabackport.common.level.items.WolfArmorItem;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class WolfArmorLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {
    private final WolfModel<Wolf> model;
    private static final Map<Level, ResourceLocation> ARMOR_CRACK_LOCATIONS = Map.of(
        Level.LOW, VanillaBackport.vanilla("textures/entity/wolf/wolf_armor_crackiness_low.png"),
        Level.MEDIUM, VanillaBackport.vanilla("textures/entity/wolf/wolf_armor_crackiness_medium.png"),
        Level.HIGH, VanillaBackport.vanilla("textures/entity/wolf/wolf_armor_crackiness_high.png")
    );

    public WolfArmorLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> renderer, EntityModelSet models) {
        super(renderer);
        this.model = new WolfModel<>(models.bakeLayer(ModModelLayers.WOLF_ARMOR));
    }

    @Override
    public void render(
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        Wolf wolf,
        float limbSwing,
        float limbSwingAmount,
        float partialTick,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        if (!wolf.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && wolf.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.WOLF_ARMOR.get())) {
            ItemStack stack = wolf.getItemBySlot(EquipmentSlot.CHEST);
            if (stack.getItem() instanceof WolfArmorItem armor) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(wolf, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(wolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertices = buffer.getBuffer(RenderType.entityCutoutNoCull(armor.getTexture()));
                this.model.renderToBuffer(poseStack, vertices, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, stack, armor);
                this.maybeRenderCracks(poseStack, buffer, packedLight, stack);
            }
        }
    }

    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack stack, WolfArmorItem armor) {
        ResourceLocation overlay = armor.getOverlayTexture();
        if (overlay == null) return;

        int color = WolfArmorItem.getColorOrDefault(stack, 0);

        // if not dyed, do not render the overlay at all
        if (color == 10511680) return;

        float red = (float)(color >> 16 & 0xFF) / 255.0F;
        float green = (float)(color >> 8 & 0xFF) / 255.0F;
        float blue = (float)(color & 0xFF) / 255.0F;

        VertexConsumer vertices = buffer.getBuffer(RenderType.entityCutoutNoCull(overlay));
        this.model.renderToBuffer(poseStack, vertices, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    private void maybeRenderCracks(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack stack) {
        Level level = ModCrackiness.WOLF_ARMOR.byDamage(stack);
        if (level != Level.NONE) {
            ResourceLocation texture = ARMOR_CRACK_LOCATIONS.get(level);
            VertexConsumer vertices = buffer.getBuffer(RenderType.entityTranslucent(texture));
            this.model.renderToBuffer(poseStack, vertices, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static MeshDefinition createMeshDefinition(CubeDeformation grow) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0F, 13.5F, -7.0F));
        head.addOrReplaceChild(
            "real_head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, grow)
                .texOffs(16, 14)
                .addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, grow)
                .texOffs(16, 14)
                .addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, grow)
                .texOffs(0, 10)
                .addBox(-0.5F, -0.001F, -5.0F, 3.0F, 3.0F, 4.0F, grow),
            PartPose.ZERO
        );
        root.addOrReplaceChild(
            "body",
            CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, grow),
            PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "upper_body",
            CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, grow),
            PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
        );
        CubeListBuilder legShape = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, grow);
        root.addOrReplaceChild("right_hind_leg", legShape, PartPose.offset(-2.5F, 16.0F, 7.0F));
        root.addOrReplaceChild("left_hind_leg", legShape, PartPose.offset(0.5F, 16.0F, 7.0F));
        root.addOrReplaceChild("right_front_leg", legShape, PartPose.offset(-2.5F, 16.0F, -4.0F));
        root.addOrReplaceChild("left_front_leg", legShape, PartPose.offset(0.5F, 16.0F, -4.0F));
        PartDefinition tail = root.addOrReplaceChild(
            "tail", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, (float) (Math.PI / 5), 0.0F, 0.0F)
        );
        tail.addOrReplaceChild(
            "real_tail", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, grow), PartPose.ZERO
        );
        return mesh;
    }
}