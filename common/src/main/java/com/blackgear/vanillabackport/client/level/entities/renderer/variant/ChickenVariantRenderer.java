package com.blackgear.vanillabackport.client.level.entities.renderer.variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.client.level.entities.model.AdultAndBabyModelPair;
import com.blackgear.vanillabackport.client.level.entities.model.chicken.BabyChickenModel;
import com.blackgear.vanillabackport.client.level.entities.model.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariants;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ChickenVariantRenderer extends AbstractVariantRenderer<Chicken, ChickenModel<Chicken>, ChickenVariant, ChickenVariant.ModelType> {
    public ChickenVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel<Chicken>>> bakeModels(EntityRendererProvider.Context context) {
        Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel<Chicken>>> map = Maps.newEnumMap(ChickenVariant.ModelType.class);
        map.put(ChickenVariant.ModelType.NORMAL,
            new AdultAndBabyModelPair<>(
                null,
                new BabyChickenModel<>(context.bakeLayer(ModModelLayers.CHICKEN_BABY))
            ));
        map.put(ChickenVariant.ModelType.COLD,
            new AdultAndBabyModelPair<>(
                new ColdChickenModel<>(context.bakeLayer(ModModelLayers.COLD_CHICKEN)),
                new BabyChickenModel<>(context.bakeLayer(ModModelLayers.CHICKEN_BABY))
            ));
        return map;
    }

    @Override
    protected ChickenVariant.ModelType getModelType(ChickenVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getAdultTexture(ChickenVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected ResourceLocation getBabyTexture(ChickenVariant variant) {
//        return variant.babyTexture().path();
        return null;
    }

    @Override
    protected BuiltInCoreRegistry<ChickenVariant> getRegistry() {
        return ModBuiltinRegistries.CHICKEN_VARIANTS;
    }

    @Override
    protected ResourceKey<ChickenVariant> getDefaultVariant() {
        return ChickenVariants.TEMPERATE;
    }
}