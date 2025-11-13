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

public class CowVariants {
    public static final ResourceKey<CowVariant> TEMPERATE = ResourceKey.create(ModRegistries.COW_VARIANT_KEY, VanillaBackport.vanilla("temperate"));
    public static final ResourceKey<CowVariant> WARM = ResourceKey.create(ModRegistries.COW_VARIANT_KEY, VanillaBackport.vanilla("warm"));
    public static final ResourceKey<CowVariant> COLD = ResourceKey.create(ModRegistries.COW_VARIANT_KEY, VanillaBackport.vanilla("cold"));

    public static void bootstrap(RegistryAccess access) {
        register("temperate", CowVariant.ModelType.NORMAL, "cow", SpawnPrioritySelectors.fallback(0));
        register(access, "warm", CowVariant.ModelType.WARM, "warm_cow", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
        register(access, "cold", CowVariant.ModelType.COLD, "cold_cow", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
    }

    private static void register(RegistryAccess access, String key, CowVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        access.lookup(Registries.BIOME).ifPresent(lookup -> register(key, type, assetId, SpawnPrioritySelectors.single(new BiomeCheck(lookup.getOrThrow(biome)), 1)));
    }

    private static void register(String key, CowVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = VanillaBackport.vanilla("entity/cow/" + assetId);
        ModBuiltinRegistries.COW_VARIANTS.register(key, new CowVariant(new ModelAndTexture<>(type, path), selectors));
    }
}