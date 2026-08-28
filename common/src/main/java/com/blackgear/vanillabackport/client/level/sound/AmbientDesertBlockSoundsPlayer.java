package com.blackgear.vanillabackport.client.level.sound;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class AmbientDesertBlockSoundsPlayer {
    private static final int IDLE_SOUND_CHANCE = 2100;
    private static final int DRY_GRASS_SOUND_CHANCE = 200;
    private static final int DEAD_BUSH_SOUND_CHANCE = 130;
    private static final int DEAD_BUSH_SOUND_BADLANDS_DECREASED_CHANCE = 3;
    private static final int SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD = 3;
    private static final int SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK = 8;
    private static final int SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK = 5;
    private static final int HORIZONTAL_DIRECTIONS = 4;

    public static void playAmbientSandSounds(Level level, BlockPos pos, RandomSource random) {
        if (!level.getBlockState(pos).is(BlockTags.SAND)) return;

        if (level.getBlockState(pos.above()).is(Blocks.AIR)) {
            if (random.nextInt(IDLE_SOUND_CHANCE) == 0 && shouldPlayAmbientSandSound(level, pos)) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.SAND_IDLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
        }
    }

    public static void playAmbientDryGrassSounds(Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(DRY_GRASS_SOUND_CHANCE) == 0 && shouldPlayDesertDryVegetationBlockSounds(level, pos.below())) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.DRY_GRASS.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
        }
    }

    public static void playAmbientDeadBushSounds(Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(DEAD_BUSH_SOUND_CHANCE) == 0) {
            BlockState belowPos = level.getBlockState(pos.below());
            if ((belowPos.is(Blocks.RED_SAND) || belowPos.is(BlockTags.TERRACOTTA)) && random.nextInt(DEAD_BUSH_SOUND_BADLANDS_DECREASED_CHANCE) != 0) {
                return;
            }

            if (shouldPlayDesertDryVegetationBlockSounds(level, pos.below())) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.DEAD_BUSH_IDLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
        }
    }

    public static boolean shouldPlayDesertDryVegetationBlockSounds(Level level, BlockPos belowPos) {
        return level.getBlockState(belowPos).is(ModBlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS)
            && level.getBlockState(belowPos.below()).is(ModBlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS);
    }

    private static boolean shouldPlayAmbientSandSound(Level level, BlockPos pos) {
        int matchingBlocksFound = 0, sidesChecked = 0;
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            mutablePos.set(pos).move(dir, SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK);
            if (columnContainsTriggeringBlock(level, mutablePos) && matchingBlocksFound++ >= SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD) {
                return true;
            }

            sidesChecked++;
            int remainingSides = HORIZONTAL_DIRECTIONS - sidesChecked;
            int potentialMatches = remainingSides + matchingBlocksFound;
            if (potentialMatches < SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD) {
                return false;
            }
        }

        return false;
    }

    private static boolean columnContainsTriggeringBlock(Level level, BlockPos.MutableBlockPos mutablePos) {
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, mutablePos.getX(), mutablePos.getZ()) - 1;
        if (Math.abs(surfaceY - mutablePos.getY()) > SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK) {
            mutablePos.move(Direction.UP, 6);
            BlockState aboveBlockState = level.getBlockState(mutablePos);
            mutablePos.move(Direction.DOWN);

            for (int i = 0; i < 10; i++) {
                BlockState currentBlockState = level.getBlockState(mutablePos);
                if (aboveBlockState.isAir() && canTriggerAmbientDesertSandSounds(currentBlockState)) {
                    return true;
                }

                aboveBlockState = currentBlockState;
                mutablePos.move(Direction.DOWN);
            }

            return false;
        } else {
            boolean hasAirAbove = level.getBlockState(mutablePos.setY(surfaceY + 1)).isAir();
            return hasAirAbove && canTriggerAmbientDesertSandSounds(level.getBlockState(mutablePos.setY(surfaceY)));
        }
    }

    private static boolean canTriggerAmbientDesertSandSounds(BlockState state) {
        return state.is(ModBlockTags.TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS);
    }
}