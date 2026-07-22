package com.blackgear.vanillabackport.common.level.entity.mob.animal.cow;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class CowVariants {
    public static final BuiltInCoreRegistry<CowVariant> REGISTRIES = BuiltInCoreRegistry.create(ResourceLocation.withDefaultNamespace("cow_variants"), VanillaBackport.NAMESPACE);
    
    public static final RegistryKey<CowVariant> TEMPERATE = register("temperate",
        CowVariant.ModelType.NORMAL,
        "cow",
        SpawnPrioritySelectors.fallback(0));
    public static final RegistryKey<CowVariant> WARM = register("warm",
        CowVariant.ModelType.WARM,
        "warm_cow",
        ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final RegistryKey<CowVariant> COLD = register("cold",
        CowVariant.ModelType.COLD,
        "cold_cow",
        ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static RegistryKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        return register(key, type, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static RegistryKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = ResourceLocation.withDefaultNamespace("entity/cow/" + assetId);
        return REGISTRIES.register(key, new CowVariant(new ModelAndTexture<>(type, path), selectors));
    }
}