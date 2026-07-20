package com.blackgear.vanillabackport.client.level.model.entity.nautilus;

import com.blackgear.vanillabackport.client.level.animation.NautilusAnimation;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class NautilusModel<T extends AbstractNautilus> extends HierarchicalModel<T> {
    protected final ModelPart body;
    protected final ModelPart nautilus;
    
    public NautilusModel(ModelPart root) {
        this.nautilus = root.getChild("root");
        this.body = this.nautilus.getChild("body");
    }
    
    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createBodyMesh(), 128, 128);
    }
    
    public static MeshDefinition createBodyMesh() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, -6.0F));
        nautilus.addOrReplaceChild(
            "shell",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26)
                .addBox(-7.0F, 0.0F, -7.0F, 14.0F, 8.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(48, 26)
                .addBox(-7.0F, 0.0F, 6.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -13.0F, 5.0F)
        );
        PartDefinition body = nautilus.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 54)
                .addBox(-5.0F, -4.51F, -3.0F, 10.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 76)
                .addBox(-5.0F, -4.51F, 7.0F, 10.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -8.5F, 12.3F)
        );
        body.addOrReplaceChild(
            "upper_mouth",
            CubeListBuilder.create().texOffs(54, 54).addBox(-5.0F, -2.0F, 0.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(-0.001F)),
            PartPose.offset(0.0F, -2.51F, 7.0F)
        );
        body.addOrReplaceChild(
            "inner_mouth",
            CubeListBuilder.create().texOffs(54, 70).addBox(-3.0F, -2.0F, -0.5F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -0.51F, 7.5F)
        );
        body.addOrReplaceChild(
            "lower_mouth",
            CubeListBuilder.create().texOffs(54, 62).addBox(-5.0F, -1.98F, 0.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(-0.001F)),
            PartPose.offset(0.0F, 1.49F, 7.0F)
        );
        return meshdefinition;
    }
    
    public static LayerDefinition createBabyBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-0.5F, 28.0F, -0.5F));
        nautilus.addOrReplaceChild(
            "shell",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-6.0F, -4.0F, -1.0F, 7.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11)
                .addBox(-6.0F, 0.0F, -1.0F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(23, 11)
                .addBox(-6.0F, 0.0F, 5.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offset(3.0F, -8.0F, -2.0F)
        );
        PartDefinition body = nautilus.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 24)
                .addBox(-2.5F, -3.01F, -1.0F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35)
                .addBox(-2.5F, -3.01F, 4.1F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.5F, -5.0F, 3.0F)
        );
        body.addOrReplaceChild(
            "upper_mouth",
            CubeListBuilder.create().texOffs(24, 24).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.001F)),
            PartPose.offset(0.0F, -2.01F, 3.9F)
        );
        body.addOrReplaceChild(
            "inner_mouth",
            CubeListBuilder.create().texOffs(24, 32).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -1.01F, 4.9F)
        );
        body.addOrReplaceChild(
            "lower_mouth",
            CubeListBuilder.create().texOffs(24, 28).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.001F)),
            PartPose.offset(0.0F, -0.01F, 3.9F)
        );
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    
    public static LayerDefinition createSaddleLayer() {
        MeshDefinition meshdefinition = createBodyMesh();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, -6.0F));
        nautilus.addOrReplaceChild(
            "shell",
            CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 16.0F, new CubeDeformation(0.2F)),
            PartPose.offset(0.0F, -13.0F, 5.0F)
        );
        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    
    public static LayerDefinition createBodyArmorLayer() {
        MeshDefinition meshdefinition = createBodyMesh();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition nautilus = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, -6.0F));
        nautilus.addOrReplaceChild(
            "shell",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 16.0F, new CubeDeformation(0.01F))
                .texOffs(0, 26)
                .addBox(-7.0F, 0.0F, -7.0F, 14.0F, 8.0F, 20.0F, new CubeDeformation(0.01F))
                .texOffs(48, 26)
                .addBox(-7.0F, 0.0F, 6.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -13.0F, 5.0F)
        );
        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    
    @Override
    public ModelPart root() {
        return this.nautilus;
    }
    
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyBodyRotation(netHeadYaw, headPitch);
        this.animateWalk(NautilusAnimation.SWIMMING, limbSwing + ageInTicks / 5.0F, limbSwingAmount + 0.2F, 2.0F, 3.0F);
    }
    
    private void applyBodyRotation(float yRot, float xRot) {
        yRot = Mth.clamp(yRot, -10.0F, 10.0F);
        xRot = Mth.clamp(xRot, -10.0F, 10.0F);
        this.body.yRot = yRot * Mth.DEG_TO_RAD;
        this.body.xRot = xRot * Mth.DEG_TO_RAD;
        
    }
}