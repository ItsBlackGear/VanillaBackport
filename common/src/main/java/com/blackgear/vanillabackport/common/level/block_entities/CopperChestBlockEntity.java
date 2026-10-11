package com.blackgear.vanillabackport.common.level.block_entities;

import com.blackgear.vanillabackport.common.level.blocks.CopperChestBlock;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.core.mixin.common.access.ChestBlockEntityAccessor;
import com.blackgear.vanillabackport.core.mixin.common.access.ContainerOpenersCounterAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class CopperChestBlockEntity extends ChestBlockEntity {
    public CopperChestBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COPPER_CHEST.get(), pos, blockState);
        ContainerOpenersCounter counter = ((ChestBlockEntityAccessor) this).getOpenersCounter();
        ((ChestBlockEntityAccessor) this).setOpenersCounter(new ContainerOpenersCounter() {
            @Override
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                if (state.getBlock() instanceof CopperChestBlock chest) {
                    playSound(level, pos, state, chest.getOpenChestSound());
                }
            }
            
            @Override
            protected void onClose(Level level, BlockPos pos, BlockState state) {
                if (state.getBlock() instanceof CopperChestBlock chest) {
                    playSound(level, pos, state, chest.getCloseChestSound());
                }
            }
            
            @Override
            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
                ((ContainerOpenersCounterAccessor) counter).callOpenerCountChanged(level, pos, state, count, openCount);
            }
            
            @Override
            protected boolean isOwnContainer(Player player) {
                return ((ContainerOpenersCounterAccessor) counter).callIsOwnContainer(player);
            }
        });
    }

    static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
        ChestType type = state.getValue(ChestBlock.TYPE);
        if (type != ChestType.LEFT) {
            double x = (double) pos.getX() + 0.5;
            double y = (double) pos.getY() + 0.5;
            double z = (double) pos.getZ() + 0.5;
            if (type == ChestType.RIGHT) {
                Direction direction = ChestBlock.getConnectedDirection(state);
                x += (double) direction.getStepX() * 0.5;
                z += (double) direction.getStepZ() * 0.5;
            }
            
            level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}