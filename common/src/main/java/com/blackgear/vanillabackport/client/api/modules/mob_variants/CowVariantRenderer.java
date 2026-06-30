package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.client.level.entity.model.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.entity.model.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariants;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class CowVariantRenderer extends AbstractVariantRenderer<Cow, CowModel<Cow>, CowVariant, CowVariant.ModelType> {
    public CowVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Map<CowVariant.ModelType, CowModel<Cow>> bakeModels(EntityRendererProvider.Context context) {
        Map<CowVariant.ModelType, CowModel<Cow>> map = Maps.newEnumMap(CowVariant.ModelType.class);
        map.put(CowVariant.ModelType.NORMAL, null);
        map.put(CowVariant.ModelType.WARM, new WarmCowModel<>(context.bakeLayer(ModModelLayers.WARM_COW)));
        map.put(CowVariant.ModelType.COLD, new ColdCowModel<>(context.bakeLayer(ModModelLayers.COLD_COW)));
        return map;
    }

    @Override
    protected CowVariant.ModelType getModelType(CowVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getTexture(CowVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected BuiltInCoreRegistry<CowVariant> getRegistry() {
        return CowVariants.REGISTRIES;
    }

    @Override
    protected ResourceKey<CowVariant> getDefaultVariant() {
        return CowVariants.TEMPERATE;
    }
}