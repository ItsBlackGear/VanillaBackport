package com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.mixin.common.access.ExplosionAccessor;
import com.blackgear.vanillabackport.core.network.ClientboundWindBurstPacket;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class WindBurst {
    public static void explode(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction interaction, ParticleOptions smallParticle, ParticleOptions largeParticle, Holder<SoundEvent> sound) {
        if (level instanceof ServerLevel server) {
            Explosion explosion = level.explode(source, damageSource, damageCalculator, x, y, z, radius, fire, interaction, false);
            
            if (!explosion.interactsWithBlocks()) {
                explosion.clearToBlow();
            }
            
            for (ServerPlayer player : server.players()) {
                if (player.distanceToSqr(x, y, z) < 4096.0) {
                    NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(
                        new ClientboundWindBurstPacket(
                            x,
                            y,
                            z,
                            radius,
                            ((ExplosionAccessor) explosion).getToBlow(),
                            explosion.getHitPlayers().get(player),
                            ((ExplosionAccessor) explosion).getBlockInteraction(),
                            smallParticle,
                            largeParticle,
                            sound
                        ),
                        
                        player
                    );
                }
            }
        }
    }
    
    //TODO: turn this into a capability API
    public static void triggerBlocks(Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        if (block instanceof DoorBlock door && state.is(BlockTags.WOODEN_DOORS)) {
            if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
                door.setOpen(null, level, state, pos, !state.getValue(DoorBlock.OPEN));
            }
        }
        
        if (block instanceof TrapDoorBlock && state.is(BlockTags.WOODEN_TRAPDOORS)) {
            boolean open = !state.getValue(TrapDoorBlock.OPEN);
            level.setBlock(pos, state.setValue(TrapDoorBlock.OPEN, open), 2);
            if (state.getValue(TrapDoorBlock.WATERLOGGED)) {
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
            
            SoundEvent sound = open ? SoundEvents.WOODEN_TRAPDOOR_OPEN : SoundEvents.WOODEN_TRAPDOOR_CLOSE;
            playBlockSound(level, pos, sound);
        }
        
        if (block instanceof FenceGateBlock) {
            boolean open = !state.getValue(FenceGateBlock.OPEN);
            level.setBlock(pos, state.setValue(FenceGateBlock.OPEN, open), 10);
            SoundEvent sound = open ? SoundEvents.FENCE_GATE_OPEN : SoundEvents.FENCE_GATE_CLOSE;
            playBlockSound(level, pos, sound);
        }
        
        if (block instanceof ButtonBlock button) {
            if (!state.getValue(BlockStateProperties.POWERED)) {
                button.press(state, level, pos);
            }
        }
        
        if (block instanceof LeverBlock lever) {
            BlockState newState = lever.pull(state, level, pos);
            float pitch = newState.getValue(BlockStateProperties.POWERED) ? 0.6F : 0.5F;
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, pitch);
        }
        
        if (block instanceof AbstractCandleBlock && AbstractCandleBlock.isLit(state)) {
            AbstractCandleBlock.extinguish(null, state, level, pos);
        }
    }
    
    private static void playBlockSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, (level.random.nextFloat() * 0.1F) + 0.9F);
    }
    
    public static void performWindBurstPostAttack(LivingEntity attacker, int level) {
        if (attacker.level() instanceof ServerLevel server) {
            if (attacker.fallDistance > 1.5F && !attacker.isFallFlying()) {
                List<Float> values = List.of(1.2F, 1.75F, 2.2F);
                
                WindBurst.explode(
                    server,
                    null,
                    null,
                    new SimpleExplosionDamageCalculator(
                        false,
                        false,
                        Optional.of(values.get(level - 1)),
                        BuiltInRegistries.BLOCK.getTag(ModBlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
                    ),
                    attacker.getX(),
                    attacker.getY(),
                    attacker.getZ(),
                    3.5F,
                    false,
                    Level.ExplosionInteraction.NONE,
                    ModParticles.GUST_EMITTER_SMALL.get(),
                    ModParticles.GUST_EMITTER_LARGE.get(),
                    ModSoundEvents.WIND_CHARGE_BURST
                );
            }
        }
    }
}