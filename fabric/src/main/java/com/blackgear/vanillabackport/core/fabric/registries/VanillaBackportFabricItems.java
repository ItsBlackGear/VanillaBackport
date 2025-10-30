package com.blackgear.vanillabackport.core.fabric.registries;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class VanillaBackportFabricItems {
    private static Item overrideBlockItem(BlockItem toOverride, BlockItem newItem){
        return Registry.registerMapping(BuiltInRegistries.ITEM, BuiltInRegistries.ITEM.getId(toOverride), BuiltInRegistries.ITEM.getKey(toOverride).getPath(), newItem);
    }

    public static void registerVanillaChanges(){
        // It works, but unstable. - Echo2craft.
        overrideBlockItem((BlockItem) Items.DECORATED_POT, new BlockItem(Blocks.DECORATED_POT, new FabricItemSettings()));
    }
}
