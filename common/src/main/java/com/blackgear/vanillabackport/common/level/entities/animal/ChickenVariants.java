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

public class ChickenVariants {
    public static final ResourceKey<ChickenVariant> TEMPERATE = ResourceKey.create(ModRegistries.CHICKEN_VARIANT_KEY, VanillaBackport.vanilla("temperate"));
    public static final ResourceKey<ChickenVariant> WARM = ResourceKey.create(ModRegistries.CHICKEN_VARIANT_KEY, VanillaBackport.vanilla("warm"));
    public static final ResourceKey<ChickenVariant> COLD = ResourceKey.create(ModRegistries.CHICKEN_VARIANT_KEY, VanillaBackport.vanilla("cold"));

    public static void bootstrap(RegistryAccess access) {
        register("temperate", ChickenVariant.ModelType.NORMAL, VanillaBackport.vanilla("entity/chicken"), SpawnPrioritySelectors.fallback(0));
        register(access, "warm", ChickenVariant.ModelType.NORMAL, "warm_chicken", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
        register(access, "cold", ChickenVariant.ModelType.COLD, "cold_chicken", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
    }

    private static void register(RegistryAccess access, String key, ChickenVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        access.lookup(Registries.BIOME).ifPresent(lookup -> {
            ResourceLocation path = VanillaBackport.vanilla("entity/chicken/" + assetId);
            register(key, type, path, SpawnPrioritySelectors.single(new BiomeCheck(lookup.getOrThrow(biome)), 1));
        });
    }

    private static void register(String key, ChickenVariant.ModelType type, ResourceLocation assetId, SpawnPrioritySelectors selectors) {
        ModBuiltinRegistries.CHICKEN_VARIANTS.resource(key, new ChickenVariant(new ModelAndTexture<>(type, assetId), selectors));
    }
}