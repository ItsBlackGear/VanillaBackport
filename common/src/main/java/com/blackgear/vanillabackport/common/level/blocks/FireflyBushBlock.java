package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.util.LevelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class FireflyBushBlock extends BushBlock implements BonemealableBlock {
    private static final double FIREFLY_CHANCE_PER_TICK = 0.7;
    private static final double FIREFLY_HORIZONTAL_RANGE = 10.0;
    private static final double FIREFLY_VERTICAL_RANGE = 5.0;
    private static final int FIREFLY_SPAWN_MAX_BRIGHTNESS_LEVEL = 13;
    private static final int FIREFLY_AMBIENT_SOUND_CHANCE_ONE_IN = 30;

    public FireflyBushBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(FIREFLY_AMBIENT_SOUND_CHANCE_ONE_IN) == 0 && LevelUtils.isMoonVisible(level) && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) <= pos.getY()) {
            level.playLocalSound(pos, ModSoundEvents.FIREFLY_BUSH_IDLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
        }

        if (LevelUtils.isMoonVisible(level) || level.getMaxLocalRawBrightness(pos) <= FIREFLY_SPAWN_MAX_BRIGHTNESS_LEVEL) {
            if (random.nextDouble() <= FIREFLY_CHANCE_PER_TICK) {
                double x = pos.getX() + random.nextDouble() * FIREFLY_HORIZONTAL_RANGE - FIREFLY_VERTICAL_RANGE;
                double y = pos.getY() + random.nextDouble() * FIREFLY_VERTICAL_RANGE;
                double z = pos.getZ() + random.nextDouble() * FIREFLY_HORIZONTAL_RANGE - FIREFLY_VERTICAL_RANGE;
                level.addParticle(ModParticles.FIREFLY.get(), x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        popResource(level, pos, new ItemStack(this));
    }
}