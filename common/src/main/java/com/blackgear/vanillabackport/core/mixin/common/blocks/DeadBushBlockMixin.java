package com.blackgear.vanillabackport.core.mixin.common.blocks;

import com.blackgear.vanillabackport.client.level.sound.AmbientDesertBlockSoundsPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeadBushBlock.class)
public class DeadBushBlockMixin extends BlockMixin {
    @Override
    public void vb$onAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        AmbientDesertBlockSoundsPlayer.playAmbientDeadBushSounds(level, pos, random);
    }
}