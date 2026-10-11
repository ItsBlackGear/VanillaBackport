package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.level.particle.particleoptions.TrailParticleOption;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.core.util.WorldUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EyeblossomBlock extends FlowerBlock {
    private final Type type;

    public EyeblossomBlock(Type type, Properties properties) {
        super(type.effect, type.effectDuration, properties);
        this.type = type;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (this.type.emitSounds() && random.nextInt(700) == 0) {
            BlockState below = level.getBlockState(pos.below());
            if (below.is(ModBlocks.PALE_MOSS_BLOCK.get())) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.EYEBLOSSOM_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.tryChangingState(state, level, pos, random)) {
            level.playSound(null, pos, this.type.transform().longSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.randomTick(state, level, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.tryChangingState(state, level, pos, random)) {
            level.playSound(null, pos, this.type.transform().shortSwitchSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        super.tick(state, level, pos, random);
    }

    private boolean tryChangingState(BlockState state, ServerLevel level, BlockPos origin, RandomSource random) {
        boolean shouldBeOpen = WorldUtilities.EnvironmentUtils.isNaturalNight(level);
        if (shouldBeOpen == this.type.open) {
            return false;
        } else {
            Type newType = this.type.transform();
            level.setBlockAndUpdate(origin, newType.state());
            level.gameEvent(GameEvent.BLOCK_CHANGE, origin, GameEvent.Context.of(state));
            newType.spawnTransformParticle(level, origin, random);
            BlockPos.betweenClosed(origin.offset(-3, -2, -3), origin.offset(3, 2, 3)).forEach(nearbyPos -> {
                BlockState nearbyState = level.getBlockState(nearbyPos);
                if (nearbyState == state) {
                    double distance = Math.sqrt(origin.distSqr(nearbyPos));
                    int delay = random.nextIntBetweenInclusive((int) (distance * 5.0), (int) (distance * 10.0));
                    level.scheduleTick(nearbyPos, state.getBlock(), delay);
                }
            });
            return true;
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide()
            && level.getDifficulty() != Difficulty.PEACEFUL
            && entity instanceof Bee bee
            && !bee.hasEffect(MobEffects.POISON)
        ) {
            bee.addEffect(new MobEffectInstance(MobEffects.POISON, 25));
        }
    }

    public enum Type {
        OPEN(true, MobEffects.BLINDNESS, 11, ModSoundEvents.EYEBLOSSOM_OPEN_LONG.get(), ModSoundEvents.EYEBLOSSOM_OPEN.get(), 16545810),
        CLOSED(false, MobEffects.CONFUSION, 7, ModSoundEvents.EYEBLOSSOM_CLOSE_LONG.get(), ModSoundEvents.EYEBLOSSOM_CLOSE.get(), 6250335);

        final boolean open;
        final MobEffect effect;
        final int effectDuration;
        final SoundEvent longSwitchSound;
        final SoundEvent shortSwitchSound;
        final int particleColor;

        Type(boolean open, MobEffect effect, int effectDuration, SoundEvent longSwitchSound, SoundEvent shortSwitchSound, int particleColor) {
            this.open = open;
            this.effect = effect;
            this.effectDuration = effectDuration;
            this.longSwitchSound = longSwitchSound;
            this.shortSwitchSound = shortSwitchSound;
            this.particleColor = particleColor;
        }

        public Block block() {
            return this.open ? ModBlocks.OPEN_EYEBLOSSOM.get() : ModBlocks.CLOSED_EYEBLOSSOM.get();
        }

        public BlockState state() {
            return this.block().defaultBlockState();
        }

        public Type transform() {
            return fromBoolean(!this.open);
        }

        public boolean emitSounds() {
            return this.open;
        }

        public static Type fromBoolean(boolean open) {
            return open ? OPEN : CLOSED;
        }

        public void spawnTransformParticle(ServerLevel level, BlockPos pos, RandomSource random) {
            Vec3 start = pos.getCenter();
            double lifetime = 0.5 + random.nextDouble();
            Vec3 velocity = new Vec3(random.nextDouble() - 0.5, random.nextDouble() + 1.0, random.nextDouble() - 0.5);
            Vec3 target = start.add(velocity.scale(lifetime));
            TrailParticleOption particle = new TrailParticleOption(target, this.particleColor, (int) (20.0 * lifetime));
            level.sendParticles(particle, start.x, start.y, start.z, 1, 0.0, 0.0, 0.0, 0.0);
        }

        public SoundEvent longSwitchSound() {
            return this.longSwitchSound;
        }
    }
}