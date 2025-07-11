package com.blackgear.vanillabackport.client.level.entities.variant;

import com.blackgear.vanillabackport.client.level.entities.model.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class PigVariantRenderer extends AbstractVariantRenderer<Pig, PigModel<Pig>> {
    public PigVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Map<AnimalVariant, PigModel<Pig>> bakeModels(EntityRendererProvider.Context context) {
        Map<AnimalVariant, PigModel<Pig>> map = Maps.newEnumMap(AnimalVariant.class);
        map.put(AnimalVariant.DEFAULT, null);
        map.put(AnimalVariant.WARM, null);
        map.put(AnimalVariant.COLD, new ColdPigModel<>(context.bakeLayer(ModModelLayers.COLD_PIG)));
        return map;
    }

    @Override
    public Map<AnimalVariant, ResourceLocation> textureByVariant() {
        Map<AnimalVariant, ResourceLocation> map = Maps.newEnumMap(AnimalVariant.class);
        map.put(AnimalVariant.COLD, VanillaBackport.resource("textures/entity/pig/cold_pig.png"));
        map.put(AnimalVariant.WARM, VanillaBackport.resource("textures/entity/pig/warm_pig.png"));
        return map;
    }
}