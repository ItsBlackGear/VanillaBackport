package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CopperBulbBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    public CopperBulbBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false).setValue(POWERED, false));
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel server) {
            this.checkAndFlip(state, server, pos);
        }
    }
    
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level instanceof ServerLevel server) {
            this.checkAndFlip(state, server, pos);
        }
    }
    
    public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
        boolean signal = level.hasNeighborSignal(pos);
        if (signal != state.getValue(POWERED)) {
            BlockState newState = state;
            if (!state.getValue(POWERED)) {
                newState = state.cycle(LIT);
                level.playSound(null, pos, newState.getValue(LIT) ? ModSoundEvents.COPPER_BULB_TURN_ON.get() : ModSoundEvents.COPPER_BULB_TURN_OFF.get(), SoundSource.BLOCKS);
            }
            
            level.setBlockAndUpdate(pos, newState.setValue(POWERED, signal));
        }
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, POWERED);
    }
    
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockState(pos).getValue(LIT) ? 15 : 0;
    }
}