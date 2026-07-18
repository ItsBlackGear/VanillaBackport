package com.blackgear.vanillabackport.client.level.model.object;

import com.blackgear.vanillabackport.common.level.entity.decoration.Cushion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

@Environment(EnvType.CLIENT)
public class CushionModel extends EntityModel<Cushion> {
    protected final ModelPart root;
    
    public CushionModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root;
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild(
            "cushion",
            CubeListBuilder.create().texOffs(0, 0).addBox(-31.0F, -4.0F, -1.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(-0.005F)),
            PartPose.offset(23.0F, 4.0F, -7.0F)
        );
        return LayerDefinition.create(meshDefinition, 64, 64);
    }
    
    @Override
    public void setupAnim(Cushion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    
    }
    
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}