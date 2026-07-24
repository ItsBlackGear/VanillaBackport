package com.blackgear.vanillabackport.client.integrations;

import com.blackgear.platform.common.v2.creative_tabs.CreativeTabIntegrations.Event;
import com.blackgear.platform.common.v2.creative_tabs.VanillaTabs;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class VanillaTabIntegrations {
    public static void bootstrap(Event event) {
        registerArmoredPaws(event);
        registerBundlesOfBravery(event);
        registerTheGardenAwakens(event);
        registerSpringToLife(event);
        registerChaseTheSkies(event);
        registerHotAsLava(event);
        registerCopperAge(event);
        registerMountsOfMayhem(event);
        registerChaosCubed(event);
        registerFallDrop(event);
    }
    
    private static void registerArmoredPaws(Event event) {
        event.register(VanillaTabs.COMBAT, (flags, output, operator) -> {
            output.after(Items.DIAMOND_HORSE_ARMOR).add(ModItems.WOLF_ARMOR.get());
        });
        
        event.register(VanillaTabs.INGREDIENTS, (flags, output, operator) -> {
            output.after(Items.SCUTE).add(ModItems.ARMADILLO_SCUTE.get());
        });
        
        event.register(VanillaTabs.SPAWN_EGGS, (flags, output, operator) -> {
            output.after(Items.ALLAY_SPAWN_EGG).add(ModItems.ARMADILLO_SPAWN_EGG.get());
        });
    }
    
    private static void registerBundlesOfBravery(Event event) {
        event.register(VanillaTabs.TOOLS_AND_UTILITIES, (flags, output, operator) -> {
            output.addAfterIfAbsent(Items.LEAD, Items.BUNDLE);
            output.after(Items.BUNDLE).add(
                ModItems.WHITE_BUNDLE.get(),
                ModItems.LIGHT_GRAY_BUNDLE.get(),
                ModItems.GRAY_BUNDLE.get(),
                ModItems.BLACK_BUNDLE.get(),
                ModItems.BROWN_BUNDLE.get(),
                ModItems.RED_BUNDLE.get(),
                ModItems.ORANGE_BUNDLE.get(),
                ModItems.YELLOW_BUNDLE.get(),
                ModItems.LIME_BUNDLE.get(),
                ModItems.GREEN_BUNDLE.get(),
                ModItems.CYAN_BUNDLE.get(),
                ModItems.LIGHT_BLUE_BUNDLE.get(),
                ModItems.BLUE_BUNDLE.get(),
                ModItems.PURPLE_BUNDLE.get(),
                ModItems.MAGENTA_BUNDLE.get(),
                ModItems.PINK_BUNDLE.get());
        });
    }
    
    private static void registerTheGardenAwakens(Event event) {
        event.register(VanillaTabs.BUILDING_BLOCKS, (flags, output, operator) -> {
            output.after(Items.CHERRY_BUTTON).add(
                ModBlocks.PALE_OAK_LOG.get(),
                ModBlocks.PALE_OAK_WOOD.get(),
                ModBlocks.STRIPPED_PALE_OAK_LOG.get(),
                ModBlocks.STRIPPED_PALE_OAK_WOOD.get(),
                ModBlocks.PALE_OAK_PLANKS.get(),
                ModBlocks.PALE_OAK_STAIRS.get(),
                ModBlocks.PALE_OAK_SLAB.get(),
                ModBlocks.PALE_OAK_FENCE.get(),
                ModBlocks.PALE_OAK_FENCE_GATE.get(),
                ModBlocks.PALE_OAK_DOOR.get(),
                ModBlocks.PALE_OAK_TRAPDOOR.get(),
                ModBlocks.PALE_OAK_PRESSURE_PLATE.get(),
                ModBlocks.PALE_OAK_BUTTON.get());
            output.after(Items.MUD_BRICK_WALL).add(
                ModBlocks.RESIN_BRICKS.get(),
                ModBlocks.RESIN_BRICK_STAIRS.get(),
                ModBlocks.RESIN_BRICK_SLAB.get(),
                ModBlocks.RESIN_BRICK_WALL.get(),
                ModBlocks.CHISELED_RESIN_BRICKS.get());
        });
        
        event.register(VanillaTabs.NATURAL_BLOCKS, (flags, output, operator) -> {
            output.after(Items.MOSS_CARPET).add(
                ModBlocks.PALE_MOSS_BLOCK.get(),
                ModBlocks.PALE_MOSS_CARPET.get(),
                ModBlocks.PALE_HANGING_MOSS.get());
            output.after(Items.CHERRY_LOG).add(ModBlocks.PALE_OAK_LOG.get());
            output.after(Items.CHERRY_LEAVES).add(ModBlocks.PALE_OAK_LEAVES.get());
            output.after(Items.CHERRY_SAPLING).add(ModBlocks.PALE_OAK_SAPLING.get());
            output.after(Items.TORCHFLOWER).add(ModBlocks.CLOSED_EYEBLOSSOM.get(), ModBlocks.OPEN_EYEBLOSSOM.get());
            output.after(Items.HONEY_BLOCK).add(ModBlocks.RESIN_BLOCK.get());
        });
        
        event.register(VanillaTabs.FUNCTIONAL_BLOCKS, (flags, output, operator) -> {
            output.after(Items.CHERRY_HANGING_SIGN).add(ModBlocks.PALE_OAK_SIGN.getFirst().get(), ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get());
        });
        
        event.register(VanillaTabs.TOOLS_AND_UTILITIES, (flags, output, operator) -> {
            output.after(Items.CHERRY_CHEST_BOAT).add(ModItems.PALE_OAK_BOAT.get(), ModItems.PALE_OAK_CHEST_BOAT.get());
        });
        
        event.register(VanillaTabs.INGREDIENTS, (flags, output, operator) -> {
            output.after(Items.HONEYCOMB).add(ModBlocks.RESIN_CLUMP.get());
            output.after(Items.NETHER_BRICK).add(ModItems.RESIN_BRICK.get());
        });
        
        event.register(VanillaTabs.SPAWN_EGGS, (flags, output, operator) -> {
            output.after(Items.SPAWNER).add(ModBlocks.CREAKING_HEART.get());
            output.after(Items.COW_SPAWN_EGG).add(ModItems.CREAKING_SPAWN_EGG.get());
        });
    }
    
    private static void registerSpringToLife(Event event) {
        event.register(VanillaTabs.NATURAL_BLOCKS, (flags, output, operator) -> {
            output.after(Items.FERN).add(ModBlocks.SHORT_DRY_GRASS.get(), ModBlocks.BUSH.get());
            output.after(Items.TORCHFLOWER).add(ModBlocks.CACTUS_FLOWER.get());
            output.after(Items.PINK_PETALS).add(ModBlocks.WILDFLOWERS.get(), ModBlocks.LEAF_LITTER.get());
            output.after(Items.SPORE_BLOSSOM).add(ModBlocks.FIREFLY_BUSH.get());
            output.after(Items.LARGE_FERN).add(ModBlocks.TALL_DRY_GRASS.get());
        });
        
        event.register(VanillaTabs.COMBAT, (flags, output, operator) -> {
            output.after(Items.EGG).add(ModItems.BROWN_EGG.get(), ModItems.BLUE_EGG.get());
        });
        
        event.register(VanillaTabs.INGREDIENTS, (flags, output, operator) -> {
            output.after(Items.EGG).add(ModItems.BROWN_EGG.get(), ModItems.BLUE_EGG.get());
        });
    }
    
    private static void registerChaseTheSkies(Event event) {
        event.register(VanillaTabs.NATURAL_BLOCKS, (flags, output, operator) -> {
            output.after(Items.SNIFFER_EGG).add(ModBlocks.DRIED_GHAST.get());
        });
        
        event.register(VanillaTabs.TOOLS_AND_UTILITIES, (flags, output, operator) -> {
            output.after(Items.SADDLE).add(
                ModItems.WHITE_HARNESS.get(),
                ModItems.LIGHT_GRAY_HARNESS.get(),
                ModItems.GRAY_HARNESS.get(),
                ModItems.BLACK_HARNESS.get(),
                ModItems.BROWN_HARNESS.get(),
                ModItems.RED_HARNESS.get(),
                ModItems.ORANGE_HARNESS.get(),
                ModItems.YELLOW_HARNESS.get(),
                ModItems.LIME_HARNESS.get(),
                ModItems.GREEN_HARNESS.get(),
                ModItems.CYAN_HARNESS.get(),
                ModItems.LIGHT_BLUE_HARNESS.get(),
                ModItems.BLUE_HARNESS.get(),
                ModItems.PURPLE_HARNESS.get(),
                ModItems.MAGENTA_HARNESS.get(),
                ModItems.PINK_HARNESS.get());
            output.after(Items.MUSIC_DISC_RELIC).add(ModItems.MUSIC_DISC_TEARS.get());
        });
        
        event.register(VanillaTabs.SPAWN_EGGS, (flags, output, operator) -> {
            output.after(Items.GUARDIAN_SPAWN_EGG).add(ModItems.HAPPY_GHAST_SPAWN_EGG.get());
        });
    }
    
    private static void registerHotAsLava(Event event) {
        event.register(VanillaTabs.TOOLS_AND_UTILITIES, (flags, output, operator) -> {
            output.after(ModItems.MUSIC_DISC_TEARS.get()).add(ModItems.MUSIC_DISC_LAVA_CHICKEN.get());
        });
    }
    
    private static void registerCopperAge(Event event) {
        event.register(VanillaTabs.BUILDING_BLOCKS, (flags, output, operator) -> {
            output.after(Items.CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.unaffected().get(),
                ModBlocks.COPPER_CHAIN.unaffected().get());
            output.after(Items.WEATHERED_CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.weathered().get(),
                ModBlocks.COPPER_CHAIN.weathered().get());
            output.after(Items.EXPOSED_CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.exposed().get(),
                ModBlocks.COPPER_CHAIN.exposed().get());
            output.after(Items.OXIDIZED_CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.oxidized().get(),
                ModBlocks.COPPER_CHAIN.oxidized().get());
            output.after(Items.WAXED_CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.waxed().get(),
                ModBlocks.COPPER_CHAIN.waxed().get());
            output.after(Items.WAXED_WEATHERED_CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.waxedWeathered().get(),
                ModBlocks.COPPER_CHAIN.waxedWeathered().get());
            output.after(Items.WAXED_EXPOSED_CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.waxedExposed().get(),
                ModBlocks.COPPER_CHAIN.waxedExposed().get());
            output.after(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB).add(
                ModBlocks.COPPER_BARS.waxedOxidized().get(),
                ModBlocks.COPPER_CHAIN.waxedOxidized().get());
        });
        
        event.register(VanillaTabs.FUNCTIONAL_BLOCKS, (flags, output, operator) -> {
            output.after(Items.SOUL_TORCH).add(ModBlocks.COPPER_TORCH.getFirst().get());
            output.after(Items.SOUL_LANTERN).add(ModBlocks.COPPER_LANTERN.asList(), Supplier::get);
            output.after(Items.CHAIN).add(ModBlocks.COPPER_CHAIN.asList(), Supplier::get);
            output.after(Items.LIGHTNING_ROD).add(
                ModBlocks.WEATHERED_LIGHTNING_ROD.get(),
                ModBlocks.EXPOSED_LIGHTNING_ROD.get(),
                ModBlocks.OXIDIZED_LIGHTNING_ROD.get(),
                ModBlocks.WAXED_LIGHTNING_ROD.get(),
                ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get(),
                ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get(),
                ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());
            output.after(Items.CHISELED_BOOKSHELF).add(
                ModBlocks.OAK_SHELF.get(),
                ModBlocks.SPRUCE_SHELF.get(),
                ModBlocks.BIRCH_SHELF.get(),
                ModBlocks.JUNGLE_SHELF.get(),
                ModBlocks.ACACIA_SHELF.get(),
                ModBlocks.DARK_OAK_SHELF.get(),
                ModBlocks.MANGROVE_SHELF.get(),
                ModBlocks.CHERRY_SHELF.get(),
                ModBlocks.PALE_OAK_SHELF.get(),
                ModBlocks.BAMBOO_SHELF.get(),
                ModBlocks.CRIMSON_SHELF.get(),
                ModBlocks.WARPED_SHELF.get());
            output.after(Items.CHEST).add(
                ModBlocks.COPPER_CHEST.get(),
                ModBlocks.WEATHERED_COPPER_CHEST.get(),
                ModBlocks.EXPOSED_COPPER_CHEST.get(),
                ModBlocks.OXIDIZED_COPPER_CHEST.get(),
                ModBlocks.WAXED_COPPER_CHEST.get(),
                ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(),
                ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(),
                ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get());
            output.after(Items.ENDER_EYE).add(
                ModBlocks.COPPER_GOLEM_STATUE.get(),
                ModBlocks.WEATHERED_COPPER_GOLEM_STATUE.get(),
                ModBlocks.EXPOSED_COPPER_GOLEM_STATUE.get(),
                ModBlocks.OXIDIZED_COPPER_GOLEM_STATUE.get(),
                ModBlocks.WAXED_COPPER_GOLEM_STATUE.get(),
                ModBlocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE.get(),
                ModBlocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE.get(),
                ModBlocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE.get());
        });
        
        event.register(VanillaTabs.REDSTONE_BLOCKS, (flags, output, operator) -> {
            output.after(Items.CHISELED_BOOKSHELF).add(ModBlocks.OAK_SHELF.get());
        });
        
        event.register(VanillaTabs.TOOLS_AND_UTILITIES, (flags, output, operator) -> {
            output.after(Items.STONE_HOE)
                .add(ModItems.COPPER_SHOVEL.get())
                .add(ModItems.COPPER_PICKAXE.get())
                .add(ModItems.COPPER_AXE.get())
                .add(ModItems.COPPER_HOE.get());
        });
        
        event.register(VanillaTabs.COMBAT, (flags, output, operator) -> {
            output.after(Items.STONE_SWORD).add(ModItems.COPPER_SWORD.get());
            output.after(Items.STONE_AXE).add(ModItems.COPPER_AXE.get());
            output.after(Items.LEATHER_BOOTS).add(
                ModItems.COPPER_HELMET.get(),
                ModItems.COPPER_CHESTPLATE.get(),
                ModItems.COPPER_LEGGINGS.get(),
                ModItems.COPPER_BOOTS.get());
            output.after(Items.LEATHER_HORSE_ARMOR).add(ModItems.COPPER_HORSE_ARMOR.get());
        });
        
        event.register(VanillaTabs.INGREDIENTS, (flags, output, operator) -> {
            output.before(Items.IRON_NUGGET).add(ModItems.COPPER_NUGGET.get());
        });
        
        event.register(VanillaTabs.SPAWN_EGGS, (flags, output, operator) -> {
            output.after(Items.COD_SPAWN_EGG).add(ModItems.COPPER_GOLEM_SPAWN_EGG.get());
        });
    }
    
    private static void registerMountsOfMayhem(Event event) {
        event.register(VanillaTabs.COMBAT, (flags, output, operator) -> {
            output.before(Items.WOODEN_AXE).add(
                ModItems.WOODEN_SPEAR.get(),
                ModItems.STONE_SPEAR.get(),
                ModItems.COPPER_SPEAR.get(),
                ModItems.IRON_SPEAR.get(),
                ModItems.GOLDEN_SPEAR.get(),
                ModItems.DIAMOND_SPEAR.get(),
                ModItems.NETHERITE_SPEAR.get());
            output.after(Items.DIAMOND_HORSE_ARMOR).add(ModItems.NETHERITE_HORSE_ARMOR.get());
            output.after(ModItems.WOLF_ARMOR.get()).add(
                ModItems.COPPER_NAUTILUS_ARMOR.get(),
                ModItems.IRON_NAUTILUS_ARMOR.get(),
                ModItems.GOLDEN_NAUTILUS_ARMOR.get(),
                ModItems.DIAMOND_NAUTILUS_ARMOR.get(),
                ModItems.NETHERITE_NAUTILUS_ARMOR.get());
        });
        
        event.register(VanillaTabs.SPAWN_EGGS, (flags, output, operator) -> {
            output.after(Items.CAMEL_SPAWN_EGG).add(ModItems.CAMEL_HUSK_SPAWN_EGG.get());
            output.after(Items.MULE_SPAWN_EGG).add(ModItems.NAUTILUS_SPAWN_EGG.get());
            output.after(Items.PANDA_SPAWN_EGG).add(ModItems.PARCHED_SPAWN_EGG.get());
            output.after(Items.ZOMBIE_HORSE_SPAWN_EGG).add(ModItems.ZOMBIE_NAUTILUS_SPAWN_EGG.get());
        });
    }
    
    private static void registerChaosCubed(Event event) {
        event.register(VanillaTabs.BUILDING_BLOCKS, (flags, output, operator) -> {
            output.after(Items.CUT_RED_SANDSTONE_SLAB).add(
                ModBlocks.CINNABAR.get(),
                ModBlocks.CINNABAR_STAIRS.get(),
                ModBlocks.CINNABAR_SLAB.get(),
                ModBlocks.CINNABAR_WALL.get(),
                ModBlocks.CHISELED_CINNABAR.get(),
                ModBlocks.POLISHED_CINNABAR.get(),
                ModBlocks.POLISHED_CINNABAR_STAIRS.get(),
                ModBlocks.POLISHED_CINNABAR_SLAB.get(),
                ModBlocks.POLISHED_CINNABAR_WALL.get(),
                ModBlocks.CINNABAR_BRICKS.get(),
                ModBlocks.CINNABAR_BRICK_STAIRS.get(),
                ModBlocks.CINNABAR_BRICK_SLAB.get(),
                ModBlocks.CINNABAR_BRICK_WALL.get())
                .add(
                ModBlocks.SULFUR.get(),
                ModBlocks.SULFUR_STAIRS.get(),
                ModBlocks.SULFUR_SLAB.get(),
                ModBlocks.SULFUR_WALL.get(),
                ModBlocks.CHISELED_SULFUR.get(),
                ModBlocks.POLISHED_SULFUR.get(),
                ModBlocks.POLISHED_SULFUR_STAIRS.get(),
                ModBlocks.POLISHED_SULFUR_SLAB.get(),
                ModBlocks.POLISHED_SULFUR_WALL.get(),
                ModBlocks.SULFUR_BRICKS.get(),
                ModBlocks.SULFUR_BRICK_STAIRS.get(),
                ModBlocks.SULFUR_BRICK_SLAB.get(),
                ModBlocks.SULFUR_BRICK_WALL.get());
        });
        
        event.register(VanillaTabs.NATURAL_BLOCKS, (flags, output, operator) -> {
            output.after(Items.PRISMARINE)
                .add(ModBlocks.CINNABAR.get())
                .add(ModBlocks.SULFUR.get())
                .add(ModBlocks.SULFUR_SPIKE.get())
                .add(ModBlocks.POTENT_SULFUR.get());
        });
        
        event.register(VanillaTabs.TOOLS_AND_UTILITIES, (flags, output, operator) -> {
            output.after(Items.TADPOLE_BUCKET).add(ModItems.SULFUR_CUBE_BUCKET.get());
            output.after(ModItems.MUSIC_DISC_LAVA_CHICKEN.get()).add(ModItems.MUSIC_DISC_BOUNCE.get());
        });
        
        event.register(VanillaTabs.SPAWN_EGGS, (flags, output, operator) -> {
            output.after(Items.SNIFFER_SPAWN_EGG).add(ModItems.SULFUR_CUBE_SPAWN_EGG.get());
        });
    }
    
    private static void registerFallDrop(Event event) {
        event.register(VanillaTabs.COLORED_BLOCKS, (flags, output, operator) -> {
            output.before(Items.WHITE_CARPET).add(
                ModBlocks.WHITE_WOOL_STAIRS.get(),
                ModBlocks.LIGHT_GRAY_WOOL_STAIRS.get(),
                ModBlocks.GRAY_WOOL_STAIRS.get(),
                ModBlocks.BLACK_WOOL_STAIRS.get(),
                ModBlocks.BROWN_WOOL_STAIRS.get(),
                ModBlocks.RED_WOOL_STAIRS.get(),
                ModBlocks.ORANGE_WOOL_STAIRS.get(),
                ModBlocks.YELLOW_WOOL_STAIRS.get(),
                ModBlocks.LIME_WOOL_STAIRS.get(),
                ModBlocks.GREEN_WOOL_STAIRS.get(),
                ModBlocks.CYAN_WOOL_STAIRS.get(),
                ModBlocks.LIGHT_BLUE_WOOL_STAIRS.get(),
                ModBlocks.BLUE_WOOL_STAIRS.get(),
                ModBlocks.PURPLE_WOOL_STAIRS.get(),
                ModBlocks.MAGENTA_WOOL_STAIRS.get(),
                ModBlocks.PINK_WOOL_STAIRS.get())
                .add(
                ModBlocks.WHITE_WOOL_SLAB.get(),
                ModBlocks.LIGHT_GRAY_WOOL_SLAB.get(),
                ModBlocks.GRAY_WOOL_SLAB.get(),
                ModBlocks.BLACK_WOOL_SLAB.get(),
                ModBlocks.BROWN_WOOL_SLAB.get(),
                ModBlocks.RED_WOOL_SLAB.get(),
                ModBlocks.ORANGE_WOOL_SLAB.get(),
                ModBlocks.YELLOW_WOOL_SLAB.get(),
                ModBlocks.LIME_WOOL_SLAB.get(),
                ModBlocks.GREEN_WOOL_SLAB.get(),
                ModBlocks.CYAN_WOOL_SLAB.get(),
                ModBlocks.LIGHT_BLUE_WOOL_SLAB.get(),
                ModBlocks.BLUE_WOOL_SLAB.get(),
                ModBlocks.PURPLE_WOOL_SLAB.get(),
                ModBlocks.MAGENTA_WOOL_SLAB.get(),
                ModBlocks.PINK_WOOL_SLAB.get());
            
            output.before(Items.CANDLE).add(
                ModItems.WHITE_CUSHION.get(),
                ModItems.LIGHT_GRAY_CUSHION.get(),
                ModItems.GRAY_CUSHION.get(),
                ModItems.BLACK_CUSHION.get(),
                ModItems.BROWN_CUSHION.get(),
                ModItems.RED_CUSHION.get(),
                ModItems.ORANGE_CUSHION.get(),
                ModItems.YELLOW_CUSHION.get(),
                ModItems.LIME_CUSHION.get(),
                ModItems.GREEN_CUSHION.get(),
                ModItems.CYAN_CUSHION.get(),
                ModItems.LIGHT_BLUE_CUSHION.get(),
                ModItems.BLUE_CUSHION.get(),
                ModItems.PURPLE_CUSHION.get(),
                ModItems.MAGENTA_CUSHION.get(),
                ModItems.PINK_CUSHION.get());
        });
    }
}