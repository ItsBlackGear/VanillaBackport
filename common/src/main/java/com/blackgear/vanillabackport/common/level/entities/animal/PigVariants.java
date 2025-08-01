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

public class PigVariants {
    public static final ResourceKey<PigVariant> TEMPERATE = register("temperate", PigVariant.ModelType.NORMAL, "pig", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<PigVariant> WARM = register("warm", PigVariant.ModelType.NORMAL, "warm_pig", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<PigVariant> COLD = register("cold", PigVariant.ModelType.COLD, "cold_pig", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        HolderSet<Biome> requiredBiomes = Environment.getCurrentServer().get().registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
        return register(key, type, assetId, SpawnPrioritySelectors.single(new BiomeCheck(requiredBiomes), 1));
    }

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = VanillaBackport.vanilla("entity/pig/" + assetId);
        return ModBuiltinRegistries.PIG_VARIANTS.resource(key, new PigVariant(new ModelAndTexture<>(type, path), selectors));
    }
}