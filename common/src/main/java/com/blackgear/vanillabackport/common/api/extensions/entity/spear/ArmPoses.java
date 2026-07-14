package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import com.blackgear.vanillabackport.common.level.item.spear.SpearAnimations;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public enum ArmPoses {
    SPEAR(false, true) {
        @Override
        public <T extends LivingEntity> void animateUseItem(T entity, PoseStack pose, float ticksUsingItem, HumanoidArm arm, ItemStack actualItem, float partial) {
            SpearAnimations.thirdPersonUseItem(entity, pose, ticksUsingItem, arm, actualItem);
        }
    };

    private final boolean twoHanded;
    private final boolean affectsOffhandPose;

    ArmPoses(boolean twoHanded, boolean affectsOffhandPose) {
        this.twoHanded = twoHanded;
        this.affectsOffhandPose = affectsOffhandPose;
    }

    public boolean isTwoHanded() {
        return this.twoHanded;
    }

    public boolean affectsOffhandPose() {
        return this.affectsOffhandPose;
    }

    public <T extends LivingEntity> void animateUseItem(
        T entity,
        PoseStack pose,
        float ticksUsingItem,
        HumanoidArm arm,
        ItemStack actualItem,
        float partial
    ) {}

    public HumanoidModel.ArmPose get() {
        return HumanoidModel.ArmPose.valueOf(this.name());
    }
}