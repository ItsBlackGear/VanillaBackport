package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.data.client.model.VanillaBlockModels;
import com.blackgear.vanillabackport.data.client.model.VanillaItemModels;
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
    public void generateBlockStateModels(VanillaBlockModels output) {
        BlockFamilies.getAllFamilies()
            .filter(BlockFamily::shouldGenerateModel)
            .forEach(family -> output.family(family.getBaseBlock()).generateFor(family));
        output.createHangingSign(ModBlocks.STRIPPED_PALE_OAK_LOG.get(), ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get(), ModBlocks.PALE_OAK_HANGING_SIGN.getSecond().get());
        output.createTrivialCube(ModBlocks.PALE_MOSS_BLOCK.get());
        output.createTrivialBlock(ModBlocks.PALE_OAK_LEAVES.get(), TexturedModel.LEAVES);
        output.woodProvider(ModBlocks.PALE_OAK_LOG.get())
            .logWithHorizontal(ModBlocks.PALE_OAK_LOG.get())
            .wood(ModBlocks.PALE_OAK_WOOD.get());
        output.woodProvider(ModBlocks.STRIPPED_PALE_OAK_LOG.get())
            .logWithHorizontal(ModBlocks.STRIPPED_PALE_OAK_LOG.get())
            .wood(ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
        output.createPlant(ModBlocks.PALE_OAK_SAPLING.get(), ModBlocks.POTTED_PALE_OAK_SAPLING.get(), BlockModelGenerators.TintState.NOT_TINTED);
        output.createPlant(ModBlocks.OPEN_EYEBLOSSOM.get(), ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(), BlockModelGenerators.TintState.NOT_TINTED);
        output.createPlant(ModBlocks.CLOSED_EYEBLOSSOM.get(), ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(), BlockModelGenerators.TintState.NOT_TINTED);
        output.createMossyCarpet(ModBlocks.PALE_MOSS_CARPET.get());
        output.createHangingMoss(ModBlocks.PALE_HANGING_MOSS.get());
        output.createCreakingHeart(ModBlocks.CREAKING_HEART.get());
        output.createMultiface(ModBlocks.RESIN_CLUMP.get(), ModBlocks.RESIN_CLUMP.get().asItem());
        output.createTrivialCube(ModBlocks.RESIN_BLOCK.get());
        output.createSpawnEgg(ModItems.CREAKING_SPAWN_EGG.get());

        output.createDriedGhastBlock();
        output.createSpawnEgg(ModItems.HAPPY_GHAST_SPAWN_EGG.get());
    }

    @Override
    public void generateItemModels(VanillaItemModels output) {
        output.createFlatItem(ModItems.RESIN_BRICK.get());
        output.createFlatItem(ModItems.PALE_OAK_BOAT.get());
        output.createFlatItem(ModItems.PALE_OAK_CHEST_BOAT.get());

        output.createFlatItem(ModItems.WHITE_HARNESS.get());
        output.createFlatItem(ModItems.ORANGE_HARNESS.get());
        output.createFlatItem(ModItems.MAGENTA_HARNESS.get());
        output.createFlatItem(ModItems.LIGHT_BLUE_HARNESS.get());
        output.createFlatItem(ModItems.YELLOW_HARNESS.get());
        output.createFlatItem(ModItems.LIME_HARNESS.get());
        output.createFlatItem(ModItems.PINK_HARNESS.get());
        output.createFlatItem(ModItems.GRAY_HARNESS.get());
        output.createFlatItem(ModItems.LIGHT_GRAY_HARNESS.get());
        output.createFlatItem(ModItems.CYAN_HARNESS.get());
        output.createFlatItem(ModItems.PURPLE_HARNESS.get());
        output.createFlatItem(ModItems.BLUE_HARNESS.get());
        output.createFlatItem(ModItems.BROWN_HARNESS.get());
        output.createFlatItem(ModItems.GREEN_HARNESS.get());
        output.createFlatItem(ModItems.RED_HARNESS.get());
        output.createFlatItem(ModItems.BLACK_HARNESS.get());
        output.createMusicDisc(ModItems.MUSIC_DISC_TEARS.get());
        output.createMusicDisc(ModItems.MUSIC_DISC_LAVA_CHICKEN.get());
    }
}