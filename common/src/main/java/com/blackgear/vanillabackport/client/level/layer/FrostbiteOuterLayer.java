package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.zombie.Frostbite;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class FrostbiteOuterLayer extends RenderLayer<Frostbite, ZombieModel<Frostbite>> {
    private static final ResourceLocation FROSTBITE_OUTER_LAYER_LOCATION = new ResourceLocation("textures/entity/zombie/frostbite_outer_layer.png");
    private final ZombieModel<Frostbite> model;
    
    public FrostbiteOuterLayer(RenderLayerParent<Frostbite, ZombieModel<Frostbite>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new ZombieModel<>(modelSet.bakeLayer(ModModelLayers.FROSTBITE_OUTER_LAYER));
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Frostbite livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        coloredCutoutModelCopyLayerRender(
            this.getParentModel(),
            this.model,
            FROSTBITE_OUTER_LAYER_LOCATION,
            poseStack,
            buffer,
            packedLight,
            livingEntity,
            limbSwing,
            limbSwingAmount,
            ageInTicks,
            netHeadYaw,
            headPitch,
            partialTick,
            1.0F,
            1.0F,
            1.0F
        );
    }
}
