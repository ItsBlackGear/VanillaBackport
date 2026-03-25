package com.blackgear.vanillabackport.data.server.recipe;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class VanillaRecipeProvider implements DataProvider {
    private final PackOutput.PathProvider recipePathProvider;
    private final PackOutput.PathProvider advancementPathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public VanillaRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.recipePathProvider = output.createRegistryElementsPathProvider(Registries.RECIPE);
        this.advancementPathProvider = output.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.registries.thenCompose(provider -> this.run(cache, provider));
    }

    private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
        Set<ResourceLocation> duplicates = Sets.newHashSet();
        List<CompletableFuture<?>> output = new ArrayList<>();
        this.buildRecipes(new RecipeOutput() {
            @Override
            public void accept(ResourceLocation location, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
                if (!duplicates.add(location)) {
                    throw new IllegalStateException("Duplicate recipe " + location);
                } else {
                    output.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(location)));
                    if (advancement != null) {
                        output.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancement.value(), advancementPathProvider.json(advancement.id())));
                    }
                }
            }

            @Override
            public Advancement.Builder advancement() {
                return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
            }
        });
        return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
    }

    protected abstract void buildRecipes(RecipeOutput output);
}