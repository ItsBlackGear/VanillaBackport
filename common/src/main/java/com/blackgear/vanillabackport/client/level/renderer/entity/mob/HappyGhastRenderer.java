package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.level.layer.GhastHarnessHandler;
import com.blackgear.vanillabackport.client.level.layer.RopesLayer;
import com.blackgear.vanillabackport.client.level.layer.SimpleEquipmentLayer;
import com.blackgear.vanillabackport.client.level.model.entity.happy_ghast.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.model.entity.happy_ghast.HappyGhastModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

@Environment(EnvType.CLIENT)
public class HappyGhastRenderer extends MobRenderer<HappyGhast, HappyGhastModel<HappyGhast>> {
    private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("textures/entity/ghast/happy_ghast.png");
    private static final ResourceLocation GHAST_BABY_LOCATION = new ResourceLocation("textures/entity/ghast/happy_ghast_baby.png");
    private static final ResourceLocation GHAST_ROPES = new ResourceLocation("textures/entity/ghast/happy_ghast_ropes.png");

    public HappyGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new HappyGhastModel<>(context.bakeLayer(ModModelLayers.HAPPY_GHAST)), 1.5F);
        this.addLayer(SimpleEquipmentLayer.of(
            this,
            GhastHarnessHandler.HARNESS_EQUIPMENT,
            EquipmentSlot.CHEST,
            HappyGhast::isHarnessed,
            new HappyGhastHarnessModel<>(context.bakeLayer(ModModelLayers.HAPPY_GHAST_HARNESS)),
            null
        ));
        this.addLayer(new RopesLayer<>(this, context.getModelSet(), GHAST_ROPES));
    }

    @Override
    public ResourceLocation getTextureLocation(HappyGhast entity) {
        return entity.isBaby() ? GHAST_BABY_LOCATION : GHAST_LOCATION;
    }

    @Override
    protected void scale(HappyGhast entity, PoseStack matrices, float partialTicks) {
        float scale = entity.isBaby() ? 0.95F : 4.0F;
        matrices.scale(scale, scale, scale);
    }
}