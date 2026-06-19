package com.blackgear.vanillabackport.common.level.entity.mob.animal.frog;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class FrogDataVariants {
    public static final BuiltInCoreRegistry<FrogDataVariant> REGISTRIES = new BuiltInCoreRegistry<>(ModRegistries.FROG_VARIANT.get(), VanillaBackport.NAMESPACE);

//    public static final ResourceKey<FrogDataVariant> TEST = register("test", "cold_frog", BiomeTags.IS_FOREST);

    private static ResourceKey<FrogDataVariant> register(String key, TagKey<Biome> biome) {
        return register(key, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<FrogDataVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = ResourceLocation.withDefaultNamespace("entity/frog/" + key + "_frog");
        return REGISTRIES.resource(key, new FrogDataVariant(new ClientAsset(texture), selectors));
    }
}