package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;

public class ModelGenerator extends FabricModelProvider {
    private static final ResourceLocation TEMPLATE_SPAWN_EGG = ModelLocationUtils.decorateItemModelLocation("template_spawn_egg");

    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        BlockFamilies.getAllFamilies()
            .filter(BlockFamily::shouldGenerateModel)
            .forEach(family -> gen.family(family.getBaseBlock()).generateFor(family));
        BlockModels models = new BlockModels(gen);
        gen.createHangingSign(ModBlocks.STRIPPED_PALE_OAK_LOG.get(), ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get(), ModBlocks.PALE_OAK_HANGING_SIGN.getSecond().get());
        gen.createTrivialCube(ModBlocks.PALE_MOSS_BLOCK.get());
        gen.createTrivialBlock(ModBlocks.PALE_OAK_LEAVES.get(), TexturedModel.LEAVES);
        gen.woodProvider(ModBlocks.PALE_OAK_LOG.get())
            .logWithHorizontal(ModBlocks.PALE_OAK_LOG.get())
            .wood(ModBlocks.PALE_OAK_WOOD.get());
        gen.woodProvider(ModBlocks.STRIPPED_PALE_OAK_LOG.get())
            .logWithHorizontal(ModBlocks.STRIPPED_PALE_OAK_LOG.get())
            .wood(ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
        gen.createPlant(ModBlocks.PALE_OAK_SAPLING.get(), ModBlocks.POTTED_PALE_OAK_SAPLING.get(), BlockModelGenerators.TintState.NOT_TINTED);
        gen.createPlant(ModBlocks.OPEN_EYEBLOSSOM.get(), ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(), BlockModelGenerators.TintState.NOT_TINTED);
        gen.createPlant(ModBlocks.CLOSED_EYEBLOSSOM.get(), ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(), BlockModelGenerators.TintState.NOT_TINTED);
        models.createMossyCarpet(ModBlocks.PALE_MOSS_CARPET.get());
        models.createHangingMoss(ModBlocks.PALE_HANGING_MOSS.get());
        models.createCreakingHeart(ModBlocks.CREAKING_HEART.get());
        models.createMultiface(ModBlocks.RESIN_CLUMP.get(), ModBlocks.RESIN_CLUMP.get().asItem());
        gen.createTrivialCube(ModBlocks.RESIN_BLOCK.get());
        gen.delegateItemModel(ModItems.CREAKING_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);

        models.createDriedGhastBlock();
        gen.delegateItemModel(ModItems.HAPPY_GHAST_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(ModItems.RESIN_BRICK.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.PALE_OAK_BOAT.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.PALE_OAK_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(ModItems.WHITE_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.ORANGE_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.MAGENTA_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.LIGHT_BLUE_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.YELLOW_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.LIME_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.PINK_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.GRAY_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.LIGHT_GRAY_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.CYAN_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.PURPLE_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.BLUE_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.BROWN_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.GREEN_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.RED_HARNESS.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ModItems.BLACK_HARNESS.get(), ModelTemplates.FLAT_ITEM);
    }
}