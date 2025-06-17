package com.blackgear.vanillabackport.client.level.entities.model;

import com.blackgear.vanillabackport.client.level.entities.animation.BatAnimation;
import com.blackgear.vanillabackport.common.level.entities.bat.BatAnimator;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.ambient.Bat;

@Environment(EnvType.CLIENT)
public class BatModel extends HierarchicalModel<Bat> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart rightWingTip;
    private final ModelPart leftWingTip;
    private final ModelPart feet;

    public BatModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.rightWing = this.body.getChild("right_wing");
        this.rightWingTip = this.rightWing.getChild("right_wing_tip");
        this.leftWing = this.body.getChild("left_wing");
        this.leftWingTip = this.leftWing.getChild("left_wing_tip");
        this.feet = this.body.getChild("feet");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild(
        "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F),
            PartPose.offset(0.0F, 17.0F, 0.0F)
        );
        PartDefinition head = root.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 7)
                .addBox(-2.0F, -3.0F, -1.0F, 4.0F, 3.0F, 2.0F),
            PartPose.offset(0.0F, 17.0F, 0.0F)
        );
        head.addOrReplaceChild(
            "right_ear",
            CubeListBuilder.create()
                .texOffs(1, 15)
                .addBox(-2.5F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F),
            PartPose.offset(-1.5F, -2.0F, 0.0F)
        );
        head.addOrReplaceChild(
            "left_ear",
            CubeListBuilder.create()
                .texOffs(8, 15)
                .addBox(-0.1F, -3.0F, 0.0F, 3.0F, 5.0F, 0.0F),
            PartPose.offset(1.1F, -3.0F, 0.0F)
        );
        PartDefinition rightWing = body.addOrReplaceChild(
            "right_wing",
            CubeListBuilder.create()
                .texOffs(12, 0)
                .addBox(-2.0F, -2.0F, 0.0F, 2.0F, 7.0F, 0.0F),
            PartPose.offset(-1.5F, 0.0F, 0.0F)
        );
        rightWing.addOrReplaceChild(
            "right_wing_tip",
            CubeListBuilder.create()
                .texOffs(16, 0)
                .addBox(-6.0F, -2.0F, 0.0F, 6.0F, 8.0F, 0.0F),
            PartPose.offset(-2.0F, 0.0F, 0.0F)
        );
        PartDefinition leftWing = body.addOrReplaceChild(
            "left_wing",
            CubeListBuilder.create()
                .texOffs(12, 7)
                .addBox(0.0F, -2.0F, 0.0F, 2.0F, 7.0F, 0.0F),
            PartPose.offset(1.5F, 0.0F, 0.0F)
        );
        leftWing.addOrReplaceChild(
            "left_wing_tip",
            CubeListBuilder.create()
                .texOffs(16, 8)
                .addBox(0.0F, -2.0F, 0.0F, 6.0F, 8.0F, 0.0F),
            PartPose.offset(2.0F, 0.0F, 0.0F)
        );
        body.addOrReplaceChild(
            "feet",
            CubeListBuilder.create()
                .texOffs(16, 16)
                .addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F),
            PartPose.offset(0.0F, 5.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(Bat entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (entity.isResting()) {
            this.head.xRot = headPitch * MthUtils.TO_DEGREES;
            this.head.yRot = netHeadYaw * MthUtils.TO_DEGREES;
        }

        if (entity instanceof BatAnimator animator) {
            this.animate(animator.flyAnimationState(), BatAnimation.BAT_FLYING, ageInTicks);
            this.animate(animator.restAnimationState(), BatAnimation.BAT_RESTING, ageInTicks);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}