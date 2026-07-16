package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class ParchedRenderer extends SkeletonRenderer {
    private static final ResourceLocation PARCHED_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/parched.png");
    
    public ParchedRenderer(EntityRendererProvider.Context context) {
        super(context, ModModelLayers.PARCHED, ModModelLayers.PARCHED_INNER_ARMOR, ModModelLayers.PARCHED_OUTER_ARMOR);
    }
    
    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
        return PARCHED_SKELETON_LOCATION;
    }
    
    public static LayerDefinition createSingleModelDualBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(16, 16)
                .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                .texOffs(28, 0)
                .addBox(-4.0F, 10.0F, -2.0F, 8.0F, 1.0F, 4.0F)
                .texOffs(16, 48)
                .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.025F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                .texOffs(0, 32)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild(
            "right_arm",
            CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(42, 33).addBox(-1.55F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F),
            PartPose.offset(-5.5F, 2.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "left_arm",
            CubeListBuilder.create().texOffs(56, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(40, 48).addBox(-1.45F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F),
            PartPose.offset(5.5F, 2.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "right_leg",
            CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(0, 49).addBox(-1.5F, -0.0F, -1.5F, 3.0F, 12.0F, 3.0F),
            PartPose.offset(-2.0F, 12.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "left_leg",
            CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(4, 49).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F),
            PartPose.offset(2.0F, 12.0F, 0.0F)
        );
        return LayerDefinition.create(mesh, 64, 64);
    }
}