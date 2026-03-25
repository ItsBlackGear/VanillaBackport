package com.blackgear.vanillabackport.data.server.recipe;

import com.blackgear.vanillabackport.common.level.crafting.BundleColoring;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.data.client.BlockFamilies;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.RecipeProvider.*;

public class RecipeGenerator extends VanillaRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        BlockFamilies.getAllFamilies()
            .filter(BlockFamily::shouldGenerateRecipe)
            .forEach(family -> generateRecipes(output, family, FeatureFlagSet.of(FeatureFlags.VANILLA)));
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

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemTags.LEAVES), RecipeCategory.MISC, ModBlocks.LEAF_LITTER.get(), 0.1F, 200)
            .unlockedBy("has_leaves", has(ItemTags.LEAVES))
            .save(output);

        SpecialRecipeBuilder.special(BundleColoring::new).save(output, "bundle_coloring");
    }

    public static ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike entry) {
        return ShapedRecipeBuilder.shaped(category, entry);
    }

    public static ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike entry, int amount) {
        return ShapedRecipeBuilder.shaped(category, entry, amount);
    }

    private void dryGhast(RecipeOutput output, ItemLike ghast) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ghast)
            .define('#', Items.GHAST_TEAR)
            .define('X', Items.SOUL_SAND)
            .pattern("###")
            .pattern("#X#")
            .pattern("###")
            .group("dry_ghast")
            .unlockedBy(getHasName(Items.GHAST_TEAR), has(Items.GHAST_TEAR))
            .save(output);
    }

    private void harness(RecipeOutput output, ItemLike harness, ItemLike carpet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, harness)
            .define('#', carpet)
            .define('G', Items.GLASS)
            .define('L', Items.LEATHER)
            .pattern("LLL")
            .pattern("G#G")
            .group("harness")
            .unlockedBy("has_dried_ghast", has(ModBlocks.DRIED_GHAST.get()))
            .save(output);
    }

    @Override
    public String getName() {
        return "Vanilla Backport recipes";
    }
}