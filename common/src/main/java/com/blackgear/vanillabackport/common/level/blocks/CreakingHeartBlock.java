package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.blockentities.CreakingHeartBlockEntity;
import com.blackgear.vanillabackport.common.level.blocks.blockstates.CreakingHeartState;
import com.blackgear.vanillabackport.common.registries.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.ModBlockStateProperties;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class CreakingHeartBlock extends BaseEntityBlock {
    public static final MapCodec<CreakingHeartBlock> CODEC = simpleCodec(CreakingHeartBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<CreakingHeartState> STATE = ModBlockStateProperties.CREAKING_HEART_STATE;
    public static final BooleanProperty NATURAL = ModBlockStateProperties.NATURAL;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public CreakingHeartBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(STATE, CreakingHeartState.UPROOTED)
                .setValue(NATURAL, false)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CreakingHeartBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        } else {
            return state.getValue(STATE) != CreakingHeartState.UPROOTED
                ? createTickerHelper(blockEntityType, ModBlockEntities.CREAKING_HEART.get(), CreakingHeartBlockEntity::serverTick)
                : null;
        }
    }

    public static boolean isNaturalNight(Level level) {
        if (!level.dimensionType().natural()) {
            return false;
        } else {
            int ticks = (int) (level.getDayTime() % 24000L);
            return ticks >= 12600 && ticks <= 23400;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isNaturalNight(level)) {
            if (state.getValue(STATE) != CreakingHeartState.UPROOTED) {
                if (random.nextInt(16) == 0 && isSurroundedByLogs(level, pos)) {
                    level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.CREAKING_HEART_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, 1);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState updatedState = updateState(state, level, pos);
        if (updatedState != state) {
            level.setBlock(pos, updatedState, 3);
        }
    }

    private static BlockState updateState(BlockState state, Level level, BlockPos pos) {
        boolean hasRequiredLogs = hasRequiredLogs(state, level, pos);
        boolean isUprooted = state.getValue(STATE) == CreakingHeartState.UPROOTED;
        return hasRequiredLogs && isUprooted
            ? state.setValue(STATE, isNaturalNight(level) ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT)
            : state;
    }

    public static boolean hasRequiredLogs(BlockState state, LevelReader level, BlockPos pos) {
        Direction.Axis axis = state.getValue(AXIS);

        for (Direction direction : directions(axis)) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            if (!neighborState.is(ModBlockTags.PALE_OAK_LOGS) || neighborState.getValue(AXIS) != axis) {
                return false;
            }
        }

        return true;
    }

    private static Direction[] directions(Direction.Axis axis) {
        return switch (axis) {
            case X -> new Direction[]{Direction.NORTH, Direction.SOUTH};
            case Y -> new Direction[]{Direction.UP, Direction.DOWN};
            case Z -> new Direction[]{Direction.EAST, Direction.WEST};
        };
    }

    private static boolean isSurroundedByLogs(LevelAccessor level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            if (!neighborState.is(ModBlockTags.PALE_OAK_LOGS)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return updateState(
            this.defaultBlockState()
                .setValue(AXIS, context.getClickedFace().getAxis()),
            context.getLevel(),
            context.getClickedPos()
        );
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(state, rotation);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, STATE, NATURAL);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.getBlockEntity(pos) instanceof CreakingHeartBlockEntity heart) {
            heart.removeProtector(player.damageSources().playerAttack(player));
            this.tryAwardExperience(player, state, level, pos);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    private void tryAwardExperience(Player player, BlockState state, Level level, BlockPos pos) {
        if (!player.getAbilities().instabuild
            && !player.isSpectator()
            && state.getValue(NATURAL)
            && level instanceof ServerLevel server
        ) {
            this.popExperience(server, pos, level.random.nextIntBetweenInclusive(20, 24));
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (state.getValue(STATE) == CreakingHeartState.UPROOTED) {
            return 0;
        } else {
            return level.getBlockEntity(pos) instanceof CreakingHeartBlockEntity heart
                ? heart.getAnalogOutputSignal()
                : 0;
        }
    }
}