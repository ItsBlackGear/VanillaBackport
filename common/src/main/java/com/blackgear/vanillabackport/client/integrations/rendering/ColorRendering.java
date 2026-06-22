package com.blackgear.vanillabackport.client.integrations.rendering;

import com.blackgear.vanillabackport.client.api.modules.leaf_litter.DryFoliageColor;
import com.blackgear.vanillabackport.client.api.modules.falling_leaves.LeafColors;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.GrassColor;

import static com.blackgear.platform.client.GameRendering.*;

@Environment(EnvType.CLIENT)
public class ColorRendering {
    public static void blockColors(BlockColorEvent event) {
        event.register(
            (state, level, pos, tint) -> level != null && pos != null
                ? LeafColors.getAverageDryFoliageColor(pos)
                : DryFoliageColor.FOLIAGE_DRY_DEFAULT,
            ModBlocks.LEAF_LITTER.get()
        );
        event.register(
            (state, level, pos, tint) -> level != null && pos != null
                ? BiomeColors.getAverageGrassColor(level, pos)
                : GrassColor.getDefaultColor(),
            ModBlocks.BUSH.get()
        );
        event.register((state, level, pos, tint) -> {
                if (tint != 0) {
                    return level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.getDefaultColor();
                } else {
                    return -1;
                }
            },
            ModBlocks.WILDFLOWERS.get()
        );
    }
    
    public static void itemColors(ItemColorEvent event) {
        event.register(event::getColor, ModBlocks.BUSH.get());
    }
}