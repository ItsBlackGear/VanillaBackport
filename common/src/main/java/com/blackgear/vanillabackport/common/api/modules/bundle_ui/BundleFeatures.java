package com.blackgear.vanillabackport.common.api.modules.bundle_ui;

import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class BundleFeatures {
    private static final String TAG_ITEMS = "Items";
    private static final String TAG_SELECTED_ITEM = "SelectedItem";
    public static final int MAX_WEIGHT = 64;
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int NO_SELECTED_ITEM = -1;

    public static final Map<DyeColor, Item> BUNDLES_BY_DYE = new HashMap<>();

    static {
        register(DyeColor.WHITE, ModItems.WHITE_BUNDLE.get());
        register(DyeColor.ORANGE, ModItems.ORANGE_BUNDLE.get());
        register(DyeColor.MAGENTA, ModItems.MAGENTA_BUNDLE.get());
        register(DyeColor.LIGHT_BLUE, ModItems.LIGHT_BLUE_BUNDLE.get());
        register(DyeColor.YELLOW, ModItems.YELLOW_BUNDLE.get());
        register(DyeColor.LIME, ModItems.LIME_BUNDLE.get());
        register(DyeColor.PINK, ModItems.PINK_BUNDLE.get());
        register(DyeColor.GRAY, ModItems.GRAY_BUNDLE.get());
        register(DyeColor.LIGHT_GRAY, ModItems.LIGHT_GRAY_BUNDLE.get());
        register(DyeColor.CYAN, ModItems.CYAN_BUNDLE.get());
        register(DyeColor.PURPLE, ModItems.PURPLE_BUNDLE.get());
        register(DyeColor.BLUE, ModItems.BLUE_BUNDLE.get());
        register(DyeColor.BROWN, ModItems.BROWN_BUNDLE.get());
        register(DyeColor.GREEN, ModItems.GREEN_BUNDLE.get());
        register(DyeColor.RED, ModItems.RED_BUNDLE.get());
        register(DyeColor.BLACK, ModItems.BLACK_BUNDLE.get());
    }

    public static void register(DyeColor dyeColor, Item item) {
        BUNDLES_BY_DYE.put(dyeColor, item);
    }

    public static boolean onBundleUpdate() {
        return VanillaBackport.COMMON_CONFIG.hasUpdatedBundles.get();
    }

    public static boolean canItemBeInBundle(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().canFitInsideContainerItems();
    }

    @Nullable
    public static ItemStack removeOne(ItemStack bundle) {
        if (!bundle.is(ModItemTags.BUNDLES)) return null;

        CompoundTag tag = bundle.getOrCreateTag();
        if (tag.contains(TAG_ITEMS)) {
            ListTag items = tag.getList(TAG_ITEMS, 10);
            if (!items.isEmpty()) {
                int selectedItem = tag.getInt(TAG_SELECTED_ITEM);
                int index = isValidIndex(selectedItem, items.size()) ? selectedItem : 0;

                if (!isValidIndex(index, items.size())) {
                    return null;
                }

                CompoundTag itemTag = items.getCompound(index);
                ItemStack removedItem = ItemStack.of(itemTag);
                items.remove(index);

                if (items.isEmpty()) {
                    bundle.removeTagKey(TAG_ITEMS);
                }

                setSelectedItem(bundle, NO_SELECTED_ITEM);
                return removedItem;
            }
        }

        return null;
    }

    public static Stream<ItemStack> getContents(ItemStack bundle) {
        CompoundTag tag = bundle.getTag();
        if (tag == null || !tag.contains(TAG_ITEMS)) {
            return Stream.empty();
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);
        return items.stream().map(nbt -> ItemStack.of((CompoundTag) nbt));
    }

    public static int getWeight(ItemStack stack) {
        if (stack.is(ModItemTags.BUNDLES)) {
            return BUNDLE_IN_BUNDLE_WEIGHT + getContentWeight(stack);
        }

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("BlockEntityTag", 10)) {
            CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("Bees", 9)) {
                ListTag bees = blockEntityTag.getList("Bees", 10);
                if (!bees.isEmpty()) return MAX_WEIGHT;
            }
        }

        int maxStackSize = stack.getMaxStackSize();
        if (maxStackSize <= 0) return MAX_WEIGHT;

        int weight = MAX_WEIGHT / maxStackSize;
        return weight == 0 ? 1 : weight;
    }

    public static int getContentWeight(ItemStack bundle) {
        CompoundTag tag = bundle.getTag();
        if (tag == null || !tag.contains(TAG_ITEMS)) return 0;

        ListTag items = tag.getList(TAG_ITEMS, 10);
        int totalWeight = 0;
        
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = ItemStack.of(items.getCompound(i));
            totalWeight += getWeight(item) * item.getCount();
        }
        return totalWeight;
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack stack, ListTag items) {
        if (!stack.isStackable()) return Optional.empty();

        for (int i = 0; i < items.size(); i++) {
            CompoundTag tag = items.getCompound(i);
            if (ItemStack.isSameItemSameTags(ItemStack.of(tag), stack)) {
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }

    public static void setSelectedItem(ItemStack bundle, int index) {
        if (!bundle.is(ModItemTags.BUNDLES)) return;

        CompoundTag tag = bundle.getOrCreateTag();
        tag.putInt(TAG_SELECTED_ITEM, index);
    }

    public static int getSelectedItem(ItemStack bundle) {
        if (!bundle.is(ModItemTags.BUNDLES)) return NO_SELECTED_ITEM;

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

    private static int getMaxAmountToAdd(ItemStack bundle, ItemStack item) {
        int itemWeight = getWeight(item);
        if (itemWeight <= 0) return 0;

        int remaining = MAX_WEIGHT - getContentWeight(bundle);
        if (remaining <= 0) return 0;

        return Math.max(remaining / itemWeight, 0);
    }

    public static int tryInsert(ItemStack bundle, ItemStack item) {
        if (!canItemBeInBundle(item) || !bundle.is(ModItemTags.BUNDLES)) return 0;

        CompoundTag tag = bundle.getOrCreateTag();
        if (!tag.contains(TAG_ITEMS)) {
            tag.put(TAG_ITEMS, new ListTag());
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);
        int maxToAdd = Math.min(item.getCount(), getMaxAmountToAdd(bundle, item));
        if (maxToAdd <= 0) return 0;

        Optional<CompoundTag> matchingItem = getMatchingItem(item, items);
        if (matchingItem.isPresent()) {
            CompoundTag itemTag = matchingItem.get();
            ItemStack existingStack = ItemStack.of(itemTag);
            existingStack.grow(maxToAdd);
            existingStack.save(itemTag);
            items.remove(itemTag);
            items.add(0, itemTag);
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
        if (!bundle.is(ModItemTags.BUNDLES)) return 0;

        ItemStack slotStack = slot.getItem();
        if (!canItemBeInBundle(slotStack)) return 0;

        int maxToAdd = Math.min(slotStack.getCount(), getMaxAmountToAdd(bundle, slotStack));
        if (maxToAdd <= 0) return 0;

        ItemStack takenStack = slot.safeTake(slotStack.getCount(), maxToAdd, player);
        return tryInsert(bundle, takenStack);
    }

    public static void toggleSelectedItem(ItemStack bundle, int index) {
        if (!bundle.is(ModItemTags.BUNDLES)) return;

        CompoundTag tag = bundle.getOrCreateTag();
        if (!tag.contains(TAG_ITEMS)) {
            setSelectedItem(bundle, NO_SELECTED_ITEM);
            return;
        }

        ListTag items = tag.getList(TAG_ITEMS, 10);
        int selected0 = tag.getInt(TAG_SELECTED_ITEM);
        int selected = (selected0 != index && isValidIndex(index, items.size())) ? index : NO_SELECTED_ITEM;
        setSelectedItem(bundle, selected);
    }

    private static boolean isValidIndex(int index, int size) {
        return index >= 0 && index < size;
    }

    public static int getNumberOfItemsToShow(ItemStack bundle) {
        CompoundTag tag = bundle.getTag();
        if (tag != null && tag.contains(TAG_ITEMS)) {
            return getItemsToShow(tag.getList(TAG_ITEMS, 10));
        }

        return 0;
    }

    public static int getItemsToShow(List<?> items) {
        int contents = items.size();
        if (VanillaBackport.CLIENT_CONFIG.endlessBundleUi.get()) {
            return contents;
        }

        int maxDisplay = contents > 12 ? 11 : 12;
        int remainder = contents % 4;
        int padding = remainder == 0 ? 0 : 4 - remainder;
        return Math.min(contents, maxDisplay - padding);
    }

    public static ItemStack getSelectedItemStack(ItemStack bundle) {
        if (!bundle.is(ModItemTags.BUNDLES)) return ItemStack.EMPTY;

        int selectedIndex = getSelectedItem(bundle);
        if (selectedIndex == NO_SELECTED_ITEM) return ItemStack.EMPTY;

        CompoundTag tag = bundle.getTag();
        if (tag == null || !tag.contains(TAG_ITEMS)) return ItemStack.EMPTY;

        ListTag items = tag.getList(TAG_ITEMS, 10);
        if (!isValidIndex(selectedIndex, items.size())) return ItemStack.EMPTY;

        return ItemStack.of(items.getCompound(selectedIndex));
    }

    public static Item getByColor(DyeColor dyeColor) {
        return BUNDLES_BY_DYE.getOrDefault(dyeColor, Items.BUNDLE);
    }
}