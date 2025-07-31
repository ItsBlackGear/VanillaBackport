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

public class CowVariants {
    public static final ResourceKey<CowVariant> TEMPERATE = register("temperate", CowVariant.ModelType.NORMAL, "cow", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<CowVariant> WARM = register("warm", CowVariant.ModelType.WARM, "warm_cow", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<CowVariant> COLD = register("cold", CowVariant.ModelType.COLD, "cold_cow", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        HolderSet<Biome> holderSet = Environment.getCurrentServer().get().registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
        return register(key, type, assetId, SpawnPrioritySelectors.single(new BiomeCheck(holderSet), 1));
    }

    private static ResourceKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = VanillaBackport.vanilla("entity/cow/" + assetId);
        return ModBuiltinRegistries.COW_VARIANTS.resource(key, new CowVariant(new ModelAndTexture<>(type, path), selectors));
    }
}