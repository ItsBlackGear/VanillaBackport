package com.blackgear.vanillabackport.common.level.crafting;

import com.blackgear.vanillabackport.common.api.bundle.BundleContents;
import com.blackgear.vanillabackport.common.registries.ModRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BundleColoring extends CustomRecipe {
    public BundleColoring(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int bundles = 0;
        int dyes = 0;

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
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
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack bundle = ItemStack.EMPTY;
        DyeItem dye = (DyeItem) Items.WHITE_DYE;

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);

            if (!stack.isEmpty()) {
                Item item = stack.getItem();

                if (item instanceof BundleItem) {
                    bundle = stack;
                } else if (item instanceof DyeItem) {
                    dye = (DyeItem) item;
                }
            }
        }

        ItemStack result = BundleContents.getByColor(dye.getDyeColor()).getDefaultInstance();
        if (
            bundle.hasTag()) {
            result.setTag(bundle.getTag().copy());
        }

        return result;
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