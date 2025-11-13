package com.blackgear.vanillabackport.common.level.crafting;

import com.blackgear.vanillabackport.common.api.bundle.BundleFeatures;
import com.blackgear.vanillabackport.common.registries.ModRecipeSerializers;
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
        int bundles = 0;
        int dyes = 0;

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BundleItem) {
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
        DyeItem dye = (DyeItem) Items.WHITE_DYE;

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);

            if (!stack.isEmpty()) {
                Item item = stack.getItem();

                if (item instanceof BundleItem) {
                    bundle = stack;
                } else if (item instanceof DyeItem) {
                    dye = (DyeItem) item;
                }
            }
        }

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
}