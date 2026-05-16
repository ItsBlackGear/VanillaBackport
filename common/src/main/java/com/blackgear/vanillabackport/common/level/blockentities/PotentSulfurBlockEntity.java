package com.blackgear.vanillabackport.common.level.blockentities;

import com.blackgear.vanillabackport.client.level.particles.particleoptions.GeyserParticleOptions;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.blocks.states.PotentSulfurState;
import com.blackgear.vanillabackport.common.registries.ModBlockEntities;
import com.blackgear.vanillabackport.core.util.CollisionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.blackgear.vanillabackport.common.level.blocks.PotentSulfurBlock.STATE;

public class PotentSulfurBlockEntity extends BlockEntity {
    public static final long GEYSER_SALT = -904011478L;
    private static final Predicate<Entity> EFFECT_PREDICATE = EntitySelector.NO_SPECTATORS.and(EntitySelector.ENTITY_STILL_ALIVE);
    public int waitingCountdown = -1;
    public long eruptionTick = -1L;

    public static final BlockEntityTicker<PotentSulfurBlockEntity> SERVER_NAUSEA_EFFECT_TICKER = (level, pos, state, sulfur) -> {
        if (level.getGameTime() % 10L == 0L) {
            BlockPos source = findNoxiousGasSourceBlock(level, pos);
            if (source != null) {
                for (LivingEntity entity : getNearbyLivingEntities(level, source)) {
                    if (canBeReachedByNoxiousGas(level, source, entity.getEyePosition())) {
                        applyNauseaEffect(entity);
                    }
                }
            }
        }
    };

    public static final BlockEntityTicker<PotentSulfurBlockEntity> CLIENT_NOXIOUS_GAS_TICKER = (level, pos, state, sulfur) -> {
        if (level.getGameTime() % 20L == 0L) {
            BlockPos source = findNoxiousGasSourceBlock(level, pos);
            if (source != null) {
                spawnNoxiousGasCloudParticle(level, Vec3.atCenterOf(source));
            }
        }
    };

    public static final Function<SoundEvent, BlockEntityTicker<PotentSulfurBlockEntity>> CLIENT_GEYSER_PLUME_TICKER = sound -> (level, pos, state, sulfur) -> {
        BlockPos source = findNoxiousGasSourceBlock(level, pos);
        if (source != null) {
            long eruptionTime = level.getGameTime() - sulfur.eruptionTick;
            if (eruptionTime % 20L == 0L) {
                spawnGeyserParticle(level, pos, source);
            }

            if (eruptionTime % 40L == 0L) {
                level.playLocalSound(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5, sound, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    };

    public static final BlockEntityTicker<PotentSulfurBlockEntity> SERVER_WAITING_COUNTDOWN_TICKER = (level, pos, state, sulfur) -> {
        if (level.getGameTime() % 20L == 0L) {
            BlockPos source = findNoxiousGasSourceBlock(level, pos);
            if (source != null) {
                if (sulfur.waitingCountdown <= 0) {
                    int waterBlocks = source.getY() - pos.getY() - 1;
                    RandomSource geyserPositional = geyserPositional((ServerLevel) level, pos);
                    if (state.getValue(STATE) == PotentSulfurState.DORMANT) {
                        sulfur.waitingCountdown = 10 * (waterBlocks - 1) + geyserPositional.nextIntBetweenInclusive(15, 30);
                    } else {
                        geyserPositional.nextInt();
                        sulfur.waitingCountdown = waterBlocks - 1 + geyserPositional.nextIntBetweenInclusive(1, 2);
                    }
                }

                if (sulfur.waitingCountdown > 0) {
                    sulfur.waitingCountdown--;
                }

                if (sulfur.waitingCountdown == 0) {
                    PotentSulfurState stateToSet = state.getValue(STATE) == PotentSulfurState.DORMANT
                        ? PotentSulfurState.ERUPTING
                        : PotentSulfurState.DORMANT;
                    level.setBlock(pos, state.setValue(STATE, stateToSet), Block.UPDATE_ALL);
                    if (stateToSet == PotentSulfurState.DORMANT) {
                        level.gameEvent(GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(state));
                    }
                }
            }
        }
    };

    public static final BlockEntityTicker<PotentSulfurBlockEntity> SERVER_LAUNCH_ENTITY_TICKER = (level, pos, state, sulfur) -> {
        BlockPos source = findNoxiousGasSourceBlock(level, pos);
        if (source != null) {
            int waterBlocks = source.getY() - pos.getY() - 1;
            int geyserForceHeight = getUnobstructedBlockCount(level, pos.above(), waterBlocks);
            AABB aabb = new AABB(pos.above()).expandTowards(0.0, geyserForceHeight - 1, 0.0);

            for (Entity target : level.getEntitiesOfClass(Entity.class, aabb, EFFECT_PREDICATE)) {
                Vec3 velocity = target.getDeltaMovement();
                if (!(target instanceof Player player && player.getAbilities().flying) && velocity.y <= 0.3F + waterBlocks * 0.1) {
                    target.addDeltaMovement(new Vec3(0.0, 0.2F, 0.0));
                    target.hurtMarked = true;
                    target.hasImpulse = true;
                }
            }

            if (level.getGameTime() % 20L == 0L) {
                level.playSound(null, pos, ModSoundEvents.GEYSER_ERUPTION_ACTIVE.get(), SoundSource.BLOCKS, 1.0F * waterBlocks, 1.0F);
            }
        }
    };

    public static BlockEntityTicker<PotentSulfurBlockEntity> sequence(BlockEntityTicker<PotentSulfurBlockEntity> first, BlockEntityTicker<PotentSulfurBlockEntity> second) {
        return (level, pos, state, sulfur) -> {
            first.tick(level, pos, state, sulfur);
            second.tick(level, pos, state, sulfur);
        };
    }

    public PotentSulfurBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTENT_SULFUR.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("countdown", this.waitingCountdown);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.waitingCountdown = tag.getInt("countdown");
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (this.eruptionTick == -1L) {
            this.eruptionTick = level.getGameTime();
        }
    }

    public void resetCountdown() {
        this.waitingCountdown = -1;
    }

    private static void applyNauseaEffect(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0, true, true));
    }

