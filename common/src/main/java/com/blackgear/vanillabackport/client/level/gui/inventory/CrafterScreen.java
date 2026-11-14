package com.blackgear.vanillabackport.client.level.gui.inventory;

import com.blackgear.vanillabackport.common.level.inventory.CrafterMenu;
import com.blackgear.vanillabackport.common.level.inventory.CrafterSlot;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.blackgear.vanillabackport.core.network.ServerboundContainerSlotStateChangedPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class CrafterScreen extends AbstractContainerScreen<CrafterMenu> {
	private static final ResourceLocation DISABLED_SLOT_LOCATION_SPRITE = new ResourceLocation("textures/container/crafter/disabled_slot.png");
	private static final ResourceLocation POWERED_REDSTONE_LOCATION_SPRITE = new ResourceLocation("textures/container/crafter/powered_redstone.png");
	private static final ResourceLocation UNPOWERED_REDSTONE_LOCATION_SPRITE = new ResourceLocation("textures/container/crafter/unpowered_redstone.png");
	private static final ResourceLocation CONTAINER_LOCATION = new ResourceLocation("textures/gui/container/crafter.png");
	private static final Component DISABLED_SLOT_TOOLTIP = Component.translatable("gui.togglable_slot");
	private final Player player;

	public CrafterScreen(CrafterMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.player = playerInventory.player;
	}

	@Override
	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
		if (slot instanceof CrafterSlot && !slot.hasItem() && !this.player.isSpectator()) {
			switch (type) {
				case PICKUP:
					if (this.menu.isSlotDisabled(slotId)) {
						this.enableSlot(slotId);
					} else if (this.menu.getCarried().isEmpty()) {
						this.disableSlot(slotId);
					}
					break;
				case SWAP:
					ItemStack stack = this.player.getInventory().getItem(mouseButton);
					if (this.menu.isSlotDisabled(slotId) && !stack.isEmpty()) {
						this.enableSlot(slotId);
					}
			}
		}

		super.slotClicked(slot, slotId, mouseButton, type);
	}

	private void enableSlot(int slot) {
		this.updateSlotState(slot, true);
	}

	private void disableSlot(int slot) {
		this.updateSlotState(slot, false);
	}

	private void updateSlotState(int slot, boolean state) {
		this.menu.setSlotState(slot, state);
		NetworkHandler.DEFAULT_CHANNEL.sendToServer(new ServerboundContainerSlotStateChangedPacket(slot, this.menu.containerId, state));
		float pitch = state ? 1.0F : 0.75F;
		this.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, pitch);
	}

	@Override
	public void renderSlot(GuiGraphics guiGraphics, Slot slot) {
		if (slot instanceof CrafterSlot crafterSlot && this.menu.isSlotDisabled(slot.index)) {
			this.renderDisabledSlot(guiGraphics, crafterSlot);
		} else {
			super.renderSlot(guiGraphics, slot);
		}
	}
	
	private void renderDisabledSlot(GuiGraphics guiGraphics, CrafterSlot slot) {
		guiGraphics.blit(DISABLED_SLOT_LOCATION_SPRITE, slot.x - 1, slot.y - 1, 0, 0, 0, 18, 18, 18, 18);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderRedstone(guiGraphics);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		if (this.hoveredSlot instanceof CrafterSlot
			&& !this.menu.isSlotDisabled(this.hoveredSlot.index)
			&& this.menu.getCarried().isEmpty()
			&& !this.hoveredSlot.hasItem()
			&& !this.player.isSpectator()) {
			guiGraphics.renderTooltip(this.font, DISABLED_SLOT_TOOLTIP, mouseX, mouseY);
		}
	}

	private void renderRedstone(GuiGraphics graphics) {
		int x = this.width / 2 + 9;
		int y = this.height / 2 - 48;
		ResourceLocation redstoneArrowTexture;
		if (this.menu.isPowered()) {
			redstoneArrowTexture = POWERED_REDSTONE_LOCATION_SPRITE;
		} else {
			redstoneArrowTexture = UNPOWERED_REDSTONE_LOCATION_SPRITE;
		}

		graphics.blit(redstoneArrowTexture, x, y, 0, 0, 0, 16, 16, 16, 16);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int x = (this.width - this.imageWidth) / 2;
		int y = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(CONTAINER_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);
	}
}
