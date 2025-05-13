package com.blackgear.vanillabackport.client.level.entities.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class HappyGhastHarnessModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart goggles;

    public HappyGhastHarnessModel(ModelPart root) {
        this.root = root;
        this.goggles = root.getChild("goggles");
    }

    public static LayerDefinition createHarnessLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "harness",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "goggles",
            CubeListBuilder.create()
                .texOffs(0, 32)
                .addBox(-8.0F, -2.5F, -2.5F, 16.0F, 5.0F, 5.0F, new CubeDeformation(0.15F)),
            PartPose.offset(0.0F, 14.0F, -5.5F)
        );
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isVehicle()) {
            this.goggles.xRot = 0.0F;
            this.goggles.y = 14.0F;
        } else {
            this.goggles.xRot = -0.7854F;
            this.goggles.y = 9.0F;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}