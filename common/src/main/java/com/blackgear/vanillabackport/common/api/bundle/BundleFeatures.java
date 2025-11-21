package com.blackgear.vanillabackport.common.api.bundle;

import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class BundleFeatures {
    private static final String TAG_ITEMS = "Items";
    private static final String TAG_SELECTED_ITEM = "SelectedItem";
    public static final int MAX_WEIGHT = 64;
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int NO_SELECTED_ITEM = -1;

    public static boolean onBundleUpdate() {
        return VanillaBackport.COMMON_CONFIG.hasUpdatedBundles.get();
    }

    public static boolean canItemBeInBundle(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().canFitInsideContainerItems();
    }

    @Nullable
    public static ItemStack removeOne(ItemStack bundle) {
        CompoundTag tag = bundle.getOrCreateTag();
        if (!tag.contains(TAG_ITEMS)) {
            return null;
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);
        if (items.isEmpty()) {
            return null;
        }

        int selectedItem = tag.getInt(TAG_SELECTED_ITEM);
        int index = isValidIndex(selectedItem, items.size()) ? selectedItem : 0;
        if (!isValidIndex(index, items.size())) {
            return null;
        }

        CompoundTag itemTag = items.getCompound(index);
        ItemStack stored = ItemStack.of(itemTag);

        // Split exactly one item out
        ItemStack one = stored.split(1);

        if (stored.isEmpty()) {
            // Remove entry if it reached 0
            items.remove(index);
            if (items.isEmpty()) {
                bundle.removeTagKey(TAG_ITEMS);
            }
        } else {
            // Persist the updated remaining count at the same index
            CompoundTag updated = new CompoundTag();
            stored.save(updated);
            items.set(index, updated);
        }

        setSelectedItem(bundle, NO_SELECTED_ITEM);
        return one.isEmpty() ? null : one;
    }

    public static Stream<ItemStack> getContents(ItemStack bundle) {
        CompoundTag tag = bundle.getTag();
        if (tag == null || !tag.contains(TAG_ITEMS)) {
            return Stream.empty();
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);
        return items.stream()
            .map(CompoundTag.class::cast)
            .map(ItemStack::of);
    }

    public static int getWeight(ItemStack stack) {
        if (stack.is(ModItemTags.BUNDLES)) {
            return BUNDLE_IN_BUNDLE_WEIGHT + getContentWeight(stack);
        }

        int max = stack.getMaxStackSize();
        if (max <= 0) return 1; // Fallback safety

        int weight = MAX_WEIGHT / max;
        return Math.max(1, weight); // Prevent zero which causes division errors
    }

    public static int getContentWeight(ItemStack bundle) {
        return getContents(bundle)
            .mapToInt(item -> getWeight(item) * item.getCount())
            .sum();
    }

    // Never match non-stackable items (e.g., bundles) to avoid merging them
    private static Optional<CompoundTag> getMatchingItem(ItemStack stack, ListTag items) {
        if (stack.getMaxStackSize() == 1) {
            return Optional.empty();
        }

        return items.stream()
            .filter(CompoundTag.class::isInstance)
            .map(CompoundTag.class::cast)
            .filter(tag -> {
                ItemStack existing = ItemStack.of(tag);
                return existing.getMaxStackSize() > 1 && ItemStack.isSameItemSameTags(existing, stack);
            })
            .findFirst();
    }

    public static void setSelectedItem(ItemStack bundle, int index) {
        CompoundTag tag = bundle.getOrCreateTag();
        tag.putInt(TAG_SELECTED_ITEM, index);
    }

    public static int getSelectedItem(ItemStack bundle) {
        CompoundTag tag = bundle.getTag();
        if (tag == null || !tag.contains(TAG_SELECTED_ITEM)) {
            return NO_SELECTED_ITEM;
        }

        int selectedItem = tag.getInt(TAG_SELECTED_ITEM);
        ListTag items = tag.getList(TAG_ITEMS, 10);

        if (!isValidIndex(selectedItem, items.size())) {
            return NO_SELECTED_ITEM;
        }

        return selectedItem;
    }

    public static int tryInsert(ItemStack bundle, ItemStack item) {
        if (!canItemBeInBundle(item)) return 0;

        CompoundTag tag = bundle.getOrCreateTag();
        if (!tag.contains(TAG_ITEMS)) {
            tag.put(TAG_ITEMS, new ListTag());
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);
        int currentWeight = getContentWeight(bundle);
        int itemWeight = getWeight(item);
        if (itemWeight <= 0) return 0; // Defensive; should not occur after clamping

        int remainingCapacity = MAX_WEIGHT - currentWeight;
        if (remainingCapacity <= 0) return 0;

        int maxToAdd = Math.min(item.getCount(), remainingCapacity / itemWeight);
        if (maxToAdd <= 0) return 0;

        if (item.getMaxStackSize() == 1) {
            for (int i = 0; i < maxToAdd; i++) {
                ItemStack one = item.copyWithCount(1);
                CompoundTag newTag = new CompoundTag();
                one.save(newTag);
                items.add(0, newTag);
            }
            item.shrink(maxToAdd);
            return maxToAdd;
        }

        Optional<CompoundTag> matchingItem = getMatchingItem(item, items);
        if (matchingItem.isPresent()) {
            CompoundTag itemTag = matchingItem.get();
            ItemStack existingStack = ItemStack.of(itemTag);
            existingStack.grow(maxToAdd);
            CompoundTag updated = new CompoundTag();
            existingStack.save(updated);
            items.remove(itemTag);
            items.add(0, updated);
        } else {
            ItemStack newStack = item.copyWithCount(maxToAdd);
            CompoundTag newTag = new CompoundTag();
            newStack.save(newTag);
            items.add(0, newTag);
        }

        item.shrink(maxToAdd);
        return maxToAdd;
    }

    public static int tryTransfer(ItemStack bundle, Slot slot, Player player) {
        ItemStack slotStack = slot.getItem();
        if (!canItemBeInBundle(slotStack)) return 0;

        int currentWeight = getContentWeight(bundle);
        int itemWeight = getWeight(slotStack);
        if (itemWeight <= 0) return 0; // Prevent division by zero

        int remainingCapacity = MAX_WEIGHT - currentWeight;
        if (remainingCapacity <= 0) return 0;

        int maxToAdd = Math.min(slotStack.getCount(), remainingCapacity / itemWeight);
        if (maxToAdd <= 0) return 0;

        ItemStack takenStack = slot.safeTake(slotStack.getCount(), maxToAdd, player);
        if (takenStack.isEmpty()) return 0;

        return tryInsert(bundle, takenStack);
    }

    public static void toggleSelectedItem(ItemStack bundle, int index) {
        CompoundTag tag = bundle.getOrCreateTag();
        int selected0 = tag.getInt(TAG_SELECTED_ITEM);

        if (!tag.contains(TAG_ITEMS)) {
            setSelectedItem(bundle, NO_SELECTED_ITEM);
            return;
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);

        if (!isValidIndex(index, items.size())) {
            setSelectedItem(bundle, NO_SELECTED_ITEM);
            return;
        }

        boolean outsideBounds = index < 0 || index >= items.size();

        int selected = selected0 != index && !outsideBounds ? index : NO_SELECTED_ITEM;
        setSelectedItem(bundle, selected);
    }

    private static boolean isValidIndex(int index, int size) {
        return index >= 0 && index < size;
    }

    public static int getNumberOfItemsToShow(ItemStack bundle) {
        CompoundTag tag = bundle.getTag();
        if (tag != null && tag.contains(TAG_ITEMS)) {
            return getItemsToShow(tag.getList(TAG_ITEMS, 10).stream().toList());
        }

        return 0;
    }

    public static int getItemsToShow(List<?> items) {
        int contents = items.size();
        int maxDisplay = contents > 12 ? 11 : 12;
        int remainder = contents % 4;
        int padding = remainder == 0 ? 0 : 4 - remainder;
        return Math.min(contents, maxDisplay - padding);
    }

    public static ItemStack getSelectedItemStack(ItemStack bundle) {
        int selectedIndex = getSelectedItem(bundle);
        if (selectedIndex == NO_SELECTED_ITEM) {
            return ItemStack.EMPTY;
        }

        CompoundTag tag = bundle.getTag();
        if (tag == null || !tag.contains(TAG_ITEMS)) {
            return ItemStack.EMPTY;
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);
        if (!isValidIndex(selectedIndex, items.size())) {
            return ItemStack.EMPTY;
        }

        CompoundTag itemTag = items.getCompound(selectedIndex);
        return ItemStack.of(itemTag);
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
}