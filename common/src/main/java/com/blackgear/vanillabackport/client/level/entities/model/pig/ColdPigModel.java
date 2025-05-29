package com.blackgear.vanillabackport.client.level.entities.model.pig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;

@Environment(EnvType.CLIENT)
public class ColdPigModel<T extends Pig> extends PigVariantModel<T> {
    public ColdPigModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = createBasePigModel();
        PartDefinition root = mesh.getRoot();
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