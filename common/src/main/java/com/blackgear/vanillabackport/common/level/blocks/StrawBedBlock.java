package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.util.BlockShaper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class StrawBedBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    
    private static final VoxelShape BASE_SHAPE = BlockShaper.column(16.0, 0.0, 4.0);
    private static final VoxelShape PILLOW_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 8.0);
    private static final Map<Direction, VoxelShape> FOOT_SHAPES = Util.make(() -> BlockShaper.rotateHorizontal(BASE_SHAPE));
    private static final Map<Direction, VoxelShape> HEAD_SHAPES = Util.make(() -> BlockShaper.rotateHorizontal(Shapes.or(BASE_SHAPE, PILLOW_SHAPE)));
    
    public StrawBedBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(PART, BedPart.FOOT)
            .setValue(OCCUPIED, false));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Map<Direction, VoxelShape> shapes = state.getValue(PART) == BedPart.HEAD ? HEAD_SHAPES : FOOT_SHAPES;
        return shapes.get(getConnectedDirection(state).getOpposite());
    }
    
    private void destroyBed(Level level, BlockPos pos) {
        level.playSound(null, pos, ModSoundEvents.STRAW_BED_BREAK_LEAVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        
        BedPart part = state.getValue(PART);
        BlockPos otherPos = pos.relative(getNeighbourDirection(part, state.getValue(FACING)));
        BlockState otherState = level.getBlockState(otherPos);
        
        if (!otherState.is(this) || otherState.getValue(PART) == part) {
            return InteractionResult.CONSUME;
        }
        
        if (part != BedPart.HEAD) {
            pos = otherPos;
            state = otherState;
        }
        
        if (!level.dimensionType().bedWorks()) {
            this.destroyBed(level, pos);
            return InteractionResult.SUCCESS;
        }
        
        if (state.getValue(OCCUPIED)) {
            player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            return InteractionResult.SUCCESS;
        }
        
        player.startSleepInBed(pos).ifLeft(problem -> {
            if (problem.getMessage() != null) {
                player.displayClientMessage(problem.getMessage(), true);
            }
        }).ifRight(unit -> player.awardStat(Stats.SLEEP_IN_BED));
        
        return InteractionResult.SUCCESS;
    }
    
    public void onStopSleeping(Level level, BlockPos pos) {
        this.destroyBed(level, pos);
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            return neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART)
                ? state.setValue(OCCUPIED, neighborState.getValue(OCCUPIED))
                : Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
    
    private static Direction getNeighbourDirection(BedPart part, Direction facing) {
        return part == BedPart.FOOT ? facing : facing.getOpposite();
    }
    
    public static Direction getConnectedDirection(BlockState state) {
        Direction facing = state.getValue(FACING);
        return state.getValue(PART) == BedPart.HEAD ? facing.getOpposite() : facing;
    }
    
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && player.isCreative()) {
            BedPart part = state.getValue(PART);
            if (part == BedPart.FOOT) {
                BlockPos headPos = pos.relative(getNeighbourDirection(part, state.getValue(FACING)));
                BlockState headState = level.getBlockState(headPos);
                if (headState.is(this) && headState.getValue(PART) == BedPart.HEAD) {
                    level.setBlock(headPos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, headPos, Block.getId(headState));
                }
            }
        }
        
        super.playerWillDestroy(level, pos, state, player);
    }
    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockPos headPos = pos.relative(state.getValue(FACING));
        level.setBlockAndUpdate(headPos, state.setValue(PART, BedPart.HEAD));
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        BlockPos relative = pos.relative(facing);
        Level level = context.getLevel();
        
        return level.getBlockState(relative).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(relative)
            ? this.defaultBlockState().setValue(FACING, facing)
            : null;
    }
    
    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        BlockPos sourcePos = pos.relative(state.getValue(FACING), state.getValue(PART) == BedPart.HEAD ? 0 : 1);
        return Mth.getSeed(sourcePos.getX(), pos.getY(), sourcePos.getZ());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }
    
    public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
        return state.getValue(PART) == BedPart.HEAD ? DoubleBlockCombiner.BlockType.FIRST : DoubleBlockCombiner.BlockType.SECOND;
    }
    
    private static boolean isBunkBed(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() instanceof StrawBedBlock;
    }
    
    public static Optional<Vec3> findStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos, Direction forward, float yaw) {
        Direction right = forward.getClockWise();
        Direction side = right.isFacingAngle(yaw) ? right.getOpposite() : right;
        if (isBunkBed(level, pos)) {
            return findBunkBedStandUpPosition(type, level, pos, forward, side);
        } else {
            int[][] offsets = bedStandUpOffsets(forward, side);
            Optional<Vec3> safePosition = findStandUpPositionAtOffset(type, level, pos, offsets, true);
            return safePosition.isPresent() ? safePosition : findStandUpPositionAtOffset(type, level, pos, offsets, false);
        }
    }
    
    private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos, Direction forward, Direction side) {
        int[][] offsets = bedSurroundStandUpOffsets(forward, side);
        Optional<Vec3> safePosition = findStandUpPositionAtOffset(type, level, pos, offsets, true);
        if (safePosition.isPresent()) {
            return safePosition;
        } else {
            BlockPos below = pos.below();
            Optional<Vec3> belowSafePosition = findStandUpPositionAtOffset(type, level, below, offsets, true);
            if (belowSafePosition.isPresent()) {
                return belowSafePosition;
            } else {
                int[][] aboveOffsets = bedAboveStandUpOffsets(forward);
                Optional<Vec3> aboveSafePosition = findStandUpPositionAtOffset(type, level, pos, aboveOffsets, true);
                if (aboveSafePosition.isPresent()) {
                    return aboveSafePosition;
                } else {
                    Optional<Vec3> unsafePosition = findStandUpPositionAtOffset(type, level, pos, offsets, false);
                    if (unsafePosition.isPresent()) {
                        return unsafePosition;
                    } else {
                        Optional<Vec3> belowUnsafePosition = findStandUpPositionAtOffset(type, level, below, offsets, false);
                        return belowUnsafePosition.isPresent() ? belowUnsafePosition : findStandUpPositionAtOffset(type, level, pos, aboveOffsets, false);
                    }
                }
            }
        }
    }
    
    private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> type, CollisionGetter level, BlockPos pos, int[][] offsets, boolean checkDangerous) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (int[] offset : offsets) {
            blockPos.set(pos.getX() + offset[0], pos.getY(), pos.getZ() + offset[1]);
            Vec3 position = DismountHelper.findSafeDismountLocation(type, level, blockPos, checkDangerous);
            if (position != null) {
                return Optional.of(position);
            }
        }
        return Optional.empty();
    }
    
    private static int[][] bedStandUpOffsets(Direction forward, Direction side) {
        return ArrayUtils.addAll(bedSurroundStandUpOffsets(forward, side), bedAboveStandUpOffsets(forward));
    }
    
    private static int[][] bedSurroundStandUpOffsets(Direction forward, Direction side) {
        return new int[][]{
            {side.getStepX(), side.getStepZ()},
            {side.getStepX() - forward.getStepX(), side.getStepZ() - forward.getStepZ()},
            {side.getStepX() - forward.getStepX() * 2, side.getStepZ() - forward.getStepZ() * 2},
            {-forward.getStepX() * 2, -forward.getStepZ() * 2},
            {-side.getStepX() - forward.getStepX() * 2, -side.getStepZ() - forward.getStepZ() * 2},
            {-side.getStepX() - forward.getStepX(), -side.getStepZ() - forward.getStepZ()},
            {-side.getStepX(), -side.getStepZ()},
            {-side.getStepX() + forward.getStepX(), -side.getStepZ() + forward.getStepZ()},
            {forward.getStepX(), forward.getStepZ()},
            {side.getStepX() + forward.getStepX(), side.getStepZ() + forward.getStepZ()}
        };
    }
    
    private static int[][] bedAboveStandUpOffsets(Direction forward) {
        return new int[][]{{0, 0}, {-forward.getStepX(), -forward.getStepZ()}};
    }
}