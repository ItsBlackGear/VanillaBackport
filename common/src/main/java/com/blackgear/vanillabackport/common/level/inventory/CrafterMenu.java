package com.blackgear.vanillabackport.common.level.inventory;

import com.blackgear.vanillabackport.common.level.blocks.CrafterBlock;
import com.blackgear.vanillabackport.common.registries.ModMenuTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrafterMenu extends AbstractContainerMenu implements ContainerListener {
	private final ResultContainer resultContainer = new ResultContainer();
	private final ContainerData containerData;
	private final Player player;
	private final CraftingContainer container;

	public CrafterMenu(int containerId, Inventory inventory) {
		super(ModMenuTypes.CRAFTER_3x3.get(), containerId);
		this.player = inventory.player;
		this.containerData = new SimpleContainerData(10);
		this.container = new TransientCraftingContainer(this, 3, 3);
		this.addSlots(inventory);
	}

	public CrafterMenu(int containerId, Inventory inventory, CraftingContainer container, ContainerData containerData) {
		super(ModMenuTypes.CRAFTER_3x3.get(), containerId);
		this.player = inventory.player;
		this.containerData = containerData;
		this.container = container;
		checkContainerSize(container, 9);
		container.startOpen(inventory.player);
		this.addSlots(inventory);
		this.addSlotListener(this);
	}

	private void addSlots(Inventory inventory) {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				int slot = x + y * 3;
				this.addSlot(new CrafterSlot(this.container, slot, 26 + x * 18, 17 + y * 18, this));
			}
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
		}

		this.addSlot(new NonInteractiveResultSlot(this.resultContainer, 0, 134, 35));
		this.addDataSlots(this.containerData);
		this.refreshRecipeResult();
	}

	public void setSlotState(int slot, boolean enabled) {
		CrafterSlot crafterSlot = (CrafterSlot) this.getSlot(slot);
		this.containerData.set(crafterSlot.index, enabled ? 0 : 1);
		this.broadcastChanges();
	}

	public boolean isSlotDisabled(int slot) {
		return slot > -1 && slot < 9 && this.containerData.get(slot) == 1;
	}

	public boolean isPowered() {
		return this.containerData.get(9) == 1;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack clicked = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			clicked = stack.copy();
			if (index < 9) {
				if (!this.moveItemStackTo(stack, 9, 45, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(stack, 0, 9, false)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (stack.getCount() == clicked.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, stack);
		}

		return clicked;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	private void refreshRecipeResult() {
		if (this.player instanceof ServerPlayer serverPlayer) {
			Level level = serverPlayer.level();
			CraftingContainer container = this.container;
			ItemStack result = CrafterBlock.getPotentialResults(level, container)
				.map(recipe -> recipe.assemble(container, level.registryAccess()))
				.orElse(ItemStack.EMPTY);
			this.resultContainer.setItem(0, result);
		}
	}

	public Container getContainer() {
		return this.container;
	}

	@Override
	public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
		this.refreshRecipeResult();
	}

	@Override
	public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
	}
}
