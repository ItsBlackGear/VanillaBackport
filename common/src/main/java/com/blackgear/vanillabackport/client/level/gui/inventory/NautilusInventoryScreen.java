package com.blackgear.vanillabackport.client.level.gui.inventory;

import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.inventory.NautilusInventoryMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class NautilusInventoryScreen extends AbstractContainerScreen<NautilusInventoryMenu> {
	private static final ResourceLocation HORSE_INVENTORY_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/container/nautilus.png");
	private final AbstractNautilus nautilus;
	private float xMouse;
	private float yMouse;

	public NautilusInventoryScreen(NautilusInventoryMenu menu, Inventory inventory, AbstractNautilus nautilus) {
		super(menu, inventory, nautilus.getDisplayName());
		this.nautilus = nautilus;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int xOffset = (this.width - this.imageWidth) / 2;
		int yOffset = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(HORSE_INVENTORY_LOCATION, xOffset, yOffset, 0, 0, this.imageWidth, this.imageHeight);
		if (this.nautilus.isSaddleable()) {
			guiGraphics.blit(HORSE_INVENTORY_LOCATION, xOffset + 7, yOffset + 35 - 18, 18, this.imageHeight + 54, 18, 18);
		}

		if (this.nautilus.canUseSlot(EquipmentSlot.BODY)) {
			guiGraphics.blit(HORSE_INVENTORY_LOCATION, xOffset + 7, yOffset + 35, 0, this.imageHeight + 54, 18, 18);
		}

		InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, xOffset + 26, yOffset + 18, xOffset + 78, yOffset + 70, 17, 0.25F, this.xMouse, this.yMouse, this.nautilus);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.xMouse = mouseX;
		this.yMouse = mouseY;
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}
}
