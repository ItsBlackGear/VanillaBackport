package com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityDataSerializers;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModules;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.data.ModBuiltInLootTables;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import com.blackgear.vanillabackport.core.util.WorldUtilities.*;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class Armadillo extends Animal {
    private static final EntityDimensions BABY_DIMENSIONS = ModEntityTypes.ARMADILLO.get().getDimensions().scale(0.6F);
    private static final EntityDataAccessor<ArmadilloState> ARMADILLO_STATE = SynchedEntityData.defineId(Armadillo.class, ModEntityDataSerializers.ARMADILLO_STATE.get());
    private long inStateTicks = 0L;
    public final AnimationState rollOutAnimationState = new AnimationState();
    public final AnimationState rollUpAnimationState = new AnimationState();
    public final AnimationState peekAnimationState = new AnimationState();
    private int scuteTime;
    private boolean peekReceivedClient = false;
    public static final Ingredient IS_FOOD = Ingredient.of(ModItemTags.ARMADILLO_FOOD);

    public Armadillo(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
        this.scuteTime = this.pickNextScuteDropTime();
    }

    @Override @Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntityTypes.ARMADILLO.get().create(level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 12.0)
            .add(Attributes.MOVEMENT_SPEED, 0.14);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ARMADILLO_STATE, ArmadilloState.IDLE);
    }

    public boolean isScared() {
        return this.getState() != ArmadilloState.IDLE;
    }

    public boolean shouldHideInShell() {
        return this.getState().shouldHideInShell(this.inStateTicks);
    }

    public boolean shouldSwitchToScaredState() {
        return this.getState() == ArmadilloState.ROLLING && this.inStateTicks > (long) ArmadilloState.ROLLING.animationDuration();
    }

    public ArmadilloState getState() {
        return this.entityData.get(ARMADILLO_STATE);
    }

    public void switchToState(ArmadilloState state) {
        this.entityData.set(ARMADILLO_STATE, state);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ARMADILLO_STATE.equals(key)) {
            this.inStateTicks = 0L;
        }

        super.onSyncedDataUpdated(key);
    }
    
    @Override
    protected Brain.Provider<Armadillo> brainProvider() {
        return ArmadilloAi.brainProvider();
    }
    
    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return ArmadilloAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }
    
    @Override @SuppressWarnings("unchecked")
    public Brain<Armadillo> getBrain() {
        return (Brain<Armadillo>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        ServerLevel level = (ServerLevel) this.level();
        ProfilerFiller profiler = level.getProfiler();
        profiler.push("armadilloBrain");
        this.getBrain().tick(level, this);
        profiler.popPush("armadilloActivityUpdate");
        ArmadilloAi.updateActivity(this);
        profiler.pop();

        if (this.isAlive() && --this.scuteTime <= 0 && this.shouldDropLoot()) {
            if (LootUtils.dropFromGiftLootTable(this, level, ModBuiltInLootTables.ARMADILLO_SHED, (serverLevel, stack) -> this.spawnAtLocation(stack))) {
                this.playSound(ModSoundEvents.ARMADILLO_SCUTE_DROP.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.gameEvent(GameEvent.ENTITY_PLACE);
            }

            this.scuteTime = this.pickNextScuteDropTime();
        }

        super.customServerAiStep();
    }

    private int pickNextScuteDropTime() {
        return this.random.nextInt(20 * TimeUtils.SECONDS_PER_MINUTE * 5) + 20 * TimeUtils.SECONDS_PER_MINUTE * 5;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

        if (this.isScared()) {
            float limit = this.getMaxHeadYRot();
            float headYRot = this.getYHeadRot();
            float delta = Mth.wrapDegrees(this.yBodyRot - headYRot);
            float targetDelta = Mth.clamp(Mth.wrapDegrees(this.yBodyRot - headYRot), -limit, limit);
            float newHeadYRot = headYRot + delta - targetDelta;
            this.setYHeadRot(newHeadYRot);
        }

        this.inStateTicks++;
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }
    
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDimensions(pose);
    }
    
    private void setupAnimationStates() {
        switch (this.getState()) {
            case IDLE:
                this.rollOutAnimationState.stop();
                this.rollUpAnimationState.stop();
                this.peekAnimationState.stop();
                break;
            case ROLLING:
                this.rollOutAnimationState.stop();
                this.rollUpAnimationState.startIfStopped(this.tickCount);
                this.peekAnimationState.stop();
                break;
            case SCARED:
                this.rollOutAnimationState.stop();
                this.rollUpAnimationState.stop();
                if (this.peekReceivedClient) {
                    this.peekAnimationState.stop();
                    this.peekReceivedClient = false;
                }

                if (this.inStateTicks == 0L) {
                    this.peekAnimationState.start(this.tickCount);
                    EntityUtils.fastForward(this.peekAnimationState, ArmadilloState.SCARED.animationDuration(), 1.0F);
                } else {
                    this.peekAnimationState.startIfStopped(this.tickCount);
                }
                break;
            case UNROLLING:
                this.rollOutAnimationState.startIfStopped(this.tickCount);
                this.rollUpAnimationState.stop();
                this.peekAnimationState.stop();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 64 && this.level().isClientSide()) {
            this.peekReceivedClient = true;
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSoundEvents.ARMADILLO_PEEK.get(), this.getSoundSource(), 1.0F, 1.0F, false);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItemTags.ARMADILLO_FOOD);
    }

    public static boolean checkArmadilloSpawnRules(EntityType<Armadillo> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(ModBlockTags.ARMADILLO_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    public boolean isScaredBy(LivingEntity entity) {
        if (!this.getBoundingBox().inflate(7.0, 2.0, 7.0).intersects(entity.getBoundingBox())) {
            return false;
        } else if (entity.getMobType() == MobType.UNDEAD) {
            return true;
        } else if (this.getLastHurtByMob() == entity) {
            return true;
        } else if (entity instanceof Player player) {
            return !player.isSpectator() && (player.isSprinting() || player.isPassenger());
        } else {
            return false;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("state", this.getState().getSerializedName());
        compound.putInt("scute_time", this.scuteTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.switchToState(ArmadilloState.fromName(compound.getString("state")));
        if (compound.contains("scute_time")) this.scuteTime = compound.getInt("scute_time");
    }

    public void rollUp() {
        if (!this.isScared()) {
            EntityUtils.stopInPlace(this);
            this.resetLove();
            this.gameEvent(GameEvent.ENTITY_INTERACT);
            this.playSound(ModSoundEvents.ARMADILLO_ROLL.get());
            this.switchToState(ArmadilloState.ROLLING);
        }
    }

    public void rollOut() {
        if (this.isScared()) {
            this.gameEvent(GameEvent.ENTITY_INTERACT);
            this.playSound(ModSoundEvents.ARMADILLO_UNROLL_FINISH.get());
            this.switchToState(ArmadilloState.IDLE);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isScared()) {
            amount = (amount - 1.0F) / 2.0F;
        }
        
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(damageSource, damageAmount);
        if (!this.isNoAi() && !this.isDeadOrDying()) {
            if (damageSource.getEntity() instanceof LivingEntity) {
                this.getBrain().setMemoryWithExpiry(ModMemoryModules.DANGER_DETECTED_RECENTLY.get(), true, 80L);
                if (this.canStayRolledUp()) {
                    this.rollUp();
                }
            } else if (this.isFreezing() || this.isOnFire()) {
                this.rollOut();
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.BRUSH) && this.brushOffScute()) {
            stack.hurtAndBreak(16, player, p -> p.broadcastBreakEvent(hand));
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return this.isScared() ? InteractionResult.FAIL : super.mobInteract(player, hand);
        }
    }
    
    public boolean brushOffScute() {
        if (this.isBaby()) {
            return false;
        } else {
            this.spawnAtLocation(new ItemStack(ModItems.ARMADILLO_SCUTE.get()));
            this.gameEvent(GameEvent.ENTITY_INTERACT);
            this.playSound(ModSoundEvents.ARMADILLO_BRUSH.get());
            return true;
        }
    }

    public boolean canStayRolledUp() {
        return !EntityUtils.isPanicking(this) && !EntityUtils.isInLiquid(this) && !this.isLeashed() && !this.isPassenger() && !this.isVehicle();
    }
    
    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.isScared();
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return ModSoundEvents.ARMADILLO_EAT.get();
    }

    @Override @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isScared() ? null : ModSoundEvents.ARMADILLO_AMBIENT.get();
    }

    @Override @Nullable
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ARMADILLO_DEATH.get();
    }

    @Override @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.isScared() ? ModSoundEvents.ARMADILLO_HURT_REDUCED.get() : ModSoundEvents.ARMADILLO_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSoundEvents.ARMADILLO_STEP.get(), 0.15F, 1.0F);
    }

    @Override
    public int getMaxHeadYRot() {
        return this.isScared() ? 0 : 32;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this) {
            @Override
            public void clientTick() {
                if (!Armadillo.this.isScared()) super.clientTick();
            }
        };
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return 0.26F;
    }
}
