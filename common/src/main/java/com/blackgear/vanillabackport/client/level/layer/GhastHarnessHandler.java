package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;

//TODO: make this datadriven... maybe?
@Environment(EnvType.CLIENT)
public class GhastHarnessHandler {
    public static final Map<ItemLike, ResourceLocation> HARNESS_EQUIPMENT = new HashMap<>();
    
    public static void register(ItemLike stack, ResourceLocation texture) {
        HARNESS_EQUIPMENT.put(stack, texture);
    }

    static {
        register(ModItems.WHITE_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/white_harness.png"));
        register(ModItems.ORANGE_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/orange_harness.png"));
        register(ModItems.MAGENTA_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/magenta_harness.png"));
        register(ModItems.LIGHT_BLUE_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/light_blue_harness.png"));
        register(ModItems.YELLOW_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/yellow_harness.png"));
        register(ModItems.LIME_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/lime_harness.png"));
        register(ModItems.PINK_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/pink_harness.png"));
        register(ModItems.GRAY_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/gray_harness.png"));
        register(ModItems.LIGHT_GRAY_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/light_gray_harness.png"));
        register(ModItems.CYAN_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/cyan_harness.png"));
        register(ModItems.PURPLE_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/purple_harness.png"));
        register(ModItems.BLUE_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/blue_harness.png"));
        register(ModItems.BROWN_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/brown_harness.png"));
        register(ModItems.GREEN_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/green_harness.png"));
        register(ModItems.RED_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/red_harness.png"));
        register(ModItems.BLACK_HARNESS.get(), ResourceLocation.withDefaultNamespace("textures/entity/ghast/harness/black_harness.png"));
    }
}