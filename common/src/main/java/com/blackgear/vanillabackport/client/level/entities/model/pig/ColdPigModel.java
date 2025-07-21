package com.blackgear.vanillabackport.client.level.entities.model.pig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.animal.Pig;

@Environment(EnvType.CLIENT)
public class ColdPigModel<T extends Pig> extends PigModel<T> {
    public ColdPigModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = QuadrupedModel.createBodyMesh(6, CubeDeformation.NONE);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F)
                .texOffs(16, 16)
                .addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F),
            PartPose.offset(0.0F, 12.0F, -6.0F)
        );
        root.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(28, 8)
                .addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F)
                .texOffs(28, 32)
                .addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, new CubeDeformation(0.5F)),
            PartPose.offsetAndRotation(0.0F, 11.0F, 2.0F, ((float) Math.PI / 2F), 0.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }
}