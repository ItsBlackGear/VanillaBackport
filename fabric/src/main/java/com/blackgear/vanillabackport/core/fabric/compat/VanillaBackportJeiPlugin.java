package com.blackgear.vanillabackport.core.fabric.compat;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleColoring;
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
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        RecipeManager recipeManager = mc.level.getRecipeManager();

        boolean hasBundleColoring = recipeManager
            .getAllRecipesFor(RecipeType.CRAFTING)
            .stream()
            .anyMatch(r -> r instanceof BundleColoring);

        if (!hasBundleColoring) return;

        Map<DyeColor, List<Item>> dyesByColor = getDyesByColor();
        List<Item> allBundles = getBundles();
        
        List<CraftingRecipe> extraRecipes = new ArrayList<>(DyeColor.values().length);

        for (DyeColor dyeColor : DyeColor.values()) {
            Item resultBundle = BundleFeatures.getByColor(dyeColor);
            List<Item> dyeItems = dyesByColor.get(dyeColor);
            if (resultBundle == null || dyeItems == null || dyeItems.isEmpty()) continue;

            ItemStack[] otherBundlesStacks = allBundles.stream()
                .filter(item -> item != resultBundle)
                .map(ItemStack::new)
                .toArray(ItemStack[]::new);

            if (otherBundlesStacks.length == 0) continue;

            Ingredient anyOtherBundle = Ingredient.of(otherBundlesStacks);
            Ingredient dyeChoices = Ingredient.of(dyeItems.stream().map(ItemStack::new));

            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, anyOtherBundle, dyeChoices);
            ItemStack output = new ItemStack(resultBundle);

            ResourceLocation baseId = BuiltInRegistries.ITEM.getKey(resultBundle);
            ResourceLocation displayId = new ResourceLocation(baseId.getNamespace(), "/" + baseId.getPath());

            ShapelessRecipe recipe = new ShapelessRecipe(displayId, "", CraftingBookCategory.MISC, output, inputs);
            extraRecipes.add(recipe);
        }

        registration.addRecipes(RecipeTypes.CRAFTING, extraRecipes);
    }

    private static List<Item> getBundles() {
        return List.copyOf(BundleFeatures.BUNDLES_BY_DYE.values());
    }

    private static Map<DyeColor, List<Item>> getDyesByColor() {
        return BuiltInRegistries.ITEM.stream()
            .filter(DyeItem.class::isInstance)
            .map(DyeItem.class::cast)
            .collect(Collectors.groupingBy(
                DyeItem::getDyeColor,
                () -> new EnumMap<>(DyeColor.class),
                Collectors.mapping(d -> (Item) d, Collectors.toList())
            ));
    }

    @Override
    public ResourceLocation getPluginUid() {
        return VanillaBackport.resource("jei_plugin");
    }
}