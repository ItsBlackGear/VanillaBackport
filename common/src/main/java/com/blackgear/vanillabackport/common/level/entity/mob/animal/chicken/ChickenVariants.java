package com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ChickenVariants {
    public static final BuiltInCoreRegistry<ChickenVariant> REGISTRIES = new BuiltInCoreRegistry<>(ModRegistries.CHICKEN_VARIANT.get(), VanillaBackport.NAMESPACE);

    public static final ResourceKey<ChickenVariant> TEMPERATE = register("temperate",
        ChickenVariant.ModelType.NORMAL,
        new ResourceLocation("entity/chicken"),
        SpawnPrioritySelectors.fallback(0)
    );
    public static final ResourceKey<ChickenVariant> WARM = register("warm",
        ChickenVariant.ModelType.NORMAL,
        "warm_chicken",
        ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS
    );
    public static final ResourceKey<ChickenVariant> COLD = register("cold",
        ChickenVariant.ModelType.COLD,
        "cold_chicken",
        ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS
    );

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String adultAssetId, TagKey<Biome> biome) {
        ResourceLocation adultTexture = new ResourceLocation("entity/chicken/" + adultAssetId);
        return register(key, type, adultTexture, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, ResourceLocation adultAssetId, SpawnPrioritySelectors selectors) {
        return REGISTRIES.resource(key, new ChickenVariant(new ModelAndTexture<>(type, adultAssetId), selectors));
    }
}