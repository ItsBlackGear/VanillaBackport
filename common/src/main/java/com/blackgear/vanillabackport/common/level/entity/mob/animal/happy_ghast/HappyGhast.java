package com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.movement.PositionAwareEntity;
import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashPhysics;
import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashableCallback;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.mixin.common.access.LivingEntityAccessor;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import com.blackgear.vanillabackport.core.util.WorldUtilities.*;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.BooleanSupplier;

public class HappyGhast extends Animal implements PlayerRideable, LeashableCallback, PositionAwareEntity {
    public static final Ingredient IS_FOOD = Ingredient.of(ModItemTags.HAPPY_GHAST_FOOD);
    private int leashHolderTime = 0;
    private int serverStillTimeout;
    private static final EntityDataAccessor<Boolean> IS_LEASH_HOLDER = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STAYS_STILL = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);
    private boolean requiresPrecisePosition;

    public HappyGhast(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new GhastMoveControl(this, true, this::isOnStillTimeout);
        this.lookControl = new HappyGhastLookControl();
    }

    private void setServerStillTimeout(int timeout) {
        if (this.serverStillTimeout <= 0 && timeout > 0 && this.level() instanceof ServerLevel level) {
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
            level.getChunkSource().chunkMap.broadcast(this, new ClientboundTeleportEntityPacket(this));
        }

        this.serverStillTimeout = timeout;
        this.syncStayStillFlag();
    }

    private PathNavigation createBabyNavigation(Level level) {
        return new BabyFlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new GhastFloatGoal(this));
        this.goalSelector
            .addGoal(
                4,
                new TemptGoal.ForNonPathfinders(
                    this,
                    1.0,
                    stack -> !this.isHarnessed() && !this.isBaby() ? stack.is(ModItemTags.HAPPY_GHAST_TEMPT_ITEMS) : IS_FOOD.test(stack),
                    false,
                    7.0
                ).setTemptRange(16)
            );
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this, 16));
    }

    private void adultGhastSetup() {
        this.moveControl = new GhastMoveControl(this, true, this::isOnStillTimeout);
        this.lookControl = new HappyGhastLookControl();
        this.navigation = this.createNavigation(this.level());

        if (this.level() instanceof ServerLevel server) {
            this.removeAllGoals(goal -> true);
            this.registerGoals();
            this.getBrain().stopAll(server, this);
            this.brain.clearMemories();
        }
    }

    private void babyGhastSetup() {
        this.moveControl = new FlyingMoveControl(this, 180, true);
        this.lookControl = new LookControl(this);
        this.navigation = this.createBabyNavigation(this.level());
        this.setServerStillTimeout(0);
        this.removeAllGoals(goal -> true);
    }

    @Override
    protected void ageBoundaryReached() {
        if (this.isBaby()) {
            this.babyGhastSetup();
        } else {
            this.adultGhastSetup();
        }

        super.ageBoundaryReached();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
            .add(Attributes.MAX_HEALTH, 20.0)
            .add(Attributes.FLYING_SPEED, 0.05)
            .add(Attributes.MOVEMENT_SPEED, 0.05)
            .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected float sanitizeScale(float scale) {
        return Math.min(scale, 1.0F);
    }

    @Override
    public boolean getRequiresPrecisePosition() {
        return this.requiresPrecisePosition;
    }

    @Override
    public void setRequiresPrecisePosition(boolean requiresPrecisePosition) {
        this.requiresPrecisePosition = requiresPrecisePosition;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isOnStillTimeout()) {
            this.setDeltaMovement(Vec3.ZERO);
            return;
        }

        float speed = (float) this.getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F;
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(speed, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(speed, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5F));
            } else {
                this.moveRelative(speed, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
            }
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if (!level.isEmptyBlock(pos)) {
            return 0.0F;
        } else {
            return level.isEmptyBlock(pos.below()) && !level.isEmptyBlock(pos.below(2)) ? 10.0F : 5.0F;
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.isBaby() || super.canBreatheUnderwater();
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }
    
    @Override
    public int getAmbientSoundInterval() {
        int interval = super.getAmbientSoundInterval();
        return this.isVehicle() ? interval * 6 : interval;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.isBaby() ? ModSoundEvents.GHASTLING_AMBIENT.get() : ModSoundEvents.HAPPY_GHAST_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return this.isBaby() ? ModSoundEvents.GHASTLING_HURT.get() : ModSoundEvents.HAPPY_GHAST_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return this.isBaby() ? ModSoundEvents.GHASTLING_DEATH.get() : ModSoundEvents.HAPPY_GHAST_DEATH.get();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override @Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
        return ModEntityTypes.HAPPY_GHAST.get().create(level);
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.2375F : 1.0F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return IS_FOOD.test(stack);
    }

    public void equipHarness() {
        this.level().playSound(null, this, ModSoundEvents.HARNESS_EQUIP.get(), SoundSource.NEUTRAL, 0.5F, 1.0F);
    }

    public boolean canBeHarnessed() {
        return this.isAlive() && !this.isBaby();
    }

    public boolean isHarnessed() {
        return this.getItemBySlot(EquipmentSlot.CHEST).is(ModItemTags.HARNESSES);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isBaby()) {
            return super.mobInteract(player, hand);
        } else {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.isEmpty()) {
                InteractionResult result = stack.interactLivingEntity(player, this, hand);
                if (result.consumesAction()) {
                    return result;
                }
            }

            if (!stack.is(Items.SHEARS) || this.isVehicle() || !this.isHarnessed() && !player.isCreative()) {
                if (this.isHarnessed()) {
                    if (!this.level().isClientSide()) player.startRiding(this);

                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                } else {
                    return super.mobInteract(player, hand);
                }
            } else {
                stack.hurtAndBreak(1, player, getSlotForHand(hand));
                this.playSound(ModSoundEvents.HARNESS_UNEQUIP.get());
                ItemStack harness = this.getItemBySlot(EquipmentSlot.CHEST);
                this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                this.spawnAtLocation(harness, this.getBbHeight() + 0.5F);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        AABB box = super.getBoundingBoxForCulling();
        float height = this.getBbHeight();
        return box.setMinY(box.minY - height / 2.0F);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        if (!this.isVehicle()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.HARNESS_GOGGLES_DOWN.get(), this.getSoundSource(), 1.0F, 1.0F);
        }

        super.addPassenger(passenger);
        if (!this.level().isClientSide) {
            if (!this.scanPlayerAboveGhast()) {
                this.setServerStillTimeout(0);
            } else if (this.serverStillTimeout > 10) {
                this.setServerStillTimeout(10);
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide) {
            this.setServerStillTimeout(10);
        }

        if (!this.isVehicle()) {
            this.clearRestriction();
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.HARNESS_GOGGLES_UP.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 4;
    }

    @Override @Nullable
    public LivingEntity getControllingPassenger() {
        return this.isHarnessed() && !this.isOnStillTimeout() && this.getFirstPassenger() instanceof Player player
            ? player
            : super.getControllingPassenger();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float forward = player.xxa;
        float strafe = 0.0F;
        float upward = 0.0F;
        if (player.zza != 0.0F) {
            float xOffset = Mth.cos(player.getXRot() * Mth.DEG_TO_RAD);
            float zOffset = -Mth.sin(player.getXRot() * Mth.DEG_TO_RAD);
            if (player.zza < 0.0F) {
                xOffset *= -0.5F;
                zOffset *= -0.5F;
            }

            upward = zOffset;
            strafe = xOffset;
        }

        if (((LivingEntityAccessor) player).isJumping()) {
            upward += 0.5F;
        }

        return new Vec3(forward, upward, strafe).scale(((double) 3.9F * this.getAttributeValue(Attributes.FLYING_SPEED)) * VanillaBackport.COMMON_CONFIG.happyGhastSpeedModifier.get());
    }

    protected Vec2 getRiddenRotation(LivingEntity controller) {
        return new Vec2(controller.getXRot() * 0.5F, controller.getYRot());
    }

    @Override
    protected void tickRidden(Player controller, Vec3 riddenInput) {
        super.tickRidden(controller, riddenInput);
        Vec2 rotation = this.getRiddenRotation(controller);
        float yRot = this.getYRot();
        float diff = Mth.wrapDegrees(rotation.y - yRot);
        yRot += diff * 0.08F;
        this.setRot(yRot, rotation.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
    }

    @Override
    protected Brain.Provider<HappyGhast> brainProvider() {
        return HappyGhastAi.brainProvider();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return HappyGhastAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override @SuppressWarnings("unchecked")
    public Brain<HappyGhast> getBrain() {
        return (Brain<HappyGhast>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        if (this.isBaby()) {
            ProfilerFiller profiler = this.level().getProfiler();
            profiler.push("happyGhastBrain");
            this.getBrain().tick((ServerLevel) this.level(), this);
            profiler.popPush("HappyGhastActivityUpdate");
            HappyGhastAi.updateActivity(this);
            profiler.pop();
        }

        this.checkRestriction();
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.leashHolderTime > 0) {
                this.leashHolderTime--;
            }

            this.setLeashHolder(this.leashHolderTime > 0);
            if (this.serverStillTimeout > 0) {
                if (this.tickCount > 60) {
                    this.serverStillTimeout--;
                }

                this.setServerStillTimeout(this.serverStillTimeout);
            }

            if (this.scanPlayerAboveGhast()) {
                this.setServerStillTimeout(10);
            }
        }
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide) {
            this.setRequiresPrecisePosition(this.isOnStillTimeout());
        }

        super.aiStep();
        this.continuousHeal();
    }

    private int getHappyGhastRestrictionRadius() {
        return !this.isBaby() && !this.isHarnessed() ? 64 : 32;
    }

    private void checkRestriction() {
        if (!this.isLeashed() && !this.isVehicle()) {
            int radius = this.getHappyGhastRestrictionRadius();
            if (!this.hasRestriction() || !this.getRestrictCenter().closerThan(this.blockPosition(), radius + 16) || radius != this.getRestrictRadius()) {
                this.restrictTo(this.blockPosition(), radius);
            }
        }
    }

    private void continuousHeal() {
        if (this.level() instanceof ServerLevel server && this.isAlive() && this.deathTime == 0 && this.getMaxHealth() != this.getHealth()) {
            boolean isFastHealing = server.dimensionType().natural() && (EnvironmentUtils.isInClouds(this) || EnvironmentUtils.precipitationAt(this.level(), this.blockPosition()) != Biome.Precipitation.NONE);
            if (this.tickCount % (isFastHealing ? 20 : 600) == 0) {
                this.heal(1.0F);
            }
        }
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_LEASH_HOLDER, false);
        builder.define(STAYS_STILL, false);
    }

    private void setLeashHolder(boolean holder) {
        this.entityData.set(IS_LEASH_HOLDER, holder);
    }

    public boolean isLeashHolder() {
        return this.entityData.get(IS_LEASH_HOLDER);
    }

    private void syncStayStillFlag() {
        this.entityData.set(STAYS_STILL, this.serverStillTimeout > 0);
    }

    public boolean staysStill() {
        return this.entityData.get(STAYS_STILL);
    }

    @Override
    public boolean vb$supportsQuadLeashAsHolder() {
        return true;
    }

    @Override
    public Vec3[] vb$getQuadLeashHolderOffsets() {
        return LeashPhysics.createQuadOffsets(this, -0.03125, 0.4375, 0.46875, 0.03125);
    }

    @Override
    protected Vec3 getLeashOffset() {
        return Vec3.ZERO;
    }

    @Override
    public double vb$leashElasticDistance() {
        return 10.0;
    }

    @Override
    public double vb$leashSnapDistance() {
        return 16.0;
    }

    @Override
    public void vb$onElasticLeashPull() {
        LeashableCallback.super.vb$onElasticLeashPull();
        this.getMoveControl().operation = MoveControl.Operation.WAIT;
    }

    @Override
    public void vb$notifyLeashHolder(Leashable leashee) {
        Entity entity = (Entity) leashee;
        if (((LeashableCallback) entity).vb$supportsQuadLeash()) {
            this.leashHolderTime = 5;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("still_timeout", this.serverStillTimeout);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setServerStillTimeout(compound.getInt("still_timeout"));
    }

    public boolean isOnStillTimeout() {
        return this.staysStill() || this.serverStillTimeout > 0;
    }

    private boolean scanPlayerAboveGhast() {
        AABB happyGhastBb = this.getBoundingBox();
        AABB ghastDetectionBox = new AABB(happyGhastBb.minX - 1.0, happyGhastBb.maxY - Mth.EPSILON, happyGhastBb.minZ - 1.0, happyGhastBb.maxX + 1.0, happyGhastBb.maxY + happyGhastBb.getYsize() / 2.0, happyGhastBb.maxZ + 1.0);

        for (Player player : this.level().players()) {
            if (!player.isSpectator()) {
                Entity rootVehicle = player.getRootVehicle();
                if (!(rootVehicle instanceof HappyGhast) && ghastDetectionBox.contains(rootVehicle.position())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new GhastBodyRotationControl(this);
    }

    @Override
    public boolean canBeCollidedWith() {
        if (!this.isBaby() && this.isAlive()) {
            return this.isVehicle() || this.isOnStillTimeout();
        } else {
            return false;
        }
    }
    
    static class BabyFlyingPathNavigation extends FlyingPathNavigation {
        public BabyFlyingPathNavigation(Mob mob, Level level) {
            super(mob, level);
            this.setCanOpenDoors(false);
            this.setCanFloat(true);
        }

        @Override
        protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
            return isClearForMovementBetween(this.mob, posVec31, posVec32, false);
        }
    }

    static class GhastBodyRotationControl extends BodyRotationControl {
        private final HappyGhast ghast;

        public GhastBodyRotationControl(HappyGhast ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        @Override
        public void clientTick() {
            if (this.ghast.isVehicle()) {
                this.ghast.yBodyRot = this.ghast.yHeadRot = this.ghast.getYRot();
            }

            super.clientTick();
        }
    }

    static class GhastFloatGoal extends FloatGoal {
        private final HappyGhast ghast;

        public GhastFloatGoal(HappyGhast ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        @Override
        public boolean canUse() {
            return !this.ghast.isOnStillTimeout() && super.canUse();
        }
    }

    class HappyGhastLookControl extends LookControl {
        HappyGhastLookControl() {
            super(HappyGhast.this);
        }

        @Override
        public void tick() {
            if (HappyGhast.this.isOnStillTimeout()) {
                float closeAngle = MthUtils.wrapDegrees90(HappyGhast.this.getYRot());
                HappyGhast.this.setYRot(HappyGhast.this.getYRot() - closeAngle);
                HappyGhast.this.setYHeadRot(HappyGhast.this.getYRot());
            } else if (this.lookAtCooldown > 0) {
                this.lookAtCooldown--;
                double x = this.wantedX - HappyGhast.this.getX();
                double z = this.wantedZ - HappyGhast.this.getZ();
                HappyGhast.this.setYRot(-((float) Mth.atan2(x, z)) * Mth.RAD_TO_DEG);
                HappyGhast.this.yBodyRot = HappyGhast.this.getYRot();
                HappyGhast.this.yHeadRot = HappyGhast.this.yBodyRot;
            } else {
                if (HappyGhast.this.isVehicle() && HappyGhast.this.getControllingPassenger() instanceof Player) {
                    return;
                }

                Vec3 movement = this.mob.getDeltaMovement();
                this.mob.setYRot(-((float) Mth.atan2(movement.x, movement.z)) * Mth.RAD_TO_DEG);
                this.mob.yBodyRot = this.mob.getYRot();
            }
        }
    }

    static class GhastMoveControl extends MoveControl {
        private final HappyGhast ghast;
        private int floatDuration;
        private final boolean careful;
        private final BooleanSupplier shouldBeStopped;

        public GhastMoveControl(HappyGhast ghast, boolean careful, BooleanSupplier shouldBeStopped) {
            super(ghast);
            this.ghast = ghast;
            this.careful = careful;
            this.shouldBeStopped = shouldBeStopped;
        }

        @Override
        public void tick() {
            if (this.shouldBeStopped.getAsBoolean()) {
                this.operation = Operation.WAIT;
                EntityUtils.stopInPlace(this.ghast);
            }

            if (this.operation == Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                    Vec3 target = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                    if (this.canReach(target)) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(target.normalize().scale(this.ghast.getAttributeValue(Attributes.FLYING_SPEED) * 5.0 / 3.0)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 target) {
            AABB entityBox = this.ghast.getBoundingBox();
            AABB targetBox = entityBox.move(target);

            if (this.careful) {
                for (BlockPos position : PositionUtils.betweenClosed(targetBox.inflate(1.0))) {
                    if (!this.blockTraversalPossible(this.ghast.level(), null, null, position, false, false)) {
                        return false;
                    }
                }
            }

            boolean inWater = this.ghast.isInWater();
            boolean inLava = this.ghast.isInLava();
            Vec3 start = this.ghast.position();
            Vec3 end = start.add(target);

            return PositionUtils.forEachBlockIntersectedBetween(
                start,
                end,
                targetBox,
                (pos, step) -> CollisionUtils.intersects(entityBox, pos) || this.blockTraversalPossible(this.ghast.level(), start, end, pos, inWater, inLava)
            );
        }

        private boolean blockTraversalPossible(
            BlockGetter level,
            @Nullable Vec3 start,
            @Nullable Vec3 end,
            BlockPos pos,
            boolean canPathThroughWater,
            boolean canPathThroughLava
        ) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) {
                return true;
            } else {
                boolean preciseBlockCollisions = start != null && end != null;
                boolean pathNoCollisions = preciseBlockCollisions
                    ? !CollisionUtils.collidedWithShapeMovingFrom(this.ghast, start, end, state.getCollisionShape(level, pos).move(pos.getX(), pos.getY(), pos.getZ()).toAabbs())
                    : state.getCollisionShape(level, pos).isEmpty();
                if (!this.careful) {
                    return pathNoCollisions;
                } else if (state.is(ModBlockTags.HAPPY_GHAST_AVOIDS)) {
                    return false;
                } else {
                    FluidState fluidState = level.getFluidState(pos);
                    if (!fluidState.isEmpty() && (!preciseBlockCollisions || CollisionUtils.collidedWithFluid(this.ghast, fluidState, pos, start, end))) {
                        if (fluidState.is(FluidTags.WATER)) {
                            return canPathThroughWater;
                        }

                        if (fluidState.is(FluidTags.LAVA)) {
                            return canPathThroughLava;
                        }
                    }

                    return pathNoCollisions;
                }
            }
        }
    }

    static class RandomFloatAroundGoal extends Goal {
        private final HappyGhast ghast;
        private final int distanceToBlocks;

        public RandomFloatAroundGoal(HappyGhast ghast, int distanceToBlocks) {
            this.ghast = ghast;
            this.distanceToBlocks = distanceToBlocks;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl control = this.ghast.getMoveControl();
            if (!control.hasWanted()) {
                return true;
            } else {
                double x = control.getWantedX() - this.ghast.getX();
                double y = control.getWantedY() - this.ghast.getY();
                double z = control.getWantedZ() - this.ghast.getZ();
                double distance = x * x + y * y + z * z;
                return distance < 1.0 || distance > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Vec3 target = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
            this.ghast.getMoveControl().setWantedPosition(target.x(), target.y(), target.z(), 1.0);
        }

        public static Vec3 getSuitableFlyToPosition(Mob mob, int distanceToBlocks) {
            Level level = mob.level();
            RandomSource random = mob.getRandom();
            Vec3 origin = mob.position();
            Vec3 target = null;

            for (int attempt = 0; attempt < 64; attempt++) {
                target = chooseRandomPositionWithRestriction(mob, origin, random);
                if (target != null && isGoodTarget(level, target, distanceToBlocks)) {
                    return target;
                }
            }

            if (target == null) {
                target = chooseRandomPosition(origin, random);
            }

            BlockPos pos = BlockPos.containing(target);
            int floor = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
            if (floor < pos.getY() && floor > level.getMinBuildHeight()) {
                target = new Vec3(target.x(), mob.getY() - Math.abs(mob.getY() - target.y()), target.z());
            }

            return target;
        }

        private static boolean isGoodTarget(Level level, Vec3 target, int distanceToBlocks) {
            if (distanceToBlocks <= 0) {
                return true;
            } else {
                BlockPos pos = BlockPos.containing(target);
                if (level.getBlockState(pos).isAir()) {
                    for (Direction direction : Direction.values()) {
                        for (int distance = 1; distance < distanceToBlocks; distance++) {
                            BlockPos neighbor = pos.relative(direction, distance);
                            if (!level.getBlockState(neighbor).isAir()) {
                                return true;
                            }
                        }
                    }
                }

                return false;
            }
        }

        private static Vec3 chooseRandomPosition(Vec3 origin, RandomSource random) {
            double x = origin.x() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double y = origin.y() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double z = origin.z() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            return new Vec3(x, y, z);
        }

        @Nullable
        private static Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 origin, RandomSource random) {
            Vec3 target = chooseRandomPosition(origin, random);
            return mob.hasRestriction() && !mob.isWithinRestriction(BlockPos.containing(target)) ? null : target;
        }
    }
}