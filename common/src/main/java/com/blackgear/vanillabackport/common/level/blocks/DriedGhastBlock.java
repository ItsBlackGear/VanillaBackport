package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.common.registries.ModBlockStateProperties;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DriedGhastBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<DriedGhastBlock> CODEC = simpleCodec(DriedGhastBlock::new);
    public static final int MAX_HYDRATION_LEVEL = 3;
    public static final IntegerProperty HYDRATION_LEVEL = ModBlockStateProperties.HYDRATION_LEVEL;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final int HYDRATION_TICK_DELAY = 5000;
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);

    @Override
    public MapCodec<DriedGhastBlock> codec() {
        return CODEC;
    }

    public DriedGhastBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.getStateDefinition()
                .any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HYDRATION_LEVEL, 0)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HYDRATION_LEVEL, WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public int getHydrationLevel(BlockState state) {
        return state.getValue(HYDRATION_LEVEL);
    }

    private boolean isReadyToSpawn(BlockState state) {
        return this.getHydrationLevel(state) == MAX_HYDRATION_LEVEL;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            this.tickWaterlogged(state, level, pos, random);
        } else {
            int hydrationLevel = this.getHydrationLevel(state);
            if (hydrationLevel > 0) {
                level.setBlock(pos, state.setValue(HYDRATION_LEVEL, hydrationLevel - 1), 2);
            }
        }
    }

    private void tickWaterlogged(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.isReadyToSpawn(state)) {
            level.playSound(null, pos, ModSoundEvents.DRIED_GHAST_TRANSITION.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, state.setValue(HYDRATION_LEVEL, this.getHydrationLevel(state) + 1), 2);
        } else {
            this.spawnGhastling(level, pos, state);
        }
    }

    private void spawnGhastling(ServerLevel level, BlockPos pos, BlockState state) {
        level.removeBlock(pos, false);
        HappyGhast ghast = ModEntities.HAPPY_GHAST.get().create(level);
        if (ghast != null) {
            Vec3 center = Vec3.atBottomCenterOf(pos);
            ghast.setBaby(true);
            float yRot = getYRot(state.getValue(FACING));
            ghast.setYHeadRot(yRot);
            ghast.setPosRaw(center.x(), center.y(), center.z());
            ghast.setYRot(yRot);
            ghast.setXRot(0.0F);
            ghast.setOldPosAndRot();
            ghast.setPos(ghast.position().x(), ghast.position().y(), ghast.position().z());
            level.addFreshEntity(ghast);
            level.playSound(null, ghast, ModSoundEvents.GHASTLING_SPAWN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public static float getYRot(Direction direction) {
        return switch (direction) {
            case NORTH -> 180.0F;
            case SOUTH -> 0.0F;
            case WEST -> 90.0F;
            case EAST -> -90.0F;
            default -> throw new IllegalStateException("No y-Rot for vertical axis: " + direction);
        };
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        if (!state.getValue(WATERLOGGED)) {
            if (random.nextInt(40) == 0) {
                level.playLocalSound(x, y, z, ModSoundEvents.DRIED_GHAST_AMBIENT.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }

            if (random.nextInt(6) == 0) {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.02, 0.0);
            }
        } else {
            if (random.nextInt(40) == 0) {
                level.playLocalSound(x, y, z, ModSoundEvents.DRIED_GHAST_AMBIENT_WATER.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }

            if (random.nextInt(6) == 0) {
                level.addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    x + (random.nextFloat() * 2.0F - 1.0F) / 3.0F,
                    y + 0.4,
                    z + (random.nextFloat() * 2.0F - 1.0F) / 3.0F,
                    0.0,
                    random.nextFloat(),
                    0.0
                );
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if ((state.getValue(WATERLOGGED) || state.getValue(HYDRATION_LEVEL) > 0) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.scheduleTick(pos, this, HYDRATION_TICK_DELAY);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState state = context.getLevel().getFluidState(context.getClickedPos());
        boolean isWaterlogged = state.is(FluidTags.WATER) && state.getAmount() == 8;
        return super.getStateForPlacement(context)
            .setValue(WATERLOGGED, isWaterlogged)
            .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.FLOWING_WATER) {
            if (!level.isClientSide()) {
                Block.dropResources(state, level, pos, null);
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            }

            return true;
        } else {
            return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (state.getValue(HYDRATION_LEVEL) == 0) {
            level.playSound(null, pos, state.getValue(WATERLOGGED) ? ModSoundEvents.DRIED_GHAST_PLACE_IN_WATER.get() : ModSoundEvents.DRIED_GHAST_PLACE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}