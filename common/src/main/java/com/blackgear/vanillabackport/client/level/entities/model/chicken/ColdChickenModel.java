package com.blackgear.vanillabackport.client.level.entities.model.chicken;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.Chicken;

@Environment(EnvType.CLIENT)
public class ColdChickenModel<T extends Chicken> extends ChickenVariantModel<T> {
    public ColdChickenModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = createBaseChickenModel();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 9)
                .addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F)
                .texOffs(38, 9)
                .addBox(0.0F, 3.0F, -1.0F, 0.0F, 3.0F, 5.0F),
            PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, ((float) Math.PI / 2F), 0.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F)
                .texOffs(44, 0)
                .addBox(-3.0F, -7.0F, -2.015F, 6.0F, 3.0F, 4.0F),
            PartPose.offset(0.0F, 15.0F, -4.0F)
        );
        return LayerDefinition.create(mesh, 64, 32);
    }
}