package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.platform.core.Environment;
import com.blackgear.vanillabackport.common.api.variant.BiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.variant.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ChickenVariants {
    public static final ResourceKey<ChickenVariant> TEMPERATE = register("temperate", ChickenVariant.ModelType.NORMAL, VanillaBackport.vanilla("entity/chicken"), SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<ChickenVariant> WARM = register("warm", ChickenVariant.ModelType.NORMAL, "warm_chicken", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<ChickenVariant> COLD = register("cold", ChickenVariant.ModelType.COLD, "cold_chicken", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        HolderSet<Biome> requiredBiomes = Environment.getCurrentServer().get().registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
        return register(key, type, assetId, SpawnPrioritySelectors.single(new BiomeCheck(requiredBiomes), 1));
    }

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = VanillaBackport.vanilla("entity/chicken/" + assetId);
        return ModBuiltinRegistries.CHICKEN_VARIANTS.resource(key, new ChickenVariant(new ModelAndTexture<>(type, path), selectors));
    }

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, ResourceLocation assetId, SpawnPrioritySelectors selectors) {
        return ModBuiltinRegistries.CHICKEN_VARIANTS.resource(key, new ChickenVariant(new ModelAndTexture<>(type, assetId), selectors));
    }
}