package com.blackgear.vanillabackport.core.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import static net.minecraft.util.FastColor.ARGB32.*;

public class ColorUtils {
    public static int scaleRGB(int color, float red, float green, float blue) {
        return color(
            alpha(color),
            Mth.clamp((int) (red(color) * red), 0, 255),
            Mth.clamp((int) (green(color) * green), 0, 255),
            Mth.clamp((int) (blue(color) * blue), 0, 255)
        );
    }

    public static int colorFromFloat(float alpha, float red, float green, float blue) {
        return color(
            (int) (alpha * 255),
            (int) (red * 255),
            (int) (green * 255),
            (int) (blue * 255)
        );
    }

    public static DyeColor getMixedColor(ServerLevel level, DyeColor colorA, DyeColor colorB) {
        CraftingContainer container = makeCraftColorInput(colorA, colorB);
        return level.getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, container, level)
            .map(recipe -> recipe.assemble(container, level.registryAccess()))
            .map(ItemStack::getItem)
            .filter(DyeItem.class::isInstance)
            .map(DyeItem.class::cast)
            .map(DyeItem::getDyeColor)
            .orElseGet(() -> level.random.nextBoolean() ? colorA : colorB);
    }

    private static CraftingContainer makeCraftColorInput(DyeColor colorA, DyeColor colorB) {
        CraftingContainer container = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
            public ItemStack quickMoveStack(Player player, int index) {
                return ItemStack.EMPTY;
            }

            public boolean stillValid(Player player) {
                return false;
            }
        }, 2, 1);
        container.setItem(0, new ItemStack(DyeItem.byColor(colorA)));
        container.setItem(1, new ItemStack(DyeItem.byColor(colorB)));
        return container;
    }
}