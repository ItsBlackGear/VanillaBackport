package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.vanillabackport.common.level.entities.boat.BoatRegistry;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ModBoatTypes {
    public static final ResourceLocation PALE_OAK = register("pale_oak", () -> ModItems.PALE_OAK_BOAT.get(), () -> ModItems.PALE_OAK_CHEST_BOAT.get(), () -> ModBlocks.PALE_OAK_PLANKS.get());
    public static final ResourceLocation POPLAR = register("poplar", () -> ModItems.POPLAR_BOAT.get(), () -> ModItems.POPLAR_CHEST_BOAT.get(), () -> ModBlocks.POPLAR_PLANKS.get());
    
    public static ResourceLocation register(String name, Supplier<Item> boat, Supplier<Item> chestBoat, Supplier<Block> planks) {
        ResourceLocation location = ResourceLocation.withDefaultNamespace(name);
        BoatRegistry.register(location, boat, chestBoat, planks, false);
        return location;
    }
    
    public static void bootstrap() {}
}