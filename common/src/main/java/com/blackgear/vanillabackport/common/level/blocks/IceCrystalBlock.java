package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.level.sound.AmbientIceCrystalBlockSoundsPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class IceCrystalBlock extends AmethystClusterBlock {
    public IceCrystalBlock(Properties properties) {
        super(7, 3, properties);
    }
    
    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) { /* NO-OP */ }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        AmbientIceCrystalBlockSoundsPlayer.playAmbientIceCrystalSounds(level, pos, random);
    }
}