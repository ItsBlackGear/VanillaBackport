package com.blackgear.vanillabackport.common.level.block_entity;

import com.blackgear.vanillabackport.client.level.particle.particleoptions.TrailParticleOption;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.block.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.block.CreakingHeartState;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.creaking.Creaking;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.mojang.datafixers.util.Either;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

import static com.blackgear.vanillabackport.core.util.Utilities.*;
import static com.blackgear.vanillabackport.core.util.WorldUtilities.*;

public class CreakingHeartBlockEntity extends BlockEntity {
    private static final Optional<Creaking> NO_CREAKING = Optional.empty();
    @Nullable private Either<Creaking, UUID> creakingInfo;
    private long ticksExisted;
    private int ticker;
    private int emitter;
    @Nullable private Vec3 emitterTarget;
    private int outputSignal;

    public CreakingHeartBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREAKING_HEART.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CreakingHeartBlockEntity heart) {
        heart.ticksExisted++;
        if (level instanceof ServerLevel server) {
            int computedOutputSignal = heart.computeAnalogOutputSignal();
            if (heart.outputSignal != computedOutputSignal) {
                heart.outputSignal = computedOutputSignal;
                level.updateNeighbourForOutputSignal(pos, ModBlocks.CREAKING_HEART.get());
            }

            if (heart.emitter > 0) {
                if (heart.emitter > 50) {
                    heart.emitParticles(server, 1, true);
                    heart.emitParticles(server, 1, false);
                }

                if (heart.emitter % 10 == 0 && heart.emitterTarget != null) {
                    heart.getCreakingProtector().ifPresent(creaking -> heart.emitterTarget = creaking.getBoundingBox().getCenter());
                    Vec3 heartPosition = Vec3.atCenterOf(pos);
                    float progress = 0.2F + 0.8F * (100 - heart.emitter) / 100.0F;
                    Vec3 soundLocation = heartPosition.subtract(heart.emitterTarget).scale(progress).add(heart.emitterTarget);
                    BlockPos soundPos = BlockPos.containing(soundLocation);
                    float volume = heart.emitter / 2.0F / 100.0F + 0.5F;
                    level.playSound(null, soundPos, ModSoundEvents.CREAKING_HEART_HURT.get(), SoundSource.BLOCKS, volume, 1.0F);
                }

                heart.emitter--;
            }

            if (heart.ticker-- < 0) {
                heart.ticker = heart.level == null ? 20 : heart.level.getRandom().nextInt(5) + 20;
                BlockState updatedState = updateCreakingState(level, state, pos, heart);
                if (updatedState != state) {
                    level.setBlockAndUpdate(pos, updatedState);
                    if (updatedState.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.UPROOTED) {
                        return;
                    }
                }

                if (heart.creakingInfo == null) {
                    if (updatedState.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE) {
                        if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && level.getDifficulty() != Difficulty.PEACEFUL) {
                            Player player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 32.0, false);
                            if (player != null) {
                                Creaking creaking = spawnProtector(server, heart);
                                if (creaking != null) {
                                    heart.setCreakingInfo(creaking);
                                    creaking.playSound(ModSoundEvents.CREAKING_SPAWN.get());
                                    level.playSound(null, heart.getBlockPos(), ModSoundEvents.CREAKING_HEART_SPAWN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                }
                            }
                        }
                    }
                } else {
                    Optional<Creaking> optionalCreaking = heart.getCreakingProtector();
                    if (optionalCreaking.isPresent()) {
                        Creaking creaking = optionalCreaking.get();
                        if (!(EnvironmentUtils.isNaturalNight(level) || VanillaBackport.COMMON_CONFIG.doCreakingHeartsWorkOnDay.get())
                            && !creaking.isPersistenceRequired()
                            || heart.distanceToCreaking() > 34.0
                            || creaking.playerIsStuckInYou()) {
                            heart.removeProtector(null);
                        }
                    }
                }
            }
        }
    }

    private static BlockState updateCreakingState(Level level, BlockState state, BlockPos pos, CreakingHeartBlockEntity heart) {
        if (!CreakingHeartBlock.hasRequiredLogs(state, level, pos) && heart.creakingInfo == null) {
            return state.setValue(CreakingHeartBlock.STATE, CreakingHeartState.UPROOTED);
        } else {
            boolean isNaturalNight = VanillaBackport.COMMON_CONFIG.doCreakingHeartsWorkOnDay.get() || EnvironmentUtils.isNaturalNight(level);
            return state.setValue(CreakingHeartBlock.STATE, isNaturalNight ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT);
        }
    }

    private double distanceToCreaking() {
        return this.getCreakingProtector()
            .map(creaking -> Math.sqrt(creaking.distanceToSqr(Vec3.atBottomCenterOf(this.getBlockPos()))))
            .orElse(0.0);
    }

    private void clearCreakingInfo() {
        this.creakingInfo = null;
        this.setChanged();
    }

    public void setCreakingInfo(Creaking creaking) {
        this.creakingInfo = Either.left(creaking);
        this.setChanged();
    }

    public void setCreakingInfo(UUID uuid) {
        this.creakingInfo = Either.right(uuid);
        this.ticksExisted = 0L;
        this.setChanged();
    }

    private Optional<Creaking> getCreakingProtector() {
        if (this.creakingInfo == null) {
            return NO_CREAKING;
        } else {
            if (this.creakingInfo.left().isPresent()) {
                Creaking creaking = this.creakingInfo.left().get();
                if (!creaking.isRemoved()) {
                    return Optional.of(creaking);
                }

                this.setCreakingInfo(creaking.getUUID());
            }

            if (this.level instanceof ServerLevel server && this.creakingInfo.right().isPresent()) {
                UUID uuid = this.creakingInfo.right().get();
                if (server.getEntity(uuid) instanceof Creaking creaking) {
                    this.setCreakingInfo(creaking);
                    return Optional.of(creaking);
                } else {
                    if (this.ticksExisted >= 30L) {
                        this.clearCreakingInfo();
                    }

                    return NO_CREAKING;
                }
            } else {
                return NO_CREAKING;
            }
        }
    }

    @Nullable
    private static Creaking spawnProtector(ServerLevel level, CreakingHeartBlockEntity heart) {
        if (!VanillaBackport.COMMON_CONFIG.hasCreaking.get()) return null;

        BlockPos pos = heart.getBlockPos();
        Optional<Creaking> spawnedMob = SpawnUtils.trySpawnMob(ModEntityTypes.CREAKING.get(), MobSpawnType.SPAWNER, level, pos, 5, 16, 8, SpawnUtils.ON_TOP_OF_COLLIDER_NO_LEAVES, true);

        if (spawnedMob.isEmpty()) {
            return null;
        } else {
            Creaking spawnedCreaking = spawnedMob.get();
            level.gameEvent(spawnedCreaking, GameEvent.ENTITY_PLACE, spawnedCreaking.position());
            level.broadcastEntityEvent(spawnedCreaking, (byte) 60);
            spawnedCreaking.setTransient(pos);
            return spawnedCreaking;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void creakingHurt() {
        Optional<Creaking> creaking = this.getCreakingProtector();
        if (creaking.isPresent()) {
            if (this.level instanceof ServerLevel server) {
                if (this.emitter <= 0) {
                    this.emitParticles(server, 20, false);
                    if (this.getBlockState().getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE && VanillaBackport.COMMON_CONFIG.hasResin.get()) {
                        int numberOfClumps = this.level.getRandom().nextIntBetweenInclusive(2, 3);

                        for (int i = 0; i < numberOfClumps; i++) {
                            this.spreadResin(server).ifPresent(pos -> {
                                this.level.playSound(null, pos, ModSoundEvents.RESIN_PLACE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                this.level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(this.getBlockState()));
                            });
                        }
                    }

                    this.emitter = 100;
                    this.emitterTarget = creaking.get().getBoundingBox().getCenter();
                }
            }
        }
    }

    private Optional<BlockPos> spreadResin(ServerLevel level) {
        RandomSource random = level.getRandom();
        Mutable<BlockPos> placedResin = new MutableObject<>(null);
        BlockPos.breadthFirstTraversal(this.worldPosition, 2, 64, (pos, acceptor) -> {
            for (Direction dir : Util.shuffledCopy(Direction.values(), random)) {
                BlockPos neighbourPos = pos.relative(dir);
                if (level.getBlockState(neighbourPos).is(ModBlockTags.CREAKING_HEART_HOLDERS)) {
                    acceptor.accept(neighbourPos);
                }
            }
        }, pos -> {
            if (level.getBlockState(pos).is(ModBlockTags.CREAKING_HEART_HOLDERS)) {
                for (Direction dir : Util.shuffledCopy(Direction.values(), random)) {
                    BlockPos neighbourPos = pos.relative(dir);
                    BlockState neighbourState = level.getBlockState(neighbourPos);
                    Direction opposite = dir.getOpposite();
                    if (neighbourState.isAir()) {
                        neighbourState = ModBlocks.RESIN_CLUMP.get().defaultBlockState();
                    } else if (neighbourState.is(Blocks.WATER) && neighbourState.getFluidState().isSource()) {
                        neighbourState = ModBlocks.RESIN_CLUMP.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
                    }

                    if (neighbourState.is(ModBlocks.RESIN_CLUMP.get()) && !MultifaceBlock.hasFace(neighbourState, opposite)) {
                        level.setBlockAndUpdate(neighbourPos, neighbourState.setValue(MultifaceBlock.getFaceProperty(opposite), true));
                        placedResin.setValue(neighbourPos);
                        return false;
                    }
                }
            }

            return true;
        });
        return Optional.ofNullable(placedResin.getValue());
    }

    private void emitParticles(ServerLevel level, int count, boolean towardsCreaking) {
        Optional<Creaking> creaking = this.getCreakingProtector();
        if (creaking.isPresent()) {
            int color = towardsCreaking ? VanillaBackport.COMMON_CONFIG.creakingParticleReverseColor.get() : VanillaBackport.COMMON_CONFIG.creakingParticleColor.get();
            RandomSource random = level.getRandom();
            
            for (double i = 0.0; i < count; i++) {
                AABB box = creaking.get().getBoundingBox();
                Vec3 source = CollisionUtils.getMinPosition(box).add(random.nextDouble() * box.getXsize(), random.nextDouble() * box.getYsize(), random.nextDouble() * box.getZsize());
                Vec3 destination = Vec3.atLowerCornerOf(this.getBlockPos()).add(random.nextDouble(), random.nextDouble(), random.nextDouble());
                if (towardsCreaking) {
                    Vec3 foo = source;
                    source = destination;
                    destination = foo;
                }

                TrailParticleOption particle = new TrailParticleOption(destination, color, random.nextInt(40) + 10);
                ModParticles.sendParticles(level, particle, true, true, source.x, source.y, source.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }

    public void removeProtector(@Nullable DamageSource source) {
        Optional<Creaking> creakingProtector = this.getCreakingProtector();
        if (creakingProtector.isPresent()) {
            Creaking creaking = creakingProtector.get();
            if (source == null) {
                creaking.tearDown();
            } else {
                creaking.creakingDeathEffects(source);
                creaking.setTearingDown();
                creaking.setHealth(0.0F);
            }

            this.clearCreakingInfo();
        }
    }

    public boolean isProtector(Creaking creaking) {
        return this.getCreakingProtector().map(target -> target == creaking).orElse(false);
    }

    public int getAnalogOutputSignal() {
        return this.outputSignal;
    }

    public int computeAnalogOutputSignal() {
        if (this.creakingInfo != null && this.getCreakingProtector().isPresent()) {
            double distance = this.distanceToCreaking();
            double scaledDistance = Mth.clamp(distance, 0.0, 32.0) / 32.0;
            return 15 - (int) Math.floor(scaledDistance * 15.0);
        } else {
            return 0;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.hasUUID("creaking")) {
            this.setCreakingInfo(tag.getUUID("creaking"));
        } else {
            this.clearCreakingInfo();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.creakingInfo != null) {
            tag.putUUID("creaking", this.creakingInfo.map(Entity::getUUID, uuid -> uuid));
        }
    }
}