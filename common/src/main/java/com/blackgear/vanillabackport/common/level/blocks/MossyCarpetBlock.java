package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MossyCarpetBlock extends Block implements BonemealableBlock {
    public static final MapCodec<MossyCarpetBlock> CODEC = simpleCodec(MossyCarpetBlock::new);
    public static final BooleanProperty BASE = BlockStateProperties.BOTTOM;
    private static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
    private static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
    private static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
    private static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
    private static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(
        Util.make(Maps.newEnumMap(Direction.class), enumMap -> {
            enumMap.put(Direction.NORTH, NORTH);
            enumMap.put(Direction.EAST, EAST);
            enumMap.put(Direction.SOUTH, SOUTH);
            enumMap.put(Direction.WEST, WEST);
        })
    );
    private static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHORT_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 10.0, 16.0);
    private static final VoxelShape EAST_SHORT_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 10.0, 16.0);
    private static final VoxelShape NORTH_SHORT_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 1.0);
    private static final VoxelShape SOUTH_SHORT_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 10.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesCache;

    @Override
    public MapCodec<MossyCarpetBlock> codec() {
        return CODEC;
    }

    public MossyCarpetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.getStateDefinition()
                .any()
                .setValue(BASE, true)
                .setValue(NORTH, WallSide.NONE)
                .setValue(EAST, WallSide.NONE)
                .setValue(SOUTH, WallSide.NONE)
                .setValue(WEST, WallSide.NONE)
        );
        this.shapesCache = ImmutableMap.copyOf(
            this.getStateDefinition()
                .getPossibleStates()
                .stream()
                .collect(Collectors.toMap(Function.identity(), MossyCarpetBlock::calculateShape))
        );
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape shape = Shapes.empty();
        if (state.getValue(BASE)) shape = DOWN_AABB;

        shape = switch (state.getValue(NORTH)) {
            case NONE -> shape;
            case LOW -> Shapes.or(shape, NORTH_SHORT_AABB);
            case TALL -> Shapes.or(shape, NORTH_AABB);
        };

        shape = switch (state.getValue(SOUTH)) {
            case NONE -> shape;
            case LOW -> Shapes.or(shape, SOUTH_SHORT_AABB);
            case TALL -> Shapes.or(shape, SOUTH_AABB);
        };

        shape = switch (state.getValue(EAST)) {
            case NONE -> shape;
            case LOW -> Shapes.or(shape, EAST_SHORT_AABB);
            case TALL -> Shapes.or(shape, EAST_AABB);
        };

        shape = switch (state.getValue(WEST)) {
            case NONE -> shape;
            case LOW -> Shapes.or(shape, WEST_SHORT_AABB);
            case TALL -> Shapes.or(shape, WEST_AABB);
        };

        return shape.isEmpty() ? Shapes.block() : shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapesCache.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(BASE) ? DOWN_AABB : Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState floorState = level.getBlockState(pos.below());
        return state.getValue(BASE)
            ? !floorState.isAir()
            : floorState.is(this)
            && floorState.getValue(BASE);
    }

    private static boolean hasFaces(BlockState state) {
        if (state.getValue(BASE)) {
            return true;
        }
        return PROPERTY_BY_DIRECTION.values()
            .stream()
            .anyMatch(property -> state.getValue(property) != WallSide.NONE);
    }

    private static boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
        BlockPos adjacent = pos.relative(direction);
        BlockState adjacentState = level.getBlockState(adjacent);
        return direction != Direction.UP && MultifaceBlock.canAttachTo(level, direction, adjacent, adjacentState);
    }

    private static BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos, boolean flag) {
        BlockState aboveState = null;
        BlockState belowState = null;
        flag |= state.getValue(BASE);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            EnumProperty<WallSide> property = getPropertyForFace(direction);
            WallSide wallSide = canSupportAtFace(level, pos, direction)
                ? (flag ? WallSide.LOW : state.getValue(property))
                : WallSide.NONE;

            if (wallSide == WallSide.LOW) {
                if (aboveState == null) {
                    aboveState = level.getBlockState(pos.above());
                }

                if (aboveState.is(ModBlocks.PALE_MOSS_CARPET.get())
                    && aboveState.getValue(property) != WallSide.NONE
                    && !aboveState.getValue(BASE)
                ) {
                    wallSide = WallSide.TALL;
                }

                if (!state.getValue(BASE)) {
                    if (belowState == null) {
                        belowState = level.getBlockState(pos.below());
                    }

                    if (belowState.is(ModBlocks.PALE_MOSS_CARPET.get()) && belowState.getValue(property) == WallSide.NONE) {
                        wallSide = WallSide.NONE;
                    }
                }
            }

            state = state.setValue(property, wallSide);
        }

        return state;
    }

    public static void placeAt(LevelAccessor level, BlockPos pos, RandomSource random, int flag) {
        BlockState base = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState();
        BlockState updatedState = getUpdatedState(base, level, pos, true);
        level.setBlock(pos, updatedState, flag);
        BlockState topperState = createTopperWithSideChance(level, pos, random::nextBoolean);

        if (!topperState.isAir()) {
            level.setBlock(pos.above(), topperState, flag);
            BlockState reUpdatedState = getUpdatedState(updatedState, level, pos, true);
            level.setBlock(pos, reUpdatedState, flag);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            RandomSource random = level.getRandom();
            BlockState topperState = createTopperWithSideChance(level, pos, random::nextBoolean);
            if (!topperState.isAir()) {
                level.setBlock(pos.above(), topperState, 3);
            }
        }
    }

    private static BlockState createTopperWithSideChance(BlockGetter level, BlockPos pos, BooleanSupplier flag) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        boolean isCarpet = aboveState.is(ModBlocks.PALE_MOSS_CARPET.get());

        if ((!isCarpet || !aboveState.getValue(BASE))
            && (isCarpet || aboveState.canBeReplaced())
        ) {
            BlockState baselessCarpet = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState().setValue(BASE, false);
            BlockState updatedState = getUpdatedState(baselessCarpet, level, pos.above(), true);

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                EnumProperty<WallSide> property = getPropertyForFace(direction);
                if (updatedState.getValue(property) != WallSide.NONE && !flag.getAsBoolean()) {
                    updatedState = updatedState.setValue(property, WallSide.NONE);
                }
            }

            return hasFaces(updatedState) && updatedState != aboveState
                ? updatedState
                : Blocks.AIR.defaultBlockState();
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getUpdatedState(this.defaultBlockState(), context.getLevel(), context.getClickedPos(), true);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState updatedState = getUpdatedState(state, level, pos, false);
            return !hasFaces(updatedState) ? Blocks.AIR.defaultBlockState() : updatedState;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BASE, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> state.setValue(NORTH, state.getValue(SOUTH))
                .setValue(EAST, state.getValue(WEST))
                .setValue(SOUTH, state.getValue(NORTH))
                .setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> state.setValue(NORTH, state.getValue(EAST))
                .setValue(EAST, state.getValue(SOUTH))
                .setValue(SOUTH, state.getValue(WEST))
                .setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 -> state.setValue(NORTH, state.getValue(WEST))
                .setValue(EAST, state.getValue(NORTH))
                .setValue(SOUTH, state.getValue(EAST))
                .setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> super.mirror(state, mirror);
        };
    }

    @Nullable
    public static EnumProperty<WallSide> getPropertyForFace(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(BASE) && !createTopperWithSideChance(level, pos, () -> true).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState topperState = createTopperWithSideChance(level, pos, () -> true);
        if (!topperState.isAir()) {
            level.setBlock(pos.above(), topperState, 3);
        }
    }
}