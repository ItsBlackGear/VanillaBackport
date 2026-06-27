package com.blackgear.vanillabackport.common.api.modules.bundle_ui;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.util.Utilities.ColorUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Consumer;

public final class BundleItemHandler {
    private static final int FULL_BAR_COLOR = ColorUtils.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int BAR_COLOR = ColorUtils.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);

    public static boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, Consumer<Entity> insertSound, Consumer<Entity> removeSound) {
        ItemStack itemInSlot = slot.getItem();
        boolean handled = false;
        
        if (action == ClickAction.PRIMARY && !itemInSlot.isEmpty()) {
            if (BundleFeatures.tryTransfer(stack, slot, player) > 0) {
                insertSound.accept(player);
            } else {
                playInsertFailSound(player);
            }
            
            handled = true;
        } else if (action == ClickAction.SECONDARY && itemInSlot.isEmpty()) {
            ItemStack removed = BundleFeatures.removeOne(stack);
            if (removed != null) {
                if (slot.safeInsert(removed).getCount() > 0) {
                    BundleFeatures.tryInsert(stack, removed);
                } else {
                    removeSound.accept(player);
                }
            }
            
            handled = true;
        }

        if (handled) broadcastChangesOnContainerMenu(player);
        return handled;
    }

    public static boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess slotAccess, Consumer<Entity> insertSound, Consumer<Entity> removeSound) {
        if (action == ClickAction.PRIMARY && other.isEmpty()) {
            BundleFeatures.toggleSelectedItem(stack, -1);
            return false;
        }

        boolean handled = false;
        if (action == ClickAction.PRIMARY && !other.isEmpty()) {
            if (slot.allowModification(player) && BundleFeatures.tryInsert(stack, other) > 0) {
                insertSound.accept(player);
            } else {
                playInsertFailSound(player);
            }
            
            handled = true;
        } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
            if (slot.allowModification(player)) {
                ItemStack removed = BundleFeatures.removeOne(stack);
                if (removed != null) {
                    removeSound.accept(player);
                    slotAccess.set(removed);
                }
            }
            
            handled = true;
        }

        if (handled) {
            broadcastChangesOnContainerMenu(player);
            return true;
        }

        BundleFeatures.toggleSelectedItem(stack, -1);
        return false;
    }

    public static boolean dropContents(ItemStack stack, Player player) {
        ItemStack removed = BundleFeatures.removeOne(stack);
        if (removed != null) {
            player.drop(removed, true);
            return true;
        }
        
        return false;
    }

    public static int getBarColor(ItemStack stack) {
        return BundleFeatures.getContentWeight(stack) >= BundleFeatures.MAX_WEIGHT ? FULL_BAR_COLOR : BAR_COLOR;
    }

    public static int getBarWidth(ItemStack stack) {
        int weight = BundleFeatures.getContentWeight(stack);
        return Math.min(1 + ((weight * 12) / BundleFeatures.MAX_WEIGHT), 13);
    }

    public static Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> items = NonNullList.create();
        BundleFeatures.getContents(stack).forEach(items::add);
        
        return Optional.of(new BundleSelectionTooltip(
            items, 
            BundleFeatures.getContentWeight(stack), 
            BundleFeatures.getSelectedItem(stack)
        ));
    }

    public static void playInsertFailSound(Entity entity) {
        entity.playSound(ModSoundEvents.BUNDLE_INSERT_FAIL.get(), 1.0F, 1.0F);
    }

    public static void broadcastChangesOnContainerMenu(Player player) {
        if (player.containerMenu != null) {
            try {
                player.containerMenu.slotsChanged(player.getInventory());
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }
}