package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlockStateProperties;
import com.blackgear.vanillabackport.core.util.BlockShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.function.Function;

public interface SegmentableBlock {
    int MIN_SEGMENT = 1;
    int MAX_SEGMENT = 4;
    IntegerProperty AMOUNT = ModBlockStateProperties.SEGMENT_AMOUNT;
    
    default Function<BlockState, VoxelShape> getShapeCalculator(DirectionProperty facing, IntegerProperty amount) {
        Map<Direction, VoxelShape> shapes = BlockShaper.rotateHorizontal(Block.box(0.0, 0.0, 0.0, 8.0, this.getShapeHeight(), 8.0));
        return state -> {
            VoxelShape shape = Shapes.empty();
            Direction direction = state.getValue(facing);
            int count = state.getValue(amount);
            
            for (int i = 0; i < count; i++) {
                shape = Shapes.or(shape, shapes.get(direction));
                direction = direction.getCounterClockWise();
            }
            
            return shape.singleEncompassing();
        };
    }
    
    default IntegerProperty getSegmentAmountProperty() {
        return AMOUNT;
    }
    
    default double getShapeHeight() {
        return 1.0;
    }
    
    default boolean canBeReplaced(BlockState state, BlockPlaceContext context, IntegerProperty segment) {
        return !context.isSecondaryUseActive() && context.getItemInHand().is(state.getBlock().asItem()) && state.getValue(segment) < MAX_SEGMENT;
    }
    
    default BlockState getStateForPlacement(BlockPlaceContext context, Block block, IntegerProperty segment, DirectionProperty facing) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        return state.is(block)
            ? state.setValue(segment, Math.min(MAX_SEGMENT, state.getValue(segment) + MIN_SEGMENT))
            : block.defaultBlockState().setValue(facing, context.getHorizontalDirection().getOpposite());
    }
}