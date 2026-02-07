package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class FrogDataVariants {
    public static final BuiltInCoreRegistry<FrogDataVariant> REGISTRY = ModBuiltinRegistries.FROG_VARIANTS;

//    public static final ResourceKey<FrogDataVariant> TEST = register(
//        "test",
//        "cold_frog",
//        BiomeTags.IS_FOREST
//    );

    private static ResourceKey<FrogDataVariant> register(String key, String assetId, TagKey<Biome> biome) {
        return register(key, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<FrogDataVariant> register(String key, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = new ResourceLocation("entity/frog/" + assetId);
        return REGISTRY.resource(key, new FrogDataVariant(new ClientAsset(texture), selectors));
    }
}