package com.blackgear.vanillabackport.client.level.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;

public class WolfArmorCrackinessLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {
    private final WolfModel<Wolf> armorModel;

    private static final ResourceLocation CRACK_1 =
            new ResourceLocation("textures/entity/wolf/wolf_armor_crackiness_low.png");
    private static final ResourceLocation CRACK_2 =
            new ResourceLocation("textures/entity/wolf/wolf_armor_crackiness_medium.png");
    private static final ResourceLocation CRACK_3 =
            new ResourceLocation("textures/entity/wolf/wolf_armor_crackiness_high.png");

    public WolfArmorCrackinessLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> parent,
                                    WolfModel<Wolf> armorModel) {
        super(parent);
        this.armorModel = armorModel;
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       Wolf wolf,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTicks,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {

        if (wolf.isInvisible()) return;

        ItemStack armor = wolf.getItemBySlot(EquipmentSlot.CHEST);
        if (armor.isEmpty() || !armor.isDamageableItem()) return;

        int max = armor.getMaxDamage();
        int dmg = armor.getDamageValue();

        int stageSize = max / 4;
        if (stageSize <= 0) return;

        int stage = dmg / stageSize;
        if (stage <= 0) return;

        ResourceLocation tex = switch (stage) {
            case 1 -> CRACK_1;
            case 2 -> CRACK_2;
            case 3 -> CRACK_3;
            default -> null;
        };

        if (tex == null) return;

        armorModel.prepareMobModel(wolf, limbSwing, limbSwingAmount, partialTicks);
        armorModel.setupAnim(wolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        renderColoredCutoutModel(
                armorModel,
                tex,
                poseStack,
                buffer,
                packedLight,
                wolf,
                1.0F, 1.0F, 1.0F
        );
    }
}
