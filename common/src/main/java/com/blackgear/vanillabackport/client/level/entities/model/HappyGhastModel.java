package com.blackgear.vanillabackport.client.level.entities.model;

import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;

@Environment(EnvType.CLIENT)
public class HappyGhastModel<T extends HappyGhast> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart[] tentacles = new ModelPart[9];

    public HappyGhastModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i] = this.body.getChild(createTentacleName(i));
        }
    }

    private static String createTentacleName(int index) {
        return "tentacle" + index;
    }

    public static LayerDefinition createBodyLayer(boolean isBaby) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild(
            "body",
            CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        if (isBaby) {
            body.addOrReplaceChild(
                "inner_body",
                CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-0.5F)),
                PartPose.offset(0.0F, 8.0F, 0.0F)
            );
        }

        body.addOrReplaceChild(
            createTentacleName(0),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F),
            PartPose.offset(-3.75F, 7.0F, -5.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(1),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F),
            PartPose.offset(1.25F, 7.0F, -5.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(2),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
            PartPose.offset(6.25F, 7.0F, -5.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(3),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F),
            PartPose.offset(-6.25F, 7.0F, 0.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(4),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F),
            PartPose.offset(-1.25F, 7.0F, 0.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(5),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F),
            PartPose.offset(3.75F, 7.0F, 0.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(6),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
            PartPose.offset(-3.75F, 7.0F, 5.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(7),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
            PartPose.offset(1.25F, 7.0F, 5.0F)
        );
        body.addOrReplaceChild(
            createTentacleName(8),
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F),
            PartPose.offset(6.25F, 7.0F, 5.0F)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.hasItemInSlot(EquipmentSlot.CHEST)) {
            this.body.xScale = 0.9375F;
            this.body.yScale = 0.9375F;
            this.body.zScale = 0.9375F;
        }
        
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i].xRot = 0.2F * Mth.sin(ageInTicks * 0.3F + (float) i) + 0.4F;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}