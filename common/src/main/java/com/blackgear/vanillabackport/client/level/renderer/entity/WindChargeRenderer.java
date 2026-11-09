package com.blackgear.vanillabackport.client.level.renderer.entity;

import com.blackgear.vanillabackport.client.level.model.object.WindChargeModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.client.registries.ModRenderTypes;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.AbstractWindCharge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class WindChargeRenderer extends EntityRenderer<AbstractWindCharge> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/projectiles/wind_charge.png");
    private final WindChargeModel model;
    
    public WindChargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WindChargeModel(context.bakeLayer(ModModelLayers.WIND_CHARGE));
    }
    
    @Override
    public void render(AbstractWindCharge entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < MIN_CAMERA_DISTANCE_SQUARED)) {
            float ageInTicks = entity.tickCount + partialTick;
            VertexConsumer vertices = buffer.getBuffer(ModRenderTypes.breezeWind(TEXTURE_LOCATION, this.xOffset(ageInTicks) % 1.0F, 0.5F));
            this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
            this.model.renderToBuffer(poseStack, vertices, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        }
    }
    
    protected float xOffset(float tickCount) {
        return tickCount * 0.03F;
    }
    
    @Override
    public ResourceLocation getTextureLocation(AbstractWindCharge entity) {
        return TEXTURE_LOCATION;
    }
}