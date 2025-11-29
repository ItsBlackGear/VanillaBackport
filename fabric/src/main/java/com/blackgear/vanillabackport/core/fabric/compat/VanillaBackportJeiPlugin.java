package com.blackgear.vanillabackport.core.fabric.compat;

import com.blackgear.vanillabackport.common.api.bundle.BundleFeatures;
import com.blackgear.vanillabackport.common.level.crafting.BundleColoring;
import com.blackgear.vanillabackport.core.VanillaBackport;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JeiPlugin
public class VanillaBackportJeiPlugin implements IModPlugin {
    private static final List<Item> BUNDLES = BuiltInRegistries.ITEM.stream()
        .filter(item -> item instanceof BundleItem)
        .toList();

    private static final Map<DyeColor, List<Item>> DYES_BY_COLOR = BuiltInRegistries.ITEM.stream()
        .filter(item -> item instanceof DyeItem)
        .map(item -> (DyeItem) item)
        .collect(Collectors.groupingBy(
            DyeItem::getDyeColor,
            () -> new EnumMap<>(DyeColor.class),
            Collectors.mapping(d -> (Item) d, Collectors.toList())
        ));

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        RecipeManager recipeManager = mc.level.getRecipeManager();

        boolean hasBundleColoring = recipeManager
            .getAllRecipesFor(RecipeType.CRAFTING)
            .stream()
            .map(RecipeHolder::value)
            .anyMatch(r -> r instanceof BundleColoring);

        if (!hasBundleColoring) return;

        List<RecipeHolder<CraftingRecipe>> extraRecipes = new ArrayList<>();

        for (DyeColor dyeColor : DyeColor.values()) {
            Item resultBundle = BundleFeatures.getByColor(dyeColor);
            List<Item> dyeItems = DYES_BY_COLOR.get(dyeColor);
            if (resultBundle == null || dyeItems == null || dyeItems.isEmpty()) continue;

            List<Item> otherBundles = BUNDLES.stream()
                .filter(item -> item != resultBundle)
                .toList();

            if (otherBundles.isEmpty()) continue;

            Ingredient anyOtherBundle = Ingredient.of(
                otherBundles.stream()
                    .map(ItemStack::new)
                    .toArray(ItemStack[]::new)
            );

            Ingredient dyeChoices = Ingredient.of(
                dyeItems.stream()
                    .map(ItemStack::new)
                    .toArray(ItemStack[]::new)
            );

            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, anyOtherBundle, dyeChoices);
            ItemStack output = new ItemStack(resultBundle);

            ResourceLocation baseId = BuiltInRegistries.ITEM.getKey(resultBundle);
            ResourceLocation displayId = ResourceLocation.fromNamespaceAndPath(baseId.getNamespace(), "/" + baseId.getPath());

            ShapelessRecipe recipe = new ShapelessRecipe("", CraftingBookCategory.MISC, output, inputs);
            RecipeHolder<CraftingRecipe> holder = new RecipeHolder<>(displayId, recipe);

            extraRecipes.add(holder);
        }

        registration.addRecipes(RecipeTypes.CRAFTING, extraRecipes);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return VanillaBackport.resource("jei_plugin");
    }
}