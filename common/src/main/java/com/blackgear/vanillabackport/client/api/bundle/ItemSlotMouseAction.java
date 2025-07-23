package com.blackgear.vanillabackport.client.api.bundle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public interface ItemSlotMouseAction {
    boolean matches(Slot slot);

    boolean onMouseScrolled(double scrollDelta, int i, ItemStack stack);

    void onStopHovering(Slot slot);

    void onSlotClicked(Slot slot, ClickType clickType);
}
