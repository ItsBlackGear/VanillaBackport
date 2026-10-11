package com.blackgear.vanillabackport.common.level.recipes;

import com.blackgear.platform.common.v2.recipe.DynamicRecipeProvider;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.BlockFamilies;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.google.common.collect.ImmutableMap;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
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
import java.util.function.Function;

public class VanillaDynamicRecipes extends DynamicRecipeProvider {
    private static final Map<BlockFamily.Variant, FamilyStonecutterRecipeProvider> STONECUTTER_RECIPE_BUILDERS = ImmutableMap.<BlockFamily.Variant, FamilyStonecutterRecipeProvider>builder()
        .put(BlockFamily.Variant.SLAB, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 2))
        .put(BlockFamily.Variant.STAIRS, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .put(BlockFamily.Variant.WALL, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.DECORATIONS, result, base, 1))
        .put(BlockFamily.Variant.CHISELED, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .put(BlockFamily.Variant.POLISHED, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .put(BlockFamily.Variant.CUT, (exporter, result, base) -> stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, result, base, 1))
        .build();
    
    @Override
    public void buildRecipes(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path) {
        buildTrickyTrialsRecipes(output);
        buildBundlesOfBraveryRecipes(output, path);
        buildSpringToLifeRecipes(output, path);
        buildChaseTheSkiesRecipes(output, path);
        buildCopperAgeRecipes(output);
        buildMountsOfMayhemRecipes(output, path);
        buildWildernessBoundRecipes(output, path);
    }
    
    private static void buildTrickyTrialsRecipes(Consumer<FinishedRecipe> output) {
        if (VanillaBackport.COMMON_CONFIG.hasTuffBlocks.get()) {
            generateRecipes(output, BlockFamilies.TUFF);
            generateRecipes(output, BlockFamilies.POLISHED_TUFF);
            generateRecipes(output, BlockFamilies.TUFF_BRICKS);
            generateStonecutterRecipes(output, BlockFamilies.TUFF, FeatureFlagSet.of(FeatureFlags.VANILLA));
            generateStonecutterRecipes(output, BlockFamilies.POLISHED_TUFF, FeatureFlagSet.of(FeatureFlags.VANILLA));
            generateStonecutterRecipes(output, BlockFamilies.TUFF_BRICKS, FeatureFlagSet.of(FeatureFlags.VANILLA));
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TUFF_BRICKS.get(), 4)
                .define('S', ModBlocks.POLISHED_TUFF.get())
                .pattern("SS")
                .pattern("SS")
                .unlockedBy("has_polished_tuff", has(ModBlocks.POLISHED_TUFF.get()))
                .save(output);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.TUFF_BRICKS.get(), ModBlocks.POLISHED_TUFF.get(), 1);
        }
        
        if (VanillaBackport.COMMON_CONFIG.hasCopperToolSet.get()) {
            copperBulb(output, ModBlocks.COPPER_BULB.get(), Blocks.COPPER_BLOCK);
            copperBulb(output, ModBlocks.EXPOSED_COPPER_BULB.get(), Blocks.EXPOSED_COPPER);
            copperBulb(output, ModBlocks.WEATHERED_COPPER_BULB.get(), Blocks.WEATHERED_COPPER);
            copperBulb(output, ModBlocks.OXIDIZED_COPPER_BULB.get(), Blocks.OXIDIZED_COPPER);
            copperBulb(output, ModBlocks.WAXED_COPPER_BULB.get(), Blocks.WAXED_COPPER_BLOCK);
            copperBulb(output, ModBlocks.WAXED_EXPOSED_COPPER_BULB.get(), Blocks.WAXED_EXPOSED_COPPER);
            copperBulb(output, ModBlocks.WAXED_WEATHERED_COPPER_BULB.get(), Blocks.WAXED_WEATHERED_COPPER);
            copperBulb(output, ModBlocks.WAXED_OXIDIZED_COPPER_BULB.get(), Blocks.WAXED_OXIDIZED_COPPER);
            
            waxRecipes(output, ModBlocks.COPPER_BULB.get(), ModBlocks.WAXED_COPPER_BULB.get());
            waxRecipes(output, ModBlocks.EXPOSED_COPPER_BULB.get(), ModBlocks.WAXED_EXPOSED_COPPER_BULB.get());
            waxRecipes(output, ModBlocks.WEATHERED_COPPER_BULB.get(), ModBlocks.WAXED_WEATHERED_COPPER_BULB.get());
            waxRecipes(output, ModBlocks.OXIDIZED_COPPER_BULB.get(), ModBlocks.WAXED_OXIDIZED_COPPER_BULB.get());
            
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_COPPER.get(), Blocks.CUT_COPPER_SLAB);
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_CHISELED_COPPER.get(), Blocks.WEATHERED_CUT_COPPER_SLAB);
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXPOSED_CHISELED_COPPER.get(), Blocks.EXPOSED_CUT_COPPER_SLAB);
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.OXIDIZED_CHISELED_COPPER.get(), Blocks.OXIDIZED_CUT_COPPER_SLAB);
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_CHISELED_COPPER.get(), Blocks.WAXED_CUT_COPPER_SLAB);
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_WEATHERED_CHISELED_COPPER.get(), Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB);
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_EXPOSED_CHISELED_COPPER.get(), Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB);
            chiseled(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_OXIDIZED_CHISELED_COPPER.get(), Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB);
            
            waxRecipes(output, ModBlocks.CHISELED_COPPER.get(), ModBlocks.WAXED_CHISELED_COPPER.get());
            waxRecipes(output, ModBlocks.EXPOSED_CHISELED_COPPER.get(), ModBlocks.WAXED_EXPOSED_CHISELED_COPPER.get());
            waxRecipes(output, ModBlocks.WEATHERED_CHISELED_COPPER.get(), ModBlocks.WAXED_WEATHERED_CHISELED_COPPER.get());
            waxRecipes(output, ModBlocks.OXIDIZED_CHISELED_COPPER.get(), ModBlocks.WAXED_OXIDIZED_CHISELED_COPPER.get());
            
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_COPPER.get(), Blocks.COPPER_BLOCK, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXPOSED_CHISELED_COPPER.get(), Blocks.EXPOSED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_CHISELED_COPPER.get(), Blocks.WEATHERED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.OXIDIZED_CHISELED_COPPER.get(), Blocks.OXIDIZED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_CHISELED_COPPER.get(), Blocks.WAXED_COPPER_BLOCK, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_EXPOSED_CHISELED_COPPER.get(), Blocks.WAXED_EXPOSED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_WEATHERED_CHISELED_COPPER.get(), Blocks.WAXED_WEATHERED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_OXIDIZED_CHISELED_COPPER.get(), Blocks.WAXED_OXIDIZED_COPPER, 4);
            
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_COPPER.get(), Blocks.CUT_COPPER, 1);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXPOSED_CHISELED_COPPER.get(), Blocks.EXPOSED_CUT_COPPER, 1);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_CHISELED_COPPER.get(), Blocks.WEATHERED_CUT_COPPER, 1);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.OXIDIZED_CHISELED_COPPER.get(), Blocks.OXIDIZED_CUT_COPPER, 1);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_CHISELED_COPPER.get(), Blocks.WAXED_CUT_COPPER, 1);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_EXPOSED_CHISELED_COPPER.get(), Blocks.WAXED_EXPOSED_CUT_COPPER, 1);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_WEATHERED_CHISELED_COPPER.get(), Blocks.WAXED_WEATHERED_CUT_COPPER, 1);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_OXIDIZED_CHISELED_COPPER.get(), Blocks.WAXED_OXIDIZED_CUT_COPPER, 1);
            
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.COPPER_GRATE.get(), Blocks.COPPER_BLOCK, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXPOSED_COPPER_GRATE.get(), Blocks.EXPOSED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_COPPER_GRATE.get(), Blocks.WEATHERED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.OXIDIZED_COPPER_GRATE.get(), Blocks.OXIDIZED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_COPPER_GRATE.get(), Blocks.WAXED_COPPER_BLOCK, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_EXPOSED_COPPER_GRATE.get(), Blocks.WAXED_EXPOSED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_WEATHERED_COPPER_GRATE.get(), Blocks.WAXED_WEATHERED_COPPER, 4);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAXED_OXIDIZED_COPPER_GRATE.get(), Blocks.WAXED_OXIDIZED_COPPER, 4);
            
            waxRecipes(output, ModBlocks.COPPER_GRATE.get(), ModBlocks.WAXED_COPPER_GRATE.get());
            waxRecipes(output, ModBlocks.EXPOSED_COPPER_GRATE.get(), ModBlocks.WAXED_EXPOSED_COPPER_GRATE.get());
            waxRecipes(output, ModBlocks.WEATHERED_COPPER_GRATE.get(), ModBlocks.WAXED_WEATHERED_COPPER_GRATE.get());
            waxRecipes(output, ModBlocks.OXIDIZED_COPPER_GRATE.get(), ModBlocks.WAXED_OXIDIZED_COPPER_GRATE.get());
            
            doorBuilder(ModBlocks.COPPER_DOOR.get(), Ingredient.of(Items.COPPER_INGOT))
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output);
            
            waxRecipes(output, ModBlocks.COPPER_DOOR.get(), ModBlocks.WAXED_COPPER_DOOR.get());
            waxRecipes(output, ModBlocks.EXPOSED_COPPER_DOOR.get(), ModBlocks.WAXED_EXPOSED_COPPER_DOOR.get());
            waxRecipes(output, ModBlocks.WEATHERED_COPPER_DOOR.get(), ModBlocks.WAXED_WEATHERED_COPPER_DOOR.get());
            waxRecipes(output, ModBlocks.OXIDIZED_COPPER_DOOR.get(), ModBlocks.WAXED_OXIDIZED_COPPER_DOOR.get());
            
            twoByTwoPacker(output, RecipeCategory.REDSTONE, ModBlocks.COPPER_TRAPDOOR.get(), Items.COPPER_INGOT);
            
            waxRecipes(output, ModBlocks.COPPER_TRAPDOOR.get(), ModBlocks.WAXED_COPPER_TRAPDOOR.get());
            waxRecipes(output, ModBlocks.EXPOSED_COPPER_TRAPDOOR.get(), ModBlocks.WAXED_EXPOSED_COPPER_TRAPDOOR.get());
            waxRecipes(output, ModBlocks.WEATHERED_COPPER_TRAPDOOR.get(), ModBlocks.WAXED_WEATHERED_COPPER_TRAPDOOR.get());
            waxRecipes(output, ModBlocks.OXIDIZED_COPPER_TRAPDOOR.get(), ModBlocks.WAXED_OXIDIZED_COPPER_TRAPDOOR.get());
        }
    }
    
    private static void buildBundlesOfBraveryRecipes(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path) {
        configurableRecipe(output, path, "bundle",
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.BUNDLE)
                .define('-', Items.STRING)
                .define('#', Items.LEATHER)
                .pattern("-")
                .pattern("#")
                .unlockedBy("has_string", has(Items.STRING)),
            VanillaBackport.COMMON_CONFIG.bundleRecipe.get());
    }
    
    private static void buildSpringToLifeRecipes(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE)
            .requires(Blocks.PUMPKIN)
            .requires(Items.SUGAR)
            .requires(ModItemTags.EGGS)
            .unlockedBy("has_carved_pumpkin", has(Blocks.CARVED_PUMPKIN))
            .unlockedBy("has_pumpkin", has(Blocks.PUMPKIN))
            .save(output, path.apply("pumpkin_pie"));
        
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Blocks.CAKE)
            .define('A', Items.MILK_BUCKET)
            .define('B', Items.SUGAR)
            .define('C', Items.WHEAT)
            .define('E', ModItemTags.EGGS)
            .pattern("AAA")
            .pattern("BEB")
            .pattern("CCC")
            .unlockedBy("has_egg", has(ModItemTags.EGGS))
            .save(output, path.apply("cake"));
        
        configurableRecipe(output, path, "lodestone",
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.LODESTONE)
                .define('S', Items.CHISELED_STONE_BRICKS)
                .define('#', Items.IRON_INGOT)
                .pattern("SSS")
                .pattern("S#S")
                .pattern("SSS")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .unlockedBy("has_lodestone", has(Items.LODESTONE)),
            VanillaBackport.COMMON_CONFIG.lodestoneRecipe.get());
    }
    
    private static void buildChaseTheSkiesRecipes(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path) {
        configurableRecipe(output, path, "lead",
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.LEAD, 2)
                .define('~', Items.STRING)
                .pattern("~~ ")
                .pattern("~~ ")
                .pattern("  ~")
                .unlockedBy("has_string", has(Items.STRING)),
            VanillaBackport.COMMON_CONFIG.leadRecipe.get());
        
        configurableRecipe(output, path, "saddle",
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.SADDLE)
                .define('X', Items.LEATHER)
                .define('#', Items.IRON_INGOT)
                .pattern(" X ")
                .pattern("X#X")
                .unlockedBy("has_leather", has(Items.LEATHER)),
            VanillaBackport.COMMON_CONFIG.saddleRecipe.get());
    }
    
    private static void buildCopperAgeRecipes(Consumer<FinishedRecipe> output) {
        if (VanillaBackport.COMMON_CONFIG.hasCopperNuggets.get()) {
            nineBlockStorageRecipesWithCustomPacking(output, RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), RecipeCategory.MISC, Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");
            
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
        }
        
        if (VanillaBackport.COMMON_CONFIG.hasCopperToolSet.get()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET.get())
                .define('X', Items.COPPER_INGOT)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE.get())
                .define('X', Items.COPPER_INGOT)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS.get())
                .define('X', Items.COPPER_INGOT)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS.get())
                .define('X', Items.COPPER_INGOT)
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_AXE.get())
                .define('#', Items.STICK)
                .define('X', Items.COPPER_INGOT)
                .pattern("XX")
                .pattern("X#")
                .pattern(" #")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_HOE.get())
                .define('#', Items.STICK)
                .define('X', Items.COPPER_INGOT)
                .pattern("XX")
                .pattern(" #")
                .pattern(" #")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_PICKAXE.get())
                .define('#', Items.STICK)
                .define('X', Items.COPPER_INGOT)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_SHOVEL.get())
                .define('#', Items.STICK)
                .define('X', Items.COPPER_INGOT)
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_SWORD.get())
                .define('#', Items.STICK)
                .define('X', Items.COPPER_INGOT)
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
        }
    }
    
    private static void buildMountsOfMayhemRecipes(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path) {
        if (VanillaBackport.COMMON_CONFIG.hasSpears.get()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_SPEAR.get())
                .define('#', Items.STICK)
                .define('X', Items.DIAMOND)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GOLDEN_SPEAR.get())
                .define('#', Items.STICK)
                .define('X', Items.GOLD_INGOT)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_SPEAR.get())
                .define('#', Items.STICK)
                .define('X', Items.IRON_INGOT)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STONE_SPEAR.get())
                .define('#', Items.STICK)
                .define('X', ItemTags.STONE_TOOL_MATERIALS)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
                .save(output);
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.WOODEN_SPEAR.get())
                .define('#', Items.STICK)
                .define('X', ItemTags.PLANKS)
                .pattern("  X")
                .pattern(" # ")
                .pattern("#  ")
                .unlockedBy("has_stick", has(Items.STICK))
                .save(output);
            
            if (VanillaBackport.COMMON_CONFIG.hasCopperToolSet.get()) {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_SPEAR.get())
                    .define('#', Items.STICK)
                    .define('X', Items.COPPER_INGOT)
                    .pattern("  X")
                    .pattern(" # ")
                    .pattern("#  ")
                    .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                    .save(output);
            }
        }
        
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
            .save(output, path.apply(getSmeltingRecipeName(Items.IRON_NUGGET)));
        
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
            .unlockedBy("has_golden_nautilus_armor", has(ModItems.GOLDEN_NAUTILUS_ARMOR.get()))
            .save(output, path.apply(getSmeltingRecipeName(Items.GOLD_NUGGET)));
        
        SimpleCookingRecipeBuilder.blasting(
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
            .unlockedBy("has_golden_nautilus_armor", has(ModItems.GOLDEN_NAUTILUS_ARMOR.get()))
            .save(output, path.apply(getBlastingRecipeName(Items.GOLD_NUGGET)));
        
        SimpleCookingRecipeBuilder.blasting(
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
            .save(output, path.apply(getBlastingRecipeName(Items.IRON_NUGGET)));
    }
    
    private static void buildWildernessBoundRecipes(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.MUSHROOM_STEW)
            .requires(ModItemTags.MUSHROOMS)
            .requires(ModItemTags.MUSHROOMS)
            .requires(Items.BOWL)
            .group("mushroom_stew")
            .unlockedBy("has_mushroom_stew", has(Items.MUSHROOM_STEW))
            .unlockedBy("has_bowl", has(Items.BOWL))
            .unlockedBy("has_mushrooms", has(ModItemTags.MUSHROOMS))
            .save(output, path.apply("mushroom_stew"));
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.RABBIT_STEW)
            .requires(Items.BAKED_POTATO)
            .requires(Items.COOKED_RABBIT)
            .requires(Items.BOWL)
            .requires(Items.CARROT)
            .requires(ModBlocks.SHELF_MUSHROOM.get())
            .group("rabbit_stew")
            .unlockedBy("has_cooked_rabbit", has(Items.COOKED_RABBIT))
            .save(output, path.apply(getConversionRecipeName(Items.RABBIT_STEW, ModBlocks.SHELF_MUSHROOM.get())));
    }
    
    protected static void configurableRecipe(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path, String name, RecipeBuilder builder, RecipeStrategy toggle) {
        switch (toggle) {
            case APPEND -> builder.save(output, path.apply(name));
            case REPLACE -> builder.save(output, new ResourceLocation(name));
            case IGNORE -> {}
        }
    }
    
    protected static void copperBulb(Consumer<FinishedRecipe> output, Block bulbBlock, Block material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, bulbBlock, 4)
            .define('C', material)
            .define('R', Items.REDSTONE)
            .define('B', Items.BLAZE_ROD)
            .pattern(" C ")
            .pattern("CBC")
            .pattern(" R ")
            .unlockedBy(getHasName(material), has(material))
            .save(output);
    }
    
    private static void generateStonecutterRecipes(Consumer<FinishedRecipe> exporter, BlockFamily family, FeatureFlagSet flagSet) {
        family.getVariants().forEach((variant, result) -> {
            if (result.requiredFeatures().isSubsetOf(flagSet)) {
                Block base = family.getBaseBlock();
                generateStonecutterRecipe(exporter, family, variant, base);
            }
        });
    }
    
    private static void generateStonecutterRecipe(Consumer<FinishedRecipe> exporter, BlockFamily family, BlockFamily.Variant variant, Block base) {
        FamilyStonecutterRecipeProvider recipeFunction = STONECUTTER_RECIPE_BUILDERS.get(variant);
        if (recipeFunction != null) {
            recipeFunction.create(exporter, family.get(variant), base);
        }
        
        if (variant == BlockFamily.Variant.POLISHED || variant == BlockFamily.Variant.CUT) {
            BlockFamily childVariantFamily = BlockFamilies.getFamily(family.get(variant));
            if (childVariantFamily != null) {
                childVariantFamily.getVariants().forEach((childVariant, r) -> generateStonecutterRecipe(exporter, childVariantFamily, childVariant, base));
            }
        }
    }
    
    public static void waxRecipes(Consumer<FinishedRecipe> output, Block unwaxed, Block waxed) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, waxed)
            .requires(unwaxed)
            .requires(Items.HONEYCOMB)
            .group(getItemName(waxed))
            .unlockedBy(getHasName(unwaxed), has(unwaxed))
            .save(output, getConversionRecipeName(waxed, Items.HONEYCOMB));
    }
    
    @FunctionalInterface
    private interface FamilyStonecutterRecipeProvider {
        void create(Consumer<FinishedRecipe> exporter, ItemLike result, ItemLike base);
    }
}