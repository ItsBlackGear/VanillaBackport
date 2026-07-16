package com.blackgear.vanillabackport.core.forge;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ArmPoses;
import net.minecraft.client.model.HumanoidModel;

import java.util.Arrays;

public class ClientEnumData {
    public static void bootstrap() {
        Arrays.stream(ArmPoses.values()).forEach(pose -> {
            HumanoidModel.ArmPose.create(pose.name(), pose.isTwoHanded(), (model, entity, arm) -> {});
        });
    }
}