package com.blackgear.vanillabackport.client.level.entities.variant;

import com.blackgear.vanillabackport.client.level.entities.model.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class PigVariantRenderer {
    protected final Map<PigVariant.ModelType, PigModel<Pig>> modelByVariant;

    public PigVariantRenderer(EntityRendererProvider.Context context) {
        this.modelByVariant = this.bakeModels(context);
    }

    public Map<PigVariant.ModelType, PigModel<Pig>> bakeModels(EntityRendererProvider.Context context) {
        Map<PigVariant.ModelType, PigModel<Pig>> map = Maps.newEnumMap(PigVariant.ModelType.class);
        map.put(PigVariant.ModelType.NORMAL, null);
        map.put(PigVariant.ModelType.COLD, new ColdPigModel<>(context.bakeLayer(ModModelLayers.COLD_PIG)));
        return map;
    }

    public ResourceLocation getTexture(Pig entity) {
        PigVariant variant = ((VariantHolder<PigVariant>) entity).getVariant();
        if (variant != null) {
            return variant.modelAndTexture().asset().path();
        }

        return null;
    }

    public Optional<PigModel<Pig>> getModel(Pig entity) {
        PigVariant variant = ((VariantHolder<PigVariant>) entity).getVariant();
        return Optional.ofNullable(this.modelByVariant.get(variant.modelAndTexture().model()));
    }
}