package com.blackgear.vanillabackport.common.level.entity.mob.animal.cow;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class CowVariants {
    public static final BuiltInCoreRegistry<CowVariant> REGISTRIES = new BuiltInCoreRegistry<>(ModRegistries.COW_VARIANT.get(), VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<CowVariant> TEMPERATE = register("temperate", CowVariant.ModelType.NORMAL, "cow", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<CowVariant> WARM = register("warm", CowVariant.ModelType.WARM, "warm_cow", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<CowVariant> COLD = register("cold", CowVariant.ModelType.COLD, "cold_cow", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        return register(key, type, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = ResourceLocation.withDefaultNamespace("entity/cow/" + assetId);
        return REGISTRIES.resource(key, new CowVariant(new ModelAndTexture<>(type, path), selectors));
    }
}