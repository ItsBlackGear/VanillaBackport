package com.blackgear.vanillabackport.client.level.model.object;

import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.AbstractWindCharge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class WindChargeModel extends HierarchicalModel<AbstractWindCharge> {
	private final ModelPart bone;
	private final ModelPart windCharge;
	private final ModelPart wind;

	public WindChargeModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.bone = root.getChild("bone");
		this.wind = this.bone.getChild("wind");
		this.windCharge = this.bone.getChild("wind_charge");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		bone.addOrReplaceChild("wind",
			CubeListBuilder.create()
				.texOffs(15, 20)
				.addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 9)
				.addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F)
		);
		bone.addOrReplaceChild("wind_charge",
			CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 0.0F, 0.0F)
		);
		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(AbstractWindCharge entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.windCharge.yRot = -ageInTicks * 16.0F * Mth.DEG_TO_RAD;
		this.wind.yRot = ageInTicks * 16.0F * Mth.DEG_TO_RAD;
	}

	@Override
	public ModelPart root() {
		return this.bone;
	}
}
