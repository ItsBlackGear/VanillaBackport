package com.blackgear.vanillabackport.common.api.bundle;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;

public class BundleSelectionTooltip extends BundleTooltip {
    private final int selectedItem;

    public BundleSelectionTooltip(NonNullList<ItemStack> items, int weight, int selectedItem) {
        super(items, weight);
        this.selectedItem = selectedItem;
    }

    public int getSelectedItem() {
        return this.selectedItem;
    }
}