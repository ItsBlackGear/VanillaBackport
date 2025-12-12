package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.MoonBrightnessCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.internal.IBiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.internal.IStructureCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class EnhancedCatVariants {
    public static final ResourceKey<EnhancedCatVariant> TABBY = register("tabby");
    public static final ResourceKey<EnhancedCatVariant> BLACK = register("black");
    public static final ResourceKey<EnhancedCatVariant> RED = register("red");
    public static final ResourceKey<EnhancedCatVariant> SIAMESE = register("siamese");
    public static final ResourceKey<EnhancedCatVariant> BRITISH_SHORTHAIR = register("british_shorthair");
    public static final ResourceKey<EnhancedCatVariant> CALICO = register("calico");
    public static final ResourceKey<EnhancedCatVariant> PERSIAN = register("persian");
    public static final ResourceKey<EnhancedCatVariant> RAGDOLL = register("ragdoll");
    public static final ResourceKey<EnhancedCatVariant> WHITE = register("white");
    public static final ResourceKey<EnhancedCatVariant> JELLIE = register("jellie");
    public static final ResourceKey<EnhancedCatVariant> ALL_BLACK = register("all_black",
        new SpawnPrioritySelectors(
            List.of(
                new PriorityProvider.Selector<>(new IStructureCheck(StructureTags.CATS_SPAWN_AS_BLACK), 1),
                new PriorityProvider.Selector<>(new MoonBrightnessCheck(MinMaxBounds.Doubles.atLeast(0.9)), 0)
            )
        )
    );

    private static ResourceKey<EnhancedCatVariant> register(String key) {
        return register(key, SpawnPrioritySelectors.fallback(0));
    }

    private static ResourceKey<EnhancedCatVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = VanillaBackport.vanilla("entity/cat/" + key);
        return ModBuiltinRegistries.CAT_VARIANTS.resource(key, new EnhancedCatVariant(new ClientAsset(texture), selectors));
    }
}