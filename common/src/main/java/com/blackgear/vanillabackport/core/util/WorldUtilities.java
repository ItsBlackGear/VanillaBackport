package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashPhysics;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static net.minecraft.world.entity.ai.util.LandRandomPos.movePosUpOutOfSolid;

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
        
        public static Function<BlockState, VoxelShape> getShapeForEachState(Block block, Function<BlockState, VoxelShape> calculator) {
            return block.getStateDefinition().getPossibleStates().stream().collect(ImmutableMap.toImmutableMap(Function.identity(), calculator))::get;
        }
    }
    
    public static class EntityUtils {
        public static Vec3 oldPosition(Entity entity) {
            return new Vec3(entity.xOld, entity.yOld, entity.zOld);
        }
        
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
        
        @Nullable
        public static LivingEntity getNearestEntity(
            Level level,
            TagKey<EntityType<?>> tag,
            TargetingConditions conditions,
            @Nullable LivingEntity source,
            double x,
            double y,
            double z,
            AABB bb
        ) {
            double bestDistance = Double.MAX_VALUE;
            LivingEntity nearestEntity = null;
            
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, bb, e -> e.getType().is(tag))) {
                if (conditions.test(source, entity)) {
                    double distance = entity.distanceToSqr(x, y, z);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        nearestEntity = entity;
                    }
                }
            }
            
            return nearestEntity;
        }
        
        public static void stopInPlace(PathfinderMob mob) {
            mob.getNavigation().stop();
            mob.setXxa(0.0F);
            mob.setYya(0.0F);
            mob.setSpeed(0.0F);
            mob.setDeltaMovement(0.0, 0.0, 0.0);
            LeashPhysics.resetAngularMomentum(mob);
        }
        
        public static boolean isLookingAtMe(LivingEntity user, LivingEntity target, double coneSize, boolean adjustForDistance, boolean seeThroughTransparentBlocks, double... gazeHeights) {
            Vec3 look = target.getViewVector(1.0F).normalize();
            
            for (double gazeHeight : gazeHeights) {
                Vec3 dir = new Vec3(user.getX() - target.getX(), gazeHeight - target.getEyeY(), user.getZ() - target.getZ());
                double dist = dir.length();
                dir = dir.normalize();
                double dot = look.dot(dir);
                
                double lookThreshold = 1.0 - coneSize / (adjustForDistance ? dist : 1.0);
                if (dot > lookThreshold && hasLineOfSight(target, user, seeThroughTransparentBlocks ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, gazeHeight)) {
                    return true;
                }
            }
            
            return false;
        }
        
        public static boolean hasLineOfSight(LivingEntity user, Entity target, ClipContext.Block blockCollidingContext, ClipContext.Fluid fluidCollidingContext, double eyeHeight) {
            if (target.level() != user.level()) {
                return false;
            } else {
                Vec3 from = new Vec3(user.getX(), user.getEyeY(), user.getZ());
                Vec3 to = new Vec3(target.getX(), eyeHeight, target.getZ());
                return to.distanceTo(from) <= 128.0 && user.level().clip(new ClipContext(from, to, blockCollidingContext, fluidCollidingContext, user)).getType() == HitResult.Type.MISS;
            }
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
    
    public static class EnvironmentUtils {
        public static boolean isNaturalNight(Level level) {
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
    
    public static class PathfindingUtils {
        public static boolean mobRestricted(PathfinderMob mob, double horizontalDist) {
            return mob.hasRestriction() && mob.getRestrictCenter().closerToCenterThan(mob.position(), mob.getRestrictRadius() + horizontalDist + 1.0);
        }
        
        @Nullable
        public static Vec3 getPosAway(PathfinderMob mob, double minHorizontalDist, double maxHorizontalDist, int verticalDist, Vec3 avoidPos) {
            Vec3 dirAway = mob.position().subtract(avoidPos);
            if (dirAway.length() == 0.0) {
                dirAway = new Vec3(mob.getRandom().nextDouble() - 0.5, 0.0, mob.getRandom().nextDouble() - 0.5);
            }
            
            boolean restrict = mobRestricted(mob, maxHorizontalDist);
            return getPosInDirection(mob, minHorizontalDist, maxHorizontalDist, verticalDist, dirAway, restrict);
        }
        
        @Nullable
        private static Vec3 getPosInDirection(PathfinderMob mob, double minHorizontalDist, double maxHorizontalDist, int verticalDist, Vec3 dir, boolean restrict) {
            return RandomPos.generateRandomPos(
                mob,
                () -> {
                    BlockPos direction = generateRandomDirectionWithinRadians(
                        mob.getRandom(), minHorizontalDist, maxHorizontalDist, verticalDist, 0, dir.x, dir.z, (float) (Math.PI / 2)
                    );
                    if (direction == null) {
                        return null;
                    } else {
                        BlockPos pos = generateRandomPosTowardDirection(mob, maxHorizontalDist, restrict, direction);
                        return pos == null ? null : movePosUpOutOfSolid(mob, pos);
                    }
                }
            );
        }
        
        @Nullable
        public static BlockPos generateRandomPosTowardDirection(PathfinderMob mob, double horizontalDist, boolean restrict, BlockPos direction) {
            BlockPos pos = generateRandomPosTowardDirection(mob, horizontalDist, mob.getRandom(), direction);
            return !GoalUtils.isOutsideLimits(pos, mob) && !GoalUtils.isRestricted(restrict, mob, pos) && !GoalUtils.isNotStable(mob.getNavigation(), pos) ? pos : null;
        }
        
        @Nullable
        public static BlockPos generateRandomDirectionWithinRadians(
            RandomSource random,
            double minHorizontalDist,
            double maxHorizontalDist,
            int verticalDist,
            int flyingHeight,
            double xDir,
            double zDir,
            double maxXzRadiansFromDir
        ) {
            double yRadiansCenter = Mth.atan2(zDir, xDir) - (float) (Math.PI / 2);
            double yRadians = yRadiansCenter + (2.0F * random.nextFloat() - 1.0F) * maxXzRadiansFromDir;
            double dist = Mth.lerp(Math.sqrt(random.nextDouble()), minHorizontalDist, maxHorizontalDist) * Mth.SQRT_OF_TWO;
            double xt = -dist * Math.sin(yRadians);
            double zt = dist * Math.cos(yRadians);
            if (!(Math.abs(xt) > maxHorizontalDist) && !(Math.abs(zt) > maxHorizontalDist)) {
                int yt = random.nextInt(2 * verticalDist + 1) - verticalDist + flyingHeight;
                return BlockPos.containing(xt, yt, zt);
            } else {
                return null;
            }
        }
        
        public static BlockPos generateRandomPosTowardDirection(PathfinderMob mob, double xzDist, RandomSource random, BlockPos direction) {
            double xt = direction.getX();
            double zt = direction.getZ();
            if (mob.hasRestriction() && xzDist > 1.0) {
                BlockPos center = mob.getRestrictCenter();
                if (mob.getX() > center.getX()) {
                    xt -= random.nextDouble() * xzDist / 2.0;
                } else {
                    xt += random.nextDouble() * xzDist / 2.0;
                }
                
                if (mob.getZ() > center.getZ()) {
                    zt -= random.nextDouble() * xzDist / 2.0;
                } else {
                    zt += random.nextDouble() * xzDist / 2.0;
                }
            }
            
            return BlockPos.containing(xt + mob.getX(), direction.getY() + mob.getY(), zt + mob.getZ());
        }
    }
}