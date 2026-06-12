package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.common.api.leash.LeashPhysics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WorldUtilities {
    public static class BlockUtils {
        public static int getBestOwnOrNeighbourSignal(SignalGetter level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return Math.max(level.getBestNeighborSignal(pos), state.isSignalSource() ? getOwnSignal(level, state, pos) : 0);
        }
        
        private static int getOwnSignal(SignalGetter level, BlockState state, BlockPos pos) {
            int best = 0;
            
            for (Direction direction : Direction.values()) {
                int signal = state.getSignal(level, pos, direction);
                if (signal >= 15) {
                    return 15;
                }
                
                if (signal > best) {
                    best = signal;
                }
            }
            
            return best;
        }
    }
    
    public static class EntityUtils {
        @Nullable
        public static <T extends Entity> T getNearestEntity(List<? extends T> entities, double x, double y, double z) {
            double best = -1.0;
            T result = null;
            
            for (T entity : entities) {
                double dist = entity.distanceToSqr(x, y, z);
                if (best == -1.0 || dist < best) {
                    best = dist;
                    result = entity;
                }
            }
            
            return result;
        }
        
        public static void stopInPlace(PathfinderMob mob) {
            mob.getNavigation().stop();
            mob.setXxa(0.0F);
            mob.setYya(0.0F);
            mob.setSpeed(0.0F);
            mob.setDeltaMovement(0.0, 0.0, 0.0);
            LeashPhysics.resetAngularMomentum(mob);
        }
    }
    
    public static class SpawnUtils {
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
    
    public static class LootUtils {
        public static boolean dropFromGiftLootTable(
            Entity entity,
            ServerLevel level,
            ResourceKey<LootTable> key,
            BiConsumer<ServerLevel, ItemStack> consumer
        ) {
            return dropFromLootTable(
                level,
                key,
                builder -> builder.withParameter(LootContextParams.ORIGIN, entity.position())
                    .withParameter(LootContextParams.THIS_ENTITY, entity)
                    .create(LootContextParamSets.GIFT),
                consumer
            );
        }
        
        private static boolean dropFromLootTable(
            ServerLevel level,
            ResourceKey<LootTable> key,
            Function<LootParams.Builder, LootParams> function,
            BiConsumer<ServerLevel, ItemStack> consumer
        ) {
            LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(key);
            LootParams lootParams = function.apply(new LootParams.Builder(level));
            List<ItemStack> list = lootTable.getRandomItems(lootParams);
            if (!list.isEmpty()) {
                list.forEach(stack -> consumer.accept(level, stack));
                return true;
            } else {
                return false;
            }
        }
    }
    
    public static class SkyUtils {
        public static boolean isMoonVisible(Level level) {
            if (!level.dimensionType().natural()) {
                return false;
            } else {
                int ticks = (int) (level.getDayTime() % 24000L);
                return ticks >= 12600 && ticks <= 23400;
            }
        }
        
        public static Biome.Precipitation precipitationAt(Level level, BlockPos pos) {
            if (!level.isRaining()) {
                return Biome.Precipitation.NONE;
            } else if (!level.canSeeSky(pos)) {
                return Biome.Precipitation.NONE;
            } else if (level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
                return Biome.Precipitation.NONE;
            } else {
                Biome biome = level.getBiome(pos).value();
                return biome.getPrecipitationAt(pos);
            }
        }
        
        public static boolean isInClouds(Entity entity) {
            if (entity.level().dimensionType().natural()) {
                int cloudHeight = 192;
                if (entity.getY() + entity.getBbHeight() < cloudHeight) {
                    return false;
                } else {
                    int cloudRange = cloudHeight + 4;
                    return entity.getY() <= cloudRange;
                }
            }
            
            return false;
        }
    }
}