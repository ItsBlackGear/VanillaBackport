package com.blackgear.vanillabackport.data.server.recipe;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.common.registries.ModRecipeSerializers;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.data.client.BlockFamilies;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeProvider.*;

public class RecipeGenerator extends VanillaRecipeProvider {
    private static final Map<BlockFamily.Variant, FamilyStonecutterRecipeProvider> STONECUTTER_RECIPE_BUILDERS = ImmutableMap.<BlockFamily.Variant, FamilyStonecutterRecipeProvider>builder()
        .put(BlockFamily.Variant.SLAB, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 2))
        .put(BlockFamily.Variant.STAIRS, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .put(BlockFamily.Variant.WALL, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.DECORATIONS, result, base, 1))
        .put(BlockFamily.Variant.CHISELED, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .put(BlockFamily.Variant.POLISHED, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .put(BlockFamily.Variant.CUT, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .build();

    public RecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> output) {
        BlockFamilies.getAllFamilies()
            .filter(family -> family.shouldGenerateRecipe(FeatureFlagSet.of(FeatureFlags.VANILLA)))
            .forEach(family -> generateRecipes(output, family));
        planksFromLog(output, ModBlocks.PALE_OAK_PLANKS.get(), ModItemTags.PALE_OAK_LOGS, 4);
        woodFromLogs(output, ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.PALE_OAK_LOG.get());
        woodFromLogs(output, ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        woodenBoat(output, ModItems.PALE_OAK_BOAT.get(), ModBlocks.PALE_OAK_PLANKS.get());
        chestBoat(output, ModItems.PALE_OAK_CHEST_BOAT.get(), ModItems.PALE_OAK_BOAT.get());
        hangingSign(output, ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        carpet(output, ModBlocks.PALE_MOSS_CARPET.get(), ModBlocks.PALE_MOSS_BLOCK.get());

        oneToOneConversionRecipe(output, Items.ORANGE_DYE, ModBlocks.OPEN_EYEBLOSSOM.get(), "orange_dye");
        oneToOneConversionRecipe(output, Items.GRAY_DYE, ModBlocks.CLOSED_EYEBLOSSOM.get(), "gray_dye");
        oneToOneConversionRecipe(output, Items.YELLOW_DYE, ModBlocks.WILDFLOWERS.get(), "yellow_dye");

        twoByTwoPacker(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICKS.get(), ModItems.RESIN_BRICK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, ModBlocks.RESIN_CLUMP.get(), RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BLOCK.get());
        shaped(RecipeCategory.MISC, ModBlocks.CREAKING_HEART.get())
            .define('R', ModBlocks.RESIN_BLOCK.get())
            .define('L', ModBlocks.PALE_OAK_LOG.get())
            .pattern(" L ")
            .pattern(" R ")
            .pattern(" L ")
            .unlockedBy("has_resin_block", has(ModBlocks.RESIN_BLOCK.get()))
            .save(output);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.RESIN_CLUMP.get()), RecipeCategory.MISC, ModItems.RESIN_BRICK.get(), 0.1f, 200)
            .unlockedBy("has_resin_clump", has(ModBlocks.RESIN_CLUMP.get()))
            .save(output);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_SLAB.get(), ModBlocks.RESIN_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_STAIRS.get(), ModBlocks.RESIN_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_WALL.get(), ModBlocks.RESIN_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RESIN_BRICKS.get(), ModBlocks.RESIN_BRICKS.get());

        this.dryGhast(output, ModBlocks.DRIED_GHAST.get());

        this.harness(output, ModItems.WHITE_HARNESS.get(), Blocks.WHITE_WOOL);
        this.harness(output, ModItems.ORANGE_HARNESS.get(), Blocks.ORANGE_WOOL);
        this.harness(output, ModItems.MAGENTA_HARNESS.get(), Blocks.MAGENTA_WOOL);
        this.harness(output, ModItems.LIGHT_BLUE_HARNESS.get(), Blocks.LIGHT_BLUE_WOOL);
        this.harness(output, ModItems.YELLOW_HARNESS.get(), Blocks.YELLOW_WOOL);
        this.harness(output, ModItems.LIME_HARNESS.get(), Blocks.LIME_WOOL);
        this.harness(output, ModItems.PINK_HARNESS.get(), Blocks.PINK_WOOL);
        this.harness(output, ModItems.GRAY_HARNESS.get(), Blocks.GRAY_WOOL);
        this.harness(output, ModItems.LIGHT_GRAY_HARNESS.get(), Blocks.LIGHT_GRAY_WOOL);
        this.harness(output, ModItems.CYAN_HARNESS.get(), Blocks.CYAN_WOOL);
        this.harness(output, ModItems.PURPLE_HARNESS.get(), Blocks.PURPLE_WOOL);
        this.harness(output, ModItems.BLUE_HARNESS.get(), Blocks.BLUE_WOOL);
        this.harness(output, ModItems.BROWN_HARNESS.get(), Blocks.BROWN_WOOL);
        this.harness(output, ModItems.GREEN_HARNESS.get(), Blocks.GREEN_WOOL);
        this.harness(output, ModItems.RED_HARNESS.get(), Blocks.RED_WOOL);
        this.harness(output, ModItems.BLACK_HARNESS.get(), Blocks.BLACK_WOOL);

        shaped(RecipeCategory.COMBAT, Items.SADDLE)
            .define('X', Items.LEATHER)
            .define('#', Items.IRON_INGOT)
            .pattern(" X ")
            .pattern("X#X")
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(output);
        shaped(RecipeCategory.TOOLS, Items.LEAD, 2)
            .define('~', Items.STRING)
            .pattern("~~ ")
            .pattern("~~ ")
            .pattern("  ~")
            .unlockedBy("has_string", has(Items.STRING))
            .save(output);

        oneToOneConversionRecipe(output, Items.PINK_DYE, ModBlocks.CACTUS_FLOWER.get(), "pink_dye");

        shaped(RecipeCategory.DECORATIONS, Blocks.LODESTONE)
            .define('S', Items.CHISELED_STONE_BRICKS)
            .define('#', Items.IRON_INGOT)
            .pattern("SSS")
            .pattern("S#S")
            .pattern("SSS")
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .unlockedBy("has_lodestone", has(Items.LODESTONE))
            .save(output);

        shaped(RecipeCategory.TOOLS, Items.BUNDLE)
            .define('-', Items.STRING)
            .define('#', Items.LEATHER)
            .pattern("-")
            .pattern("#")
            .unlockedBy("has_string", has(Items.STRING))
            .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE)
            .requires(Blocks.PUMPKIN)
            .requires(Items.SUGAR)
            .requires(ModItemTags.EGGS)
            .unlockedBy("has_carved_pumpkin", has(Blocks.CARVED_PUMPKIN))
            .unlockedBy("has_pumpkin", has(Blocks.PUMPKIN))
            .save(output);

        shaped(RecipeCategory.FOOD, Blocks.CAKE)
            .define('A', Items.MILK_BUCKET)
            .define('B', Items.SUGAR)
            .define('C', Items.WHEAT)
            .define('E', ModItemTags.EGGS)
            .pattern("AAA")
            .pattern("BEB")
            .pattern("CCC")
            .unlockedBy("has_egg", has(ModItemTags.EGGS))
            .save(output);

        shaped(RecipeCategory.COMBAT, ModItems.WOLF_ARMOR.get())
            .define('X', ModItems.ARMADILLO_SCUTE.get())
            .pattern("X  ")
            .pattern("XXX")
            .pattern("X X")
            .unlockedBy("has_armadillo_scute", has(ModItems.ARMADILLO_SCUTE.get()))
            .save(output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemTags.LEAVES), RecipeCategory.MISC, ModBlocks.LEAF_LITTER.get(), 0.1F, 200)
            .unlockedBy("has_leaves", has(ItemTags.LEAVES))
            .save(output);

        SpecialRecipeBuilder.special(ModRecipeSerializers.BUNDLE_COLORING.get()).save(output, "bundle_coloring");
        
        // Chaos Cubed

        this.generateStonecutterRecipes(output, BlockFamilies.SULFUR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(output, BlockFamilies.POLISHED_SULFUR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(output, BlockFamilies.SULFUR_BRICKS, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(output, BlockFamilies.CINNABAR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(output, BlockFamilies.POLISHED_CINNABAR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(output, BlockFamilies.CINNABAR_BRICKS, FeatureFlagSet.of(FeatureFlags.VANILLA));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SULFUR_BRICKS.get(), 4)
            .define('S', ModBlocks.POLISHED_SULFUR.get())
            .pattern("SS")
            .pattern("SS")
            .unlockedBy("has_polished_sulfur", has(ModBlocks.POLISHED_SULFUR.get()))
            .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CINNABAR_BRICKS.get(), 4)
            .define('S', ModBlocks.POLISHED_CINNABAR.get())
            .pattern("SS")
            .pattern("SS")
            .unlockedBy("has_polished_cinnabar", has(ModBlocks.POLISHED_CINNABAR.get()))
            .save(output);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SULFUR_BRICKS.get(), ModBlocks.POLISHED_SULFUR.get(), 1);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CINNABAR_BRICKS.get(), ModBlocks.POLISHED_CINNABAR.get(), 1);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SULFUR.get(), 4)
            .define('S', ModBlocks.SULFUR_SPIKE.get())
            .pattern("SS")
            .pattern("SS")
            .unlockedBy("has_sulfur_spike", has(ModBlocks.SULFUR_SPIKE.get()))
            .save(output);
        threeByThreePacker(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POTENT_SULFUR.get(), ModBlocks.SULFUR.get());
        
        // Copper Age
        nineBlockStorageRecipesWithCustomPacking(output, RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), RecipeCategory.MISC, Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");
        
        this.shelf(output, ModBlocks.ACACIA_SHELF.get(), Items.STRIPPED_ACACIA_LOG);
        this.shelf(output, ModBlocks.BAMBOO_SHELF.get(), Items.STRIPPED_BAMBOO_BLOCK);
        this.shelf(output, ModBlocks.BIRCH_SHELF.get(), Items.STRIPPED_BIRCH_LOG);
        this.shelf(output, ModBlocks.CHERRY_SHELF.get(), Items.STRIPPED_CHERRY_LOG);
        this.shelf(output, ModBlocks.CRIMSON_SHELF.get(), Items.STRIPPED_CRIMSON_STEM);
        this.shelf(output, ModBlocks.DARK_OAK_SHELF.get(), Items.STRIPPED_DARK_OAK_LOG);
        this.shelf(output, ModBlocks.JUNGLE_SHELF.get(), Items.STRIPPED_JUNGLE_LOG);
        this.shelf(output, ModBlocks.MANGROVE_SHELF.get(), Items.STRIPPED_MANGROVE_LOG);
        this.shelf(output, ModBlocks.OAK_SHELF.get(), Items.STRIPPED_OAK_LOG);
        this.shelf(output, ModBlocks.PALE_OAK_SHELF.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        this.shelf(output, ModBlocks.SPRUCE_SHELF.get(), Items.STRIPPED_SPRUCE_LOG);
        this.shelf(output, ModBlocks.WARPED_SHELF.get(), Items.STRIPPED_WARPED_STEM);
        
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_AXE.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("XX")
            .pattern("X#")
            .pattern(" #")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_HOE.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("XX")
            .pattern(" #")
            .pattern(" #")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_PICKAXE.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("XXX")
            .pattern(" # ")
            .pattern(" # ")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_SHOVEL.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("X")
            .pattern("#")
            .pattern("#")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_SWORD.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("X")
            .pattern("X")
            .pattern("#")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("XXX")
            .pattern("X X")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("X X")
            .pattern("XXX")
            .pattern("XXX")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("XXX")
            .pattern("X X")
            .pattern("X X")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("X X")
            .pattern("X X")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_CHEST.get())
            .define('#', Items.COPPER_INGOT)
            .define('X', Items.CHEST)
            .pattern("###")
            .pattern("#X#")
            .pattern("###")
            .unlockedBy("has_copper_chest", has(ModBlocks.COPPER_CHEST.get()))
            .save(output);
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(
                    ModItems.COPPER_PICKAXE.get(),
                    ModItems.COPPER_SHOVEL.get(),
                    ModItems.COPPER_AXE.get(),
                    ModItems.COPPER_HOE.get(),
                    ModItems.COPPER_SWORD.get(),
                    ModItems.COPPER_SPEAR.get(),
                    ModItems.COPPER_HELMET.get(),
                    ModItems.COPPER_CHESTPLATE.get(),
                    ModItems.COPPER_LEGGINGS.get(),
                    ModItems.COPPER_BOOTS.get(),
                    ModItems.COPPER_HORSE_ARMOR.get(),
                    ModItems.COPPER_NAUTILUS_ARMOR.get()
                ),
                RecipeCategory.MISC,
                ModItems.COPPER_NUGGET.get(),
                0.1F,
                200
            )
            .unlockedBy("has_copper_pickaxe", has(ModItems.COPPER_PICKAXE.get()))
            .unlockedBy("has_copper_shovel", has(ModItems.COPPER_SHOVEL.get()))
            .unlockedBy("has_copper_axe", has(ModItems.COPPER_AXE.get()))
            .unlockedBy("has_copper_hoe", has(ModItems.COPPER_HOE.get()))
            .unlockedBy("has_copper_sword", has(ModItems.COPPER_SWORD.get()))
            .unlockedBy("has_copper_spear", has(ModItems.COPPER_SPEAR.get()))
            .unlockedBy("has_copper_helmet", has(ModItems.COPPER_HELMET.get()))
            .unlockedBy("has_copper_chestplate", has(ModItems.COPPER_CHESTPLATE.get()))
            .unlockedBy("has_copper_leggings", has(ModItems.COPPER_LEGGINGS.get()))
            .unlockedBy("has_copper_boots", has(ModItems.COPPER_BOOTS.get()))
            .unlockedBy("has_copper_horse_armor", has(ModItems.COPPER_HORSE_ARMOR.get()))
            .unlockedBy("has_copper_nautilus_armor", has(ModItems.COPPER_NAUTILUS_ARMOR.get()))
            .save(output, getSmeltingRecipeName(ModItems.COPPER_NUGGET.get()));
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(
                    ModItems.COPPER_PICKAXE.get(),
                    ModItems.COPPER_SHOVEL.get(),
                    ModItems.COPPER_AXE.get(),
                    ModItems.COPPER_HOE.get(),
                    ModItems.COPPER_SWORD.get(),
                    ModItems.COPPER_SPEAR.get(),
                    ModItems.COPPER_HELMET.get(),
                    ModItems.COPPER_CHESTPLATE.get(),
                    ModItems.COPPER_LEGGINGS.get(),
                    ModItems.COPPER_BOOTS.get(),
                    ModItems.COPPER_HORSE_ARMOR.get(),
                    ModItems.COPPER_NAUTILUS_ARMOR.get()
                ),
                RecipeCategory.MISC,
                ModItems.COPPER_NUGGET.get(),
                0.1F,
                100
            )
            .unlockedBy("has_copper_pickaxe", has(ModItems.COPPER_PICKAXE.get()))
            .unlockedBy("has_copper_shovel", has(ModItems.COPPER_SHOVEL.get()))
            .unlockedBy("has_copper_axe", has(ModItems.COPPER_AXE.get()))
            .unlockedBy("has_copper_hoe", has(ModItems.COPPER_HOE.get()))
            .unlockedBy("has_copper_sword", has(ModItems.COPPER_SWORD.get()))
            .unlockedBy("has_copper_spear", has(ModItems.COPPER_SPEAR.get()))
            .unlockedBy("has_copper_helmet", has(ModItems.COPPER_HELMET.get()))
            .unlockedBy("has_copper_chestplate", has(ModItems.COPPER_CHESTPLATE.get()))
            .unlockedBy("has_copper_leggings", has(ModItems.COPPER_LEGGINGS.get()))
            .unlockedBy("has_copper_boots", has(ModItems.COPPER_BOOTS.get()))
            .unlockedBy("has_copper_horse_armor", has(ModItems.COPPER_HORSE_ARMOR.get()))
            .unlockedBy("has_copper_nautilus_armor", has(ModItems.COPPER_NAUTILUS_ARMOR.get()))
            .save(output, getBlastingRecipeName(ModItems.COPPER_NUGGET.get()));
        
        shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_TORCH.getFirst().get(), 4)
            .define('X', Ingredient.of(Items.COAL, Items.CHARCOAL))
            .define('#', Items.STICK)
            .define('C', ModItems.COPPER_NUGGET.get())
            .pattern("C")
            .pattern("X")
            .pattern("#")
            .unlockedBy("has_copper_nugget", has(ModItems.COPPER_NUGGET.get()))
            .save(output);
        shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_LANTERN.unaffected().get())
            .define('#', ModBlocks.COPPER_TORCH.getFirst().get())
            .define('X', ModItems.COPPER_NUGGET.get())
            .pattern("XXX")
            .pattern("X#X")
            .pattern("XXX")
            .unlockedBy("has_copper_torch", has(ModBlocks.COPPER_TORCH.getFirst().get()))
            .save(output);
        shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_BARS.unaffected().get(), 16)
            .define('#', Items.COPPER_INGOT)
            .pattern("###")
            .pattern("###")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.DECORATIONS, ModBlocks.COPPER_CHAIN.unaffected().get())
            .define('I', Items.COPPER_INGOT)
            .define('N', ModItems.COPPER_NUGGET.get())
            .pattern("N")
            .pattern("I")
            .pattern("N")
            .unlockedBy("has_copper_nugget", has(ModItems.COPPER_NUGGET.get()))
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        
        // Mounts of Mayhem
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(
                    ModItems.GOLDEN_SPEAR.get(),
                    ModItems.GOLDEN_NAUTILUS_ARMOR.get()
                ),
                RecipeCategory.MISC,
                Items.GOLD_NUGGET,
                0.1F,
                200
            )
            .unlockedBy("has_golden_spear", has(ModItems.GOLDEN_SPEAR.get()))
            .unlockedBy("has_iron_nautilus_armor", has(ModItems.GOLDEN_NAUTILUS_ARMOR.get()))
            .save(output, getSmeltingRecipeName(Items.GOLD_NUGGET));
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(
                    ModItems.IRON_SPEAR.get(),
                    ModItems.IRON_NAUTILUS_ARMOR.get()
                ),
                RecipeCategory.MISC,
                Items.IRON_NUGGET,
                0.1F,
                200
            )
            .unlockedBy("has_iron_spear", has(ModItems.IRON_SPEAR.get()))
            .unlockedBy("has_iron_nautilus_armor", has(ModItems.IRON_NAUTILUS_ARMOR.get()))
            .save(output, getSmeltingRecipeName(Items.IRON_NUGGET));
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(
                    ModItems.GOLDEN_SPEAR.get(),
                    ModItems.GOLDEN_NAUTILUS_ARMOR.get()
                ),
                RecipeCategory.MISC,
                Items.GOLD_NUGGET,
                0.1F,
                100
            )
            .unlockedBy("has_golden_spear", has(ModItems.GOLDEN_SPEAR.get()))
            .unlockedBy("has_iron_nautilus_armor", has(ModItems.GOLDEN_NAUTILUS_ARMOR.get()))
            .save(output, getBlastingRecipeName(Items.GOLD_NUGGET));
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(
                    ModItems.IRON_SPEAR.get(),
                    ModItems.IRON_NAUTILUS_ARMOR.get()
                ),
                RecipeCategory.MISC,
                Items.IRON_NUGGET,
                0.1F,
                100
            )
            .unlockedBy("has_iron_spear", has(ModItems.IRON_SPEAR.get()))
            .unlockedBy("has_iron_nautilus_armor", has(ModItems.IRON_NAUTILUS_ARMOR.get()))
            .save(output, getBlastingRecipeName(Items.IRON_NUGGET));
        
        shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_SPEAR.get())
            .define('#', Items.STICK)
            .define('X', Items.DIAMOND)
            .pattern("  X")
            .pattern(" # ")
            .pattern("#  ")
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.GOLDEN_SPEAR.get())
            .define('#', Items.STICK)
            .define('X', Items.GOLD_INGOT)
            .pattern("  X")
            .pattern(" # ")
            .pattern("#  ")
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.IRON_SPEAR.get())
            .define('#', Items.STICK)
            .define('X', Items.IRON_INGOT)
            .pattern("  X")
            .pattern(" # ")
            .pattern("#  ")
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_SPEAR.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("  X")
            .pattern(" # ")
            .pattern("#  ")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.STONE_SPEAR.get())
            .define('#', Items.STICK)
            .define('X', ItemTags.STONE_TOOL_MATERIALS)
            .pattern("  X")
            .pattern(" # ")
            .pattern("#  ")
            .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
            .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.WOODEN_SPEAR.get())
            .define('#', Items.STICK)
            .define('X', ItemTags.PLANKS)
            .pattern("  X")
            .pattern(" # ")
            .pattern("#  ")
            .unlockedBy("has_stick", has(Items.STICK))
            .save(output);
        
        netheriteSmithing(output, ModItems.DIAMOND_SPEAR.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_SPEAR.get());
        netheriteSmithing(output, Items.DIAMOND_HORSE_ARMOR, RecipeCategory.COMBAT, ModItems.NETHERITE_HORSE_ARMOR.get());
        netheriteSmithing(output, ModItems.DIAMOND_NAUTILUS_ARMOR.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_NAUTILUS_ARMOR.get());
        
        // Miscellaneous
        List<Item> dyes = List.of(Items.BLACK_DYE, Items.BLUE_DYE, Items.BROWN_DYE, Items.CYAN_DYE, Items.GRAY_DYE, Items.GREEN_DYE, Items.LIGHT_BLUE_DYE, Items.LIGHT_GRAY_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.ORANGE_DYE, Items.PINK_DYE, Items.PURPLE_DYE, Items.RED_DYE, Items.YELLOW_DYE, Items.WHITE_DYE);
        List<Item> wool_stairs = List.of(ModBlocks.BLACK_WOOL_STAIRS.get().asItem(), ModBlocks.BLUE_WOOL_STAIRS.get().asItem(), ModBlocks.BROWN_WOOL_STAIRS.get().asItem(), ModBlocks.CYAN_WOOL_STAIRS.get().asItem(), ModBlocks.GRAY_WOOL_STAIRS.get().asItem(), ModBlocks.GREEN_WOOL_STAIRS.get().asItem(), ModBlocks.LIGHT_BLUE_WOOL_STAIRS.get().asItem(), ModBlocks.LIGHT_GRAY_WOOL_STAIRS.get().asItem(), ModBlocks.LIME_WOOL_STAIRS.get().asItem(), ModBlocks.MAGENTA_WOOL_STAIRS.get().asItem(), ModBlocks.ORANGE_WOOL_STAIRS.get().asItem(), ModBlocks.PINK_WOOL_STAIRS.get().asItem(), ModBlocks.PURPLE_WOOL_STAIRS.get().asItem(), ModBlocks.RED_WOOL_STAIRS.get().asItem(), ModBlocks.YELLOW_WOOL_STAIRS.get().asItem(), ModBlocks.WHITE_WOOL_STAIRS.get().asItem());
        List<Item> wool_slabs = List.of(ModBlocks.BLACK_WOOL_SLAB.get().asItem(), ModBlocks.BLUE_WOOL_SLAB.get().asItem(), ModBlocks.BROWN_WOOL_SLAB.get().asItem(), ModBlocks.CYAN_WOOL_SLAB.get().asItem(), ModBlocks.GRAY_WOOL_SLAB.get().asItem(), ModBlocks.GREEN_WOOL_SLAB.get().asItem(), ModBlocks.LIGHT_BLUE_WOOL_SLAB.get().asItem(), ModBlocks.LIGHT_GRAY_WOOL_SLAB.get().asItem(), ModBlocks.LIME_WOOL_SLAB.get().asItem(), ModBlocks.MAGENTA_WOOL_SLAB.get().asItem(), ModBlocks.ORANGE_WOOL_SLAB.get().asItem(), ModBlocks.PINK_WOOL_SLAB.get().asItem(), ModBlocks.PURPLE_WOOL_SLAB.get().asItem(), ModBlocks.RED_WOOL_SLAB.get().asItem(), ModBlocks.YELLOW_WOOL_SLAB.get().asItem(), ModBlocks.WHITE_WOOL_SLAB.get().asItem());
        List<Item> harness = List.of(ModItems.BLACK_HARNESS.get(), ModItems.BLUE_HARNESS.get(), ModItems.BROWN_HARNESS.get(), ModItems.CYAN_HARNESS.get(), ModItems.GRAY_HARNESS.get(), ModItems.GREEN_HARNESS.get(), ModItems.LIGHT_BLUE_HARNESS.get(), ModItems.LIGHT_GRAY_HARNESS.get(), ModItems.LIME_HARNESS.get(), ModItems.MAGENTA_HARNESS.get(), ModItems.ORANGE_HARNESS.get(), ModItems.PINK_HARNESS.get(), ModItems.PURPLE_HARNESS.get(), ModItems.RED_HARNESS.get(), ModItems.YELLOW_HARNESS.get(), ModItems.WHITE_HARNESS.get());
        List<Item> cushion = List.of(ModItems.BLACK_CUSHION.get(), ModItems.BLUE_CUSHION.get(), ModItems.BROWN_CUSHION.get(), ModItems.CYAN_CUSHION.get(), ModItems.GRAY_CUSHION.get(), ModItems.GREEN_CUSHION.get(), ModItems.LIGHT_BLUE_CUSHION.get(), ModItems.LIGHT_GRAY_CUSHION.get(), ModItems.LIME_CUSHION.get(), ModItems.MAGENTA_CUSHION.get(), ModItems.ORANGE_CUSHION.get(), ModItems.PINK_CUSHION.get(), ModItems.PURPLE_CUSHION.get(), ModItems.RED_CUSHION.get(), ModItems.YELLOW_CUSHION.get(), ModItems.WHITE_CUSHION.get());
        colorBlockWithDye(output, dyes, wool_stairs, "wool_stairs");
        colorBlockWithDye(output, dyes, wool_slabs, "wool_slabs");
        colorBlockWithDye(output, dyes, harness, "harness_dye");
        colorBlockWithDye(output, dyes, cushion, "cushion_dye");
        
        this.cushionRecipe(output, ModBlocks.WHITE_WOOL_SLAB.get().asItem(), ModItems.WHITE_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.ORANGE_WOOL_SLAB.get().asItem(), ModItems.ORANGE_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.MAGENTA_WOOL_SLAB.get().asItem(), ModItems.MAGENTA_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.LIGHT_BLUE_WOOL_SLAB.get().asItem(), ModItems.LIGHT_BLUE_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.YELLOW_WOOL_SLAB.get().asItem(), ModItems.YELLOW_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.LIME_WOOL_SLAB.get().asItem(), ModItems.LIME_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.PINK_WOOL_SLAB.get().asItem(), ModItems.PINK_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.GRAY_WOOL_SLAB.get().asItem(), ModItems.GRAY_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.LIGHT_GRAY_WOOL_SLAB.get().asItem(), ModItems.LIGHT_GRAY_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.CYAN_WOOL_SLAB.get().asItem(), ModItems.CYAN_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.PURPLE_WOOL_SLAB.get().asItem(), ModItems.PURPLE_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.BLUE_WOOL_SLAB.get().asItem(), ModItems.BLUE_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.BROWN_WOOL_SLAB.get().asItem(), ModItems.BROWN_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.GREEN_WOOL_SLAB.get().asItem(), ModItems.GREEN_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.RED_WOOL_SLAB.get().asItem(), ModItems.RED_CUSHION.get());
        this.cushionRecipe(output, ModBlocks.BLACK_WOOL_SLAB.get().asItem(), ModItems.BLACK_CUSHION.get());

        List<BlockFamily> concrete_families = List.of(BlockFamilies.BLACK_CONCRETE, BlockFamilies.BLUE_CONCRETE,
            BlockFamilies.BROWN_CONCRETE, BlockFamilies.CYAN_CONCRETE, BlockFamilies.GRAY_CONCRETE,
            BlockFamilies.GREEN_CONCRETE, BlockFamilies.LIGHT_BLUE_CONCRETE, BlockFamilies.LIGHT_GRAY_CONCRETE,
            BlockFamilies.LIME_CONCRETE, BlockFamilies.MAGENTA_CONCRETE, BlockFamilies.ORANGE_CONCRETE,
            BlockFamilies.PINK_CONCRETE, BlockFamilies.PURPLE_CONCRETE, BlockFamilies.RED_CONCRETE,
            BlockFamilies.YELLOW_CONCRETE, BlockFamilies.WHITE_CONCRETE);
        concrete_families.forEach(blockFamily -> this.generateStonecutterRecipes(output, blockFamily, FeatureFlagSet.of(FeatureFlags.VANILLA)));
    }

    public static ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike entry) {
        return ShapedRecipeBuilder.shaped(category, entry);
    }

    public static ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike entry, int amount) {
        return ShapedRecipeBuilder.shaped(category, entry, amount);
    }

    private void dryGhast(Consumer<FinishedRecipe> exporter, ItemLike ghast) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ghast)
            .define('#', Items.GHAST_TEAR)
            .define('X', Items.SOUL_SAND)
            .pattern("###")
            .pattern("#X#")
            .pattern("###")
            .group("dry_ghast")
            .unlockedBy(getHasName(Items.GHAST_TEAR), has(Items.GHAST_TEAR))
            .save(exporter);
    }

    private void harness(Consumer<FinishedRecipe> exporter, ItemLike harness, ItemLike carpet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, harness)
            .define('#', carpet)
            .define('G', Items.GLASS)
            .define('L', Items.LEATHER)
            .pattern("LLL")
            .pattern("G#G")
            .group("harness")
            .unlockedBy("has_dried_ghast", has(ModBlocks.DRIED_GHAST.get()))
            .save(exporter);
    }

    private void generateStonecutterRecipes(Consumer<FinishedRecipe> exporter, BlockFamily family, FeatureFlagSet flagSet) {
        family.getVariants().forEach((variant, result) -> {
            if (result.requiredFeatures().isSubsetOf(flagSet)) {
                Block base = family.getBaseBlock();
                this.generateStonecutterRecipe(exporter, family, variant, base);
            }
        });
    }

    private void generateStonecutterRecipe(Consumer<FinishedRecipe> exporter, BlockFamily family, BlockFamily.Variant variant, Block base) {
        FamilyStonecutterRecipeProvider recipeFunction = STONECUTTER_RECIPE_BUILDERS.get(variant);
        if (recipeFunction != null) {
            recipeFunction.create(exporter, family.get(variant), base);
        }

        if (variant == BlockFamily.Variant.POLISHED || variant == BlockFamily.Variant.CUT) {
            BlockFamily childVariantFamily = BlockFamilies.getFamily(family.get(variant));
            if (childVariantFamily != null) {
                childVariantFamily.getVariants().forEach((childVariant, r) -> this.generateStonecutterRecipe(exporter, childVariantFamily, childVariant, base));
            }
        }
    }
    
    public void shelf(Consumer<FinishedRecipe> output, ItemLike result, ItemLike strippedLogs) {
        shaped(RecipeCategory.DECORATIONS, result, 6)
            .define('#', strippedLogs)
            .pattern("###")
            .pattern("   ")
            .pattern("###")
            .group("shelf")
            .unlockedBy(getHasName(strippedLogs), has(strippedLogs))
            .save(output);
    }
    
    public void cushionRecipe(Consumer<FinishedRecipe> output, Item woolSlab, Item result) {
        shaped(RecipeCategory.DECORATIONS, result, 1)
            .define('#', woolSlab)
            .group("cushion")
            .unlockedBy(getHasName(woolSlab), has(woolSlab))
            .pattern("###")
            .save(output);
    }

    @FunctionalInterface
    private interface FamilyStonecutterRecipeProvider {
        void create(Consumer<FinishedRecipe> exporter, ItemLike result, ItemLike base);
    }

    @Override
    public String getName() {
        return "Vanilla Backport recipes";
    }
}