package com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ChickenVariants {
    public static final BuiltInCoreRegistry<ChickenVariant> REGISTRIES = BuiltInCoreRegistry.create(new ResourceLocation("chicken_variants"), VanillaBackport.NAMESPACE);

    public static final RegistryKey<ChickenVariant> TEMPERATE = register("temperate",
        ChickenVariant.ModelType.NORMAL,
        new ResourceLocation("entity/chicken"),
        SpawnPrioritySelectors.fallback(0)
    );
    public static final RegistryKey<ChickenVariant> WARM = register("warm",
        ChickenVariant.ModelType.NORMAL,
        "warm_chicken",
        ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS
    );
    public static final RegistryKey<ChickenVariant> COLD = register("cold",
        ChickenVariant.ModelType.COLD,
        "cold_chicken",
        ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS
    );

    private static RegistryKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String adultAssetId, TagKey<Biome> biome) {
        ResourceLocation adultTexture = new ResourceLocation("entity/chicken/" + adultAssetId);
        return register(key, type, adultTexture, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static RegistryKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, ResourceLocation adultAssetId, SpawnPrioritySelectors selectors) {
        return REGISTRIES.register(key, new ChickenVariant(new ModelAndTexture<>(type, adultAssetId), selectors));
    }
}