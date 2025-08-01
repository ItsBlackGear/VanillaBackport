package com.blackgear.vanillabackport.client.level.entities.variant;

import com.blackgear.vanillabackport.client.level.entities.model.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class ChickenVariantRenderer {
    protected final Map<ChickenVariant.ModelType, ChickenModel<Chicken>> modelByVariant;

    public ChickenVariantRenderer(EntityRendererProvider.Context context) {
        this.modelByVariant = this.bakeModels(context);
    }

    public Map<ChickenVariant.ModelType, ChickenModel<Chicken>> bakeModels(EntityRendererProvider.Context context) {
        Map<ChickenVariant.ModelType, ChickenModel<Chicken>> map = Maps.newEnumMap(ChickenVariant.ModelType.class);
        map.put(ChickenVariant.ModelType.NORMAL, null);
        map.put(ChickenVariant.ModelType.COLD, new ColdChickenModel<>(context.bakeLayer(ModModelLayers.COLD_CHICKEN)));
        return map;
    }

    public ResourceLocation getTexture(Chicken entity) {
        ChickenVariant variant = ((VariantHolder<ChickenVariant>) entity).getVariant();
        if (variant != null) {
            return variant.modelAndTexture().asset().path();
        }

        return null;
    }

    public Optional<ChickenModel<Chicken>> getModel(Chicken entity) {
        ChickenVariant variant = ((VariantHolder<ChickenVariant>) entity).getVariant();
        return Optional.ofNullable(this.modelByVariant.get(variant.modelAndTexture().model()));
    }
}