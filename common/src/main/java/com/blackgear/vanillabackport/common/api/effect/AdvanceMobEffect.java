package com.blackgear.vanillabackport.common.api.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

// Extended version of MobEffect, based on 1.21+ code. - Echo2craft.
public class AdvanceMobEffect extends MobEffect {
    private static final int AMBIENT_ALPHA = Mth.floor(38.25F);
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<SoundEvent> soundOnAdded = Optional.empty();
    private final Function<MobEffectInstance, Supplier<ParticleOptions>> particleFactory;
    /*public AdvanceMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.particleFactory = (pMobEffectInstance) ->
                () -> ColorParticleOption.create(
                        ModParticles.ENTITY_EFFECT.get(),
                        ARGBUtils.color(pMobEffectInstance.isAmbient() ? AMBIENT_ALPHA : 255, color)
                );
    }*/

    /*public AdvanceMobEffect(MobEffectCategory pCategory, int pColor, ParticleOptions pParticle) {
        super(pCategory, pColor);
        this.particleFactory = (pMobEffectInstance) -> () -> pParticle;
    }*/

    // Constructor for custom ParticleOptions (lazy)
    public AdvanceMobEffect(MobEffectCategory pCategory, int pColor, Supplier<ParticleOptions> particleSupplier) {
        super(pCategory, pColor);
        this.particleFactory = (effect) -> particleSupplier;
    }

    public static AdvanceMobEffect create(MobEffectCategory pCategory, int pColor, Supplier<SimpleParticleType> pParticle){
        return new AdvanceMobEffect(pCategory,pColor, pParticle::get);
    }

    // Constructor for sound + custom particle
    public AdvanceMobEffect(MobEffectCategory pCategory, int pColor, SoundEvent pSound, Supplier<ParticleOptions> particleSupplier) {
        this(pCategory, pColor, particleSupplier);
        this.soundOnAdded = Optional.of(pSound);
    }

    public void onEffectAdded(LivingEntity pEntity, int pAmplifier) {
        this.soundOnAdded.ifPresent((pSoundEvent) -> {
            pEntity.level().playSound(
                    null,
                    pEntity.getX(),
                    pEntity.getY(),
                    pEntity.getZ(),
                    pSoundEvent,
                    pEntity.getSoundSource(),
                    1.0F,
                    1.0F
            );
        });
    }

    /*@Override
    public void applyEffectTick(LivingEntity pEntity, int pAmplifier) {
        onEffectAdded(pEntity,pAmplifier);
        super.applyEffectTick(pEntity,pAmplifier);
    }*/

    public void onMobRemoved(ServerLevel pServerLevel, LivingEntity pEntity, int pAmplifier, Entity.RemovalReason pRemovalReason){}
    public void onMobHurt(ServerLevel pServerLevel, LivingEntity pEntity, int pAmplifier, DamageSource pDamageSource, float pAmount){}

    public ParticleOptions createParticleOptions(MobEffectInstance pEffect) {
        return this.particleFactory.apply(pEffect).get();
    }

    // Builder part below,for testing.
    public AdvanceMobEffect withSoundOnAdded(SoundEvent pSound) {
        this.soundOnAdded = Optional.of(pSound);
        return this;
    }

    public AdvanceMobEffect withSoundOnAdded(Supplier<SoundEvent> pSound) {
        this.soundOnAdded = Optional.of(pSound.get());
        return this;
    }
}
