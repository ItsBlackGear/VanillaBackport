package com.blackgear.vanillabackport.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SpawnExtras {
    public static final SpawnUtil.Strategy ON_TOP_OF_COLLIDER_NO_LEAVES = (level, pos, target, mutable, state) -> state.getCollisionShape(level, mutable).isEmpty()
        && !target.is(BlockTags.LEAVES)
        && Block.isFaceFull(target.getCollisionShape(level, pos), Direction.UP);

    public static <T extends Mob> Optional<T> trySpawnMob(
        EntityType<T> entityType,
        MobSpawnType spawnType,
        ServerLevel level,
        BlockPos pos,
        int attempts,
        int spread,
        int yOffset,
        SpawnUtil.Strategy strategy,
        boolean checkForCollisions
    ) {
        BlockPos.MutableBlockPos mutable = pos.mutable();

        for (int i = 0; i < attempts; i++) {
            int xOffset = Mth.randomBetweenInclusive(level.random, -spread, spread);
            int zOffset = Mth.randomBetweenInclusive(level.random, -spread, spread);
            mutable.setWithOffset(pos, xOffset, yOffset, zOffset);
            if (level.getWorldBorder().isWithinBounds(mutable)
                && moveToPossibleSpawnPosition(level, yOffset, mutable, strategy)
                && (!checkForCollisions || level.noCollision(entityType.getSpawnAABB(mutable.getX() + 0.5, mutable.getY(), mutable.getZ() + 0.5)))) {
                T mob = entityType.create(level, null, mutable, spawnType, false, false);
                if (mob != null) {
                    if (mob.checkSpawnRules(level, spawnType) && mob.checkSpawnObstruction(level)) {
                        level.addFreshEntityWithPassengers(mob);
                        mob.playAmbientSound();
                        return Optional.of(mob);
                    }

                    mob.discard();
                }
            }
        }

        return Optional.empty();
    }

    private static boolean moveToPossibleSpawnPosition(ServerLevel level, int yOffset, BlockPos.MutableBlockPos pos, SpawnUtil.Strategy strategy) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(pos);
        BlockState state = level.getBlockState(mutable);

        for (int i = yOffset; i >= -yOffset; i--) {
            pos.move(Direction.DOWN);
            mutable.setWithOffset(pos, Direction.UP);
            BlockState target = level.getBlockState(pos);
            if (strategy.canSpawnOn(level, pos, target, mutable, state)) {
                pos.move(Direction.UP);
                return true;
            }

            state = target;
        }

        return false;
    }
}