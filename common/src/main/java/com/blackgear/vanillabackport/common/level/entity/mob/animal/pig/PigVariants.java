package com.blackgear.vanillabackport.common.level.entity.mob.animal.pig;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variants.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.variants.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.variants.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class PigVariants {
    public static final BuiltInCoreRegistry<PigVariant> REGISTRIES = new BuiltInCoreRegistry<>(ModRegistries.PIG_VARIANT.get(), VanillaBackport.NAMESPACE);

    public static final ResourceKey<PigVariant> TEMPERATE = register("temperate", PigVariant.ModelType.NORMAL, "pig", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<PigVariant> WARM = register("warm", PigVariant.ModelType.NORMAL, "warm_pig", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<PigVariant> COLD = register("cold", PigVariant.ModelType.COLD, "cold_pig", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        return register(key, type, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = ResourceLocation.withDefaultNamespace("entity/pig/" + assetId);
        return REGISTRIES.resource(key, new PigVariant(new ModelAndTexture<>(type, path), selectors));
    }
}