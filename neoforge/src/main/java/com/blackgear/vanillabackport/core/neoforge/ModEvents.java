package com.blackgear.vanillabackport.core.neoforge;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.level.block.ChestBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import java.util.Objects;

@EventBusSubscriber(modid = VanillaBackport.MOD_ID)
public class ModEvents {
  @SubscribeEvent
  public static void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlock(
        Capabilities.ItemHandler.BLOCK,
        (level, pos, state, blockEntity, direction) ->
            new InvWrapper(Objects.requireNonNull(ChestBlock.getContainer((ChestBlock) state.getBlock(), state, level, pos, true))),
        ModBlocks.COPPER_CHEST.get(),
        ModBlocks.WAXED_COPPER_CHEST.get(),
        ModBlocks.EXPOSED_COPPER_CHEST.get(),
        ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(),
        ModBlocks.WEATHERED_COPPER_CHEST.get(),
        ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(),
        ModBlocks.OXIDIZED_COPPER_CHEST.get(),
        ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get()
    );


    event.registerBlockEntity(
        Capabilities.ItemHandler.BLOCK,
        ModBlockEntities.SHELF.get(),
        (container, side) -> new InvWrapper(container)
    );
  }
}
