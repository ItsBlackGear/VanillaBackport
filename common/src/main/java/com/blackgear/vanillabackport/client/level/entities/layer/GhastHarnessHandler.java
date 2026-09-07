package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.level.layer.GhastEquipmentManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * added for retro-compatibility with Dye the World, highly recommended for the dev to migrate!
 */
@Environment(EnvType.CLIENT) @Deprecated(forRemoval = true)
public class GhastHarnessHandler {
    public static void register(ItemStack stack, ResourceLocation texture) {
        GhastEquipmentManager.register(stack.getItem(), texture);
    }
}