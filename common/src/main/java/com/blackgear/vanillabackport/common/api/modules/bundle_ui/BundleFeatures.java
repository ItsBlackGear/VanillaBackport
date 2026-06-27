package com.blackgear.vanillabackport.common.api.modules.bundle_ui;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class BundleFeatures {
    public static final Map<DyeColor, Item> BUNDLES_BY_DYE = new EnumMap<>(DyeColor.class);

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

    public static void toggleSelectedItem(ItemStack stack, int index) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents != null) {
            BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
            ((ModernBundle.Mutable) mutable).toggleSelectedItem(index);
            stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        }
    }

    public static int getSelectedItem(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return ((ModernBundle) (Object) contents).getSelectedItem();
    }

    public static ItemStack getSelectedItemStack(ItemStack stack) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) return ItemStack.EMPTY;
        
        ModernBundle bundle = (ModernBundle) (Object) contents;
        int selectedIndex = bundle.getSelectedItem();
        
        return selectedIndex != -1 ? contents.getItemUnsafe(selectedIndex) : ItemStack.EMPTY;
    }

    public static int getNumberOfItemsToShow(ItemStack stack) {
        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return ((ModernBundle) (Object) contents).getNumberOfItemsToShow();
    }

    public static Optional<ItemStack> removeOneItemFromBundle(ItemStack stack, Player player, BundleContents contents) {
        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
        ItemStack itemStack = mutable.removeOne();
        
        if (itemStack == null) return Optional.empty();

        playRemoveOneSound(player);
        stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        return Optional.of(itemStack);
    }

    public static Item getByColor(DyeColor dyeColor) {
        return BUNDLES_BY_DYE.getOrDefault(dyeColor, Items.BUNDLE);
    }

    public static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    public static void playInsertFailSound(Entity entity) {
        entity.playSound(ModSoundEvents.BUNDLE_INSERT_FAIL.get(), 1.0F, 1.0F);
    }
}