    private static List<LivingEntity> getNearbyLivingEntities(Level level, BlockPos pos) {
        AABB aabb = new AABB(pos).inflate(2.5, 0.0, 2.5);
        return level.getEntitiesOfClass(LivingEntity.class, aabb, EFFECT_PREDICATE);
    }

    public static RandomSource geyserPositional(ServerLevel level, BlockPos pos) {
        return new XoroshiroRandomSource(level.getSeed() ^ GEYSER_SALT).forkPositional().at(pos);
    }

    private static void spawnGeyserParticle(Level level, BlockPos sulfur, BlockPos source) {
        int waterBlocks = source.getY() - sulfur.getY() - 1;
        level.addParticle(new GeyserParticleOptions(ModParticles.GEYSER.get(), waterBlocks), source.getX() + 0.5, source.getY(), source.getZ() + 0.5, 0.0, 0.0, 0.0);
    }

    private static void spawnNoxiousGasCloudParticle(Level level, Vec3 pos) {
        level.addParticle(ModParticles.NOXIOUS_GAS_CLOUD.get(), pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
    }

    private static int getUnobstructedBlockCount(Level level, BlockPos pos, int waterBlocks) {
        int geyserForceHeight = 6 * waterBlocks;
        CollisionContext context = CollisionUtils.positionContext(pos.below().getY());

        for (int i = 0; i < geyserForceHeight; i++) {
            BlockPos currentPos = pos.above(i);
            BlockState state = level.getBlockState(currentPos);
            if (!isGeyserPassableBlock(state, level, currentPos, context)) {
                return i;
            }
        }

        return geyserForceHeight;
    }

    private static boolean isGeyserPassableBlock(BlockState state, Level level, BlockPos pos, CollisionContext context) {
        return state.isAir() || state.is(Blocks.WATER) || state.getCollisionShape(level, pos, context).isEmpty();
    }

    @Nullable
    private static BlockPos findNoxiousGasSourceBlock(Level level, BlockPos origin) {
        int maxY = origin.getY() + 4 + 1;
        CollisionContext context = CollisionUtils.positionContext(origin.getY());
        BlockPos.MutableBlockPos pos = origin.above(1).mutable();

        while (pos.getY() <= maxY) {
            BlockState state = level.getBlockState(pos);
            boolean isWaterLogged = level.getFluidState(pos).isSourceOfType(Fluids.WATER);
            if (!isWaterLogged || !state.is(Blocks.WATER) && !isGeyserPassableBlock(state, level, pos, context)) {
                if (state.isAir() || isGeyserPassableBlock(state, level, pos, context)) {
                    return pos.immutable();
                }
                break;
            }

            pos.move(Direction.UP);
        }

        return null;
    }

    public static boolean canBeReachedByNoxiousGas(Level level, BlockPos origin, Vec3 pos) {
        BlockPos blockPos = BlockPos.containing(pos);
        CollisionContext context = CollisionUtils.positionContext(blockPos.below().getY());
        if (!isGeyserPassableBlock(level.getBlockState(blockPos), level, blockPos, context)) {
            return false;
        } else if (pos.distanceToSqr(Vec3.atCenterOf(origin)) > 9.0) {
            return false;
        } else {
            Vec3 belowSource = Vec3.atCenterOf(origin.below());
            Vec3 belowPos = pos.with(Direction.Axis.Y, pos.y - 1.0);
            return isWater(level, belowPos) && haveLineOfSight(level, belowSource, belowPos);
        }
    }

    private static boolean isWater(Level level, Vec3 pos) {
        return level.getFluidState(BlockPos.containing(pos)).isSourceOfType(Fluids.WATER);
    }

    private static boolean haveLineOfSight(Level level, Vec3 from, Vec3 to) {
        HitResult hitResult = level.clip(new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, null));
        return hitResult.getType() != HitResult.Type.BLOCK;
    }
}