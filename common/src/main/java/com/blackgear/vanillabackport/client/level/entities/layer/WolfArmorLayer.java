package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.items.AnimalArmorItem;
import com.blackgear.vanillabackport.common.level.items.DyeableAnimalArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class WolfArmorLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {
    private final WolfModel<Wolf> model;

    public WolfArmorLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new WolfModel<>(modelSet.bakeLayer(ModModelLayers.WOLF_ARMOR));
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            Wolf wolf,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        ItemStack stack = wolf.getItemBySlot(EquipmentSlot.CHEST);
        if (!(stack.getItem() instanceof AnimalArmorItem armorItem)) {
            return;
        }

        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(wolf, limbSwing, limbSwingAmount, partialTicks);
        this.model.setupAnim(wolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        String materialName = armorItem.getMaterial().getName();

        ResourceLocation baseTex = new ResourceLocation("textures/entity/equipment/wolf_body/" + materialName + ".png");

        VertexConsumer base = buffer.getBuffer(RenderType.entityCutoutNoCull(baseTex));
        this.model.renderToBuffer(
                poseStack, base, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F // always white
        );

        if (stack.getItem() instanceof DyeableAnimalArmorItem dyeable && dyeable.hasCustomColor(stack)) {

            ResourceLocation overlayTex = new ResourceLocation("textures/entity/equipment/wolf_body/" + materialName + "_overlay.png");

            int color = dyeable.getColor(stack);
            float r = (color >> 16 & 255) / 255.0F;
            float g = (color >> 8 & 255) / 255.0F;
            float b = (color & 255) / 255.0F;

            VertexConsumer overlay = buffer.getBuffer(RenderType.entityCutoutNoCull(overlayTex));
            this.model.renderToBuffer(
                    poseStack, overlay, packedLight, OverlayTexture.NO_OVERLAY,
                    r, g, b, 1.0F
            );
        }
    }

    public WolfModel<Wolf> getArmorModel() {
        return this.model;
    }
}