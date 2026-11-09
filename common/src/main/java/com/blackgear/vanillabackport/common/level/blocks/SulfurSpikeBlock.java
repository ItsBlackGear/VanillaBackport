package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.minecraft.world.level.block.state.BlockState;

public class SulfurSpikeBlock extends SpeleothemBlock {
    public SulfurSpikeBlock(BlockState blockToGrowOn, Properties properties) {
        super(blockToGrowOn, properties);
    }
    
    @Override
    protected LevelEvents getStalactiteLandingSound() {
        return LevelEvents.SOUND_SULFUR_SPIKE_LAND;
    }
    
    @Override
    protected int getMaxGrowthLength() {
        return 2;
    }
}