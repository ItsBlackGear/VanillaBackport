package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.crafting.BundleColoring;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final CoreRegistry<RecipeSerializer<?>> SERIALIZERS = CoreRegistry.create(BuiltInRegistries.RECIPE_SERIALIZER, VanillaBackport.MOD_ID);

    public static final Supplier<RecipeSerializer<BundleColoring>> BUNDLE_COLORING = SERIALIZERS.register(
        "crafting_special_bundlecoloring",
        () -> new SimpleCraftingRecipeSerializer<>(BundleColoring::new)
    );
}