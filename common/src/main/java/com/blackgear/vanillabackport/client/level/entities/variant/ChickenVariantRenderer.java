package com.blackgear.vanillabackport.client.level.entities.variant;

import com.blackgear.vanillabackport.client.level.entities.model.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ChickenVariantRenderer extends AbstractVariantRenderer<Chicken, ChickenModel<Chicken>> {
    public ChickenVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Map<AnimalVariant, ChickenModel<Chicken>> bakeModels(EntityRendererProvider.Context context) {
        Map<AnimalVariant, ChickenModel<Chicken>> map = Maps.newEnumMap(AnimalVariant.class);
        map.put(AnimalVariant.DEFAULT, null);
        map.put(AnimalVariant.WARM, null);
        map.put(AnimalVariant.COLD, new ColdChickenModel<>(context.bakeLayer(ModModelLayers.COLD_CHICKEN)));
        return map;
    }

    @Override
    public Map<AnimalVariant, ResourceLocation> textureByVariant() {
        Map<AnimalVariant, ResourceLocation> map = Maps.newEnumMap(AnimalVariant.class);
        map.put(AnimalVariant.COLD, VanillaBackport.resource("textures/entity/chicken/cold_chicken.png"));
        map.put(AnimalVariant.WARM, VanillaBackport.resource("textures/entity/chicken/warm_chicken.png"));
        return map;
    }
}