package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.data.tags.create.CreateItemTags;
import com.blackgear.vanillabackport.core.data.tags.loader.ConventionalItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        new BlockItemTagGenerator() {
            @Override
            protected TagHolder tag(TagKey<Block> block, TagKey<Item> item) {
            return new TagHolder(ItemTagGenerator.this.getOrCreateTagBuilder(item), null);
            }
        }.addTags();
        this.handleConventionalTags();
        this.handleArchetypes();

        this.getOrCreateTagBuilder(ItemTags.BOATS)
            .add(ModItems.PALE_OAK_BOAT.get());

        this.getOrCreateTagBuilder(ItemTags.CHEST_BOATS)
            .add(ModItems.PALE_OAK_CHEST_BOAT.get());

        this.getOrCreateTagBuilder(ModItemTags.BUNDLES)
            .add(
                Items.BUNDLE,
                ModItems.BLACK_BUNDLE.get(),
                ModItems.BLUE_BUNDLE.get(),
                ModItems.BROWN_BUNDLE.get(),
                ModItems.CYAN_BUNDLE.get(),
                ModItems.GRAY_BUNDLE.get(),
                ModItems.GREEN_BUNDLE.get(),
                ModItems.LIGHT_BLUE_BUNDLE.get(),
                ModItems.LIGHT_GRAY_BUNDLE.get(),
                ModItems.LIME_BUNDLE.get(),
                ModItems.MAGENTA_BUNDLE.get(),
                ModItems.ORANGE_BUNDLE.get(),
                ModItems.PINK_BUNDLE.get(),
                ModItems.PURPLE_BUNDLE.get(),
                ModItems.RED_BUNDLE.get(),
                ModItems.YELLOW_BUNDLE.get(),
                ModItems.WHITE_BUNDLE.get()
            );

        this.getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
            .add(ModItems.RESIN_BRICK.get());

        this.getOrCreateTagBuilder(ModItemTags.HARNESSES)
            .add(
                ModItems.WHITE_HARNESS.get(),
                ModItems.ORANGE_HARNESS.get(),
                ModItems.MAGENTA_HARNESS.get(),
                ModItems.LIGHT_BLUE_HARNESS.get(),
                ModItems.YELLOW_HARNESS.get(),
                ModItems.LIME_HARNESS.get(),
                ModItems.PINK_HARNESS.get(),
                ModItems.GRAY_HARNESS.get(),
                ModItems.LIGHT_GRAY_HARNESS.get(),
                ModItems.CYAN_HARNESS.get(),
                ModItems.PURPLE_HARNESS.get(),
                ModItems.BLUE_HARNESS.get(),
                ModItems.BROWN_HARNESS.get(),
                ModItems.GREEN_HARNESS.get(),
                ModItems.RED_HARNESS.get(),
                ModItems.BLACK_HARNESS.get()
            )
            .addOptional(ResourceLocation.fromNamespaceAndPath("vanillabackport", "harnesses"));

        this.getOrCreateTagBuilder(ModItemTags.HAPPY_GHAST_FOOD)
            .add(Items.SNOWBALL);

        this.getOrCreateTagBuilder(ModItemTags.HAPPY_GHAST_TEMPT_ITEMS)
            .addTag(ModItemTags.HAPPY_GHAST_FOOD)
            .addTag(ModItemTags.HARNESSES);

        this.getOrCreateTagBuilder(ModItemTags.EGGS)
            .add(Items.EGG, ModItems.BLUE_EGG.get(), ModItems.BROWN_EGG.get());
        
        this.getOrCreateTagBuilder(ItemTags.SWORDS)
            .add(ModItems.COPPER_SWORD.get());
        
        this.getOrCreateTagBuilder(ItemTags.AXES)
            .add(ModItems.COPPER_AXE.get());
        
        this.getOrCreateTagBuilder(ItemTags.PICKAXES)
            .add(ModItems.COPPER_PICKAXE.get());
        
        this.getOrCreateTagBuilder(ItemTags.SHOVELS)
            .add(ModItems.COPPER_SHOVEL.get());
        
        this.getOrCreateTagBuilder(ItemTags.HOES)
            .add(ModItems.COPPER_HOE.get());
        
        this.getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
            .add(ModItems.COPPER_HELMET.get());
        
        this.getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
            .add(ModItems.COPPER_CHESTPLATE.get());
        
        this.getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
            .add(ModItems.COPPER_LEGGINGS.get());
        
        this.getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
            .add(ModItems.COPPER_BOOTS.get());
        
        this.getOrCreateTagBuilder(ModItemTags.SHEARABLE_FROM_COPPER_GOLEM)
            .add(Items.POPPY);
        
        this.getOrCreateTagBuilder(ModItemTags.SPEARS)
            .add(
                ModItems.WOODEN_SPEAR.get(),
                ModItems.STONE_SPEAR.get(),
                ModItems.COPPER_SPEAR.get(),
                ModItems.IRON_SPEAR.get(),
                ModItems.GOLDEN_SPEAR.get(),
                ModItems.DIAMOND_SPEAR.get(),
                ModItems.NETHERITE_SPEAR.get()
            );
        
        this.getOrCreateTagBuilder(ModItemTags.CAMEL_HUSK_FOOD)
            .add(Items.RABBIT_FOOT);
        
        this.getOrCreateTagBuilder(ModItemTags.ZOMBIE_HORSE_FOOD)
            .add(Items.RED_MUSHROOM);
        
        this.getOrCreateTagBuilder(ModItemTags.CUSHIONS)
            .add(
                ModItems.BLACK_CUSHION.get(),
                ModItems.BLUE_CUSHION.get(),
                ModItems.BROWN_CUSHION.get(),
                ModItems.CYAN_CUSHION.get(),
                ModItems.GRAY_CUSHION.get(),
                ModItems.GREEN_CUSHION.get(),
                ModItems.LIGHT_BLUE_CUSHION.get(),
                ModItems.LIGHT_GRAY_CUSHION.get(),
                ModItems.LIME_CUSHION.get(),
                ModItems.MAGENTA_CUSHION.get(),
                ModItems.ORANGE_CUSHION.get(),
                ModItems.PINK_CUSHION.get(),
                ModItems.PURPLE_CUSHION.get(),
                ModItems.RED_CUSHION.get(),
                ModItems.YELLOW_CUSHION.get(),
                ModItems.WHITE_CUSHION.get()
            );
        
        this.getOrCreateTagBuilder(ModItemTags.NAUTILUS_TAMING_ITEMS)
            .add(Items.PUFFERFISH_BUCKET, Items.PUFFERFISH);
        
        this.getOrCreateTagBuilder(ModItemTags.NAUTILUS_BUCKET_FOOD)
            .add(Items.PUFFERFISH_BUCKET, Items.COD_BUCKET, Items.SALMON_BUCKET, Items.TROPICAL_FISH_BUCKET);
        
        this.getOrCreateTagBuilder(ModItemTags.NAUTILUS_FOOD)
            .forceAddTag(ItemTags.FISHES)
            .addTag(ModItemTags.NAUTILUS_BUCKET_FOOD);
    }

    private void handleArchetypes() {
        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_FOOD)
            .add(Items.SLIME_BALL);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
            .forceAddTag(ItemTags.PLANKS)
            .forceAddTag(ItemTags.LOGS)
            .forceAddTag(ItemTags.BAMBOO_BLOCKS)
            .add(Items.BAMBOO_MOSAIC);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
            .add(Items.AMETHYST_BLOCK, Items.ANDESITE, Items.BASALT, Items.BLACKSTONE, Items.BRICKS, Items.CALCITE)
            // Chiseled Blocks
            .add(
                ModBlocks.CHISELED_CINNABAR.get().asItem(),
                Items.CHISELED_DEEPSLATE,
                Items.CHISELED_NETHER_BRICKS,
                Items.CHISELED_POLISHED_BLACKSTONE,
                Items.CHISELED_QUARTZ_BLOCK,
                Items.CHISELED_RED_SANDSTONE,
                Items.CHISELED_SANDSTONE,
                Items.CHISELED_STONE_BRICKS,
                ModBlocks.CHISELED_SULFUR.get().asItem(),
                Items.CHISELED_TUFF,
                Items.CHISELED_TUFF_BRICKS
            )
            .add(ModBlocks.CINNABAR.get().asItem(), ModBlocks.CINNABAR_BRICKS.get().asItem(), Items.COBBLED_DEEPSLATE, Items.COBBLESTONE)
            // Cracked Blocks
            .add(
                Items.CRACKED_DEEPSLATE_BRICKS,
                Items.CRACKED_DEEPSLATE_TILES,
                Items.CRACKED_NETHER_BRICKS,
                Items.CRACKED_POLISHED_BLACKSTONE_BRICKS,
                Items.CRACKED_STONE_BRICKS
            )
            .add(Items.CRIMSON_NYLIUM, Items.CRYING_OBSIDIAN, Items.CUT_RED_SANDSTONE, Items.CUT_SANDSTONE, Items.DARK_PRISMARINE)
            .add(Items.DEEPSLATE, Items.DEEPSLATE_BRICKS, Items.DEEPSLATE_TILES)
            .add(
                Items.DIAMOND_BLOCK,
                Items.DIORITE,
                Items.DRIPSTONE_BLOCK,
                Items.EMERALD_BLOCK,
                Items.END_STONE,
                Items.END_STONE_BRICKS,
                Items.GILDED_BLACKSTONE,
                Items.GLOWSTONE,
                Items.GRANITE,
                Items.LAPIS_BLOCK
            )
            .add(
                Items.MOSSY_COBBLESTONE,
                Items.MOSSY_STONE_BRICKS,
                Items.MUD_BRICKS,
                Items.NETHER_BRICKS,
                Items.NETHERRACK,
                Items.OBSERVER,
                Items.OBSIDIAN
            )
            .add(
                Items.POLISHED_ANDESITE,
                Items.POLISHED_BASALT,
                Items.POLISHED_BLACKSTONE,
                Items.POLISHED_BLACKSTONE_BRICKS,
                ModBlocks.POLISHED_CINNABAR.get().asItem(),
                Items.POLISHED_DEEPSLATE,
                Items.POLISHED_DIORITE,
                Items.POLISHED_GRANITE,
                ModBlocks.POLISHED_SULFUR.get().asItem(),
                Items.POLISHED_TUFF
            )
            .add(
                Items.PRISMARINE,
                Items.PRISMARINE_BRICKS,
                Items.PURPUR_BLOCK,
                Items.PURPUR_PILLAR,
                Items.QUARTZ_BLOCK,
                Items.QUARTZ_BRICKS,
                Items.NETHER_QUARTZ_ORE,
                Items.QUARTZ_PILLAR
            )
            .add(Items.RED_NETHER_BRICKS, Items.RED_SANDSTONE, Items.REDSTONE_LAMP, Items.SANDSTONE, Items.SEA_LANTERN)
            .add(Items.SMOOTH_BASALT, Items.SMOOTH_QUARTZ, Items.SMOOTH_RED_SANDSTONE, Items.SMOOTH_SANDSTONE, Items.SMOOTH_STONE)
            .add(
                Items.STONE,
                Items.STONE_BRICKS,
                ModBlocks.SULFUR.get().asItem(),
                ModBlocks.SULFUR_BRICKS.get().asItem(),
                Items.TUFF,
                Items.TUFF_BRICKS,
                Items.WARPED_NYLIUM
            )
            .forceAddTag(ConventionalItemTags.CONCRETE)
            .forceAddTag(ItemTags.COAL_ORES)
            .forceAddTag(ItemTags.LAPIS_ORES)
            .forceAddTag(ItemTags.REDSTONE_ORES)
            .forceAddTag(ItemTags.DIAMOND_ORES)
            .forceAddTag(ItemTags.EMERALD_ORES)
            .forceAddTag(ItemTags.TERRACOTTA)
            .forceAddTag(ConventionalItemTags.GLAZED_TERRACOTTA);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT)
            // Coral Blocks
            .add(Items.TUBE_CORAL_BLOCK, Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK)
            // Dead Coral Blocks
            .add(Items.DEAD_TUBE_CORAL_BLOCK, Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK)
            // Sponge / Kelp
            .add(Items.SPONGE, Items.WET_SPONGE, Items.DRIED_KELP_BLOCK)
            // Moss
            .add(Items.MOSS_BLOCK, ModBlocks.PALE_MOSS_BLOCK.get().asItem())
            // Resin
            .add(ModBlocks.RESIN_BLOCK.get().asItem(), ModBlocks.RESIN_BRICKS.get().asItem(), ModBlocks.CHISELED_RESIN_BRICKS.get().asItem())
            // Farm Blocks
            .add(Items.MELON, Items.HAY_BLOCK, Items.PUMPKIN, Items.CARVED_PUMPKIN, Items.JACK_O_LANTERN)
            // Froglights
            .add(Items.OCHRE_FROGLIGHT, Items.PEARLESCENT_FROGLIGHT, Items.VERDANT_FROGLIGHT);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING)
            // Frosty Blocks
            .add(Items.BLUE_ICE, Items.PACKED_ICE, Items.SNOW_BLOCK);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE)
            // Soul Blocks
            .add(Items.SOUL_SAND, Items.SOUL_SOIL);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT)
            .forceAddTag(ItemTags.WOOL);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
            // Compacted Metal Blocks
            .add(Items.IRON_BLOCK, Items.GOLD_BLOCK, Items.RAW_COPPER_BLOCK, Items.RAW_GOLD_BLOCK, Items.RAW_IRON_BLOCK)
            // Netherite
            .add(Items.NETHERITE_BLOCK, Items.ANCIENT_DEBRIS)
            // Copper Blocks
            .add(Items.COPPER_BLOCK, Items.EXPOSED_COPPER, Items.WEATHERED_COPPER, Items.OXIDIZED_COPPER, Items.WAXED_COPPER_BLOCK, Items.WAXED_EXPOSED_COPPER, Items.WAXED_WEATHERED_COPPER, Items.WAXED_OXIDIZED_COPPER)
