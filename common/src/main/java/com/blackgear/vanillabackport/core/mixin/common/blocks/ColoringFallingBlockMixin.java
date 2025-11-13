package com.blackgear.vanillabackport.core.mixin.common.blocks;

import com.blackgear.vanillabackport.client.level.sound.AmbientDesertBlockSoundsPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ColoredFallingBlock.class)
public class ColoringFallingBlockMixin extends Block {
    public ColoringFallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, random);
        super.animateTick(state, level, pos, random);
    }
}