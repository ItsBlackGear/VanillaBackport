package com.blackgear.vanillabackport.common.api.modules.bundle_ui;

import com.blackgear.vanillabackport.common.registries.ModRecipeSerializers;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BundleColoring extends CustomRecipe {
    public BundleColoring(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (!VanillaBackport.COMMON_CONFIG.hasDyeableBundleRecipe.get()) return false;
        
        int bundles = 0;
        int dyes = 0;

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (!stack.isEmpty()) {
                if (isBundle(stack.getItem())) {
                    bundles++;
                } else if (stack.getItem() instanceof DyeItem) {
                    dyes++;
                } else {
                    return false;
                }

                if (dyes > 1 || bundles > 1) {
                    return false;
                }
            }
        }

        return bundles == 1 && dyes == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack bundle = ItemStack.EMPTY;
        DyeItem dye = null;

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (isBundle(item)) {
                    bundle = stack;
                } else if (item instanceof DyeItem dyeItem) {
                    dye = dyeItem;
                }
            }
        }
        
        if (bundle.isEmpty() || dye == null) return ItemStack.EMPTY;
        
        Item result = BundleFeatures.getByColor(dye.getDyeColor());
        return bundle.transmuteCopy(result, 1);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.BUNDLE_COLORING.get();
    }

    private static boolean isBundle(Item item) {
        return item == Items.BUNDLE || BundleFeatures.BUNDLES_BY_DYE.containsValue(item);
    }
}