package com.blackgear.vanillabackport.client.level.sound;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.util.Utilities.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class AmbientIceBlockSoundsPlayer {
	public static void playAmbientIceSounds(Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(10000) == 0 && shouldPlayAmbientIceSound(level, pos)) {
			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.ICE_BLOCK_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
		}
	}
	
	private static boolean shouldPlayAmbientIceSound(Level level, BlockPos pos) {
        if (level.getBlockState(pos.below()).is(Blocks.WATER) && level.getBlockState(pos.above()).isAir()) {
            for (BlockPos rayPos : PositionUtils.horizontalRays(pos, 8)) {
                if (!level.getBlockState(rayPos).is(Blocks.ICE)) {
                    return false;
                }
            }
            
            return true;
        }
        
		return false;
    }
}
