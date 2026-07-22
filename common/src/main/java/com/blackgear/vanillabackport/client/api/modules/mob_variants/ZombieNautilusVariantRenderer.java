package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.client.level.model.entity.nautilus.NautilusModel;
import com.blackgear.vanillabackport.client.level.model.entity.nautilus.ZombieNautilusCoralModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilus;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilusVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilusVariants;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ZombieNautilusVariantRenderer extends AbstractVariantRenderer<ZombieNautilus, NautilusModel<ZombieNautilus>, ZombieNautilusVariant, ZombieNautilusVariant.ModelType> {
    public ZombieNautilusVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Map<ZombieNautilusVariant.ModelType, NautilusModel<ZombieNautilus>> bakeModels(EntityRendererProvider.Context context) {
        Map<ZombieNautilusVariant.ModelType, NautilusModel<ZombieNautilus>> map = Maps.newEnumMap(ZombieNautilusVariant.ModelType.class);
        map.put(ZombieNautilusVariant.ModelType.NORMAL, new NautilusModel<>(context.bakeLayer(ModModelLayers.ZOMBIE_NAUTILUS)));
        map.put(ZombieNautilusVariant.ModelType.WARM, new ZombieNautilusCoralModel<>(context.bakeLayer(ModModelLayers.ZOMBIE_NAUTILUS_CORAL)));
        return map;
    }

    @Override
    protected ZombieNautilusVariant.ModelType getModelType(ZombieNautilusVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getTexture(ZombieNautilusVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected BuiltInCoreRegistry<ZombieNautilusVariant> getRegistry() {
        return ZombieNautilusVariants.REGISTRIES;
    }

    @Override
    protected RegistryKey<ZombieNautilusVariant> getDefaultVariant() {
        return ZombieNautilusVariants.TEMPERATE;
    }
}