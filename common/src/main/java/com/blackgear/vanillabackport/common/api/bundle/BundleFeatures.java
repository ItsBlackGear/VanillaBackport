package com.blackgear.vanillabackport.common.api.bundle;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.ModChecker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;

import java.util.Optional;

public class BundleFeatures {
    public static boolean onBundleUpdate() {
        return VanillaBackport.COMMON_CONFIG.hasUpdatedBundles.get() && !ModChecker.BEST_BUNDLES_LOADED;
    }

    public static boolean canItemBeInBundle(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().canFitInsideContainerItems();
    }

    public static void toggleSelectedItem(ItemStack stack, int index) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents != null) {
            BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
            ((IBundle.Mutable) mutable).toggleSelectedItem(index);
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        }
    }

    public static int getSelectedItem(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return ((IBundle)(Object)contents).getSelectedItem();
    }

    public static ItemStack getSelectedItemStack(ItemStack stack) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        IBundle ibundle = (IBundle)(Object)contents;
        return contents != null && ibundle.getSelectedItem() != -1
            ? contents.getItemUnsafe(ibundle.getSelectedItem())
            : ItemStack.EMPTY;
    }

    public static int getNumberOfItemsToShow(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return ((IBundle)(Object)contents).getNumberOfItemsToShow();
    }

    public static Optional<ItemStack> removeOneItemFromBundle(ItemStack stack, Player player, BundleContents contents) {
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
        ItemStack itemStack = mutable.removeOne();
        if (itemStack != null) {
            BundleFeatures.playRemoveOneSound(player);
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
            return Optional.of(itemStack);
        } else {
            return Optional.empty();
        }
    }

    public static Item getByColor(DyeColor dyeColor) {
        return switch (dyeColor) {
            case WHITE -> ModItems.WHITE_BUNDLE.get();
            case ORANGE -> ModItems.ORANGE_BUNDLE.get();
            case MAGENTA -> ModItems.MAGENTA_BUNDLE.get();
            case LIGHT_BLUE -> ModItems.LIGHT_BLUE_BUNDLE.get();
            case YELLOW -> ModItems.YELLOW_BUNDLE.get();
            case LIME -> ModItems.LIME_BUNDLE.get();
            case PINK -> ModItems.PINK_BUNDLE.get();
            case GRAY -> ModItems.GRAY_BUNDLE.get();
            case LIGHT_GRAY -> ModItems.LIGHT_GRAY_BUNDLE.get();
            case CYAN -> ModItems.CYAN_BUNDLE.get();
            case BLUE -> ModItems.BLUE_BUNDLE.get();
            case BROWN -> ModItems.BROWN_BUNDLE.get();
            case GREEN -> ModItems.GREEN_BUNDLE.get();
            case RED -> ModItems.RED_BUNDLE.get();
            case BLACK -> ModItems.BLACK_BUNDLE.get();
            case PURPLE -> ModItems.PURPLE_BUNDLE.get();
            default -> Items.BUNDLE;
        };
    }

    public static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    public static void playInsertFailSound(Entity entity) {
        entity.playSound(ModSoundEvents.BUNDLE_INSERT_FAIL.get(), 1.0F, 1.0F);
    }

    public static void broadcastChangesOnContainerMenu(Player player) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu != null) {
            try {
                menu.slotsChanged(player.getInventory());
            } catch (IndexOutOfBoundsException ignored) {
                // This works as fallback for a very specific set of mods that lead to crash when opening a very specific inventory...
                // i couldn't replicate this properly but hey, this works :shrug:
            }
        }
    }
}
