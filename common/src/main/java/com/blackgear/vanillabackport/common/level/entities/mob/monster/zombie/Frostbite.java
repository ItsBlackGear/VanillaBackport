package com.blackgear.vanillabackport.common.level.entities.mob.monster.zombie;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.projectile.IceBall;
import com.blackgear.vanillabackport.common.registries.entities.ModMobEffects;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Frostbite extends RangedZombie {
    public Frostbite(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, 0.0F);
    }
    
    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new RangedZombieRangedAttackGoal(this, 1.0, this::getRangedAttackIntervalMin, this::getRangedAttackIntervalMax, 16.0F, false, false));
        this.goalSelector.addGoal(2, new RangedZombieMeleeAttackGoal(this, 1.0, false, false));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes().add(Attributes.MAX_HEALTH, 24.0).add(Attributes.MOVEMENT_SPEED, 0.21F);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.FROSTBITE_AMBIENT.get();
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource source) {
        return ModSoundEvents.FROSTBITE_HURT.get();
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.FROSTBITE_DEATH.get();
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return ModSoundEvents.FROSTBITE_STEP.get();
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        if (random.nextFloat() > 0.75) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ICE_BALL.get()));
        }
    }
    
    @Override
    public Item getRangedWeaponType() {
        return ModItems.ICE_BALL.get();
    }
    
    @Override
    protected Projectile getProjectile(Level level, LivingEntity owner, ItemStack stack) {
        return new IceBall(level, owner);
    }
    
    @Override
    protected SoundEvent getRangedAttackSound() {
        return ModSoundEvents.ICE_BALL_THROW.get();
    }
    
    @Override
    public boolean doHurtTarget(Entity target) {
        boolean result = super.doHurtTarget(target);
        if (result && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModMobEffects.FREEZING, 60), this);
        }
        
        return result;
    }
    
    private int getRangedAttackIntervalMin() {
        return (int) (20.0F * switch (this.level().getDifficulty()) {
            case PEACEFUL, NORMAL -> 1.0F;
            case EASY -> 3.0F;
            case HARD -> 0.75F;
        });
    }
    
    private int getRangedAttackIntervalMax() {
        return (int) (20.0F * switch (this.level().getDifficulty()) {
            case PEACEFUL, NORMAL -> 2.0F;
            case EASY -> 3.0F;
            case HARD -> 1.5F;
        });
    }
    
    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.POWDER_SNOW)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }
    
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        super.performRangedAttack(target, power);
        this.swing(InteractionHand.MAIN_HAND);
    }
    
    @Override
    protected void doUnderWaterConversion() {
        this.convertToZombieType(EntityType.ZOMBIE);
        if (!this.isSilent()) {
            LevelEvents.CONVERT_FROSTBITE_TO_ZOMBIE.trigger(this.level(), null, this.blockPosition(), 0);
        }
    }
}