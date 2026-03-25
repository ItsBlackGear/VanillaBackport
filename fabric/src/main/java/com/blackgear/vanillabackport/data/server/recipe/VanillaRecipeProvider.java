package com.blackgear.vanillabackport.data.server.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class VanillaRecipeProvider implements DataProvider {
    private final PackOutput.PathProvider recipePathProvider;
    private final PackOutput.PathProvider advancementPathProvider;

    protected VanillaRecipeProvider(PackOutput output) {
        this.recipePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
        this.advancementPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Set<ResourceLocation> duplicates = Sets.newHashSet();
        List<CompletableFuture<?>> output = new ArrayList<>();
        this.buildRecipes(recipe -> {
            if (!duplicates.add(recipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + recipe.getId());
            } else {
                output.add(DataProvider.saveStable(cache, recipe.serializeRecipe(), this.recipePathProvider.json(recipe.getId())));
                JsonObject advancement = recipe.serializeAdvancement();
                if (advancement != null) {
                    output.add(DataProvider.saveStable(cache, advancement, this.advancementPathProvider.json(recipe.getAdvancementId())));
                }
            }
        });

        return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
    }

    protected abstract void buildRecipes(Consumer<FinishedRecipe> consumer);
}