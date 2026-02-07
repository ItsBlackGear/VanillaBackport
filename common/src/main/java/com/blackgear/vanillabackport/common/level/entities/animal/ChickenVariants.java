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

public class ChickenVariants {
    public static final BuiltInCoreRegistry<ChickenVariant> REGISTRY = ModBuiltinRegistries.CHICKEN_VARIANTS;

    public static final ResourceKey<ChickenVariant> TEMPERATE = register(
        "temperate",
        ChickenVariant.ModelType.NORMAL,
        new ResourceLocation("entity/chicken"),
        SpawnPrioritySelectors.fallback(0)
    );
    public static final ResourceKey<ChickenVariant> WARM = register(
        "warm",
        ChickenVariant.ModelType.NORMAL,
        "warm_chicken",
        ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS
    );
    public static final ResourceKey<ChickenVariant> COLD = register(
        "cold",
        ChickenVariant.ModelType.COLD,
        "cold_chicken",
        ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS
    );

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String adultAssetId, TagKey<Biome> biome) {
        ResourceLocation adultTexture = new ResourceLocation("entity/chicken/" + adultAssetId);
        return register(key, type, adultTexture, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, ResourceLocation adultAssetId, SpawnPrioritySelectors selectors) {
        return REGISTRY.resource(key, new ChickenVariant(new ModelAndTexture<>(type, adultAssetId), selectors));
    }
}