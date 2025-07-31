package com.blackgear.vanillabackport.client.level.entities.variant;

import com.blackgear.vanillabackport.client.level.entities.model.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.entities.model.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariant;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;

import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CowVariantRenderer {
    protected final Map<CowVariant.ModelType, CowModel<Cow>> modelByVariant;

    public CowVariantRenderer(EntityRendererProvider.Context context) {
        this.modelByVariant = this.bakeModels(context);
    }

    public Map<CowVariant.ModelType, CowModel<Cow>> bakeModels(EntityRendererProvider.Context context) {
        Map<CowVariant.ModelType, CowModel<Cow>> map = Maps.newEnumMap(CowVariant.ModelType.class);
        map.put(CowVariant.ModelType.NORMAL, null);
        map.put(CowVariant.ModelType.WARM, new WarmCowModel<>(context.bakeLayer(ModModelLayers.WARM_COW)));
        map.put(CowVariant.ModelType.COLD, new ColdCowModel<>(context.bakeLayer(ModModelLayers.COLD_COW)));
        return map;
    }

    public ResourceLocation getTexture(Cow entity) {
        CowVariant variant = ((VariantHolder<CowVariant>) entity).getVariant();
        if (variant != null) {
            return variant.modelAndTexture().asset().path();
        }

        return null;
    }

    public Optional<CowModel<Cow>> getModel(Cow entity) {
        CowVariant variant = ((VariantHolder<CowVariant>) entity).getVariant();
        return Optional.ofNullable(this.modelByVariant.get(variant.modelAndTexture().model()));
    }
}