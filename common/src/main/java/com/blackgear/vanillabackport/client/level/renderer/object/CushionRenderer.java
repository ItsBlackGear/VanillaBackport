package com.blackgear.vanillabackport.client.level.renderer.object;

import com.blackgear.vanillabackport.client.level.model.object.CushionModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.decoration.Cushion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;

@Environment(EnvType.CLIENT)
public class CushionRenderer extends EntityRenderer<Cushion> {
    private static final EnumMap<DyeColor, ResourceLocation> TEXTURES_BY_COLOR = Util.make(new EnumMap<>(DyeColor.class), textures -> {
        for (DyeColor color : DyeColor.values()) {
            textures.put(color, ResourceLocation.withDefaultNamespace("textures/entity/cushion/" + color.getName() + "_cushion.png"));
        }
    });
    private final CushionModel model;
    
    public CushionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CushionModel(context.bakeLayer(ModModelLayers.CUSHION));
    }
    
    @Override
    public ResourceLocation getTextureLocation(Cushion cushion) {
        return TEXTURES_BY_COLOR.get(cushion.getColor());
    }
    
    @Override
    public void render(Cushion cushion, float entityYaw, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight) {
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(Direction.fromYRot(cushion.getYRot()).toYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        pose.translate(0.0, -0.25, 0.0);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(cushion)));
        this.model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        pose.popPose();
        super.render(cushion, entityYaw, partialTick, pose, buffer, packedLight);
    }
}
