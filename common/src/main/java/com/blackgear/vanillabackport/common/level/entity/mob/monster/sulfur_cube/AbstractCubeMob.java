package com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube;

import com.blackgear.vanillabackport.common.api.extensions.entity.movement.TravelAwareEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class AbstractCubeMob extends AgeableMob implements TravelAwareEntity {
    protected static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(AbstractCubeMob.class, EntityDataSerializers.INT);
    public float targetSquish;
    public float squish;
    public float oSquish;
    private boolean wasOnGround = false;

    protected AbstractCubeMob(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
        this.fixupDimensions();
        this.moveControl = new CubeMobMoveControl<>(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CubeMobFloatGoal(this));
        this.goalSelector.addGoal(4, new CubeMobRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new CubeMobKeepOnJumpingGoal(this));
        this.addBehaviourGoals();
        this.addTargetingGoals();
    }

    protected abstract void addBehaviourGoals();

    protected abstract void addTargetingGoals();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_SIZE, 1);
    }

    public void setSize(int size, boolean updateHealth) {
        int actualSize = Mth.clamp(size, 1, 127);
        this.entityData.set(ID_SIZE, actualSize);
        this.reapplyPosition();
        this.refreshDimensions();
        this.setCubeMobHealth(actualSize);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * actualSize);
        if (updateHealth) this.setHealth(this.getMaxHealth());
    }

    protected void setCubeMobHealth(int actualSize) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(actualSize * actualSize);
    }

    public int getSize() {
        return this.entityData.get(ID_SIZE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Size", this.getSize() - 1);
        compound.putBoolean("wasOnGround", this.wasOnGround);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setSize(compound.getInt("Size") + 1, false);
        super.readAdditionalSaveData(compound);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }

    public boolean isTiny() {
        return this.getSize() <= 1;
    }

    @Nullable
    protected abstract ParticleOptions getParticleType();

    @Override
    public void tick() {
        this.oSquish = this.squish;
        this.squish = this.squish + (this.targetSquish - this.squish) * 0.5F;
        super.tick();
        if (this.onGround() && !this.wasOnGround) {
            float size = this.getDimensions(this.getPose()).width() * 2.0F;
            float radius = size / 2.0F;

            for (int i = 0; i < size * 16.0F; i++) {
                float direction = this.random.nextFloat() * (float) (Math.PI * 2);
                float offset = this.random.nextFloat() * 0.5F + 0.5F;
                float x = Mth.sin(direction) * radius * offset;
                float z = Mth.cos(direction) * radius * offset;
                ParticleOptions particle = this.getParticleType();
                if (particle != null) {
                    this.level().addParticle(particle, this.getX() + x, this.getY(), this.getZ() + z, 0.0, 0.0, 0.0);
                }
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.targetSquish = -0.5F;
        } else if (!this.onGround() && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }

        this.wasOnGround = this.onGround();
        this.decreaseSquish();
    }

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }

    protected int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }

    @Override
    public void refreshDimensions() {
        double oldX = this.getX();
        double oldY = this.getY();
        double oldZ = this.getZ();
        super.refreshDimensions();
        this.setPos(oldX, oldY, oldZ);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ID_SIZE.equals(key)) {
            this.refreshDimensions();
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }

        super.onSyncedDataUpdated(key);
    }

    @Override @SuppressWarnings("unchecked")
    public EntityType<? extends AbstractCubeMob> getType() {
        return (EntityType<? extends AbstractCubeMob>) super.getType();
    }

    @Override
    public void remove(RemovalReason reason) {
        int size = this.getSize();
        if (!this.level().isClientSide() && size > 1 && this.isDeadOrDying()) {
            float width = this.getDimensions(this.getPose()).width();
            float xzSpawnOffset = width / 2.0F;
            int halfSize = size / 2;
            int count = this.getSplitCount();

            for (int i = 0; i < count; i++) {
                float x = (i % 2.0F - 0.5F) * xzSpawnOffset;
                float z = (i / 2.0F - 0.5F) * xzSpawnOffset;
                AbstractCubeMob cube = this.getType().create(this.level());
                if (cube != null) {
                    if (this.isPersistenceRequired()) cube.setPersistenceRequired();

                    cube.setCustomName(this.getCustomName());
                    cube.setNoAi(this.isNoAi());
                    cube.setInvulnerable(this.isInvulnerable());
                    this.setUpSplitCube(cube, halfSize, x, z);
                    this.level().addFreshEntity(cube);
                }
            }
        }

        super.remove(reason);
    }

    protected void setUpSplitCube(AbstractCubeMob cube, int halfSize, float x, float z) {
        cube.setSize(halfSize, true);
        cube.moveTo(this.getX() + x, this.getY() + 0.5, this.getZ() + z, this.random.nextFloat() * 360.0F, 0.0F);
    }

    protected int getSplitCount() {
        return 2 + this.random.nextInt(3);
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);
        if (entity instanceof IronGolem target && this.isDealsDamage()) {
            this.dealDamage(target);
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.isDealsDamage()) {
            this.dealDamage(player);
        }
    }

    protected void dealDamage(LivingEntity target) {
        if (this.level() instanceof ServerLevel level && this.isAlive() && this.isWithinMeleeAttackRange(target) && this.hasLineOfSight(target)) {
            DamageSource damageSource = this.damageSources().mobAttack(this);
            if (target.hurt(damageSource, this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) / 0.2F + 1.0F);
                EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
            }
        }
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float scale) {
        return new Vec3(0.0, dimensions.height() - 0.015625 * this.getSize() * scale, 0.0);
    }

    protected boolean isDealsDamage() {
        return !this.isTiny() && this.isEffectiveAi();
    }

    protected float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return this.getType().getDimensions().scale(this.getSize());
    }

    @Override
    public void jumpFromGround() {
        Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(movement.x, this.getJumpPower(), movement.z);
        this.hasImpulse = true;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F * this.getSize();
    }

    @Override
    public int getMaxHeadXRot() {
        return 0;
    }

    protected boolean doPlayJumpSound() {
        return this.getSize() > 0;
    }

    public float getSoundPitch() {
        float pitchAdjuster = this.isTiny() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * pitchAdjuster;
    }

    protected abstract SoundEvent getJumpSound();

    @Override @Nullable protected abstract SoundEvent getHurtSound(DamageSource damageSource);

    @Override @Nullable protected abstract SoundEvent getDeathSound();

    protected abstract SoundEvent getSquishSound();

    @Override @Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setSpawnSize(level, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected void setSpawnSize(ServerLevelAccessor level, DifficultyInstance difficulty) {
        RandomSource random = level.getRandom();
        int sizeScale = random.nextInt(3);
        if (sizeScale < 2 && random.nextFloat() < 0.5F * difficulty.getSpecialMultiplier()) {
            sizeScale++;
        }

        int size = 1 << sizeScale;
        this.setSize(size, true);
    }

    private static class CubeMobFloatGoal extends Goal {
        private final AbstractCubeMob cube;

        public CubeMobFloatGoal(AbstractCubeMob cube) {
            this.cube = cube;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            cube.getNavigation().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return (this.cube.isInWater() || this.cube.isInLava()) && this.cube.getMoveControl() instanceof CubeMobMoveControl;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.cube.getRandom().nextFloat() < 0.8F) {
                this.cube.getJumpControl().jump();
            }

            if (this.cube.getMoveControl() instanceof CubeMobMoveControl<?> control) {
                control.setWantedMovement(1.2);
            }
        }
    }

    private static class CubeMobKeepOnJumpingGoal extends Goal {
        private final AbstractCubeMob cube;

        public CubeMobKeepOnJumpingGoal(AbstractCubeMob cube) {
            this.cube = cube;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.cube.isPassenger();
        }

        @Override
        public void tick() {
            if (this.cube.getMoveControl() instanceof CubeMobMoveControl<?> control) {
                control.setWantedMovement(1.0);
            }
        }
    }

    protected static class CubeMobMoveControl<T extends AbstractCubeMob> extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private boolean isAggressive;
        protected final T cube;

        public CubeMobMoveControl(T cube) {
            super(cube);
            this.cube = cube;
            this.yRot = 180.0F * cube.getYRot() / (float) Math.PI;
        }

        public void setDirection(float yRot, boolean isAggressive) {
            this.yRot = yRot;
            this.isAggressive = isAggressive;
        }

        public void setWantedMovement(double speedModifier) {
            this.speedModifier = speedModifier;
            this.operation = Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.cube.setYRot(this.rotlerp(this.cube.getYRot(), this.yRot, 90.0F));
            this.cube.yHeadRot = this.cube.getYRot();
            this.cube.yBodyRot = this.cube.getYRot();
            if (this.operation != Operation.MOVE_TO) {
                this.cube.setZza(0.0F);
            } else {
                this.operation = Operation.WAIT;
                if (this.cube.onGround()) {
                    this.cube.setSpeed((float)(this.speedModifier * this.cube.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.cube.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.cube.getJumpControl().jump();
                        if (this.cube.doPlayJumpSound()) {
                            this.cube.playSound(this.cube.getJumpSound(), this.cube.getSoundVolume(), this.cube.getSoundPitch());
                        }
                    } else {
                        this.cube.xxa = 0.0F;
                        this.cube.zza = 0.0F;
                        this.cube.setSpeed(0.0F);
                    }
                } else {
                    this.cube.setSpeed((float)(this.speedModifier * this.cube.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }
        }
    }

    private static class CubeMobRandomDirectionGoal extends Goal {
        private final AbstractCubeMob cube;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public CubeMobRandomDirectionGoal(AbstractCubeMob cube) {
            this.cube = cube;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.cube.getTarget() == null
                && (this.cube.onGround() || this.cube.isInWater() || this.cube.isInLava() || this.cube.hasEffect(MobEffects.LEVITATION))
                && this.cube.getMoveControl() instanceof CubeMobMoveControl;
        }

        @Override
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.cube.getRandom().nextInt(60));
                this.chosenDegrees = this.cube.getRandom().nextInt(360);
            }

            if (this.cube.getMoveControl() instanceof CubeMobMoveControl<?> control) {
                control.setDirection(this.chosenDegrees, false);
            }
        }
    }
}