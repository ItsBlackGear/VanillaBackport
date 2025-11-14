package com.blackgear.vanillabackport.client.level.entities.model;

import com.blackgear.vanillabackport.client.level.entities.animation.ArmadilloAnimation;
import com.blackgear.vanillabackport.common.level.entities.armadillo.Armadillo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ArmadilloModel extends AgeableHierarchicalModel<Armadillo> {
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart cube;
	private final ModelPart head;
	private final ModelPart tail;

	public ArmadilloModel(ModelPart root) {
		super(0.6F, 16.02F);
		this.root = root;
		this.body = root.getChild("body");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.head = this.body.getChild("head");
		this.tail = this.body.getChild("tail");
		this.cube = root.getChild("cube");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		PartDefinition body = root.addOrReplaceChild(
			"body",
			CubeListBuilder.create()
				.texOffs(0, 20)
				.addBox(-4.0F, -7.0F, -10.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.3F))
				.texOffs(0, 40)
				.addBox(-4.0F, -7.0F, -10.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 21.0F, 4.0F)
		);
		body.addOrReplaceChild(
			"tail",
			CubeListBuilder.create().texOffs(44, 53).addBox(-0.5F, -0.0865F, 0.0933F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 0.5061F, 0.0F, 0.0F)
		);
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -11.0F));
		head.addOrReplaceChild(
			"head_cube",
			CubeListBuilder.create().texOffs(43, 15).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F)
		);
		PartDefinition rightEar = head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.offset(-1.0F, -1.0F, 0.0F));
		rightEar.addOrReplaceChild(
			"right_ear_cube",
			CubeListBuilder.create().texOffs(43, 10).addBox(-2.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(-0.5F, 0.0F, -0.6F, 0.1886F, -0.3864F, -0.0718F)
		);
		PartDefinition leftEar = head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.offset(1.0F, -2.0F, 0.0F));
		leftEar.addOrReplaceChild(
			"left_ear_cube",
			CubeListBuilder.create().texOffs(47, 10).addBox(0.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.5F, 1.0F, -0.6F, 0.1886F, 0.3864F, 0.0718F)
		);
		root.addOrReplaceChild(
			"right_hind_leg",
			CubeListBuilder.create().texOffs(51, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-2.0F, 21.0F, 4.0F)
		);
		root.addOrReplaceChild(
			"left_hind_leg",
			CubeListBuilder.create().texOffs(42, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offset(2.0F, 21.0F, 4.0F)
		);
		root.addOrReplaceChild(
			"right_front_leg",
			CubeListBuilder.create().texOffs(51, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-2.0F, 21.0F, -4.0F)
		);
		root.addOrReplaceChild(
			"left_front_leg",
			CubeListBuilder.create().texOffs(42, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offset(2.0F, 21.0F, -4.0F)
		);
		root.addOrReplaceChild(
			"cube",
			CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -6.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 24.0F, 0.0F)
		);
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(Armadillo entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (entity.shouldHideInShell()) {
			this.body.skipDraw = true;
			this.leftHindLeg.visible = false;
			this.rightHindLeg.visible = false;
			this.tail.visible = false;
			this.cube.visible = true;
		} else {
			this.body.skipDraw = false;
			this.leftHindLeg.visible = true;
			this.rightHindLeg.visible = true;
			this.tail.visible = true;
			this.cube.visible = false;
			this.head.xRot = Mth.clamp(headPitch, -22.5F, 25.0F) * (float) (Math.PI / 180.0);
			this.head.yRot = Mth.clamp(netHeadYaw, -32.5F, 32.5F) * (float) (Math.PI / 180.0);
		}

		this.animateWalk(ArmadilloAnimation.ARMADILLO_WALK, limbSwing, limbSwingAmount, 16.5F, 2.5F);
		this.animate(entity.rollOutAnimationState, ArmadilloAnimation.ARMADILLO_ROLL_OUT, ageInTicks, 1.0F);
		this.animate(entity.rollUpAnimationState, ArmadilloAnimation.ARMADILLO_ROLL_UP, ageInTicks, 1.0F);
		this.animate(entity.peekAnimationState, ArmadilloAnimation.ARMADILLO_PEEK, ageInTicks, 1.0F);
	}
}
