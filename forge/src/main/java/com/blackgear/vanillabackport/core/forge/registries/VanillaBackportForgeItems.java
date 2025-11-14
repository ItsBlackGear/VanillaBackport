package com.blackgear.vanillabackport.core.forge.registries;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// Simple forge class to update decorated pot stacking feature. - Echo2craft.
public class VanillaBackportForgeItems {
    protected static final DeferredRegister<Item> VANILLA_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VanillaBackport.NAMESPACE);
    private static void registerVanillaChanges(){
        // Override existing decorated pot item.
        VANILLA_ITEMS.register(Items.DECORATED_POT.toString(), () -> new BlockItem(Blocks.DECORATED_POT, new Item.Properties()));
    }
    // Register it to event bus.
    public static void register(IEventBus pBus) {
        registerVanillaChanges();
        VANILLA_ITEMS.register(pBus);
    }
}
