package com.blackgear.vanillabackport.common.level.entity.mob.animal.camel;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.modifiers.CamelSoundModifier;
import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CamelHusk extends Camel implements ControllableMob, CamelSoundModifier {
    public CamelHusk(EntityType<? extends Camel> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return true;
    }
    
    @Override
    public boolean isMobControlled() {
        return this.getFirstPassenger() instanceof Mob;
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        this.setPersistenceRequired();
        return super.mobInteract(player, hand);
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
    
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    @Override
    public boolean canBeLeashed(Player player) {
        return !this.isMobControlled();
    }
    
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItemTags.CAMEL_HUSK_FOOD);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.CAMEL_HUSK_AMBIENT.get();
    }
    
    @Override
    public boolean canMate(Animal otherAnimal) {
        return false;
    }
    
    @Override
    public @Nullable Camel getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }
    
    @Override
    public boolean canFallInLove() {
        return false;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.CAMEL_HUSK_DEATH.get();
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.CAMEL_HUSK_HURT.get();
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (state.getSoundType() == SoundType.SAND) {
            this.playSound(ModSoundEvents.CAMEL_HUSK_STEP_SAND.get(), 1.0F, 1.0F);
        } else {
            this.playSound(ModSoundEvents.CAMEL_HUSK_STEP.get(), 1.0F, 1.0F);
        }
    }
    
    @Override
    public SoundEvent getDashingSound() {
        return ModSoundEvents.CAMEL_HUSK_DASH.get();
    }
    
    @Override
    public SoundEvent getDashReadySound() {
        return ModSoundEvents.CAMEL_HUSK_DASH_READY.get();
    }
    
    @Override
    protected @Nullable SoundEvent getEatingSound() {
        return ModSoundEvents.CAMEL_HUSK_EAT.get();
    }
    
    @Override
    public SoundEvent getStandUpSound() {
        return ModSoundEvents.CAMEL_HUSK_STAND.get();
    }
    
    @Override
    public SoundEvent getSitDownSound() {
        return ModSoundEvents.CAMEL_HUSK_SIT.get();
    }
    
    @Override
    public SoundEvent getSaddleSoundEvent() {
        return ModSoundEvents.CAMEL_HUSK_SADDLE.get();
    }
    
    @Override
    public float chargeSpeedModifier() {
        return 4.0F;
    }
    
    public static class HuskGroupData extends Zombie.ZombieGroupData {
        public boolean triedToSpawnCamelHusk = false;
        
        public HuskGroupData(Zombie.ZombieGroupData data) {
            super(data.isBaby, data.canSpawnJockey);
        }
    }
}