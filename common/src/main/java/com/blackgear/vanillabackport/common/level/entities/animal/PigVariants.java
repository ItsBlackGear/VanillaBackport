package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class PigVariants {
    public static final BuiltInCoreRegistry<PigVariant> REGISTRY = ModBuiltinRegistries.PIG_VARIANTS;

    public static final ResourceKey<PigVariant> TEMPERATE = register(
        "temperate",
        PigVariant.ModelType.NORMAL,
        "pig",
        SpawnPrioritySelectors.fallback(0)
    );
    public static final ResourceKey<PigVariant> WARM = register(
        "warm",
        PigVariant.ModelType.NORMAL,
        "warm_pig",
        ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS
    );
    public static final ResourceKey<PigVariant> COLD = register(
        "cold",
        PigVariant.ModelType.COLD,
        "cold_pig",
        ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS
    );

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String adultAssetId, TagKey<Biome> biome) {
        return register(key, type, adultAssetId, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String adultAssetId, SpawnPrioritySelectors selectors) {
        ResourceLocation adultTexture = new ResourceLocation("entity/pig/" + adultAssetId);
        return REGISTRY.resource(key, new PigVariant(new ModelAndTexture<>(type, adultTexture), selectors));
    }
}