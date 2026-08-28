package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.ContainerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class WeatheringCopperChestBlock extends CopperChestBlock implements WeatheringCopper {
    public WeatheringCopperChestBlock(WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, Properties properties) {
        super(weatherState, openSound, closeSound, properties);
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(TYPE) != ChestType.RIGHT
            && level.getBlockEntity(pos) instanceof ChestBlockEntity chest
            && ContainerHandler.getEntitiesWithContainerOpen(chest).isEmpty()) {
            this.onRandomTick(state, level, pos, random);
        }
    }
    
    @Override
    public WeatherState getAge() {
        return this.getState();
    }
    
    @Override
    public boolean isWaxed() {
        return false;
    }
}