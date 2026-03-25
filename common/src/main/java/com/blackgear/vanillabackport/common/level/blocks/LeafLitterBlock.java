package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.registries.ModBlockStateProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class LeafLitterBlock extends BushBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = ModBlockStateProperties.SEGMENT_AMOUNT;
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

        if (shape.isEmpty()) {
            return Shapes.empty();
        } else {
            return Shapes.box(shape.min(Axis.X), shape.min(Axis.Y), shape.min(Axis.Z), shape.max(Axis.X), shape.max(Axis.Y), shape.max(Axis.Z));
        }
    });

    public LeafLitterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.getStateDefinition()
                .any()
                .setValue(FACING, Direction.NORTH)
                .setValue(AMOUNT, 1));
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
            && useContext.getItemInHand().is(state.getBlock().asItem())
            && state.getValue(AMOUNT) < 4
            || super.canBeReplaced(state, useContext);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_PROPERTIES.apply(state.getValue(FACING), state.getValue(AMOUNT));
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        return state.is(this)
            ? state.setValue(AMOUNT, Math.min(4, state.getValue(AMOUNT) + 1))
            : this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AMOUNT);
    }
}