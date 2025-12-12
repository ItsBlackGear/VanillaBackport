package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.internal.IBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class EnhancedFrogVariants {
    public static final ResourceKey<EnhancedFrogVariant> TEMPERATE = register("temperate", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<EnhancedFrogVariant> WARM = register("warm", BiomeTags.SPAWNS_WARM_VARIANT_FROGS);
    public static final ResourceKey<EnhancedFrogVariant> COLD = register("cold", BiomeTags.SPAWNS_COLD_VARIANT_FROGS);

    private static ResourceKey<EnhancedFrogVariant> register(String key, TagKey<Biome> biome) {
        return register(key, SpawnPrioritySelectors.single(new IBiomeCheck(biome), 1));
    }

    private static ResourceKey<EnhancedFrogVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = VanillaBackport.vanilla("entity/frog/" + key + "_frog");
        return ModBuiltinRegistries.FROG_VARIANTS.resource(key, new EnhancedFrogVariant(new ClientAsset(texture), selectors));
    }
}