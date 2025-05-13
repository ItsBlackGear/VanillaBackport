package com.blackgear.vanillabackport.client.level.entities.model;

import com.blackgear.vanillabackport.client.level.entities.animation.CreakingAnimation;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.blackgear.vanillabackport.core.util.MthUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CreakingModel<T extends Creaking> extends HierarchicalModel<T> {
    public static final List<ModelPart> NO_PARTS = List.of();
    private final ModelPart root;
    private final ModelPart head;
    private final List<ModelPart> headParts;

    private boolean eyesGlowing;

    public CreakingModel(ModelPart root) {
        this.root = root.getChild("root");
        ModelPart upperBody = this.root.getChild("upper_body");
        this.head = upperBody.getChild("head");
        this.headParts = List.of(this.head);
    }

    private static MeshDefinition createMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition base = mesh.getRoot();
        PartDefinition root = base.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upperBody = root.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(-1.0F, -19.0F, 0.0F));
        upperBody.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-3.0F, -10.0F, -3.0F, 6.0F, 10.0F, 6.0F)
                .texOffs(28, 31)
                .addBox(-3.0F, -13.0F, -3.0F, 6.0F, 3.0F, 6.0F)
                .texOffs(12, 40)
                .addBox(3.0F, -13.0F, 0.0F, 9.0F, 14.0F, 0.0F)
                .texOffs(34, 12)
                .addBox(-12.0F, -14.0F, 0.0F, 9.0F, 14.0F, 0.0F),
            PartPose.offset(-3.0F, -11.0F, 0.0F)
        );
        upperBody.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(0.0F, -3.0F, -3.0F, 6.0F, 13.0F, 5.0F)
                .texOffs(24, 0)
                .addBox(-6.0F, -4.0F, -3.0F, 6.0F, 7.0F, 5.0F),
            PartPose.offset(0.0F, -7.0F, 1.0F)
        );
        upperBody.addOrReplaceChild(
            "right_arm",
            CubeListBuilder.create()
                .texOffs(22, 13)
                .addBox(-2.0F, -1.5F, -1.5F, 3.0F, 21.0F, 3.0F)
                .texOffs(46, 0)
                .addBox(-2.0F, 19.5F, -1.5F, 3.0F, 4.0F, 3.0F),
            PartPose.offset(-7.0F, -9.5F, 1.5F)
        );
        upperBody.addOrReplaceChild(
            "left_arm",
            CubeListBuilder.create()
                .texOffs(30, 40)
                .addBox(0.0F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F)
                .texOffs(52, 12)
                .addBox(0.0F, -5.0F, -1.5F, 3.0F, 4.0F, 3.0F)
                .texOffs(52, 19)
                .addBox(0.0F, 15.0F, -1.5F, 3.0F, 4.0F, 3.0F),
            PartPose.offset(6.0F, -9.0F, 0.5F)
        );
        root.addOrReplaceChild(
            "left_leg",
            CubeListBuilder.create()
                .texOffs(42, 40)
                .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 16.0F, 3.0F)
                .texOffs(45, 55)
                .addBox(-1.5F, 15.7F, -4.5F, 5.0F, 0.0F, 9.0F),
            PartPose.offset(1.5F, -16.0F, 0.5F)
        );
        root.addOrReplaceChild(
            "right_leg",
            CubeListBuilder.create()
                .texOffs(0, 34)
                .addBox(-3.0F, -1.5F, -1.5F, 3.0F, 19.0F, 3.0F)
                .texOffs(45, 46)
                .addBox(-5.0F, 17.2F, -4.5F, 5.0F, 0.0F, 9.0F)
                .texOffs(12, 34)
                .addBox(-3.0F, -4.5F, -1.5F, 3.0F, 3.0F, 3.0F),
            PartPose.offset(-1.0F, -17.5F, 0.5F)
        );
        return mesh;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = createMesh();
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        if (entity.isTearingDown()) {
            this.eyesGlowing = entity.hasGlowingEyes();
        } else {
            this.eyesGlowing = entity.isActive();
        }
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.xRot = headPitch * MthUtils.TO_DEGREES;
        this.head.yRot = netHeadYaw * MthUtils.TO_DEGREES;

        if (entity.canMove()) {
            this.animateWalk(CreakingAnimation.CREAKING_WALK, limbSwing, limbSwingAmount, 1.0F, 1.0F);
        }

        this.animate(entity.attackAnimationState, CreakingAnimation.CREAKING_ATTACK, ageInTicks);
        this.animate(entity.invulnerabilityAnimationState, CreakingAnimation.CREAKING_INVULNERABLE, ageInTicks);
        this.animate(entity.deathAnimationState, CreakingAnimation.CREAKING_DEATH, ageInTicks);
    }

    public List<ModelPart> getHeadParts() {
        return !this.eyesGlowing ? NO_PARTS : this.headParts;
    }
}
