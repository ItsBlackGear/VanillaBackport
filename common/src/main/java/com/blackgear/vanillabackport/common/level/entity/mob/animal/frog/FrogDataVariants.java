package com.blackgear.vanillabackport.common.level.entity.mob.animal.frog;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class FrogDataVariants {
    public static final BuiltInCoreRegistry<FrogDataVariant> REGISTRIES = BuiltInCoreRegistry.create(ResourceLocation.withDefaultNamespace("frog_variants"), VanillaBackport.NAMESPACE);

    public static final RegistryKey<FrogDataVariant> TEST = register("test", BiomeTags.IS_FOREST);

    private static RegistryKey<FrogDataVariant> register(String key, TagKey<Biome> biome) {
        return register(key, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static RegistryKey<FrogDataVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = ResourceLocation.withDefaultNamespace("entity/frog/" + key + "_frog");
        return REGISTRIES.register(key, new FrogDataVariant(new ClientAsset(texture), selectors));
    }
}