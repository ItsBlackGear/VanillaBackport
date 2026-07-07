package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.level.layer.SulfurCubeInnerLayer;
import com.blackgear.vanillabackport.client.level.layer.SulfurCubeOuterLayer;
import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SulfurCubeModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class SulfurCubeRenderer extends MobRenderer<SulfurCube, SulfurCubeModel> {
    private static final ResourceLocation SULFUR_CUBE_INNER_LOCATION = new ResourceLocation("textures/entity/sulfur_cube/sulfur_cube_inner.png");

    public SulfurCubeRenderer(EntityRendererProvider.Context context) {
        super(context, new SulfurCubeModel(context.bakeLayer(ModModelLayers.SULFUR_CUBE_INNER), true), 0.25F);
        this.addLayer(new SulfurCubeInnerLayer(this, context.getModelSet(), context.getBlockRenderDispatcher()));
        this.addLayer(new SulfurCubeOuterLayer(this, context.getModelSet()));
    }

    @Override
    public void render(SulfurCube cube, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
        this.shadowRadius = 0.25F * (float) cube.getSize();
        super.render(cube, entityYaw, partialTicks, pose, buffer, packedLight);
    }

    @Override
    protected void scale(SulfurCube cube, PoseStack pose, float partialTicks) {
        // downscale slightly
        pose.scale(0.999F, 0.999F, 0.999F);
        pose.translate(0.0F, 0.001F, 0.0F);

        // apply size and squish
        float size = (float) cube.getSize();
        float squish = cube.getContainedBlock().isEmpty() ? Mth.lerp(partialTicks, cube.oSquish, cube.squish) / (size * 0.5F + 1.0F) : 0.0F;
        float progress = 1.0F / (squish + 1.0F);
        pose.scale(progress * size, 1.0F / progress * size, progress * size);

        int fuse = cube.getFuse();
        if (fuse > -1 && (float)fuse - partialTicks + 1.0F < 10.0F) {
            float swell = 1.0F - ((float)fuse - partialTicks + 1.0F) / 10.0F;
            swell = Mth.clamp(swell, 0.0F, 1.0F);
            swell *= swell;
            swell *= swell;
            float scale = 1.0F + swell * 0.3F;
            pose.scale(scale, scale, scale);
        }

        float vOffset = cube.isBaby() ? 1.24F : 0.98F;
        float extraDownscale = cube.isBaby() ? 1.0F : 0.5F;
        float onePixelUpIfVisible = (cube.isInvisible() ? 0.0F : 1.0F) / 16.0F;
        pose.scale(extraDownscale, extraDownscale, extraDownscale);
        pose.translate(-0.0F, vOffset - onePixelUpIfVisible, -0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(SulfurCube entity) {
        return SULFUR_CUBE_INNER_LOCATION;
    }
}