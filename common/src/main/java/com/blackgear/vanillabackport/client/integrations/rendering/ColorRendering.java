package com.blackgear.vanillabackport.client.integrations.rendering;

import com.blackgear.vanillabackport.client.api.modules.falling_leaves.LeafColors;
import com.blackgear.vanillabackport.client.api.modules.leaf_litter.DryFoliageColor;
import com.blackgear.vanillabackport.common.level.items.WolfArmorItem;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.GrassColor;

import static com.blackgear.platform.client.GameRendering.*;

@Environment(EnvType.CLIENT)
public class ColorRendering {
    public static void blockColors(BlockColorEvent event) {
        event.register((state, level, pos, tint) -> level != null && pos != null
                ? LeafColors.getAverageDryFoliageColor(pos)
                : DryFoliageColor.FOLIAGE_DRY_DEFAULT,
            ModBlocks.LEAF_LITTER.get());
        event.register((state, level, pos, tint) -> level != null && pos != null
                ? BiomeColors.getAverageGrassColor(level, pos)
                : GrassColor.getDefaultColor(),
            ModBlocks.BUSH.get());
        event.register((state, level, pos, tint) -> tint != 0
                ? level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.getDefaultColor()
                : -1,
            ModBlocks.WILDFLOWERS.get());
    }
    
    public static void itemColors(ItemColorEvent event) {
        event.register((stack, i) -> i != 1 ? -1 : WolfArmorItem.getColorOrDefault(stack, 0), ModItems.WOLF_ARMOR.get());
        event.register(event::getColor, ModBlocks.BUSH.get());
    }
}