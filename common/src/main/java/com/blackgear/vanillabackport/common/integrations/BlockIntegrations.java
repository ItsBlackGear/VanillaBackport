package com.blackgear.vanillabackport.common.integrations;

import com.blackgear.platform.common.integration.BlockIntegration;
import com.blackgear.vanillabackport.common.integrations.dispenser.BoatDispenseBehavior;
import com.blackgear.vanillabackport.common.integrations.dispenser.SulfurCubeBucketDispenseBehavior;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.level.block.Blocks;

public class BlockIntegrations {
    private static void registerFuel(BlockIntegration.Event event) {
        // Spring to Life
        event.registerFuelItem(ModBlocks.SHORT_DRY_GRASS.get(), 100);
        event.registerFuelItem(ModBlocks.TALL_DRY_GRASS.get(), 100);
        event.registerFuelItem(ModBlocks.LEAF_LITTER.get(), 100);
    }
    
    private static void registerFlammables(BlockIntegration.Event event) {
        // The Garden Awakens
        event.registerFlammableBlock(ModBlocks.PALE_OAK_PLANKS.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_SLAB.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_FENCE_GATE.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_FENCE.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_STAIRS.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_LOG.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.STRIPPED_PALE_OAK_LOG.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_WOOD.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_LEAVES.get(), 30, 60);
        event.registerFlammableBlock(ModBlocks.PALE_MOSS_BLOCK.get(), 5, 100);
        event.registerFlammableBlock(ModBlocks.PALE_MOSS_CARPET.get(), 5, 100);
        event.registerFlammableBlock(ModBlocks.PALE_HANGING_MOSS.get(), 5, 100);
        event.registerFlammableBlock(ModBlocks.OPEN_EYEBLOSSOM.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.CLOSED_EYEBLOSSOM.get(), 60, 100);
        
        // Spring to Life
        event.registerFlammableBlock(ModBlocks.BUSH.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.FIREFLY_BUSH.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.WILDFLOWERS.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.CACTUS_FLOWER.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.SHORT_DRY_GRASS.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.TALL_DRY_GRASS.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.LEAF_LITTER.get(), 60, 100);
    }
    
    private static void registerCompostables(BlockIntegration.Event event) {
        // The Garden Awakens
        event.registerCompostableItem(ModBlocks.PALE_OAK_LEAVES.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_OAK_SAPLING.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_MOSS_CARPET.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_HANGING_MOSS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_MOSS_BLOCK.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.OPEN_EYEBLOSSOM.get(), 0.65F);
        event.registerCompostableItem(ModBlocks.CLOSED_EYEBLOSSOM.get(), 0.65F);
        
        // Spring to Life
        event.registerCompostableItem(ModBlocks.BUSH.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.LEAF_LITTER.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.FIREFLY_BUSH.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.WILDFLOWERS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.CACTUS_FLOWER.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.SHORT_DRY_GRASS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.TALL_DRY_GRASS.get(), 0.3F);
    }
    
    private static void registerStrippables(BlockIntegration.Event event) {
        // The Garden Awakens
        event.registerStrippableBlock(ModBlocks.PALE_OAK_LOG.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        event.registerStrippableBlock(ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
    }
    
    private static void registerWaxables(BlockIntegration.Event event) {
        event.registerWaxableBlock(ModBlocks.COPPER_CHEST.get(), ModBlocks.WAXED_COPPER_CHEST.get());
        event.registerWaxableBlock(ModBlocks.EXPOSED_COPPER_CHEST.get(), ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get());
        event.registerWaxableBlock(ModBlocks.WEATHERED_COPPER_CHEST.get(), ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get());
        event.registerWaxableBlock(ModBlocks.OXIDIZED_COPPER_CHEST.get(), ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get());
        
        event.registerWaxableBlock(Blocks.LIGHTNING_ROD, ModBlocks.WAXED_LIGHTNING_ROD.get());
        event.registerWaxableBlock(ModBlocks.EXPOSED_LIGHTNING_ROD.get(), ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get());
        event.registerWaxableBlock(ModBlocks.WEATHERED_LIGHTNING_ROD.get(), ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get());
        event.registerWaxableBlock(ModBlocks.OXIDIZED_LIGHTNING_ROD.get(), ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());
        
        ModBlocks.COPPER_LANTERN.waxedMapping().forEach((from, to) -> event.registerWaxableBlock(from.get(), to.get()));
        ModBlocks.COPPER_BARS.waxedMapping().forEach((from, to) -> event.registerWaxableBlock(from.get(), to.get()));
        ModBlocks.COPPER_CHAIN.waxedMapping().forEach((from, to) -> event.registerWaxableBlock(from.get(), to.get()));
    }
    
    private static void registerOxidables(BlockIntegration.Event event) {
        event.registerOxidableBlock(ModBlocks.COPPER_CHEST.get(), ModBlocks.EXPOSED_COPPER_CHEST.get());
        event.registerOxidableBlock(ModBlocks.EXPOSED_COPPER_CHEST.get(), ModBlocks.WEATHERED_COPPER_CHEST.get());
        event.registerOxidableBlock(ModBlocks.WEATHERED_COPPER_CHEST.get(), ModBlocks.OXIDIZED_COPPER_CHEST.get());
        
        event.registerOxidableBlock(Blocks.LIGHTNING_ROD, ModBlocks.EXPOSED_LIGHTNING_ROD.get());
        event.registerOxidableBlock(ModBlocks.EXPOSED_LIGHTNING_ROD.get(), ModBlocks.WEATHERED_LIGHTNING_ROD.get());
        event.registerOxidableBlock(ModBlocks.WEATHERED_LIGHTNING_ROD.get(), ModBlocks.OXIDIZED_LIGHTNING_ROD.get());
        
        ModBlocks.COPPER_LANTERN.weatheringMapping().forEach((from, to) -> event.registerOxidableBlock(from.get(), to.get()));
        ModBlocks.COPPER_BARS.weatheringMapping().forEach((from, to) -> event.registerOxidableBlock(from.get(), to.get()));
        ModBlocks.COPPER_CHAIN.weatheringMapping().forEach((from, to) -> event.registerOxidableBlock(from.get(), to.get()));
    }
    
    private static void registerDispensables(BlockIntegration.Event event) {
        // The Garden Awakens
        event.registerDispenserBehavior(ModItems.PALE_OAK_BOAT.get(), new BoatDispenseBehavior());
        event.registerDispenserBehavior(ModItems.PALE_OAK_CHEST_BOAT.get(), new BoatDispenseBehavior(true));
        
        // Spring to Life
        event.registerDispenserBehavior(ModItems.BLUE_EGG.get(), new ProjectileDispenseBehavior(ModItems.BLUE_EGG.get()));
        event.registerDispenserBehavior(ModItems.BROWN_EGG.get(), new ProjectileDispenseBehavior(ModItems.BROWN_EGG.get()));
        
        // Chaos Cubed
        event.registerDispenserBehavior(ModItems.SULFUR_CUBE_BUCKET.get(), new SulfurCubeBucketDispenseBehavior());
    }
    
    public static void bootstrap(BlockIntegration.Event event) {
        registerFuel(event);
        registerFlammables(event);
        registerCompostables(event);
        registerStrippables(event);
        registerWaxables(event);
        registerOxidables(event);
        registerDispensables(event);
    }
}