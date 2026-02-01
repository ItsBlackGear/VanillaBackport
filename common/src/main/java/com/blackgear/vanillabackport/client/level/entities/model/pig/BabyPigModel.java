package com.blackgear.vanillabackport.client.level.entities.model.pig;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.animal.Pig;

@Environment(EnvType.CLIENT)
public class BabyPigModel<T extends Pig> extends PigModel<T> {
    public BabyPigModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -4.5F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.5F)
        );
        root.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 15)
                .addBox(-3.51F, -5.0F, -5.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(6, 27)
                .addBox(-1.5F, -2.0F, -6.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 19.0F, -2.0F)
        );
        root.addOrReplaceChild(
            "left_front_leg",
            CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(2.5F, 22.0F, -3.0F)
        );
        root.addOrReplaceChild(
            "right_front_leg",
            CubeListBuilder.create().texOffs(23, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-2.5F, 22.0F, -3.0F)
        );
        root.addOrReplaceChild(
            "left_hind_leg",
            CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(2.5F, 22.0F, 4.0F)
        );
        root.addOrReplaceChild(
            "right_hind_leg",
            CubeListBuilder.create().texOffs(23, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-2.5F, 22.0F, 4.0F)
        );
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.headParts().forEach(part -> part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
        this.bodyParts().forEach(part -> part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
    }
}