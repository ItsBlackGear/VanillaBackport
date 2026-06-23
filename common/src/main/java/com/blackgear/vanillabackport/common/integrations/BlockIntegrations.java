package com.blackgear.vanillabackport.common.integrations;

import com.blackgear.platform.common.integration.BlockIntegration.Event;
import com.blackgear.vanillabackport.common.integrations.dispenser.ArmadilloBrushDispenseBehavior;
import com.blackgear.vanillabackport.common.integrations.dispenser.BoatDispenseBehavior;
import com.blackgear.vanillabackport.common.integrations.dispenser.EggVariantProjectileDispenseBehavior;
import com.blackgear.vanillabackport.common.integrations.dispenser.SulfurCubeBucketDispenseBehavior;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.world.item.Items;

public class BlockIntegrations {
    private static void registerFuel(Event event) {
        // Spring to Life
        event.registerFuelItem(ModBlocks.SHORT_DRY_GRASS.get(), 100);
        event.registerFuelItem(ModBlocks.TALL_DRY_GRASS.get(), 100);
        event.registerFuelItem(ModBlocks.LEAF_LITTER.get(), 100);
    }
    
    private static void registerFlammables(Event event) {
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
    
    private static void registerCompostables(Event event) {
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
    
    private static void registerStrippables(Event event) {
        // The Garden Awakens
        event.registerStrippableBlock(ModBlocks.PALE_OAK_LOG.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        event.registerStrippableBlock(ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
    }
    
    private static void registerDispensables(Event event) {
        // Armored Paws
        event.registerDispenserBehavior(Items.BRUSH, new ArmadilloBrushDispenseBehavior());
        
        // The Garden Awakens
        event.registerDispenserBehavior(ModItems.PALE_OAK_BOAT.get(), new BoatDispenseBehavior());
        event.registerDispenserBehavior(ModItems.PALE_OAK_CHEST_BOAT.get(), new BoatDispenseBehavior(true));
        
        // Spring to Life
        event.registerDispenserBehavior(ModItems.BLUE_EGG.get(), new EggVariantProjectileDispenseBehavior(ChickenVariants.COLD));
        event.registerDispenserBehavior(ModItems.BROWN_EGG.get(), new EggVariantProjectileDispenseBehavior(ChickenVariants.WARM));
        
        // Chaos Cubed
        event.registerDispenserBehavior(ModItems.SULFUR_CUBE_BUCKET.get(), new SulfurCubeBucketDispenseBehavior());
    }
    
    public static void bootstrap(Event event) {
        registerFuel(event);
        registerFlammables(event);
        registerCompostables(event);
        registerStrippables(event);
        registerDispensables(event);
    }
}