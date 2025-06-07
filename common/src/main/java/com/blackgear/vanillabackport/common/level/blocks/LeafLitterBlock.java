package com.blackgear.vanillabackport.common.level.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

public class LeafLitterBlock extends BushBlock {
    public static final MapCodec<LeafLitterBlock> CODEC = simpleCodec(LeafLitterBlock::new);
    public static final int MIN_SEGMENT = 1;
    public static final int MAX_SEGMENT = 4;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = BlockStateProperties.FLOWER_AMOUNT;
    private static final BiFunction<Direction, Integer, VoxelShape> SHAPE_BY_PROPERTIES = Util.memoize((direction, value) -> {
        VoxelShape shape = Shapes.empty();
        VoxelShape[] shapes = new VoxelShape[] {
            Block.box(8.0F, 0.0F, 8.0F, 16.0F, 3.0F, 16.0F),
            Block.box(8.0F, 0.0F, 0.0F, 16.0F, 3.0F, 8.0F),
            Block.box(0.0F, 0.0F, 0.0F, 8.0F, 3.0F, 8.0F),
            Block.box(0.0F, 0.0F, 8.0F, 8.0F, 3.0F, 16.0F)
        };

        for (int index = 0; index < value; ++index) {
            int i = Math.floorMod(index - direction.get2DDataValue(), 4);
            shape = Shapes.or(shape, shapes[i]);
        }

        return shape.singleEncompassing();
    });

    @Override
    public MapCodec<LeafLitterBlock> codec() {
        return CODEC;
    }

    public LeafLitterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.getStateDefinition()
                .any()
                .setValue(FACING, Direction.NORTH)
                .setValue(AMOUNT, MIN_SEGMENT)
        );
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive()
            && useContext.getItemInHand().is(this.asItem())
            && state.getValue(AMOUNT) < MAX_SEGMENT
            || super.canBeReplaced(state, useContext);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_PROPERTIES.apply(state.getValue(FACING), state.getValue(AMOUNT));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        return state.is(this)
            ? state.setValue(AMOUNT, Math.min(MAX_SEGMENT, state.getValue(AMOUNT) + 1))
            : this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AMOUNT);
    }
}
