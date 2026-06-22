package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.client.level.entities.model.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.pig.PigVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.pig.PigVariants;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class PigVariantRenderer extends AbstractVariantRenderer<Pig, PigModel<Pig>, PigVariant, PigVariant.ModelType> {
    public PigVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Map<PigVariant.ModelType, PigModel<Pig>> bakeModels(EntityRendererProvider.Context context) {
        Map<PigVariant.ModelType, PigModel<Pig>> map = Maps.newEnumMap(PigVariant.ModelType.class);
        map.put(PigVariant.ModelType.NORMAL, null);
        map.put(PigVariant.ModelType.COLD, new ColdPigModel<>(context.bakeLayer(ModModelLayers.COLD_PIG)));
        return map;
    }

    @Override
    protected PigVariant.ModelType getModelType(PigVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getTexture(PigVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected BuiltInCoreRegistry<PigVariant> getRegistry() {
        return PigVariants.REGISTRIES;
    }

    @Override
    protected ResourceKey<PigVariant> getDefaultVariant() {
        return PigVariants.TEMPERATE;
    }
}