package com.blackgear.vanillabackport.common.level.entity.mob.animal.pig;

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

public class PigVariants {
    public static final BuiltInCoreRegistry<PigVariant> REGISTRIES = BuiltInCoreRegistry.create(ResourceLocation.withDefaultNamespace("pig_variants"), VanillaBackport.NAMESPACE);

    public static final RegistryKey<PigVariant> TEMPERATE = register("temperate",
        PigVariant.ModelType.NORMAL,
        "pig",
        SpawnPrioritySelectors.fallback(0));
    public static final RegistryKey<PigVariant> WARM = register("warm",
        PigVariant.ModelType.NORMAL,
        "warm_pig",
        ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final RegistryKey<PigVariant> COLD = register("cold",
        PigVariant.ModelType.COLD,
        "cold_pig",
        ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static RegistryKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        return register(key, type, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static RegistryKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = ResourceLocation.withDefaultNamespace("entity/pig/" + assetId);
        return REGISTRIES.register(key, new PigVariant(new ModelAndTexture<>(type, path), selectors));
    }
}