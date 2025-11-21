package com.blackgear.vanillabackport.core.forge.compat;

import com.blackgear.vanillabackport.common.api.bundle.BundleFeatures;
import com.blackgear.vanillabackport.common.level.crafting.BundleColoring;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EmiEntrypoint
public class VanillaBackportEmiPlugin implements EmiPlugin {
    private static final List<Item> BUNDLES = BuiltInRegistries.ITEM.stream()
        .filter(item -> item instanceof BundleItem)
        .toList();

    // Dynamically collect all dye items (vanilla + modded).
    private static final Map<DyeColor, List<Item>> DYES_BY_COLOR = BuiltInRegistries.ITEM.stream()
        .filter(item -> item instanceof DyeItem)
        .map(item -> (DyeItem) item)
        .collect(Collectors.groupingBy(
            DyeItem::getDyeColor,
            () -> new EnumMap<>(DyeColor.class),
            Collectors.mapping(d -> (Item) d, Collectors.toList())
        ));

    @Override
    public void register(EmiRegistry registry) {
        boolean hasBundleColoring = registry.getRecipeManager()
            .getAllRecipesFor(RecipeType.CRAFTING)
            .stream()
            .anyMatch(r -> r instanceof BundleColoring);

        if (!hasBundleColoring) return;

        for (DyeColor dyeColor : DyeColor.values()) {
            Item resultBundle = BundleFeatures.getByColor(dyeColor);
            List<Item> dyeItems = DYES_BY_COLOR.get(dyeColor);
            if (resultBundle == null || dyeItems == null || dyeItems.isEmpty()) continue;

            EmiIngredient anyOtherBundle = EmiIngredient.of(
                BUNDLES.stream()
                    .filter(item -> item != resultBundle)
                    .map(EmiStack::of)
                    .toList()
            );

            // Accept any dye item matching this color.
            EmiIngredient dyeChoices = EmiIngredient.of(
                dyeItems.stream()
                    .map(EmiStack::of)
                    .toList()
            );

            List<EmiIngredient> inputs = List.of(anyOtherBundle, dyeChoices);
            EmiStack output = EmiStack.of(resultBundle);

            ResourceLocation baseId = BuiltInRegistries.ITEM.getKey(resultBundle);
            ResourceLocation displayId = new ResourceLocation(baseId.getNamespace(), "/" + baseId.getPath());

            registry.addRecipe(new EmiCraftingRecipe(inputs, output, displayId, true));
        }
    }
}