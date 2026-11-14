package com.blackgear.vanillabackport.common.level.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CrafterSlot extends Slot {
	private final CrafterMenu menu;

	public CrafterSlot(Container container, int slot, int x, int y, CrafterMenu menu) {
		super(container, slot, x, y);
		this.menu = menu;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return !this.menu.isSlotDisabled(this.index) && super.mayPlace(stack);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.menu.slotsChanged(this.container);
	}
}
