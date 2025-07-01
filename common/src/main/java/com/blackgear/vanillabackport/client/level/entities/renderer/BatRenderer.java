package com.blackgear.vanillabackport.client.level.entities.renderer;

import com.blackgear.vanillabackport.client.level.entities.model.BatModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ambient.Bat;

@Environment(EnvType.CLIENT)
public class BatRenderer extends MobRenderer<Bat, BatModel> {
    private static final ResourceLocation BAT_LOCATION = VanillaBackport.resource("textures/entity/bat.png");

    public BatRenderer(EntityRendererProvider.Context context) {
        super(context, new BatModel(context.bakeLayer(ModModelLayers.BAT)), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(Bat entity) {
        return BAT_LOCATION;
    }
}