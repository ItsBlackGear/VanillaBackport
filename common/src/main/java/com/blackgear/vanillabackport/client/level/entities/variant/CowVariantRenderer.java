package com.blackgear.vanillabackport.client.level.entities.variant;

import com.blackgear.vanillabackport.client.level.entities.model.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.entities.model.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class CowVariantRenderer extends AbstractVariantRenderer<Cow, CowModel<Cow>> {
    public CowVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Map<AnimalVariant, CowModel<Cow>> bakeModels(EntityRendererProvider.Context context) {
        Map<AnimalVariant, CowModel<Cow>> map = Maps.newEnumMap(AnimalVariant.class);
        map.put(AnimalVariant.DEFAULT, null);
        map.put(AnimalVariant.WARM, new WarmCowModel<>(context.bakeLayer(ModModelLayers.WARM_COW)));
        map.put(AnimalVariant.COLD, new ColdCowModel<>(context.bakeLayer(ModModelLayers.COLD_COW)));
        return map;
    }

    @Override
    public Map<AnimalVariant, ResourceLocation> textureByVariant() {
        Map<AnimalVariant, ResourceLocation> map = Maps.newEnumMap(AnimalVariant.class);
        map.put(AnimalVariant.COLD, VanillaBackport.resource("textures/entity/cow/cold_cow.png"));
        map.put(AnimalVariant.WARM, VanillaBackport.resource("textures/entity/cow/warm_cow.png"));
        return map;
    }
}