package com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Nautilus extends AbstractNautilus {
    public Nautilus(EntityType<? extends AbstractNautilus> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return NautilusAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }
    
    @Override
    protected Brain.Provider<Nautilus> brainProvider() {
        return NautilusAi.brainProvider();
    }
    
    @Override @SuppressWarnings("unchecked")
    public Brain<Nautilus> getBrain() {
        return (Brain<Nautilus>) super.getBrain();
    }
    
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        Nautilus baby = ModEntityTypes.NAUTILUS.get().create(level);
        if (baby != null && this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }
        
        return baby;
    }
    
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.isBaby() ? super.getDimensions(pose).scale(0.5F) : super.getDimensions(pose);
    }
    
    @Override
    protected void customServerAiStep() {
        ProfilerFiller profiler = this.level().getProfiler();
        profiler.push("nautilusBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        profiler.popPush("nautilusActivityUpdate");
        NautilusAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep();
    }
    
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (this.isBaby()) {
            return this.isUnderWater() ? ModSoundEvents.BABY_NAUTILUS_AMBIENT.get() : ModSoundEvents.BABY_NAUTILUS_AMBIENT_ON_LAND.get();
        } else {
            return this.isUnderWater() ? ModSoundEvents.NAUTILUS_AMBIENT.get() : ModSoundEvents.NAUTILUS_AMBIENT_ON_LAND.get();
        }
    }
    
    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        if (this.isBaby()) {
            return this.isUnderWater() ? ModSoundEvents.BABY_NAUTILUS_HURT.get() : ModSoundEvents.BABY_NAUTILUS_HURT_ON_LAND.get();
        } else {
            return this.isUnderWater() ? ModSoundEvents.NAUTILUS_HURT.get() : ModSoundEvents.NAUTILUS_HURT_ON_LAND.get();
        }
    }
    
    @Override
    protected @Nullable SoundEvent getDeathSound() {
        if (this.isBaby()) {
            return this.isUnderWater() ? ModSoundEvents.BABY_NAUTILUS_DEATH.get() : ModSoundEvents.BABY_NAUTILUS_DEATH_ON_LAND.get();
        } else {
            return this.isUnderWater() ? ModSoundEvents.NAUTILUS_DEATH.get() : ModSoundEvents.NAUTILUS_DEATH_ON_LAND.get();
        }
    }
    
    @Override
    protected @Nullable SoundEvent getDashSound() {
        return this.isUnderWater() ? ModSoundEvents.NAUTILUS_DASH.get() : ModSoundEvents.NAUTILUS_DASH_ON_LAND.get();
    }
    
    @Override
    protected @Nullable SoundEvent getDashReadySound() {
        return this.isUnderWater() ? ModSoundEvents.NAUTILUS_DASH_READY.get() : ModSoundEvents.NAUTILUS_DASH_READY_ON_LAND.get();
    }
    
    @Override
    protected void playEatingSound() {
        SoundEvent nautilusEatSound = this.isBaby() ? ModSoundEvents.BABY_NAUTILUS_EAT.get() : ModSoundEvents.NAUTILUS_EAT.get();
        this.playSound(nautilusEatSound);
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return this.isBaby() ? ModSoundEvents.BABY_NAUTILUS_SWIM.get() : ModSoundEvents.NAUTILUS_SWIM.get();
    }
    
    protected void handleAirSupply(int preTickAirSupply) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(preTickAirSupply - 1);
            if (this.getAirSupply() <= -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().dryOut(), 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }
    }
    
    @Override
    public void baseTick() {
        int airSupply = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(airSupply);
    }
    
    @Override
    public boolean canBeLeashed(Player player) {
        return !this.isAggravated();
    }
}