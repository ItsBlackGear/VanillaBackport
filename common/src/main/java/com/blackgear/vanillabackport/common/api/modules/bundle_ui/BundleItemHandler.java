package com.blackgear.vanillabackport.common.api.modules.bundle_ui;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;
import java.util.function.Consumer;

public class BundleItemHandler {
    private static final int FULL_BAR_COLOR = FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int BAR_COLOR = FastColor.ARGB32.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);
    
    public static boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, Consumer<Entity> insertSound, Consumer<Entity> removeSound) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) return false;
        
        ItemStack itemInSlot = slot.getItem();
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
        boolean handled = false;
        
        if (action == ClickAction.PRIMARY && !itemInSlot.isEmpty()) {
            if (mutable.tryTransfer(slot, player) > 0) {
                insertSound.accept(player);
            } else {
                BundleFeatures.playInsertFailSound(player);
            }
            
            handled = true;
        } else if (action == ClickAction.SECONDARY && itemInSlot.isEmpty()) {
            ItemStack removed = mutable.removeOne();
            if (removed != null) {
                ItemStack insert = slot.safeInsert(removed);
                if (insert.getCount() > 0) {
                    mutable.tryInsert(insert);
                } else {
                    removeSound.accept(player);
                }
            }
            
            handled = true;
        }
        
        if (handled) {
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
            broadcastChangesOnContainerMenu(player);
        }
        
        return handled;
    }
    
    public static boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access, Consumer<Entity> insertSound, Consumer<Entity> removeSound) {
        if (action == ClickAction.PRIMARY && other.isEmpty()) {
            BundleFeatures.toggleSelectedItem(stack, -1);
            return false;
        }
        
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) return false;
        
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
        boolean handled = false;
        
        if (action == ClickAction.PRIMARY && !other.isEmpty()) {
            if (slot.allowModification(player) && mutable.tryInsert(other) > 0) {
                insertSound.accept(player);
            } else {
                BundleFeatures.playInsertFailSound(player);
            }
            
            handled = true;
        } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
            if (slot.allowModification(player)) {
                ItemStack removed = mutable.removeOne();
                if (removed != null) {
                    removeSound.accept(player);
                    access.set(removed);
                }
            }
            
            handled = true;
        }
        
        if (handled) {
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
            broadcastChangesOnContainerMenu(player);
            
            return true;
        }
        
        BundleFeatures.toggleSelectedItem(stack, -1);
        return false;
    }
    
    public static boolean dropContents(ItemStack stack, Player player) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents != null && !contents.isEmpty()) {
            Optional<ItemStack> removed = BundleFeatures.removeOneItemFromBundle(stack, player, contents);
            if (removed.isPresent()) {
                player.drop(removed.get(), true);
                return true;
            }
        }
        
        return false;
    }
    
    public static int getBarColor(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return contents.weight().compareTo(Fraction.ONE) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
    }
    
    public static void broadcastChangesOnContainerMenu(Player player) {
        if (player.containerMenu != null) {
            try {
                player.containerMenu.slotsChanged(player.getInventory());
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }
}