//            .add(Items.COPPER_BULB)
            // Cut Copper Blocks
            .add(Items.CUT_COPPER, Items.EXPOSED_CUT_COPPER, Items.WEATHERED_CUT_COPPER, Items.OXIDIZED_CUT_COPPER, Items.WAXED_CUT_COPPER, Items.WAXED_EXPOSED_CUT_COPPER, Items.WAXED_WEATHERED_CUT_COPPER, Items.WAXED_OXIDIZED_CUT_COPPER)
//            .add(Items.CHISELED_COPPER);
            // Metal Ores
            .forceAddTag(ItemTags.GOLD_ORES)
            .forceAddTag(ItemTags.IRON_ORES)
            .forceAddTag(ItemTags.COPPER_ORES);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING)
            // Fun Guys
            .add(Items.BROWN_MUSHROOM_BLOCK, Items.RED_MUSHROOM_BLOCK, Items.MUSHROOM_STEM, Items.MYCELIUM)
            .add(Items.SHROOMLIGHT)
            .forceAddTag(ItemTags.WART_BLOCKS);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_STICKY)
            .add(Items.HONEYCOMB_BLOCK);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE)
            .add(Items.TNT);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_HOT)
            .add(Items.MAGMA_BLOCK);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
            .forceAddTag(ConventionalItemTags.CONCRETE_POWDERS)
            .add(Items.MUD, Items.MUDDY_MANGROVE_ROOTS, Items.PACKED_MUD)
            .add(Items.COAL_BLOCK)
            .add(Items.DIRT, Items.COARSE_DIRT, Items.ROOTED_DIRT, Items.PODZOL, Items.GRASS_BLOCK, Items.CLAY)
            .add(Items.BONE_BLOCK);

        this.getOrCreateTagBuilder(ModItemTags.SULFUR_CUBE_SWALLOWABLE)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_STICKY)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE)
            .forceAddTag(ModItemTags.SULFUR_CUBE_ARCHETYPE_HOT);
    }

    private void handleConventionalTags() {
        this.getOrCreateTagBuilder(ConventionalItemTags.EGGS)
            .addTag(ModItemTags.EGGS);

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_BLACK)
            .add(ModItems.BLACK_BUNDLE.get(), ModItems.BLACK_HARNESS.get(), ModItems.BLACK_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_BLUE)
            .add(ModItems.BLUE_BUNDLE.get(), ModItems.BLUE_HARNESS.get(), ModItems.BLUE_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_BROWN)
            .add(ModItems.BROWN_BUNDLE.get(), ModItems.BROWN_HARNESS.get(), ModItems.BROWN_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_CYAN)
            .add(ModItems.CYAN_BUNDLE.get(), ModItems.CYAN_HARNESS.get(), ModItems.CYAN_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_GRAY)
            .add(ModItems.GRAY_BUNDLE.get(), ModItems.GRAY_HARNESS.get(), ModItems.GRAY_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_GREEN)
            .add(ModItems.GREEN_BUNDLE.get(), ModItems.GREEN_HARNESS.get(), ModItems.GRAY_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_LIGHT_BLUE)
            .add(ModItems.LIGHT_BLUE_BUNDLE.get(), ModItems.LIGHT_BLUE_HARNESS.get(), ModItems.LIGHT_BLUE_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_LIGHT_GRAY)
            .add(ModItems.LIGHT_GRAY_BUNDLE.get(), ModItems.LIGHT_GRAY_HARNESS.get(), ModItems.LIGHT_GRAY_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_LIME)
            .add(ModItems.LIME_BUNDLE.get(), ModItems.LIME_HARNESS.get(), ModItems.LIME_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_MAGENTA)
            .add(ModItems.MAGENTA_BUNDLE.get(), ModItems.MAGENTA_HARNESS.get(), ModItems.MAGENTA_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_ORANGE)
            .add(ModItems.ORANGE_BUNDLE.get(), ModItems.ORANGE_HARNESS.get(), ModItems.ORANGE_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_PINK)
            .add(ModItems.PINK_BUNDLE.get(), ModItems.PINK_HARNESS.get(), ModItems.PINK_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_PURPLE)
            .add(ModItems.PURPLE_BUNDLE.get(), ModItems.PURPLE_HARNESS.get(), ModItems.PURPLE_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_RED)
            .add(ModItems.RED_BUNDLE.get(), ModItems.RED_HARNESS.get(), ModItems.RED_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_YELLOW)
            .add(ModItems.YELLOW_BUNDLE.get(), ModItems.YELLOW_HARNESS.get(), ModItems.YELLOW_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.DYED_WHITE)
            .add(ModItems.WHITE_BUNDLE.get(), ModItems.WHITE_HARNESS.get(), ModItems.WHITE_CUSHION.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.NUGGETS_COPPER)
            .add(ModItems.COPPER_NUGGET.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.MUSIC_DISCS)
            .add(ModItems.MUSIC_DISC_TEARS.get(), ModItems.MUSIC_DISC_LAVA_CHICKEN.get(), ModItems.MUSIC_DISC_BOUNCE.get());

        this.getOrCreateTagBuilder(ConventionalItemTags.GLAZED_TERRACOTTA)
            .add(
                Items.BLACK_GLAZED_TERRACOTTA,
                Items.BLUE_GLAZED_TERRACOTTA,
                Items.BROWN_GLAZED_TERRACOTTA,
                Items.CYAN_GLAZED_TERRACOTTA,
                Items.GRAY_GLAZED_TERRACOTTA,
                Items.GREEN_GLAZED_TERRACOTTA,
                Items.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Items.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Items.LIME_GLAZED_TERRACOTTA,
                Items.MAGENTA_GLAZED_TERRACOTTA,
                Items.ORANGE_GLAZED_TERRACOTTA,
                Items.PINK_GLAZED_TERRACOTTA,
                Items.PURPLE_GLAZED_TERRACOTTA,
                Items.RED_GLAZED_TERRACOTTA,
                Items.YELLOW_GLAZED_TERRACOTTA,
                Items.WHITE_GLAZED_TERRACOTTA
            );

        this.getOrCreateTagBuilder(ConventionalItemTags.CONCRETE)
            .add(
                Items.BLACK_CONCRETE,
                Items.BLUE_CONCRETE,
                Items.BROWN_CONCRETE,
                Items.CYAN_CONCRETE,
                Items.GRAY_CONCRETE,
                Items.GREEN_CONCRETE,
                Items.LIGHT_BLUE_CONCRETE,
                Items.LIGHT_GRAY_CONCRETE,
                Items.LIME_CONCRETE,
                Items.MAGENTA_CONCRETE,
                Items.ORANGE_CONCRETE,
                Items.PINK_CONCRETE,
                Items.PURPLE_CONCRETE,
                Items.RED_CONCRETE,
                Items.YELLOW_CONCRETE,
                Items.WHITE_CONCRETE
            );

        this.getOrCreateTagBuilder(ConventionalItemTags.CONCRETE_POWDERS)
            .add(
                Items.BLACK_CONCRETE_POWDER,
                Items.BLUE_CONCRETE_POWDER,
                Items.BROWN_CONCRETE_POWDER,
                Items.CYAN_CONCRETE_POWDER,
                Items.GRAY_CONCRETE_POWDER,
                Items.GREEN_CONCRETE_POWDER,
                Items.LIGHT_BLUE_CONCRETE_POWDER,
                Items.LIGHT_GRAY_CONCRETE_POWDER,
                Items.LIME_CONCRETE_POWDER,
                Items.MAGENTA_CONCRETE_POWDER,
                Items.ORANGE_CONCRETE_POWDER,
                Items.PINK_CONCRETE_POWDER,
                Items.PURPLE_CONCRETE_POWDER,
                Items.RED_CONCRETE_POWDER,
                Items.YELLOW_CONCRETE_POWDER,
                Items.WHITE_CONCRETE_POWDER
            );

        this.getOrCreateTagBuilder(CreateItemTags.MODDED_STRIPPED_WOOD)
            .add(ModBlocks.STRIPPED_PALE_OAK_WOOD.get().asItem());

        this.getOrCreateTagBuilder(CreateItemTags.MODDED_STRIPPED_LOGS)
            .add(ModBlocks.STRIPPED_PALE_OAK_LOG.get().asItem());
        
        this.getOrCreateTagBuilder(ModItemTags.SHEARABLE_FROM_COPPER_GOLEM)
            .add(Items.POPPY);
        
        this.getOrCreateTagBuilder(ConventionalItemTags.HORSE_ARMOR)
            .add(ModItems.COPPER_HORSE_ARMOR.get())
            .add(ModItems.NETHERITE_HORSE_ARMOR.get());
        
        this.getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
            .addTag(ModItemTags.SPEARS);
        
        this.getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE)
            .addTag(ModItemTags.SPEARS);
        
        this.getOrCreateTagBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE)
            .addTag(ModItemTags.SPEARS);
        
        this.getOrCreateTagBuilder(ItemTags.WEAPON_ENCHANTABLE)
            .addTag(ModItemTags.SPEARS);
        
        this.getOrCreateTagBuilder(ModItemTags.LUNGE_ENCHANTABLE)
            .addTag(ModItemTags.SPEARS);
    }

    protected DualTagHolder getDualTagBuilder(TagKey<Item> forge, TagKey<Item> fabric) {
        return new DualTagHolder(this.getOrCreateTagBuilder(fabric), this.getOrCreateTagBuilder(forge));
    }

    protected record DualTagHolder(FabricTagProvider<Item>.FabricTagBuilder forge, FabricTagProvider<Item>.FabricTagBuilder fabric) {
        public DualTagHolder add(ItemLike entry) {
            this.forge.add(entry.asItem());
            this.fabric.add(entry.asItem());
            return this;
        }

        public DualTagHolder add(Item... toAdd) {
            this.forge.add(toAdd);
            this.fabric.add(toAdd);
            return this;
        }

        public DualTagHolder addOptional(ResourceLocation location) {
            this.forge.addOptional(location);
            this.fabric.addOptional(location);
            return this;
        }

        public DualTagHolder addTag(TagKey<Item> tag) {
            this.forge.addTag(tag);
            this.fabric.addTag(tag);
            return this;
        }

        public DualTagHolder addOptionalTag(TagKey<Item> tag) {
            this.forge.addOptionalTag(tag);
            this.fabric.addOptionalTag(tag);
            return this;
        }
    }
}