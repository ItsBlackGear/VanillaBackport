package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;

public class BrittleIceBlock extends IceBlock {
	public static final MapCodec<BrittleIceBlock> CODEC = simpleCodec(BrittleIceBlock::new);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	@Override
	public MapCodec<BrittleIceBlock> codec() {
		return CODEC;
	}

	public BrittleIceBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}
	
	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level instanceof ServerLevel server
			&& this.shouldEntityIncreaseCracking(entity, pos, server)
			&& !server.getBlockTicks().hasScheduledTick(pos, this)
		) {
			if (this.slightlyMelt(state, server, pos)) {
				this.propagateMelting(server, pos, server.getRandom());
			}
			
			server.scheduleTick(pos, this, 24);
		}
	}
    
    private boolean shouldEntityIncreaseCracking(Entity entity, BlockPos pos, ServerLevel level) {
        if (entity instanceof LivingEntity && !entity.getType().is(ModEntityTypeTags.DOES_NOT_BREAK_BRITTLE_ICE)) {
            return entity instanceof Player player ? !level.getServer().isUnderSpawnProtection(level, pos, player) : level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
		} else {
			return false;
		}
    }
	
	@Override
	protected void melt(BlockState state, Level level, BlockPos pos) {
		level.destroyBlock(pos, false);
	}
	
	private void propagateMelting(ServerLevel level, BlockPos pos, RandomSource random) {
		BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
		
		for (Direction direction : Direction.values()) {
			neighborPos.setWithOffset(pos, direction);
			BlockState neighbour = level.getBlockState(neighborPos);
			if (neighbour.is(this) && !this.slightlyMelt(neighbour, level, neighborPos)) {
				level.scheduleTick(neighborPos, this, 24);
			}
		}
	}
	
	protected boolean slightlyMelt(BlockState state, Level level, BlockPos pos) {
		int age = state.getValue(AGE);
		if (age < 3) {
			level.setBlock(pos, state.setValue(AGE, age + 1), 2);
			SoundEvent crackingSound = ModSoundEvents.BRITTLE_ICE_CRACK.get();
			if (crackingSound != null) {
				level.playSound(null, pos, crackingSound, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
			
			this.playCrackingIceParticle(state, level, pos);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
			return false;
		} else {
			this.melt(state, level, pos);
			return true;
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	
	private void playCrackingIceParticle(BlockState state, Level level, BlockPos pos) {
		int age = state.getValue(AGE);
		if (age == 0) {
			level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, getId(state));
		}
	}
}