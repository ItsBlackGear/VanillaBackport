package com.blackgear.vanillabackport.client.level.entities.renderer.variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.client.level.entities.model.AdultAndBabyModelPair;
import com.blackgear.vanillabackport.client.level.entities.model.cow.BabyCowModel;
import com.blackgear.vanillabackport.client.level.entities.model.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.entities.model.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariants;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
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
    protected Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel<Cow>>> bakeModels(EntityRendererProvider.Context context) {
        Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel<Cow>>> map = Maps.newEnumMap(CowVariant.ModelType.class);
        map.put(CowVariant.ModelType.NORMAL,
            new AdultAndBabyModelPair<>(
                null,
                new BabyCowModel<>(context.bakeLayer(ModModelLayers.COW_BABY))
            ));
        map.put(CowVariant.ModelType.WARM,
            new AdultAndBabyModelPair<>(
                new WarmCowModel<>(context.bakeLayer(ModModelLayers.WARM_COW)),
                new BabyCowModel<>(context.bakeLayer(ModModelLayers.WARM_COW_BABY))
            ));
        map.put(CowVariant.ModelType.COLD,
            new AdultAndBabyModelPair<>(
                new ColdCowModel<>(context.bakeLayer(ModModelLayers.COLD_COW)),
                new BabyCowModel<>(context.bakeLayer(ModModelLayers.COLD_COW_BABY))
            ));
        return map;
    }

    @Override
    protected CowVariant.ModelType getModelType(CowVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getAdultTexture(CowVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected ResourceLocation getBabyTexture(CowVariant variant) {
        return null;
//        return variant.babyTexture().path();
    }

    @Override
    protected BuiltInCoreRegistry<CowVariant> getRegistry() {
        return ModBuiltinRegistries.COW_VARIANTS;
    }

    @Override
    protected ResourceKey<CowVariant> getDefaultVariant() {
        return CowVariants.TEMPERATE;
    }
}