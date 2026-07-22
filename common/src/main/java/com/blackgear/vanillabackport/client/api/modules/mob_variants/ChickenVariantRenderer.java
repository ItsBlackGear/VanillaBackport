package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.client.level.model.entity.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ChickenVariantRenderer extends AbstractVariantRenderer<Chicken, ChickenModel<Chicken>, ChickenVariant, ChickenVariant.ModelType> {
    public ChickenVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Map<ChickenVariant.ModelType, ChickenModel<Chicken>> bakeModels(EntityRendererProvider.Context context) {
        Map<ChickenVariant.ModelType, ChickenModel<Chicken>> map = Maps.newEnumMap(ChickenVariant.ModelType.class);
        map.put(ChickenVariant.ModelType.NORMAL, null);
        map.put(ChickenVariant.ModelType.COLD, new ColdChickenModel<>(context.bakeLayer(ModModelLayers.COLD_CHICKEN)));
        return map;
    }

    @Override
    protected ChickenVariant.ModelType getModelType(ChickenVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getTexture(ChickenVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected BuiltInCoreRegistry<ChickenVariant> getRegistry() {
        return ChickenVariants.REGISTRIES;
    }

    @Override
    protected RegistryKey<ChickenVariant> getDefaultVariant() {
        return ChickenVariants.TEMPERATE;
    }
}