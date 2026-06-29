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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        BlockFamilies.getAllFamilies()
            .filter(family -> family.shouldGenerateRecipe(FeatureFlagSet.of(FeatureFlags.VANILLA)))
            .forEach(family -> generateRecipes(exporter, family));
        planksFromLog(exporter, ModBlocks.PALE_OAK_PLANKS.get(), ModItemTags.PALE_OAK_LOGS, 4);
        woodFromLogs(exporter, ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.PALE_OAK_LOG.get());
        woodFromLogs(exporter, ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        woodenBoat(exporter, ModItems.PALE_OAK_BOAT.get(), ModBlocks.PALE_OAK_PLANKS.get());
        chestBoat(exporter, ModItems.PALE_OAK_CHEST_BOAT.get(), ModItems.PALE_OAK_BOAT.get());
        hangingSign(exporter, ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        carpet(exporter, ModBlocks.PALE_MOSS_CARPET.get(), ModBlocks.PALE_MOSS_BLOCK.get());

        oneToOneConversionRecipe(exporter, Items.ORANGE_DYE, ModBlocks.OPEN_EYEBLOSSOM.get(), "orange_dye");
        oneToOneConversionRecipe(exporter, Items.GRAY_DYE, ModBlocks.CLOSED_EYEBLOSSOM.get(), "gray_dye");
        oneToOneConversionRecipe(exporter, Items.YELLOW_DYE, ModBlocks.WILDFLOWERS.get(), "yellow_dye");

        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICKS.get(), ModItems.RESIN_BRICK.get());
        nineBlockStorageRecipes(exporter, RecipeCategory.MISC, ModBlocks.RESIN_CLUMP.get(), RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BLOCK.get());
        shaped(RecipeCategory.MISC, ModBlocks.CREAKING_HEART.get())
            .define('R', ModBlocks.RESIN_BLOCK.get())
            .define('L', ModBlocks.PALE_OAK_LOG.get())
            .pattern(" L ")
            .pattern(" R ")
            .pattern(" L ")
            .unlockedBy("has_resin_block", has(ModBlocks.RESIN_BLOCK.get()))
            .save(exporter);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.RESIN_CLUMP.get()), RecipeCategory.MISC, ModItems.RESIN_BRICK.get(), 0.1f, 200)
            .unlockedBy("has_resin_clump", has(ModBlocks.RESIN_CLUMP.get()))
            .save(exporter);
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_SLAB.get(), ModBlocks.RESIN_BRICKS.get(), 2);
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_STAIRS.get(), ModBlocks.RESIN_BRICKS.get());
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESIN_BRICK_WALL.get(), ModBlocks.RESIN_BRICKS.get());
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RESIN_BRICKS.get(), ModBlocks.RESIN_BRICKS.get());

        this.dryGhast(exporter, ModBlocks.DRIED_GHAST.get());

        this.harness(exporter, ModItems.WHITE_HARNESS.get(), Blocks.WHITE_WOOL);
        this.harness(exporter, ModItems.ORANGE_HARNESS.get(), Blocks.ORANGE_WOOL);
        this.harness(exporter, ModItems.MAGENTA_HARNESS.get(), Blocks.MAGENTA_WOOL);
        this.harness(exporter, ModItems.LIGHT_BLUE_HARNESS.get(), Blocks.LIGHT_BLUE_WOOL);
        this.harness(exporter, ModItems.YELLOW_HARNESS.get(), Blocks.YELLOW_WOOL);
        this.harness(exporter, ModItems.LIME_HARNESS.get(), Blocks.LIME_WOOL);
        this.harness(exporter, ModItems.PINK_HARNESS.get(), Blocks.PINK_WOOL);
        this.harness(exporter, ModItems.GRAY_HARNESS.get(), Blocks.GRAY_WOOL);
        this.harness(exporter, ModItems.LIGHT_GRAY_HARNESS.get(), Blocks.LIGHT_GRAY_WOOL);
        this.harness(exporter, ModItems.CYAN_HARNESS.get(), Blocks.CYAN_WOOL);
        this.harness(exporter, ModItems.PURPLE_HARNESS.get(), Blocks.PURPLE_WOOL);
        this.harness(exporter, ModItems.BLUE_HARNESS.get(), Blocks.BLUE_WOOL);
        this.harness(exporter, ModItems.BROWN_HARNESS.get(), Blocks.BROWN_WOOL);
        this.harness(exporter, ModItems.GREEN_HARNESS.get(), Blocks.GREEN_WOOL);
        this.harness(exporter, ModItems.RED_HARNESS.get(), Blocks.RED_WOOL);
        this.harness(exporter, ModItems.BLACK_HARNESS.get(), Blocks.BLACK_WOOL);

        shaped(RecipeCategory.COMBAT, Items.SADDLE)
            .define('X', Items.LEATHER)
            .define('#', Items.IRON_INGOT)
            .pattern(" X ")
            .pattern("X#X")
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(exporter);
        shaped(RecipeCategory.TOOLS, Items.LEAD, 2)
            .define('~', Items.STRING)
            .pattern("~~ ")
            .pattern("~~ ")
            .pattern("  ~")
            .unlockedBy("has_string", has(Items.STRING))
            .save(exporter);

        oneToOneConversionRecipe(exporter, Items.PINK_DYE, ModBlocks.CACTUS_FLOWER.get(), "pink_dye");

        shaped(RecipeCategory.DECORATIONS, Blocks.LODESTONE)
            .define('S', Items.CHISELED_STONE_BRICKS)
            .define('#', Items.IRON_INGOT)
            .pattern("SSS")
            .pattern("S#S")
            .pattern("SSS")
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .unlockedBy("has_lodestone", has(Items.LODESTONE))
            .save(exporter);

        shaped(RecipeCategory.TOOLS, Items.BUNDLE)
            .define('-', Items.STRING)
            .define('#', Items.LEATHER)
            .pattern("-")
            .pattern("#")
            .unlockedBy("has_string", has(Items.STRING))
            .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE)
            .requires(Blocks.PUMPKIN)
            .requires(Items.SUGAR)
            .requires(ModItemTags.EGGS)
            .unlockedBy("has_carved_pumpkin", has(Blocks.CARVED_PUMPKIN))
            .unlockedBy("has_pumpkin", has(Blocks.PUMPKIN))
            .save(exporter);

        shaped(RecipeCategory.FOOD, Blocks.CAKE)
            .define('A', Items.MILK_BUCKET)
            .define('B', Items.SUGAR)
            .define('C', Items.WHEAT)
            .define('E', ModItemTags.EGGS)
            .pattern("AAA")
            .pattern("BEB")
            .pattern("CCC")
            .unlockedBy("has_egg", has(ModItemTags.EGGS))
            .save(exporter);

        shaped(RecipeCategory.COMBAT, ModItems.WOLF_ARMOR.get())
            .define('X', ModItems.ARMADILLO_SCUTE.get())
            .pattern("X  ")
            .pattern("XXX")
            .pattern("X X")
            .unlockedBy("has_armadillo_scute", has(ModItems.ARMADILLO_SCUTE.get()))
            .save(exporter);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemTags.LEAVES), RecipeCategory.MISC, ModBlocks.LEAF_LITTER.get(), 0.1F, 200)
            .unlockedBy("has_leaves", has(ItemTags.LEAVES))
            .save(exporter);

        SpecialRecipeBuilder.special(ModRecipeSerializers.BUNDLE_COLORING.get()).save(exporter, "bundle_coloring");
        
        // Chaos Cubed

        this.generateStonecutterRecipes(exporter, BlockFamilies.SULFUR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(exporter, BlockFamilies.POLISHED_SULFUR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(exporter, BlockFamilies.SULFUR_BRICKS, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(exporter, BlockFamilies.CINNABAR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(exporter, BlockFamilies.POLISHED_CINNABAR, FeatureFlagSet.of(FeatureFlags.VANILLA));
        this.generateStonecutterRecipes(exporter, BlockFamilies.CINNABAR_BRICKS, FeatureFlagSet.of(FeatureFlags.VANILLA));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SULFUR_BRICKS.get(), 4)
            .define('S', ModBlocks.POLISHED_SULFUR.get())
            .pattern("SS")
            .pattern("SS")
            .unlockedBy("has_polished_sulfur", has(ModBlocks.POLISHED_SULFUR.get()))
            .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CINNABAR_BRICKS.get(), 4)
            .define('S', ModBlocks.POLISHED_CINNABAR.get())
            .pattern("SS")
            .pattern("SS")
            .unlockedBy("has_polished_cinnabar", has(ModBlocks.POLISHED_CINNABAR.get()))
            .save(exporter);
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SULFUR_BRICKS.get(), ModBlocks.POLISHED_SULFUR.get(), 1);
        stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CINNABAR_BRICKS.get(), ModBlocks.POLISHED_CINNABAR.get(), 1);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SULFUR.get(), 4)
            .define('S', ModBlocks.SULFUR_SPIKE.get())
            .pattern("SS")
            .pattern("SS")
            .unlockedBy("has_sulfur_spike", has(ModBlocks.SULFUR_SPIKE.get()))
            .save(exporter);
        threeByThreePacker(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POTENT_SULFUR.get(), ModBlocks.SULFUR.get());
        
        // Copper Age
        
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_AXE.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("XX")
            .pattern("X#")
            .pattern(" #")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_HOE.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("XX")
            .pattern(" #")
            .pattern(" #")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_PICKAXE.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("XXX")
            .pattern(" # ")
            .pattern(" # ")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.TOOLS, ModItems.COPPER_SHOVEL.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("X")
            .pattern("#")
            .pattern("#")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_SWORD.get())
            .define('#', Items.STICK)
            .define('X', Items.COPPER_INGOT)
            .pattern("X")
            .pattern("X")
            .pattern("#")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("XXX")
            .pattern("X X")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("X X")
            .pattern("XXX")
            .pattern("XXX")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("XXX")
            .pattern("X X")
            .pattern("X X")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS.get())
            .define('X', Items.COPPER_INGOT)
            .pattern("X X")
            .pattern("X X")
            .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
            .save(exporter);
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(
                    ModItems.COPPER_PICKAXE.get(),
                    ModItems.COPPER_SHOVEL.get(),
                    ModItems.COPPER_AXE.get(),
                    ModItems.COPPER_HOE.get(),
                    ModItems.COPPER_SWORD.get(),
//                    ModItems.COPPER_SPEAR.get(),
                    ModItems.COPPER_HELMET.get(),
                    ModItems.COPPER_CHESTPLATE.get(),
                    ModItems.COPPER_LEGGINGS.get(),
                    ModItems.COPPER_BOOTS.get(),
                    ModItems.COPPER_HORSE_ARMOR.get()
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
            .unlockedBy("has_copper_helmet", has(ModItems.COPPER_HELMET.get()))
            .unlockedBy("has_copper_chestplate", has(ModItems.COPPER_CHESTPLATE.get()))
            .unlockedBy("has_copper_leggings", has(ModItems.COPPER_LEGGINGS.get()))
            .unlockedBy("has_copper_boots", has(ModItems.COPPER_BOOTS.get()))
            .unlockedBy("has_copper_horse_armor", has(ModItems.COPPER_HORSE_ARMOR.get()))
            //.unlockedBy("has_copper_nautilus_armor", has(ModItems.COPPER_NAUTILUS_ARMOR.get()))
            .save(exporter, getSmeltingRecipeName(ModItems.COPPER_NUGGET.get()));
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(
                    ModItems.COPPER_PICKAXE.get(),
                    ModItems.COPPER_SHOVEL.get(),
                    ModItems.COPPER_AXE.get(),
                    ModItems.COPPER_HOE.get(),
                    ModItems.COPPER_SWORD.get(),
//                    ModItems.COPPER_SPEAR.get(),
                    ModItems.COPPER_HELMET.get(),
                    ModItems.COPPER_CHESTPLATE.get(),
                    ModItems.COPPER_LEGGINGS.get(),
                    ModItems.COPPER_BOOTS.get(),
                    ModItems.COPPER_HORSE_ARMOR.get()
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
            .unlockedBy("has_copper_helmet", has(ModItems.COPPER_HELMET.get()))
            .unlockedBy("has_copper_chestplate", has(ModItems.COPPER_CHESTPLATE.get()))
            .unlockedBy("has_copper_leggings", has(ModItems.COPPER_LEGGINGS.get()))
            .unlockedBy("has_copper_boots", has(ModItems.COPPER_BOOTS.get()))
            .unlockedBy("has_copper_horse_armor", has(ModItems.COPPER_HORSE_ARMOR.get()))
            //.unlockedBy("has_copper_nautilus_armor", has(ModItems.COPPER_NAUTILUS_ARMOR.get()))
            .save(exporter, getBlastingRecipeName(ModItems.COPPER_NUGGET.get()));
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

    @FunctionalInterface
    private interface FamilyStonecutterRecipeProvider {
        void create(Consumer<FinishedRecipe> exporter, ItemLike result, ItemLike base);
    }

    @Override
    public String getName() {
        return "Vanilla Backport recipes";
    }
}