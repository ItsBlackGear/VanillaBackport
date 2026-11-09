package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IcicleBlock extends SpeleothemBlock {
    private static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
    private static final List<Direction> ONLY_GROW_STALACTITES = List.of(Direction.DOWN);
    
    public IcicleBlock(BlockState blockToGrowOn, Properties properties) {
        super(blockToGrowOn, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ATTACHED, false));
    }
    
    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOnDamage(level, state, pos, entity, fallDistance);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ATTACHED);
    }
    
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        } else {
            boolean attached = isStalactiteStartPos(state, context.getLevel(), context.getClickedPos()) || isStalagmiteStartPos(state, context.getLevel(), context.getClickedPos());
            return state.setValue(ATTACHED, attached);
        }
    }
    
    @Override
    protected LevelEvents getStalactiteLandingSound() {
        return LevelEvents.SOUND_ICICLE_LAND;
    }
    
    @Override
    protected int getMaxGrowthLength() {
        return 4;
    }
    
    @Override
    protected boolean canGrow(LevelReader level, BlockPos pos) {
        if (!super.canGrow(level, pos)) {
            return false;
        } else {
            return !this.wouldMelt(level, pos) && isStalactite(level.getBlockState(pos));
        }
    }
    
    @Override
    protected boolean shouldMergeTips() {
        return false;
    }
    
    @Override
    protected List<Direction> getNaturalGrowthDirections() {
        return ONLY_GROW_STALACTITES;
    }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isFreeHangingStalactite(state)) {
            if (random.nextFloat() <= 0.02F) {
                this.spawnDripParticle(level, pos, state, pos.above(), null);
            }
        }
    }
    
    @Override
    protected @Nullable ParticleOptions getDripParticle(Level level, @Nullable Fluid fluidAbove, BlockPos posAbove) {
        return new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SNOW.defaultBlockState());
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.isStartPos(state, level, pos) && this.wouldMelt(level, pos)) {
            if (!isStalagmite(state) && (!isTip(state, true) || level.getBlockState(pos.below()).isAir())) {
                spawnFallingStalactite(state, level, pos);
            } else {
                level.destroyBlock(pos, true);
            }
        } else {
            super.randomTick(state, level, pos, random);
        }
    }
    
    private boolean isStartPos(BlockState state, ServerLevel level, BlockPos pos) {
        return isStalactiteStartPos(state, level, pos) || isStalagmiteStartPos(state, level, pos);
    }
    
    private boolean wouldMelt(LevelReader level, BlockPos pos) {
        return level.dimensionType().ultraWarm() || this.isTooBright(level, pos) || this.isAboveMeltingBlock(level, pos);
    }
    
    private boolean isAboveMeltingBlock(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(ModBlockTags.MELTS_ICICLE_ABOVE) || level.getBlockState(pos.below(2)).is(ModBlockTags.MELTS_ICICLE_ABOVE);
    }
    
    private boolean isTooBright(LevelReader level, BlockPos pos) {
        return level.getBrightness(LightLayer.BLOCK, pos) > 4;
    }
}
