package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.level.blocks.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.blocks.DriedGhastBlock;
import com.blackgear.vanillabackport.common.level.blocks.HangingMossBlock;
import com.blackgear.vanillabackport.common.level.blocks.MossyCarpetBlock;
import com.blackgear.vanillabackport.common.level.blocks.blockstates.CreakingHeartState;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.data.client.model.ModelTemplates;
import com.blackgear.vanillabackport.data.client.model.TextureMappings;
import com.blackgear.vanillabackport.data.client.model.TexturedModels;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;

import java.util.List;
import java.util.function.Function;

public record BlockModels(BlockModelGenerators gen) {
    private static final List<Pair<Direction, Function<ResourceLocation, Variant>>> MULTIFACE_GENERATOR = List.of(
        Pair.of(Direction.NORTH, path -> Variant.variant().with(VariantProperties.MODEL, path)),
        Pair.of(
            Direction.EAST,
            path -> Variant.variant()
                .with(VariantProperties.MODEL, path)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.UV_LOCK, true)
        ),
        Pair.of(
            Direction.SOUTH,
            path -> Variant.variant()
                .with(VariantProperties.MODEL, path)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                .with(VariantProperties.UV_LOCK, true)
        ),
        Pair.of(
            Direction.WEST,
            path -> Variant.variant()
                .with(VariantProperties.MODEL, path)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                .with(VariantProperties.UV_LOCK, true)
        ),
        Pair.of(
            Direction.UP,
            path -> Variant.variant()
                .with(VariantProperties.MODEL, path)
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)
                .with(VariantProperties.UV_LOCK, true)
        ),
        Pair.of(
            Direction.DOWN,
            path -> Variant.variant()
                .with(VariantProperties.MODEL, path)
                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                .with(VariantProperties.UV_LOCK, true)
        )
    );

    public void createMossyCarpet(Block block) {
        ResourceLocation baseModel = TexturedModel.CARPET.create(block, gen.modelOutput);
        ResourceLocation tallSideModel = TexturedModels.MOSSY_CARPET_SIDE
                .get(block)
                .updateTextures(mapping -> mapping.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_tall")))
                .createWithSuffix(block, "_side_tall", gen.modelOutput);
        ResourceLocation shortSideModel = TexturedModels.MOSSY_CARPET_SIDE
                .get(block)
                .updateTextures(mapping -> mapping.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side_small")))
                .createWithSuffix(block, "_side_small", gen.modelOutput);
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

        gen.blockStateOutput.accept(generator);
    }

    public void createHangingMoss(Block block) {
        PropertyDispatch dispatch = PropertyDispatch.property(HangingMossBlock.TIP).generate(value -> {
            String suffix = value ? "_tip" : "";
            TextureMapping textureMapping = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix));
            ResourceLocation path = BlockModelGenerators.TintState.NOT_TINTED.getCross().createWithSuffix(block, suffix, textureMapping, gen.modelOutput);
            return Variant.variant().with(VariantProperties.MODEL, path);
        });

        gen.createSimpleFlatItemModel(block);
        gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(dispatch));
    }

    public void createCreakingHeart(Block block) {
        ResourceLocation base = TexturedModel.COLUMN_ALT.create(block, gen.modelOutput);
        ResourceLocation baseSide = TexturedModel.COLUMN_HORIZONTAL_ALT.create(block, gen.modelOutput);
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

        gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(state));
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
        ).createWithSuffix(block, suffix, gen.modelOutput);
    }

    public void createMultiface(Block block, Item item) {
        gen.createSimpleFlatItemModel(item);
        ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(block);
        MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);
        Condition.TerminalCondition terminalCondition = Util.make(Condition.condition(), (terminalConditionx) -> BlockModelGenerators.MULTIFACE_GENERATOR.stream().map(Pair::getFirst).forEach((booleanProperty) -> {
            if (block.defaultBlockState().hasProperty(booleanProperty)) {
                terminalConditionx.term(booleanProperty, false);
            }
        }));

        for(Pair<BooleanProperty, Function<ResourceLocation, Variant>> pair : BlockModelGenerators.MULTIFACE_GENERATOR) {
            BooleanProperty booleanProperty = pair.getFirst();
            Function<ResourceLocation, Variant> function = pair.getSecond();
            if (block.defaultBlockState().hasProperty(booleanProperty)) {
                multiPartGenerator.with(Condition.condition().term(booleanProperty, true), function.apply(resourceLocation));
                multiPartGenerator.with(terminalCondition, function.apply(resourceLocation));
            }
        }

        gen.blockStateOutput.accept(multiPartGenerator);
    }

    public void createDriedGhastBlock() {
        ResourceLocation location = ModelLocationUtils.getModelLocation(ModBlocks.DRIED_GHAST.get(), "_hydration_0");
        gen.delegateItemModel(ModBlocks.DRIED_GHAST.get(), location);
        Function<Integer, ResourceLocation> function = integer -> {
            String string = switch (integer) {
                case 1 -> "_hydration_1";
                case 2 -> "_hydration_2";
                case 3 -> "_hydration_3";
                default -> "_hydration_0";
            };
            TextureMapping mapping = TextureMappings.driedGhast(string);
            return ModelTemplates.DRIED_GHAST.createWithSuffix(ModBlocks.DRIED_GHAST.get(), string, mapping, gen.modelOutput);
        };
        gen.blockStateOutput
            .accept(
                MultiVariantGenerator.multiVariant(ModBlocks.DRIED_GHAST.get())
                    .with(PropertyDispatch.property(DriedGhastBlock.HYDRATION_LEVEL).generate(integer -> Variant.variant().with(VariantProperties.MODEL, function.apply(integer))))
                    .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
    }
}