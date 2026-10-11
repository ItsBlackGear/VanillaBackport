package com.blackgear.vanillabackport.core.mixin.common.desert_ambient_sounds;

import com.blackgear.vanillabackport.client.level.sound.AmbientDesertBlockSoundsPlayer;
import com.blackgear.vanillabackport.common.api.extensions.access.block.BlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SandBlock.class)
public class SandBlockMixin implements BlockExtension {
    @Override
    public void vb$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, random);
    }
}