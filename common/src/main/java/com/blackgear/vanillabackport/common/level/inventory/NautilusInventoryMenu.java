package com.blackgear.vanillabackport.common.level.inventory;

import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NautilusInventoryMenu extends AbstractContainerMenu {
	private final Container horseContainer;
	private final Container armorContainer;
	private final AbstractNautilus nautilus;

	public NautilusInventoryMenu(int containerId, Inventory inventory, Container container, AbstractNautilus nautilus) {
		super(null, containerId);
		this.horseContainer = container;
		this.armorContainer = nautilus.getBodyArmorAccess();
		this.nautilus = nautilus;
		container.startOpen(inventory.player);
		this.addSlot(new Slot(container, 0, 8, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(Items.SADDLE) && !this.hasItem() && nautilus.isSaddleable();
			}

			@Override
			public boolean isActive() {
				return nautilus.isSaddleable();
			}
		});
		this.addSlot(new ArmorSlot(this.armorContainer, nautilus, EquipmentSlot.BODY, 0, 8, 36, null) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return nautilus.isBodyArmorItem(stack);
			}

			@Override
			public boolean isActive() {
				return nautilus.canUseSlot(EquipmentSlot.BODY);
			}
		});

		for (int k = 0; k < 3; k++) {
			for (int l = 0; l < 9; l++) {
				this.addSlot(new Slot(inventory, l + k * 9 + 9, 8 + l * 18, 102 + k * 18 + -18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return !this.nautilus.hasInventoryChanged(this.horseContainer)
			&& this.horseContainer.stillValid(player)
			&& this.armorContainer.stillValid(player)
			&& this.nautilus.isAlive()
			&& player.canInteractWithEntity(this.nautilus, 4.0);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack clicked = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			clicked = stack.copy();
			int playerContainerStart = this.horseContainer.getContainerSize() + 1;
			if (index < playerContainerStart) {
				if (!this.moveItemStackTo(stack, playerContainerStart, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(1).mayPlace(stack) && !this.getSlot(1).hasItem()) {
				if (!this.moveItemStackTo(stack, 1, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(0).mayPlace(stack)) {
				if (!this.moveItemStackTo(stack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (playerContainerStart <= 1 || !this.moveItemStackTo(stack, 2, playerContainerStart, false)) {
				int playerContainerEnd = playerContainerStart + 27;
				int playerHotBarEnd = playerContainerEnd + 9;
				if (index >= playerContainerEnd && index < playerHotBarEnd) {
					if (!this.moveItemStackTo(stack, playerContainerStart, playerContainerEnd, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= playerContainerStart && index < playerContainerEnd) {
					if (!this.moveItemStackTo(stack, playerContainerEnd, playerHotBarEnd, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(stack, playerContainerEnd, playerContainerEnd, false)) {
					return ItemStack.EMPTY;
				}

				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return clicked;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.horseContainer.stopOpen(player);
	}
}