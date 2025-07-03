package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.data.client.model.provider.VanillaBlockModels;
import com.blackgear.vanillabackport.data.client.model.provider.VanillaItemModels;
import com.blackgear.vanillabackport.data.client.model.provider.VanillaModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.TexturedModel;

public class ModelGenerator extends VanillaModelGenerator {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(VanillaBlockModels gen) {
        BlockFamilies.getAllFamilies()
            .filter(BlockFamily::shouldGenerateModel)
            .forEach(family -> gen.family(family.getBaseBlock()).generateFor(family));

        // The Garden Awakens
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
        gen.createMossyCarpet(ModBlocks.PALE_MOSS_CARPET.get());
        gen.createHangingMoss(ModBlocks.PALE_HANGING_MOSS.get());
        gen.createCreakingHeart(ModBlocks.CREAKING_HEART.get());
        gen.createMultiface(ModBlocks.RESIN_CLUMP.get(), ModBlocks.RESIN_CLUMP.get().asItem());
        gen.createTrivialCube(ModBlocks.RESIN_BLOCK.get());
        gen.createSpawnEgg(ModItems.CREAKING_SPAWN_EGG.get());

        // Chase the Skies
        gen.createDriedGhastBlock();
        gen.createSpawnEgg(ModItems.HAPPY_GHAST_SPAWN_EGG.get());

        // Spring to Life
        gen.createCrossBlockWithDefaultItem(ModBlocks.BUSH.get(), BlockModelGenerators.TintState.TINTED);
        gen.createCrossBlock(ModBlocks.FIREFLY_BUSH.get(), BlockModelGenerators.TintState.NOT_TINTED);
        gen.createSimpleFlatItemModel(ModBlocks.FIREFLY_BUSH.get().asItem());
        gen.createFlowerBed(ModBlocks.WILDFLOWERS.get());
        gen.createCrossBlockWithDefaultItem(ModBlocks.CACTUS_FLOWER.get(), BlockModelGenerators.TintState.NOT_TINTED);
        gen.createCrossBlockWithDefaultItem(ModBlocks.SHORT_DRY_GRASS.get(), BlockModelGenerators.TintState.NOT_TINTED);
        gen.createCrossBlockWithDefaultItem(ModBlocks.TALL_DRY_GRASS.get(), BlockModelGenerators.TintState.NOT_TINTED);
    }

    @Override
    public void generateItemModels(VanillaItemModels gen) {
        // The Garden Awakens
        gen.createFlatItem(ModItems.RESIN_BRICK.get());
        gen.createFlatItem(ModItems.PALE_OAK_BOAT.get());
        gen.createFlatItem(ModItems.PALE_OAK_CHEST_BOAT.get());

        // Chase the Skies
        gen.createFlatItem(ModItems.WHITE_HARNESS.get());
        gen.createFlatItem(ModItems.ORANGE_HARNESS.get());
        gen.createFlatItem(ModItems.MAGENTA_HARNESS.get());
        gen.createFlatItem(ModItems.LIGHT_BLUE_HARNESS.get());
        gen.createFlatItem(ModItems.YELLOW_HARNESS.get());
        gen.createFlatItem(ModItems.LIME_HARNESS.get());
        gen.createFlatItem(ModItems.PINK_HARNESS.get());
        gen.createFlatItem(ModItems.GRAY_HARNESS.get());
        gen.createFlatItem(ModItems.LIGHT_GRAY_HARNESS.get());
        gen.createFlatItem(ModItems.CYAN_HARNESS.get());
        gen.createFlatItem(ModItems.PURPLE_HARNESS.get());
        gen.createFlatItem(ModItems.BLUE_HARNESS.get());
        gen.createFlatItem(ModItems.BROWN_HARNESS.get());
        gen.createFlatItem(ModItems.GREEN_HARNESS.get());
        gen.createFlatItem(ModItems.RED_HARNESS.get());
        gen.createFlatItem(ModItems.BLACK_HARNESS.get());
        gen.createMusicDisc(ModItems.MUSIC_DISC_TEARS.get());
        gen.createMusicDisc(ModItems.MUSIC_DISC_LAVA_CHICKEN.get());
    }
}