package com.blackgear.vanillabackport.common.level.inventory;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
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

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 102 + row * 18 + -18));
			}
		}

		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
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
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			int i = this.horseContainer.getContainerSize() + 1;
			if (index < i) {
				if (!this.moveItemStackTo(itemStack2, i, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(1).mayPlace(itemStack2) && !this.getSlot(1).hasItem()) {
				if (!this.moveItemStackTo(itemStack2, 1, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(0).mayPlace(itemStack2)) {
				if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (i <= 1 || !this.moveItemStackTo(itemStack2, 2, i, false)) {
				int k = i + 27;
				int m = k + 9;
				if (index >= k && index < m) {
					if (!this.moveItemStackTo(itemStack2, i, k, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= i && index < k) {
					if (!this.moveItemStackTo(itemStack2, k, m, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemStack2, k, k, false)) {
					return ItemStack.EMPTY;
				}
				
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemStack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.horseContainer.stopOpen(player);
	}
}