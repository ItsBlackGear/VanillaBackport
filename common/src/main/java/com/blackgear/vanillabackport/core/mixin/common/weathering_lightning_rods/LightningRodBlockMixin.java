package com.blackgear.vanillabackport.core.mixin.common.weathering_lightning_rods;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin extends Block implements WeatheringCopper {
    public LightningRodBlockMixin(Properties properties) {
        super(properties);
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.is(Blocks.LIGHTNING_ROD)) this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.is(Blocks.LIGHTNING_ROD) || super.isRandomlyTicking(state);
    }
    
    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }
}