package com.blackgear.vanillabackport.client.level.entity.model.wolf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class WolfArmorModel {
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
            PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, Mth.HALF_PI, 0.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "upper_body",
            CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, grow),
            PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, Mth.HALF_PI, 0.0F, 0.0F)
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