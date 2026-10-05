package com.blackgear.vanillabackport.common.level.recipes;

import com.blackgear.platform.common.v2.recipe.DynamicRecipeProvider;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;
import java.util.function.Function;

public class VanillaDynamicRecipes extends DynamicRecipeProvider {
    @Override
    public void buildRecipes(Consumer<FinishedRecipe> output, Function<String, ResourceLocation> path) {
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
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.LEAD, 2)
            .define('~', Items.STRING)
            .pattern("~~ ")
            .pattern("~~ ")
            .pattern("  ~")
            .unlockedBy("has_string", has(Items.STRING))
            .save(output, path.apply("lead"));
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.BUNDLE)
            .define('-', Items.STRING)
            .define('#', Items.LEATHER)
            .pattern("-")
            .pattern("#")
            .unlockedBy("has_string", has(Items.STRING))
            .save(output, path.apply("bundle"));
        
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.SADDLE)
            .define('X', Items.LEATHER)
            .define('#', Items.IRON_INGOT)
            .pattern(" X ")
            .pattern("X#X")
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(output, path.apply("saddle"));
        
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.LODESTONE)
            .define('S', Items.CHISELED_STONE_BRICKS)
            .define('#', Items.IRON_INGOT)
            .pattern("SSS")
            .pattern("S#S")
            .pattern("SSS")
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .unlockedBy("has_lodestone", has(Items.LODESTONE))
            .save(output, path.apply("lodestone"));
        
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
    }
}