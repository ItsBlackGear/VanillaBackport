package com.blackgear.vanillabackport.common.integrations;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;

public class MobSpawns {
    static boolean checkCamelSpawnRules(EntityType<? extends Camel> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(ModBlockTags.CAMELS_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8;
    }
    
    static boolean checkMonsterSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(level, pos, random) && Mob.checkMobSpawnRules(type, level, spawnType, pos, random);
    }
    
    static boolean checkSurfaceMonstersSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(type, level, spawnType, pos, random) && (spawnType == MobSpawnType.SPAWNER || level.canSeeSky(pos));
    }
}