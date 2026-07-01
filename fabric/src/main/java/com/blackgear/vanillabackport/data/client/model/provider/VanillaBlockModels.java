package com.blackgear.vanillabackport.data.client.model.provider;

import com.blackgear.vanillabackport.common.level.block.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.block.DriedGhastBlock;
import com.blackgear.vanillabackport.common.level.block.HangingMossBlock;
import com.blackgear.vanillabackport.common.level.block.MossyCarpetBlock;
import com.blackgear.vanillabackport.common.level.block.states.CreakingHeartState;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockStateProperties;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.data.client.model.ModModelTemplates;
import com.blackgear.vanillabackport.data.client.model.ModTextureMappings;
import com.blackgear.vanillabackport.data.client.model.ModTexturedModels;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.*;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class VanillaBlockModels extends BlockModelGenerators {
    private static final ResourceLocation TEMPLATE_SPAWN_EGG = ModelLocationUtils.decorateItemModelLocation("template_spawn_egg");
    private static final List<Pair<Direction, Function<ResourceLocation, Variant>>> MULTIFACE_GENERATOR = List.of(
        Pair.of(Direction.NORTH, path -> Variant.variant().with(VariantProperties.MODEL, path)),
        Pair.of(Direction.EAST, path -> Variant.variant()
            .with(VariantProperties.MODEL, path)
            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
            .with(VariantProperties.UV_LOCK, true)),
        Pair.of(Direction.SOUTH, path -> Variant.variant()
            .with(VariantProperties.MODEL, path)
            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
            .with(VariantProperties.UV_LOCK, true)),
        Pair.of(Direction.WEST, path -> Variant.variant()
            .with(VariantProperties.MODEL, path)
            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
            .with(VariantProperties.UV_LOCK, true)),
        Pair.of(Direction.UP, path -> Variant.variant()
            .with(VariantProperties.MODEL, path)
            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
            .with(VariantProperties.UV_LOCK, true)),
        Pair.of(Direction.DOWN, path -> Variant.variant()
            .with(VariantProperties.MODEL, path)
            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
            .with(VariantProperties.UV_LOCK, true))
    );

    public VanillaBlockModels(Consumer<BlockStateGenerator> blockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput, Consumer<Item> skippedAutoModelsOutput) {
        super(blockStateOutput, modelOutput, skippedAutoModelsOutput);
    }

    public void createSpawnEgg(ItemLike item) {
        this.delegateItemModel(item.asItem(), TEMPLATE_SPAWN_EGG);
    }

    public void createMossyCarpet(Block block) {
        ResourceLocation baseModel = TexturedModel.CARPET.create(block, this.modelOutput);
        ResourceLocation tallSideModel = ModTexturedModels.MOSSY_CARPET_SIDE
            .get(block)
            .updateTextures(mapping -> mapping.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_tall")))
            .createWithSuffix(block, "_side_tall", this.modelOutput);
        ResourceLocation shortSideModel = ModTexturedModels.MOSSY_CARPET_SIDE
            .get(block)
            .updateTextures(mapping -> mapping.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_small")))
            .createWithSuffix(block, "_side_small", this.modelOutput);

        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
        Condition.TerminalCondition terminal = Condition.condition().term(MossyCarpetBlock.BASE, false);
        generator.with(Condition.condition().term(MossyCarpetBlock.BASE, true), Variant.variant().with(VariantProperties.MODEL, baseModel));
        generator.with(terminal, Variant.variant().with(VariantProperties.MODEL, baseModel));

        MULTIFACE_GENERATOR.stream().map(Pair::getFirst).forEach(direction -> {
            EnumProperty<WallSide> property = MossyCarpetBlock.getPropertyForFace(direction);
            if (property != null && block.defaultBlockState().hasProperty(property)) {
                terminal.term(property, WallSide.NONE);
            }
        });

        for (Pair<Direction, Function<ResourceLocation, Variant>> pair : MULTIFACE_GENERATOR) {
            Direction direction = pair.getFirst();
            EnumProperty<WallSide> property = MossyCarpetBlock.getPropertyForFace(direction);

            if (property != null) {
                Function<ResourceLocation, Variant> function = pair.getSecond();
                generator.with(Condition.condition().term(property, WallSide.TALL), function.apply(tallSideModel));
                generator.with(Condition.condition().term(property, WallSide.LOW), function.apply(shortSideModel));
                generator.with(terminal, function.apply(tallSideModel));
            }
        }

        this.blockStateOutput.accept(generator);
    }

    public void createHangingMoss(Block block) {
        PropertyDispatch dispatch = PropertyDispatch.property(HangingMossBlock.TIP).generate(value -> {
            String suffix = value ? "_tip" : "";
            TextureMapping textureMapping = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
            ResourceLocation path = TintState.NOT_TINTED.getCross().createWithSuffix(block, suffix, textureMapping, this.modelOutput);
            return Variant.variant().with(VariantProperties.MODEL, path);
        });

        this.createSimpleFlatItemModel(block);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(dispatch));
    }

    public void createCreakingHeart(Block block) {
        ResourceLocation base = TexturedModel.COLUMN_ALT.create(block, this.modelOutput);
        ResourceLocation baseSide = TexturedModel.COLUMN_HORIZONTAL_ALT.create(block, this.modelOutput);
        ResourceLocation awake = this.createCreakingHeartModel(TexturedModel.COLUMN_ALT, block, "_awake");
        ResourceLocation awakeSide = this.createCreakingHeartModel(TexturedModel.COLUMN_HORIZONTAL_ALT, block, "_awake");
        ResourceLocation dormant = this.createCreakingHeartModel(TexturedModel.COLUMN_ALT, block, "_dormant");
        ResourceLocation dormantSide = this.createCreakingHeartModel(TexturedModel.COLUMN_HORIZONTAL_ALT, block, "_dormant");

        PropertyDispatch state = PropertyDispatch.properties(BlockStateProperties.AXIS, CreakingHeartBlock.STATE)
            .select(Direction.Axis.Y, CreakingHeartState.UPROOTED, createVariant(base, VariantProperties.Rotation.R0, VariantProperties.Rotation.R0))
            .select(Direction.Axis.Z, CreakingHeartState.UPROOTED, createVariant(baseSide, VariantProperties.Rotation.R90, VariantProperties.Rotation.R0))
            .select(Direction.Axis.X, CreakingHeartState.UPROOTED, createVariant(baseSide, VariantProperties.Rotation.R90, VariantProperties.Rotation.R90))
            .select(Direction.Axis.Y, CreakingHeartState.DORMANT, createVariant(dormant, VariantProperties.Rotation.R0, VariantProperties.Rotation.R0))
            .select(Direction.Axis.Z, CreakingHeartState.DORMANT, createVariant(dormantSide, VariantProperties.Rotation.R90, VariantProperties.Rotation.R0))
            .select(Direction.Axis.X, CreakingHeartState.DORMANT, createVariant(dormantSide, VariantProperties.Rotation.R90, VariantProperties.Rotation.R90))
            .select(Direction.Axis.Y, CreakingHeartState.AWAKE, createVariant(awake, VariantProperties.Rotation.R0, VariantProperties.Rotation.R0))
            .select(Direction.Axis.Z, CreakingHeartState.AWAKE, createVariant(awakeSide, VariantProperties.Rotation.R90, VariantProperties.Rotation.R0))
            .select(Direction.Axis.X, CreakingHeartState.AWAKE, createVariant(awakeSide, VariantProperties.Rotation.R90, VariantProperties.Rotation.R90));

        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(state));
    }

    private Variant createVariant(ResourceLocation model, VariantProperties.Rotation xRot, VariantProperties.Rotation yRot) {
        Variant variant = Variant.variant().with(VariantProperties.MODEL, model);

        if (xRot != VariantProperties.Rotation.R0) variant = variant.with(VariantProperties.X_ROT, xRot);
        if (yRot != VariantProperties.Rotation.R0) variant = variant.with(VariantProperties.Y_ROT, yRot);

        return variant;
    }

    private ResourceLocation createCreakingHeartModel(TexturedModel.Provider provider, Block block, String suffix) {
        return provider.updateTexture(mapping -> mapping
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, suffix))
            .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top" + suffix))
        ).createWithSuffix(block, suffix, this.modelOutput);
    }

    public void createMultiface(Block block, Item item) {
        this.createSimpleFlatItemModel(item);
        ResourceLocation model = ModelLocationUtils.getModelLocation(block);
        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);

        Condition.TerminalCondition terminalCondition = Util.make(
            Condition.condition(),
            condition -> BlockModelGenerators.MULTIFACE_GENERATOR.stream()
                .map(Pair::getFirst)
                .forEach((facingDirection) -> {
                    if (block.defaultBlockState().hasProperty(facingDirection)) {
                        condition.term(facingDirection, false);
                    }
                })
        );

        for(Pair<BooleanProperty, Function<ResourceLocation, Variant>> pair : BlockModelGenerators.MULTIFACE_GENERATOR) {
            BooleanProperty booleanProperty = pair.getFirst();
            Function<ResourceLocation, Variant> function = pair.getSecond();
            if (block.defaultBlockState().hasProperty(booleanProperty)) {
                generator.with(Condition.condition().term(booleanProperty, true), function.apply(model));
                generator.with(terminalCondition, function.apply(model));
            }
        }

        this.blockStateOutput.accept(generator);
    }

    public void createDriedGhastBlock() {
        ResourceLocation model = ModelLocationUtils.getModelLocation(ModBlocks.DRIED_GHAST.get(), "_hydration_0");
        this.delegateItemModel(ModBlocks.DRIED_GHAST.get(), model);
        Function<Integer, ResourceLocation> hydrationModel = level -> {
            String suffix = switch (level) {
                case 1 -> "_hydration_1";
                case 2 -> "_hydration_2";
                case 3 -> "_hydration_3";
                default -> "_hydration_0";
            };
            TextureMapping mapping = ModTextureMappings.driedGhast(suffix);
            return ModModelTemplates.DRIED_GHAST.createWithSuffix(ModBlocks.DRIED_GHAST.get(), suffix, mapping, this.modelOutput);
        };

        this.blockStateOutput
            .accept(
                MultiVariantGenerator.multiVariant(ModBlocks.DRIED_GHAST.get())
                    .with(PropertyDispatch.property(DriedGhastBlock.HYDRATION_LEVEL).generate(level -> Variant.variant().with(VariantProperties.MODEL, hydrationModel.apply(level))))
                    .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
    }

    public void createLeafLitter(Block block) {
        ResourceLocation resourceLocation = ModTexturedModels.LEAF_LITTER_1.create(block, this.modelOutput);
        ResourceLocation resourceLocation2 = ModTexturedModels.LEAF_LITTER_2.create(block, this.modelOutput);
        ResourceLocation resourceLocation3 = ModTexturedModels.LEAF_LITTER_3.create(block, this.modelOutput);
        ResourceLocation resourceLocation4 = ModTexturedModels.LEAF_LITTER_4.create(block, this.modelOutput);
        this.createSimpleFlatItemModel(block.asItem());
        this.blockStateOutput.accept(
            MultiPartGenerator.multiPart(block)
                .with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 1)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 1)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 1)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 1)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 2, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation2)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 2, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation2)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 2, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation2)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 2, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation2)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation3)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation3)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation3)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 3)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation3)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 4)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation4)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 4)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation4)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 4)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation4)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                ).with(
                    Condition.condition()
                        .term(ModBlockStateProperties.SEGMENT_AMOUNT, 4)
                        .term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),
                    Variant.variant()
                        .with(VariantProperties.MODEL, resourceLocation4)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                )
        );
    }

    public void createSpeleothem(Block block) {
        this.skipAutoItemBlock(block);
        PropertyDispatch.C2<Direction, DripstoneThickness> generator = PropertyDispatch.properties(BlockStateProperties.VERTICAL_DIRECTION, BlockStateProperties.DRIPSTONE_THICKNESS);

        for (DripstoneThickness thickness : DripstoneThickness.values()) {
            generator.select(Direction.UP, thickness, this.createSpeleothemVariant(Direction.UP, thickness, block));
        }

        for (DripstoneThickness thickness : DripstoneThickness.values()) {
            generator.select(Direction.DOWN, thickness, this.createSpeleothemVariant(Direction.DOWN, thickness, block));
        }

        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(generator));
    }

    private Variant createSpeleothemVariant(Direction direction, DripstoneThickness thickness, Block block) {
        String suffix = "_" + direction.getSerializedName() + "_" + thickness.getSerializedName();
        TextureMapping texture = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
        return Variant.variant().with(VariantProperties.MODEL, net.minecraft.data.models.model.ModelTemplates.POINTED_DRIPSTONE.createWithSuffix(block, suffix, texture, this.modelOutput));
    }
    
    public void createChest(Block chest, Block particles) {
        BlockEntityModelGenerator generator = new BlockEntityModelGenerator(ModelLocationUtils.getModelLocation(chest), particles);
        generator.createWithoutBlockItem(chest);
        
        ModModelTemplates.CHEST.create(ModelLocationUtils.getModelLocation(chest.asItem()), TextureMapping.particle(particles), this.modelOutput);
    }
    
    public void createCopperChain(Block unwaxed, Block waxed) {
        ResourceLocation block = ModTexturedModels.CHAIN.create(unwaxed, this.modelOutput);
        this.createAxisAlignedPillarBlockCustomModel(unwaxed, block);
        this.createAxisAlignedPillarBlockCustomModel(waxed, block);
        ResourceLocation item = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(unwaxed.asItem()), TextureMapping.layer0(unwaxed.asItem()), this.modelOutput);
        this.delegateItemModel(waxed.asItem(), item);
    }
    
    public void createCopperBars(Block unwaxed, Block waxed) {
        TextureMapping textures = ModTextureMappings.bars(unwaxed);
        ResourceLocation postEndResource = ModModelTemplates.BARS_POST_ENDS.create(unwaxed, textures, this.modelOutput);
        ResourceLocation postResource = ModModelTemplates.BARS_POST.create(unwaxed, textures, this.modelOutput);
        ResourceLocation capResource = ModModelTemplates.BARS_CAP.create(unwaxed, textures, this.modelOutput);
        ResourceLocation capAltResource = ModModelTemplates.BARS_CAP_ALT.create(unwaxed, textures, this.modelOutput);
        ResourceLocation sideResource = ModModelTemplates.BARS_POST_SIDE.create(unwaxed, textures, this.modelOutput);
        ResourceLocation sideAltResource = ModModelTemplates.BARS_POST_SIDE_ALT.create(unwaxed, textures, this.modelOutput);
        this.createBars(unwaxed, postEndResource, postResource, capResource, capAltResource, sideResource, sideAltResource);
        this.createBars(waxed, postEndResource, postResource, capResource, capAltResource, sideResource, sideAltResource);
        this.createSimpleFlatItemModel(unwaxed);
        this.delegateItemModel(waxed, ModelLocationUtils.getModelLocation(unwaxed.asItem()));
    }
    
    private void createBars(Block block, ResourceLocation postEnd, ResourceLocation post, ResourceLocation cap, ResourceLocation capAlt, ResourceLocation side, ResourceLocation sideAlt) {
        this.blockStateOutput
            .accept(
                MultiPartGenerator.multiPart(block)
                    .with(Variant.variant().with(VariantProperties.MODEL, postEnd))
                    .with(
                        Condition.condition()
                            .term(BlockStateProperties.NORTH, false)
                            .term(BlockStateProperties.EAST, false)
                            .term(BlockStateProperties.SOUTH, false)
                            .term(BlockStateProperties.WEST, false),
                        Variant.variant().with(VariantProperties.MODEL, post)
                    )
                    .with(
                        Condition.condition()
                            .term(BlockStateProperties.NORTH, true)
                            .term(BlockStateProperties.EAST, false)
                            .term(BlockStateProperties.SOUTH, false)
                            .term(BlockStateProperties.WEST, false),
                        Variant.variant().with(VariantProperties.MODEL, cap)
                    )
                    .with(
                        Condition.condition()
                            .term(BlockStateProperties.NORTH, false)
                            .term(BlockStateProperties.EAST, true)
                            .term(BlockStateProperties.SOUTH, false)
                            .term(BlockStateProperties.WEST, false),
                        Variant.variant().with(VariantProperties.MODEL, cap).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                    )
                    .with(
                        Condition.condition()
                            .term(BlockStateProperties.NORTH, false)
                            .term(BlockStateProperties.EAST, false)
                            .term(BlockStateProperties.SOUTH, true)
                            .term(BlockStateProperties.WEST, false),
                        Variant.variant().with(VariantProperties.MODEL, capAlt)
                    )
                    .with(
                        Condition.condition()
                            .term(BlockStateProperties.NORTH, false)
                            .term(BlockStateProperties.EAST, false)
                            .term(BlockStateProperties.SOUTH, false)
                            .term(BlockStateProperties.WEST, true),
                        Variant.variant().with(VariantProperties.MODEL, capAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                    )
                    .with(
                        Condition.condition().term(BlockStateProperties.NORTH, true),
                        Variant.variant().with(VariantProperties.MODEL, side)
                    )
                    .with(
                        Condition.condition().term(BlockStateProperties.EAST, true),
                        Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                    )
                    .with(
                        Condition.condition().term(BlockStateProperties.SOUTH, true),
                        Variant.variant().with(VariantProperties.MODEL, sideAlt)
                    )
                    .with(
                        Condition.condition().term(BlockStateProperties.WEST, true),
                        Variant.variant().with(VariantProperties.MODEL, sideAlt).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                    )
            );
    }
    
    public void createLightningRod(Block unwaxed, Block waxed) {
        if (unwaxed == null) {
            ResourceLocation on = ModelLocationUtils.getModelLocation(Blocks.LIGHTNING_ROD, "_on");
            ResourceLocation off = ModelLocationUtils.getModelLocation(Blocks.LIGHTNING_ROD);
            this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(waxed, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(waxed))).with(this.createColumnWithFacing()).with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off)));
            this.delegateItemModel(waxed, ModelLocationUtils.getModelLocation(Blocks.LIGHTNING_ROD.asItem()));
        } else {
            ResourceLocation on = ModelLocationUtils.getModelLocation(Blocks.LIGHTNING_ROD, "_on");
            ResourceLocation off = ModModelTemplates.LIGHTNING_ROD.create(unwaxed, TextureMapping.defaultTexture(unwaxed), this.modelOutput);
            this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(unwaxed, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(unwaxed))).with(this.createColumnWithFacing()).with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off)));
            this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(waxed, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(waxed))).with(this.createColumnWithFacing()).with(createBooleanModelDispatch(BlockStateProperties.POWERED, on, off)));
            this.delegateItemModel(waxed, ModelLocationUtils.getModelLocation(unwaxed.asItem()));
        }
    }
    
    public void createCopperLantern(Block unwaxed, Block waxed) {
        ResourceLocation ground = TexturedModel.LANTERN.create(unwaxed, this.modelOutput);
        ResourceLocation hanging = TexturedModel.HANGING_LANTERN.create(unwaxed, this.modelOutput);
        this.createSimpleFlatItemModel(unwaxed.asItem());
        this.delegateItemModel(waxed, ModelLocationUtils.getModelLocation(unwaxed.asItem()));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(unwaxed).with(createBooleanModelDispatch(BlockStateProperties.HANGING, hanging, ground)));
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(waxed).with(createBooleanModelDispatch(BlockStateProperties.HANGING, hanging, ground)));
    }
}