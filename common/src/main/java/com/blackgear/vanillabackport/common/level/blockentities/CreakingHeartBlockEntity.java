package com.blackgear.vanillabackport.common.level.blockentities;

import com.blackgear.vanillabackport.client.level.particles.particleoptions.TrailParticleOption;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.blocks.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.blocks.blockstates.CreakingHeartState;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.blackgear.vanillabackport.common.registries.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.mojang.datafixers.util.Either;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
            int signal = heart.computeAnalogOutputSignal();
            if (heart.outputSignal != signal) {
                heart.outputSignal = signal;
                level.updateNeighbourForOutputSignal(pos, ModBlocks.CREAKING_HEART.get());
            }

            if (heart.emitter > 0) {
                if (heart.emitter > 50) {
                    heart.emitParticles(server, 1, true);
                    heart.emitParticles(server, 1, false);
                }

                if (heart.emitter % 10 == 0 && heart.emitterTarget != null) {
                    heart.getCreakingProtector().ifPresent(creaking -> heart.emitterTarget = creaking.getBoundingBox().getCenter());
                    Vec3 center = Vec3.atCenterOf(pos);
                    float emission = 0.2F + 0.8F * (100 - heart.emitter) / 100.0F;
                    Vec3 position = center.subtract(heart.emitterTarget).scale(emission).add(heart.emitterTarget);
                    BlockPos target = BlockPos.containing(position);
                    float volume = heart.emitter / 2.0F / 100.0F + 0.5F;
                    level.playSound(null, target, ModSoundEvents.CREAKING_HEART_HURT.get(), SoundSource.BLOCKS, volume, 1.0F);
                }

                heart.emitter--;
            }

            if (heart.ticker-- < 0) {
                heart.ticksExisted = heart.level == null ? 20 : heart.level.random.nextInt(5) + 20;
                BlockState updatedState = updateCreakingState(level, state, pos, heart);
                if (updatedState != state) {
                    level.setBlock(pos, updatedState, 3);
                    if (updatedState.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.UPROOTED) {
                        return;
                    }
                }

                if (heart.creakingInfo == null) {
                    if (updatedState.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE) {
                        if (level.getDifficulty() != Difficulty.PEACEFUL) {
                            if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
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
                    }
                } else {
                    Optional<Creaking> protector = heart.getCreakingProtector();
                    if (protector.isPresent()) {
                        Creaking creaking = protector.get();
                        if (!CreakingHeartBlock.isNaturalNight(level)
                            && !creaking.isPersistenceRequired()
                            || heart.distanceToCreaking() > 34.0
                            || creaking.playerIsStuckInYou()
                        ) {
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
            boolean isNaturalNight = CreakingHeartBlock.isNaturalNight(level);
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
        if (!VanillaBackport.CONFIG.spawnCreakingFromHearts.get()) {
            return null;
        }

        BlockPos pos = heart.getBlockPos();
        Optional<Creaking> protector = SpawnUtil.trySpawnMob(
            ModEntities.CREAKING.get(),
            MobSpawnType.SPAWNER,
            level,
            pos,
            5,
            16,
            8,
            (server, blockPos, blockState, blockPos2, blockState2) -> blockState2.getCollisionShape(server, blockPos2).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape(server, blockPos), Direction.UP)
        );

        if (protector.isEmpty()) {
            return null;
        } else {
            Creaking creaking = protector.get();
            level.gameEvent(creaking, GameEvent.ENTITY_PLACE, creaking.position());
            level.broadcastEntityEvent(creaking, (byte) 60); // Poof particles
            creaking.setTransient(pos);
            return creaking;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveCustomOnly(provider);
    }

    public void creakingHurt() {
        Creaking creaking = this.getCreakingProtector().orElse(null);
        if (creaking != null) {
            if (this.level instanceof ServerLevel server) {
                if (this.emitter <= 0) {
                    this.emitParticles(server, 20, false);
                    if (this.getBlockState().getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE
                        && VanillaBackport.CONFIG.generateResin.get()
                    ) {
                        int i = this.level.getRandom().nextIntBetweenInclusive(2, 3);

                        for (int j = 0; j < i; j++) {
                            this.spreadResin().ifPresent(pos -> {
                                this.level.playSound(null, pos, ModSoundEvents.RESIN_PLACE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                this.level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(this.getBlockState()));
                            });
                        }
                    }

                    this.emitter = 100;
                    this.emitterTarget = creaking.getBoundingBox().getCenter();
                }
            }
        }
    }

    private Optional<BlockPos> spreadResin() {
        if (this.level == null) return Optional.empty();

        Mutable<BlockPos> mutable = new MutableObject<>(null);
        BlockPos.breadthFirstTraversal(this.worldPosition, 2, 64, (pos, consumer) -> {
            for (Direction direction : Util.shuffledCopy(Direction.values(), this.level.getRandom())) {
                BlockPos neighbor = pos.relative(direction);

                if (this.level.getBlockState(neighbor).is(ModBlockTags.PALE_OAK_LOGS)) {
                    consumer.accept(neighbor);
                }
            }
        }, pos -> {
            if (this.level.getBlockState(pos).is(ModBlockTags.PALE_OAK_LOGS)) {
                for (Direction direction : Util.shuffledCopy(Direction.values(), this.level.getRandom())) {
                    BlockPos neighbor = pos.relative(direction);
                    BlockState neighborState = this.level.getBlockState(neighbor);
                    Direction opposite = direction.getOpposite();
                    Block resinClump = ModBlocks.RESIN_CLUMP.get();

                    if (neighborState.isAir()) {
                        neighborState = resinClump.defaultBlockState();
                    } else if (neighborState.is(Blocks.WATER) && neighborState.getFluidState().isSource()) {
                        neighborState = resinClump.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
                    }

                    if (neighborState.is(resinClump) && !MultifaceBlock.hasFace(neighborState, opposite)) {
                        this.level.setBlock(neighbor, neighborState.setValue(MultifaceBlock.getFaceProperty(opposite), true), 3);
                        mutable.setValue(neighbor);
                        return false;
                    }
                }
            }

            return true;
        });
        return Optional.ofNullable(mutable.getValue());
    }

    private void emitParticles(ServerLevel level, int count, boolean reverseDirection) {
        Creaking creaking = this.getCreakingProtector().orElse(null);
        if (creaking != null) {
            int color = reverseDirection ? 16545810 : 6250335;
            RandomSource random = level.getRandom();

            for (double i = 0.0; i < count; i++) {
                AABB creakingBounds = creaking.getBoundingBox();
                Vec3 currentPos = new Vec3(creakingBounds.minX, creakingBounds.minY, creakingBounds.minZ)
                    .add(random.nextDouble() * creakingBounds.getXsize(), random.nextDouble() * creakingBounds.getYsize(), random.nextDouble() * creakingBounds.getZsize());
                Vec3 heartPos = Vec3.atLowerCornerOf(this.getBlockPos()).add(random.nextDouble(), random.nextDouble(), random.nextDouble());

                if (reverseDirection) {
                    Vec3 target = currentPos;
                    currentPos = heartPos;
                    heartPos = target;
                }

                TrailParticleOption particle = new TrailParticleOption(heartPos, color, random.nextInt(40) + 10);
                ModParticles.sendParticles(level, particle, true, true, currentPos.x, currentPos.y, currentPos.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }

    public void removeProtector(@Nullable DamageSource source) {
        Creaking creaking = this.getCreakingProtector().orElse(null);
        if (creaking != null) {
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
            double signalFromDistance = Mth.clamp(distance, 0.0, 32.0) / 32.0;
            return 15 - (int) Math.floor(signalFromDistance * 15.0);
        } else {
            return 0;
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.hasUUID("creaking")) {
            this.setCreakingInfo(tag.getUUID("creaking"));
        } else {
            this.clearCreakingInfo();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (this.creakingInfo != null) {
            tag.putUUID("creaking", this.creakingInfo.map(Entity::getUUID, uuid -> uuid));
        }
    }
}