package com.blackgear.vanillabackport.client.api.modules.bundle_ui;

import com.blackgear.platform.client.event.screen.HudInteractions;
import com.blackgear.platform.core.util.event.CancellableResult;
import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.blackgear.vanillabackport.core.network.ServerboundSelectBundleItemPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class BundleMouseActions implements ItemSlotMouseAction {
    public static final BundleMouseActions INSTANCE = new BundleMouseActions();
    private final ScrollWheelHandler scrollWheelHandler = new ScrollWheelHandler();

    public static void bootstrap() {
        HudInteractions.SCROLLING_PRE.register((minecraft, screen, mouseX, mouseY, scrollDelta) -> {
            if (!BundleFeatures.onBundleUpdate()) return CancellableResult.PASS;

            if (screen instanceof AbstractContainerScreen<?> container && container.hoveredSlot != null) {
                Slot slot = container.hoveredSlot;
                if (slot.hasItem() && INSTANCE.matches(slot) && INSTANCE.onMouseScrolled(scrollDelta, slot.index, slot.getItem())) {
                    return CancellableResult.CANCEL;
                }
            }
            return CancellableResult.PASS;
        });

        HudInteractions.STOP_HOVERING.register((minecraft, screen, slot) -> {
            if (slot != null && slot.hasItem() && BundleFeatures.onBundleUpdate() && INSTANCE.matches(slot)) {
                INSTANCE.onStopHovering(slot);
            }
        });

        HudInteractions.SLOT_CLICK.register((minecraft, screen, slot, clickType) -> {
            if (slot != null && slot.hasItem() && BundleFeatures.onBundleUpdate() && INSTANCE.matches(slot)) {
                INSTANCE.onSlotClicked(slot, clickType);
            }
        });
    }

    @Override
    public boolean matches(Slot slot) {
        return slot.getItem().is(ModItemTags.BUNDLES);
    }

    @Override
    public boolean onMouseScrolled(double scrollDelta, int slotId, ItemStack stack) {
        int itemsToShow = BundleFeatures.getNumberOfItemsToShow(stack);
        if (itemsToShow == 0) return false;

        Vector2i scroll = this.scrollWheelHandler.onMouseScroll(scrollDelta);
        int delta = scroll.y == 0 ? -scroll.x : scroll.y;

        if (delta != 0) {
            int selectedItem = BundleFeatures.getSelectedItem(stack);
            int selectedItemIndex = ScrollWheelHandler.getNextScrollWheelSelection(delta, selectedItem, itemsToShow);
            if (selectedItem != selectedItemIndex) {
                this.toggleSelectedBundleItem(stack, slotId, selectedItemIndex);
            }
        }
        return true;
    }

    @Override
    public void onStopHovering(Slot slot) {
        this.unselectedBundleItem(slot.getItem(), slot.index);
    }

    @Override
    public void onSlotClicked(Slot slot, ClickType clickType) {
        if (clickType == ClickType.QUICK_MOVE || clickType == ClickType.SWAP) {
            this.unselectedBundleItem(slot.getItem(), slot.index);
        }
    }

    private void toggleSelectedBundleItem(ItemStack stack, int slotId, int selectedItemIndex) {
        if (selectedItemIndex < BundleFeatures.getNumberOfItemsToShow(stack)) {
            BundleFeatures.toggleSelectedItem(stack, selectedItemIndex);
            NetworkHandler.DEFAULT_CHANNEL.sendToServer(new ServerboundSelectBundleItemPacket(slotId, selectedItemIndex));
        }
    }

    public void unselectedBundleItem(ItemStack stack, int slotId) {
        this.toggleSelectedBundleItem(stack, slotId, -1);
    }
}