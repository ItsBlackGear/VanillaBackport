package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.variant.BiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.variant.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class PigVariants {
    public static final ResourceKey<PigVariant> TEMPERATE = ResourceKey.create(ModRegistries.PIG_VARIANT_KEY, VanillaBackport.vanilla("temperate"));
    public static final ResourceKey<PigVariant> WARM = ResourceKey.create(ModRegistries.PIG_VARIANT_KEY, VanillaBackport.vanilla("warm"));
    public static final ResourceKey<PigVariant> COLD = ResourceKey.create(ModRegistries.PIG_VARIANT_KEY, VanillaBackport.vanilla("cold"));

    public static void bootstrap(RegistryAccess access) {
        register("temperate", PigVariant.ModelType.NORMAL, "pig", SpawnPrioritySelectors.fallback(0));
        register(access, "warm", PigVariant.ModelType.NORMAL, "warm_pig", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
        register(access, "cold", PigVariant.ModelType.COLD, "cold_pig", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
    }

    private static void register(RegistryAccess access, String key, PigVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        access.lookup(Registries.BIOME).ifPresent(lookup -> register(key, type, assetId, SpawnPrioritySelectors.single(new BiomeCheck(lookup.getOrThrow(biome)), 1)));
    }

    private static void register(String key, PigVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = VanillaBackport.vanilla("entity/pig/" + assetId);
        ModBuiltinRegistries.PIG_VARIANTS.resource(key, new PigVariant(new ModelAndTexture<>(type, path), selectors));
    }
}