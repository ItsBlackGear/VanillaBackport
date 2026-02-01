package com.blackgear.vanillabackport.client.level.entities.model.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;

@Environment(EnvType.CLIENT)
public class BabyWolfModel<T extends Wolf> extends WolfModel<T> {
    public BabyWolfModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild(
            "head",
            CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -3.25F, -3.0F, 6.0F, 5.0F, 5.0F).texOffs(17, 12).addBox(-1.5F, -0.25F, -5.0F, 3.0F, 2.0F, 2.0F),
            PartPose.offset(0.0F, 18.25F, -4.0F)
        );
        head.addOrReplaceChild("real_head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild(
            "right_ear", CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F), PartPose.offset(-2.0F, -4.25F, -0.5F)
        );
        head.addOrReplaceChild(
            "left_ear", CubeListBuilder.create().texOffs(20, 5).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F), PartPose.offset(2.0F, -4.25F, -0.5F)
        );
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 4.0F, 8.0F), PartPose.offset(0.0F, 19.0F, 0.0F));
        root.addOrReplaceChild(
            "right_hind_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(-1.5F, 21.0F, 3.0F)
        );
        root.addOrReplaceChild(
            "left_hind_leg", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(1.5F, 21.0F, 3.0F)
        );
        root.addOrReplaceChild(
            "right_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(-1.5F, 21.0F, -3.0F)
        );
        root.addOrReplaceChild(
            "left_front_leg", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(1.5F, 21.0F, -3.0F)
        );
        PartDefinition tail = root.addOrReplaceChild(
            "tail",
            CubeListBuilder.create().texOffs(18, 16).addBox(-1.0F, -0.5F, -1.25F, 2.0F, 6.0F, 2.0F),
            PartPose.offsetAndRotation(0.0F, 18.5F, 3.75F, 0.9599F, 0.0F, 0.0F)
        );
        tail.addOrReplaceChild("real_tail", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
//        if (entity.isAngry()) {
//            this.tail.yRot = 0.0F;
//        } else {
//            this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//        }
//
//        if (entity.isInSittingPose()) {
//            this.setSittingPose(entity);
//        } else {
//            // Reset to standing pose
//            this.body.setPos(0.0F, 19.0F, 0.0F);
//            this.body.xRot = 0.0F;
//            this.tail.setPos(0.0F, 18.5F, 3.75F);
//            this.rightHindLeg.setPos(-1.5F, 21.0F, 3.0F);
//            this.leftHindLeg.setPos(1.5F, 21.0F, 3.0F);
//            this.rightFrontLeg.setPos(-1.5F, 21.0F, -3.0F);
//            this.leftFrontLeg.setPos(1.5F, 21.0F, -3.0F);
//
//            this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//            this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
//            this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
//            this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//        }
//
//        // shake off water
//        this.body.zRot = entity.getBodyRollAngle(partialTick, -0.16F);
//        this.head.zRot = entity.getHeadRollAngle(partialTick) + entity.getBodyRollAngle(partialTick, 0.0F);
//        this.tail.zRot = entity.getBodyRollAngle(partialTick, -0.2F);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        this.head.xRot = headPitch * 0.017453292F;
//        this.head.yRot = netHeadYaw * 0.017453292F;
//        this.tail.xRot = ageInTicks;
    }

    private void setSittingPose(T entity) {
//        float ageScale = entity.getScale();
//        this.body.setPos(0.0F, 19.0F + 4.0F * ageScale, -2.0F * ageScale);
//        this.body.xRot = (float) (Math.PI / 4) + 1.0F;
//        this.tail.setPos(0.0F, 18.5F + 9.0F * ageScale, 3.75F - 2.0F * ageScale);
//        this.rightHindLeg.setPos(-1.5F, 21.0F + 6.7F * ageScale, 3.0F - 5.0F * ageScale);
//        this.rightHindLeg.xRot = (float) (Math.PI * 3.0 / 2.0);
//        this.leftHindLeg.setPos(1.5F, 21.0F + 6.7F * ageScale, 3.0F - 5.0F * ageScale);
//        this.leftHindLeg.xRot = (float) (Math.PI * 3.0 / 2.0);
//        this.rightFrontLeg.xRot = 5.811947F;
//        this.rightFrontLeg.setPos(-1.5F + 0.01F * ageScale, 21.0F + ageScale, -3.0F);
//        this.leftFrontLeg.xRot = 5.811947F;
//        this.leftFrontLeg.setPos(1.5F - 0.01F * ageScale, 21.0F + ageScale, -3.0F);
    }

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.headParts().forEach(part -> part.render(pose, buffer, packedLight, packedOverlay, red, green, blue, alpha));
        this.bodyParts().forEach(part -> part.render(pose, buffer, packedLight, packedOverlay, red, green, blue, alpha));
    }
}