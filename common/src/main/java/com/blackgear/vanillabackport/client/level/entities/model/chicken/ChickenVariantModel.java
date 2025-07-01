package com.blackgear.vanillabackport.client.level.entities.model.chicken;

import com.blackgear.vanillabackport.core.util.MthUtils;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;

@Environment(EnvType.CLIENT)
public class ChickenVariantModel<T extends Chicken> extends ChickenModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart beak;
    private final ModelPart redThing;

    public ChickenVariantModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.beak = root.getChild("beak");
        this.redThing = root.getChild("red_thing");
        this.body = root.getChild("body");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
    }

    protected static MeshDefinition createBaseChickenModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F),
            PartPose.offset(0.0F, 15.0F, -4.0F)
        );
        root.addOrReplaceChild(
            "beak",
            CubeListBuilder.create()
                .texOffs(14, 0)
                .addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F),
            PartPose.offset(0.0F, 15.0F, -4.0F)
        );
        root.addOrReplaceChild(
            "red_thing",
            CubeListBuilder.create()
                .texOffs(14, 4)
                .addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F),
            PartPose.offset(0.0F, 15.0F, -4.0F)
        );
        root.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 9)
                .addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, ((float) Math.PI / 2F), 0.0F, 0.0F)
        );

        CubeListBuilder legShape = CubeListBuilder.create()
            .texOffs(26, 0)
            .addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);

        root.addOrReplaceChild(
            "right_leg",
            legShape,
            PartPose.offset(-2.0F, 19.0F, 1.0F)
        );
        root.addOrReplaceChild(
            "left_leg",
            legShape,
            PartPose.offset(1.0F, 19.0F, 1.0F)
        );
        root.addOrReplaceChild(
            "right_wing",
            CubeListBuilder.create()
                .texOffs(24, 13)
                .addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
            PartPose.offset(-4.0F, 13.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "left_wing",
            CubeListBuilder.create()
                .texOffs(24, 13)
                .addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
            PartPose.offset(4.0F, 13.0F, 0.0F)
        );

        return mesh;
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.beak, this.redThing);
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing);
    }

    @Override
    public void setupAnim(
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        this.head.xRot = headPitch * MthUtils.TO_DEGREES;
        this.head.yRot = netHeadYaw * MthUtils.TO_DEGREES;
        this.beak.xRot = this.head.xRot;
        this.beak.yRot = this.head.yRot;
        this.redThing.xRot = this.head.xRot;
        this.redThing.yRot = this.head.yRot;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.zRot = ageInTicks;
        this.leftWing.zRot = -ageInTicks;
    }
}