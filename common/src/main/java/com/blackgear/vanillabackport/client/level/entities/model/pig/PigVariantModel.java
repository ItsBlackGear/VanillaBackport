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
public class PigVariantModel<T extends Pig> extends PigModel<T> {
    public PigVariantModel(ModelPart root) {
        super(root);
    }

    protected static MeshDefinition createBasePigModel() {
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

        return mesh;
    }
}