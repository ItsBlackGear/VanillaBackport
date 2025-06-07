package com.blackgear.vanillabackport.data.server.recipe;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.data.client.BlockFamilies;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {
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
    }

    public static ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike entry) {
        return ShapedRecipeBuilder.shaped(category, entry);
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
}