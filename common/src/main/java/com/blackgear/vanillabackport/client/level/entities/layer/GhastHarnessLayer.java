package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.common.registries.ModItems;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

// TODO: remove
// the only reason why this still exists is to prevent "Dye the World" from crashing
@Environment(EnvType.CLIENT)
public class GhastHarnessLayer {
    public static final Map<ItemStack, ResourceLocation> TEXTURE_BY_ITEM = new ImmutableMap.Builder<ItemStack, ResourceLocation>()
        .put(new ItemStack(ModItems.WHITE_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/white_harness.png"))
        .put(new ItemStack(ModItems.ORANGE_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/orange_harness.png"))
        .put(new ItemStack(ModItems.MAGENTA_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/magenta_harness.png"))
        .put(new ItemStack(ModItems.LIGHT_BLUE_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/light_blue_harness.png"))
        .put(new ItemStack(ModItems.YELLOW_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/yellow_harness.png"))
        .put(new ItemStack(ModItems.LIME_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/lime_harness.png"))
        .put(new ItemStack(ModItems.PINK_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/pink_harness.png"))
        .put(new ItemStack(ModItems.GRAY_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/gray_harness.png"))
        .put(new ItemStack(ModItems.LIGHT_GRAY_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/light_gray_harness.png"))
        .put(new ItemStack(ModItems.CYAN_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/cyan_harness.png"))
        .put(new ItemStack(ModItems.PURPLE_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/purple_harness.png"))
        .put(new ItemStack(ModItems.BLUE_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/blue_harness.png"))
        .put(new ItemStack(ModItems.BROWN_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/brown_harness.png"))
        .put(new ItemStack(ModItems.GREEN_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/green_harness.png"))
        .put(new ItemStack(ModItems.RED_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/red_harness.png"))
        .put(new ItemStack(ModItems.BLACK_HARNESS.get()), new ResourceLocation("textures/entity/ghast/harness/black_harness.png"))
        .build();
}