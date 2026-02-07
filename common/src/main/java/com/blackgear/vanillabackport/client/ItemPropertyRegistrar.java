package com.blackgear.vanillabackport.client;

import com.blackgear.platform.common.item.ItemPropertyRegistry;
import com.blackgear.vanillabackport.common.registries.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;

public class ItemPropertyRegistrar {
    public static void bootstrap() {
        ItemPropertyRegistry.register(ModItems.BLACK_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.WHITE_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.GRAY_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.LIGHT_GRAY_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.LIGHT_BLUE_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.BLUE_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.CYAN_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.YELLOW_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.RED_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.PURPLE_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.MAGENTA_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.PINK_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.GREEN_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.LIME_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.BROWN_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
        ItemPropertyRegistry.register(ModItems.ORANGE_BUNDLE.get(), ResourceLocation.withDefaultNamespace("filled"), ItemPropertyRegistrar::bundleDisplay);
    }

    private static float bundleDisplay(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return BundleItem.getFullnessDisplay(stack);
    }
}