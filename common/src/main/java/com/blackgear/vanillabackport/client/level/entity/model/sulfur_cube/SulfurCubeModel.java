package com.blackgear.vanillabackport.client.level.entity.model.sulfur_cube;

import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class SulfurCubeModel extends HierarchicalModel<SulfurCube> {
    private final ModelPart root;
    private final boolean skipRendering;

    public SulfurCubeModel(ModelPart root) {
        this.root = root;
        this.skipRendering = false;
    }

    public SulfurCubeModel(ModelPart root, boolean skipRendering) {
        this.root = root;
        this.skipRendering = skipRendering;
    }

    public static LayerDefinition createOuterBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "cube",
            CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, -9.0F, 18.0F, 18.0F, 18.0F),
            PartPose.ZERO
        );
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createInnerBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "cube",
            CubeListBuilder.create().texOffs(0, 36).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
            PartPose.ZERO
        );
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(SulfurCube entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.skipRendering) return;
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
    }
}