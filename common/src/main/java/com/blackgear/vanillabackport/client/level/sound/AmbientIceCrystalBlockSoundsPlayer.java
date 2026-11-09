package com.blackgear.vanillabackport.client.level.sound;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class AmbientIceCrystalBlockSoundsPlayer {
	public static void playAmbientIceCrystalSounds(Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(250) == 0 && shouldPlayAmbientIceCrystalSound(level, pos)) {
			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.ICE_CRYSTAL_AMBIENT.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
		}
	}

	private static boolean shouldPlayAmbientIceCrystalSound(Level level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(Blocks.PACKED_ICE);
	}
}
