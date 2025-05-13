package com.blackgear.vanillabackport.common.level.entities.happyghast;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.mixin.access.LivingEntityAccessor;
import com.blackgear.vanillabackport.core.util.CollisionUtils;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class HappyGhast extends Animal implements Saddleable, PlayerRideable {
    public static final Ingredient IS_FOOD = Ingredient.of(ModItemTags.HAPPY_GHAST_FOOD);
    private boolean requiresPrecisePosition;

    public HappyGhast(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new GhastMoveControl(this);
        this.lookControl = new HappyGhastLookControl();
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 2.6F * this.getScale();
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
                new HappyGhastTemptGoal(
                    this,
                    1.0,
                    stack -> !this.isSaddled() && !this.isBaby() ? stack.is(ModItemTags.HAPPY_GHAST_TEMPT_ITEMS) : IS_FOOD.test(stack),
                    false,
                    7.0
                )
            );
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
    }

    private void adultGhastSetup() {
        this.moveControl = new GhastMoveControl(this);
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

    public boolean getRequiresPrecisePosition() {
        return this.requiresPrecisePosition;
    }

    public void setRequiresPrecisePosition(boolean bl) {
        this.requiresPrecisePosition = bl;
    }

    public void stopInPlace() {
        this.getNavigation().stop();
        this.setXxa(0.0F);
        this.setYya(0.0F);
        this.setSpeed(0.0F);
        this.setDeltaMovement(0.0, 0.0, 0.0);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isPlayerAboveGhast()) {
            this.setDeltaMovement(Vec3.ZERO);
            return;
        }

        float speed = 0.09F;
        if (this.isInWater()) {
            this.moveRelative(speed, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
        } else if (this.isInLava()) {
            this.moveRelative(speed, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
        } else {
            this.moveRelative(speed, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
        }
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
        return ModEntities.HAPPY_GHAST.get().create(level);
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.2375F : 1.0F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return IS_FOOD.test(stack);
    }

    @Override
    public void equipSaddle(@Nullable SoundSource source) {
        this.level().playSound(null, this, ModSoundEvents.HARNESS_EQUIP.get(), SoundSource.NEUTRAL, 0.5F, 1.0F);
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public boolean isSaddled() {
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

            if (!stack.is(Items.SHEARS) || this.isVehicle() || !this.isSaddled() && !player.isCreative()) {
                if (this.isSaddled()) {
                    if (!this.level().isClientSide()) {
                        player.startRiding(this);
                    }

                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                } else {
                    return super.mobInteract(player, hand);
                }
            } else {
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
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
    protected void positionRider(Entity passenger, MoveFunction callback) {
        if (this.hasPassenger(passenger)) {
            double y = this.getY() + passenger.getMyRidingOffset();
            int index = this.getPassengers().indexOf(passenger);

            double offsetX = 0.0;
            double offsetZ = 0.0;

            if (index == 0) { // Front
                offsetZ = 1.8;
            } else if (index == 1) { // Left
                offsetX = -1.8;
            } else if (index == 2) { // Back
                offsetZ = -1.8;
            } else if (index == 3) { // Right
                offsetX = 1.8;
            }

            float yaw = this.getYRot() * ((float)Math.PI / 180F);
            double rotatedX = offsetX * Mth.cos(yaw) - offsetZ * Mth.sin(yaw);
            double rotatedZ = offsetX * Mth.sin(yaw) + offsetZ * Mth.cos(yaw);

            callback.accept(passenger, this.getX() + rotatedX, y + 3.75, this.getZ() + rotatedZ);
        }
    }

    @Override
    protected void addPassenger(Entity passenger) {
        if (!this.isVehicle()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.HARNESS_GOGGLES_DOWN.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.isVehicle()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.HARNESS_GOGGLES_UP.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 4;
    }

    @Override @Nullable
    public LivingEntity getControllingPassenger() {
        return !this.isNoAi() && !this.isPlayerAboveGhast() && this.getFirstPassenger() instanceof Player player
            ? player
            : super.getControllingPassenger();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float forward = player.xxa;
        float strafe = 0.0F;
        float upward = 0.0F;
        if (player.zza != 0.0F) {
            float xOffset = Mth.cos(player.getXRot() * (float) (Math.PI / 180.0));
            float zOffset = -Mth.sin(player.getXRot() * (float) (Math.PI / 180.0));
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

        return new Vec3(forward, upward, strafe).scale(0.18F * VanillaBackport.CONFIG.happyGhastSpeedModifier.get());
    }

    protected Vec2 getRiddenRotation(LivingEntity livingEntity) {
        return new Vec2(livingEntity.getXRot() * 0.5F, livingEntity.getYRot());
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.tickRidden(player, vec3);
        Vec2 riddenRotation = this.getRiddenRotation(player);
        float yRot = this.getYRot();
        float degrees = Mth.wrapDegrees(riddenRotation.y - yRot);
        yRot += degrees * 0.08F;
        this.setRot(yRot, riddenRotation.x);
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
            this.level().getProfiler().push("happyGhastBrain");
            this.getBrain().tick((ServerLevel) this.level(), this);
            this.level().getProfiler().popPush("HappyGhastActivityUpdate");
            HappyGhastAi.updateActivity(this);
            this.level().getProfiler().pop();
        }

        this.checkRestriction();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.continuousHeal();
    }

    private int getHappyGhastRestrictionRadius() {
        return !this.isBaby() && !this.isSaddled() ? 64 : 32;
    }

    private void checkRestriction() {
        if (!this.isLeashed() && !this.isVehicle()) {
            int restrictionRadius = this.getHappyGhastRestrictionRadius();
            if (!this.hasRestriction() || !this.getRestrictCenter().closerThan(this.blockPosition(), restrictionRadius + 16)) {
                this.restrictTo(this.blockPosition(), restrictionRadius);
            }
        }
    }

    private void continuousHeal() {
        if (this.level() instanceof ServerLevel server && this.isAlive() && this.deathTime == 0 && this.getMaxHealth() != this.getHealth()) {
            boolean canHeal = server.dimensionType().natural() && (this.isInClouds() || this.precipitationAt(this.blockPosition()) != Biome.Precipitation.NONE);
            if (this.tickCount % (canHeal ? 20 : 600) == 0) {
                this.heal(1.0F);
            }
        }
    }

    private Biome.Precipitation precipitationAt(BlockPos pos) {
        if (!this.level().isRaining()) {
            return Biome.Precipitation.NONE;
        } else if (!this.level().canSeeSky(pos)) {
            return Biome.Precipitation.NONE;
        } else if (this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return Biome.Precipitation.NONE;
        } else {
            Biome biome = this.level().getBiome(pos).value();
            return biome.getPrecipitationAt(pos);
        }
    }

    private boolean isInClouds() {
        if (this.level().dimensionType().natural()) {
            int cloudHeight = 192;
            if (this.getY() + this.getBbHeight() < cloudHeight) {
                return false;
            } else {
                int cloudRange = cloudHeight + 4;
                return this.getY() <= cloudRange;
            }
        }

        return false;
    }

    public boolean isPlayerAboveGhast() {
        AABB box = this.getBoundingBox();
        AABB topSurface = new AABB(box.minX - 1.0, box.maxY, box.minZ - 1.0, box.maxX + 1.0, box.maxY + box.getYsize() / 2.0, box.maxZ + 1.0);

        for (Player player : this.level().players()) {
            if (player.isSpectator()) continue;

            if (topSurface.contains(player.getX(), player.getY(), player.getZ())) {
                return true;
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
        return !this.isBaby() && this.isPlayerAboveGhast();
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
                this.ghast.yHeadRot = this.ghast.getYRot();
                this.ghast.yBodyRot = this.ghast.yHeadRot;
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
            return !this.ghast.isPlayerAboveGhast() && super.canUse();
        }
    }

    class HappyGhastLookControl extends LookControl {
        HappyGhastLookControl() {
            super(HappyGhast.this);
        }

        @Override
        public void tick() {
            if (HappyGhast.this.isPlayerAboveGhast()) {
                float degrees = wrapDegrees90(HappyGhast.this.getYRot());
                HappyGhast.this.setYRot(HappyGhast.this.getYRot() - degrees);
                HappyGhast.this.setYHeadRot(HappyGhast.this.getYRot());
            } else if (this.lookAtCooldown > 0) {
                this.lookAtCooldown--;
                double x = this.wantedX - HappyGhast.this.getX();
                double z = this.wantedZ - HappyGhast.this.getZ();
                HappyGhast.this.setYRot(-((float) Mth.atan2(x, z)) * (180.0F / (float)Math.PI));
                HappyGhast.this.yBodyRot = HappyGhast.this.getYRot();
                HappyGhast.this.yHeadRot = HappyGhast.this.yBodyRot;
            } else {
                if (HappyGhast.this.isVehicle() && HappyGhast.this.getControllingPassenger() instanceof Player) {
                    return;
                }

                Vec3 motion = this.mob.getDeltaMovement();
                this.mob.setYRot(-((float) Mth.atan2(motion.x, motion.z)) * (180F / (float) Math.PI));
                this.mob.yBodyRot = this.mob.getYRot();
            }
        }

        public static float wrapDegrees90(float degrees) {
            float result = degrees % 90.0F;
            if (result >= 45.0F) result -= 90.0F;
            if (result < -45.0F) result += 90.0F;
            return result;
        }
    }

    static class GhastMoveControl extends MoveControl {
        private final HappyGhast ghast;
        private int floatDuration;

        public GhastMoveControl(HappyGhast ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        @Override
        public void tick() {
            if (this.ghast.canBeCollidedWith()) {
                this.operation = Operation.WAIT;
                this.ghast.stopInPlace();
                this.ghast.setRequiresPrecisePosition(true);
            }

            if (this.operation == Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;

                    Vec3 target = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                    double distance = target.length();
                    target = target.normalize();

                    if (this.canReach(target, Mth.ceil(distance))) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(target.scale(0.1)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 direction, int steps) {
            boolean hasBlockCollision = !CollisionUtils.noBlockCollision(this.ghast.level(), this.ghast, this.ghast.getBoundingBox());
            AABB box = this.ghast.getBoundingBox();

            for (int step = 1; step < steps; ++step) {
                box = box.move(direction);

                if (step == steps - 1) {
                    box = box.inflate(1.0);
                } else if (hasBlockCollision && step < steps - 1) {
                    continue;
                }

                boolean inLiquid = this.ghast.isInWater() || this.ghast.isInLava();
                if (!CollisionUtils.noCollision(this.ghast.level(), this.ghast, box, !inLiquid)) {
                    return false;
                }
            }

            return true;
        }

        public void setWait() {
            this.operation = Operation.WAIT;
        }
    }

    static class RandomFloatAroundGoal extends Goal {
        private final HappyGhast ghast;
        private final int distanceToBlocks;

        public RandomFloatAroundGoal(HappyGhast ghast) {
            this(ghast, 0);
        }

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
            if (floor < pos.getY() && floor > level.dimensionType().minY()) {
                target = new Vec3(target.x(), mob.getY() - Math.abs(mob.getY() - target.y()), target.z());
            }

            return target;
        }

        private static boolean isGoodTarget(Level level, Vec3 target, int distanceToBlocks) {
            if (distanceToBlocks <= 0) {
                return true;
            } else {
                BlockPos pos = BlockPos.containing(target);
                if (!level.getBlockState(pos).isAir()) {
                    return false;
                }

                for (Direction direction : Direction.values()) {
                    for (int distance = 1; distance < distanceToBlocks; distance++) {
                        BlockPos neighbor = pos.relative(direction, distance);
                        if (!level.getBlockState(neighbor).isAir()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        private static Vec3 chooseRandomPosition(Vec3 origin, RandomSource random) {
            double flyRadius = 16.0F;
            double x = origin.x() + (random.nextFloat() * 2.0F - 1.0F) * flyRadius;
            double y = origin.y() + (random.nextFloat() * 2.0F - 1.0F) * flyRadius;
            double z = origin.z() + (random.nextFloat() * 2.0F - 1.0F) * flyRadius;
            return new Vec3(x, y, z);
        }

        @Nullable
        private static Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 origin, RandomSource random) {
            Vec3 target = chooseRandomPosition(origin, random);
            return mob.hasRestriction() && !mob.isWithinRestriction(BlockPos.containing(target)) ? null : target;
        }
    }
}