package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SpeleothemBlock extends Block implements Fallable, SimpleWaterloggedBlock {
    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape TIP_MERGE_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0, 0.0, 5.0, 11.0, 11.0, 11.0);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape BASE_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    protected final BlockState blockToGrowOn;

    public SpeleothemBlock(BlockState blockToGrowOn, BlockBehaviour.Properties properties) {
        super(properties);
        this.blockToGrowOn = blockToGrowOn;
        this.registerDefaultState(
            this.stateDefinition.any()
                .setValue(TIP_DIRECTION, Direction.UP)
                .setValue(THICKNESS, DripstoneThickness.TIP)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP_DIRECTION, THICKNESS, WATERLOGGED);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return this.isValidSpeleothemPlacement(level, pos, state.getValue(TIP_DIRECTION));
    }

    @Override
    public BlockState updateShape(
        BlockState state,
        Direction direction,
        BlockState neighborState,
        LevelAccessor level,
        BlockPos pos,
        BlockPos neighborPos
    ) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (direction != Direction.UP && direction != Direction.DOWN) {
            return state;
        } else {
            Direction tipDirection = state.getValue(TIP_DIRECTION);
            if (tipDirection == Direction.DOWN && level.getBlockTicks().hasScheduledTick(pos, this)) {
                return state;
            } else if (direction == tipDirection.getOpposite() && !this.canSurvive(state, level, pos)) {
                if (tipDirection == Direction.DOWN) {
                    level.scheduleTick(pos, this, 2);
                } else {
                    level.scheduleTick(pos, this, 1);
                }

                return state;
            } else {
                boolean merge = state.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE;
                DripstoneThickness thickness = this.calculateSpeleothemThickness(level, pos, tipDirection, merge);
                return state.setValue(THICKNESS, thickness);
            }
        }
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction defaultTipDirection = context.getNearestLookingVerticalDirection().getOpposite();
        Direction tipDirection = calculateTipDirection(level, pos, defaultTipDirection);
        if (tipDirection == null) {
            return null;
        } else {
            boolean merge = !context.isSecondaryUseActive();
            DripstoneThickness thickness = this.calculateSpeleothemThickness(level, pos, tipDirection, merge);
            return this.defaultBlockState()
                .setValue(TIP_DIRECTION, tipDirection)
                .setValue(THICKNESS, thickness)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
        }
    }

    @Nullable
    private Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction tipDirection) {
        if (this.isValidSpeleothemPlacement(level, pos, tipDirection)) {
            return tipDirection;
        } else {
            if (!this.isValidSpeleothemPlacement(level, pos, tipDirection.getOpposite())) {
                return null;
            }

            return tipDirection.getOpposite();
        }
    }

    private DripstoneThickness calculateSpeleothemThickness(LevelReader level, BlockPos pos, Direction tipDirection, boolean merge) {
        Direction direction = tipDirection.getOpposite();
        BlockState inFrontState = level.getBlockState(pos.relative(tipDirection));
        if (isSpeleothemWithDirection(inFrontState, direction)) {
            return !merge && inFrontState.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
        } else if (!isSpeleothemWithDirection(inFrontState, tipDirection)) {
            return DripstoneThickness.TIP;
        } else {
            DripstoneThickness inFrontThickness = inFrontState.getValue(THICKNESS);
            if (inFrontThickness != DripstoneThickness.TIP && inFrontThickness != DripstoneThickness.TIP_MERGE) {
                BlockState behindState = level.getBlockState(pos.relative(direction));
                return !isSpeleothemWithDirection(behindState, tipDirection) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
            } else {
                return DripstoneThickness.FRUSTUM;
            }
        }
    }

    private boolean isValidSpeleothemPlacement(LevelReader level, BlockPos pos, Direction tipDirection) {
        BlockPos behindPos = pos.relative(tipDirection.getOpposite());
        BlockState behindState = level.getBlockState(behindPos);
        return behindState.isFaceSturdy(level, behindPos, tipDirection) || isSpeleothemWithDirection(behindState, tipDirection) && behindState.is(this);
    }

    private static boolean isSpeleothemWithDirection(BlockState state, Direction dir) {
        return state.is(ModBlockTags.SPELEOTHEMS) && state.getValue(TIP_DIRECTION) == dir;
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide()) {
            BlockPos blockPos = hit.getBlockPos();
            if (
                projectile.mayInteract(level, blockPos) &&
                projectile instanceof ThrownTrident &&
                projectile.getDeltaMovement().length() > 0.6
            ) {
                level.destroyBlock(blockPos, true);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isStalagmite(state) && !this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        } else {
            spawnFallingStalactite(state, level, pos);
        }
    }

    private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
        MutableBlockPos fallPos = pos.mutable();
        BlockState fallState = state;

        while (isStalactite(fallState)) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, fallPos, fallState);
            if (isTip(fallState, true)) {
                int size = Math.max(1 + pos.getY() - fallPos.getY(), 6);
                float damagePerFallDistance = (float) size;
                fallingBlockEntity.setHurtsEntities(damagePerFallDistance, 40);
                break;
            }

            fallPos.move(Direction.DOWN);
            fallState = level.getBlockState(fallPos);
        }
    }

    private static boolean isStalagmite(BlockState state) {
        return isSpeleothemWithDirection(state, Direction.UP);
    }

    private static boolean isStalactite(BlockState state) {
        return isSpeleothemWithDirection(state, Direction.DOWN);
    }

    private static boolean isTip(BlockState state, boolean merge) {
        if (!state.is(ModBlockTags.SPELEOTHEMS)) {
            return false;
        } else {
            DripstoneThickness thickness = state.getValue(THICKNESS);
            return thickness == DripstoneThickness.TIP || merge && thickness == DripstoneThickness.TIP_MERGE;
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            level.levelEvent(1045, pos, 0);
        }
    }

    @Override
    public DamageSource getFallDamageSource(Entity entity) {
        return entity.damageSources().fallingStalactite(entity);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(THICKNESS)) {
            case TIP_MERGE -> TIP_MERGE_SHAPE;
            case TIP -> state.getValue(TIP_DIRECTION) == Direction.DOWN ? TIP_SHAPE_DOWN : TIP_SHAPE_UP;
            case FRUSTUM -> FRUSTUM_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            case BASE -> BASE_SHAPE;
        };
        Vec3 offset = state.getOffset(level, pos);
        return shape.move(offset.x, 0.0, offset.z);
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.125F;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.011377778F && isStalactiteStartPos(state, level, pos)) {
            this.tryGrowStalactiteOrStalagmiteIfPossible(state, level, pos, random);
        }
    }

    private static boolean isStalactiteStartPos(BlockState state, LevelReader level, BlockPos pos) {
        return isStalactite(state) && !level.getBlockState(pos.above()).is(Blocks.POINTED_DRIPSTONE);
    }

    public void tryGrowStalactiteOrStalagmiteIfPossible(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.canGrow(level, pos)) {
            BlockPos stalactiteTipPos = findTip(state, level, pos, 7, false);
            if (stalactiteTipPos != null) {
                BlockState stalactiteTipState = level.getBlockState(stalactiteTipPos);
                if (isFreeHangingStalactite(stalactiteTipState) && this.canTipGrow(stalactiteTipState, level, stalactiteTipPos)) {
                    if (random.nextBoolean()) {
                        this.grow(level, stalactiteTipPos, Direction.DOWN);
                    } else {
                        this.growStalagmiteBelow(level, stalactiteTipPos);
                    }
                }
            }
        }
    }

    protected boolean canGrow(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(this.blockToGrowOn.getBlock());
    }

    @Nullable
    private static BlockPos findTip(BlockState speleothemState, LevelAccessor level, BlockPos speleothemPos, int maxSearchLength, boolean merge) {
        if (isTip(speleothemState, merge)) {
            return speleothemPos;
        } else {
            Direction searchDirection = speleothemState.getValue(TIP_DIRECTION);
            BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> state.is(speleothemState.getBlock()) && state.getValue(TIP_DIRECTION) == searchDirection;
            return findBlockVertical(
                level,
                speleothemPos,
                searchDirection.getAxisDirection(),
                pathPredicate,
                speleothem -> isTip(speleothem, merge),
                maxSearchLength
            ).orElse(null);
        }
    }

    private static Optional<BlockPos> findBlockVertical(
        LevelAccessor level,
        BlockPos pos,
        Direction.AxisDirection axis,
        BiPredicate<BlockPos, BlockState> pathPredicate,
        Predicate<BlockState> targetPredicate,
        int maxSteps
    ) {
        Direction direction = Direction.get(axis, Direction.Axis.Y);
        MutableBlockPos mutablePos = pos.mutable();

        for(int i = 1; i < maxSteps; ++i) {
            mutablePos.move(direction);
            BlockState state = level.getBlockState(mutablePos);
            if (targetPredicate.test(state)) {
                return Optional.of(mutablePos.immutable());
            }

            if (level.isOutsideBuildHeight(mutablePos.getY()) || !pathPredicate.test(mutablePos, state)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private boolean canTipGrow(BlockState state, ServerLevel level, BlockPos pos) {
        Direction growDirection = state.getValue(TIP_DIRECTION);
        BlockPos growPos = pos.relative(growDirection);
        BlockState stateAtGrowPos = level.getBlockState(growPos);
        if (!stateAtGrowPos.getFluidState().isEmpty()) {
            return false;
        } else {
            return stateAtGrowPos.isAir() || this.isUnmergedTipWithDirection(stateAtGrowPos, growDirection.getOpposite());
        }
    }

    private boolean isUnmergedTipWithDirection(BlockState state, Direction dir) {
        return isTip(state, false) && state.getValue(TIP_DIRECTION) == dir && state.is(this);
    }

    private void grow(ServerLevel server, BlockPos pos, Direction direction) {
        BlockPos targetPos = pos.relative(direction);
        BlockState existingStateAtTargetPos = server.getBlockState(targetPos);
        if (this.isUnmergedTipWithDirection(existingStateAtTargetPos, direction.getOpposite())) {
            this.createMergedTips(existingStateAtTargetPos, server, targetPos);
        } else if (existingStateAtTargetPos.isAir() || existingStateAtTargetPos.is(Blocks.WATER)) {
            this.createSpeleothem(server, targetPos, direction, DripstoneThickness.TIP);
        }
    }

    private void createSpeleothem(LevelAccessor level, BlockPos pos, Direction direction, DripstoneThickness thickness) {
        BlockState state = this.defaultBlockState()
            .setValue(TIP_DIRECTION, direction)
            .setValue(THICKNESS, thickness)
            .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
        level.setBlock(pos, state, 3);
    }

    private void createMergedTips(BlockState state, LevelAccessor level, BlockPos tipPos) {
        BlockPos stalactitePos;
        BlockPos stalagmitePos;
        if (state.getValue(TIP_DIRECTION) == Direction.UP) {
            stalagmitePos = tipPos;
            stalactitePos = tipPos.above();
        } else {
            stalactitePos = tipPos;
            stalagmitePos = tipPos.below();
        }

        createSpeleothem(level, stalactitePos, Direction.DOWN, DripstoneThickness.TIP_MERGE);
        createSpeleothem(level, stalagmitePos, Direction.UP, DripstoneThickness.TIP_MERGE);
    }

    private void growStalagmiteBelow(ServerLevel level, BlockPos posAboveStalagmite) {
        MutableBlockPos pos = posAboveStalagmite.mutable();

        for(int i = 0; i < 10; ++i) {
            pos.move(Direction.DOWN);
            BlockState state = level.getBlockState(pos);
            if (!state.getFluidState().isEmpty()) {
                return;
            }

            if (this.isUnmergedTipWithDirection(state, Direction.UP) && this.canTipGrow(state, level, pos)) {
                this.grow(level, pos, Direction.UP);
                return;
            }

            if (this.isValidSpeleothemPlacement(level, pos, Direction.UP) && !level.isWaterAt(pos.below())) {
                this.grow(level, pos.below(), Direction.UP);
                return;
            }

            if (!this.blocksStalagmiteScan(level, pos, state)) {
                return;
            }
        }
    }

    private boolean blocksStalagmiteScan(BlockGetter level, BlockPos pos, BlockState state) {
        return false;
    }

    public static boolean isFreeHangingStalactite(BlockState state) {
        return isStalactite(state) && state.getValue(THICKNESS) == DripstoneThickness.TIP && !state.getValue(WATERLOGGED);
    }
}
