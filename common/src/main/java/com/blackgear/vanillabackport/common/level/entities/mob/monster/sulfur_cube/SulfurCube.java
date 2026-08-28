package com.blackgear.vanillabackport.common.level.entities.mob.monster.sulfur_cube;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.movement.MotionAwareEntity;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.happy_ghast.TemptGoal;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.sulfur_cube.SulfurCubeArchetype.AttributeEntry;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModDamageTypeTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.mixin.common.access.LivingEntityAccessor;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import com.blackgear.vanillabackport.core.util.WorldUtilities.*;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.blackgear.vanillabackport.common.level.entities.mob.monster.sulfur_cube.SulfurCubeArchetype.*;

public class SulfurCube extends AbstractCubeMob implements Bucketable, Shearable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MAX_FUSE = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.INT);
    private static final Predicate<ItemEntity> ALLOWED_ITEMS = item -> !item.hasPickUpDelay() && item.isAlive() && isSwallowableItem(item.getItem());
    private int pickupTimer = 0;
    private int pushSoundCooldown = 0;
    private boolean floatsInLiquids = false;
    private Optional<ExplosionData> explosionData = Optional.empty();
    private KnockbackModifiers knockbackModifier = DEFAULT_KNOCKBACK_MODIFIERS;
    private SulfurCubeArchetype.SoundSettings soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;
    private int fuse = -1;
    private final List<ContactDamage> contactDamages = new ArrayList<>();

    @Nullable private DamageSource pendingKnockbackSource = null;
    private float pendingKnockbackDamage = 0f;

    private static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
        @Override
        public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
            return !state.is(Blocks.NETHER_PORTAL) && super.shouldBlockExplode(explosion, reader, pos, state, power);
        }

        @Override
        public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid) {
            return state.is(Blocks.NETHER_PORTAL) ? Optional.empty() : super.getBlockExplosionResistance(explosion, reader, pos, state, fluid);
        }
    };

    public SulfurCube(EntityType<? extends AbstractCubeMob> entityType, Level level) {
        super(entityType, level);
        this.lookControl = new SulfurCubeLookControl();
        this.moveControl = new SulfurCubeMoveControl<>(this);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
        builder.define(MAX_FUSE, -1);
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(
            2,
            new SulfurCubeTemptGoal(
                this,
                1.0,
                stack -> this.isBaby() ? stack.is(ModItemTags.SULFUR_CUBE_FOOD) : isSwallowableItem(stack),
                false,
                1.0
            ).setTemptRange(8)
        );
        this.goalSelector.addGoal(3, new SulfurCubeSearchForItemsGoal(this));
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    public int getFuse() {
        return this.fuse;
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }

    public boolean isPrimed() {
        return this.getFuse() >= 0;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (MAX_FUSE.equals(key)) {
            this.setFuse(this.entityData.get(MAX_FUSE));
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public SoundEvent getPickupSound() {
        return ModSoundEvents.BUCKET_FILL_SULFUR_CUBE.get();
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag -> {
            tag.putInt("age", this.getAge());
            ItemStack absorbedBlock = this.getContainedBlock();
            if (!absorbedBlock.isEmpty()) {
                tag.put("absorbed_block", absorbedBlock.save(this.level().registryAccess()));
            }

            if (this.isPersistenceRequired()) {
                tag.putBoolean("PersistenceRequired", this.isPersistenceRequired());
            }
        });
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.hasBodyItem() || super.canBreatheUnderwater();
    }

    @Override
    public Vec3 getLightProbePosition(float partialTicks) {
        Vec3 base = super.getLightProbePosition(partialTicks);
        return base.add(0, 0.5 * this.getSize(), 0);
    }

    @Override
    public void postTravelInFluid() {
        if (this.hasBodyItem() && this.floatsInLiquids) {
            float vibeAmount = 0.2F * Mth.sin(this.tickCount * 0.4F);
            double immersion = this.getFluidHeight(this.isInWater() ? FluidTags.WATER : FluidTags.LAVA) - this.getFluidJumpThreshold() + vibeAmount;
            if (immersion > 0.0) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, Math.min(1.0, immersion) * 0.04F, 0.0));
            }
        }
    }

    @Override
    public double getFluidJumpThreshold() {
        return this.getBbHeight() * 0.2;
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        this.setAge(tag.getInt("age"));
        if (tag.contains("absorbed_block", Tag.TAG_COMPOUND)) {
            ItemStack absorbedBlock = ItemStack.parse(this.level().registryAccess(), tag.getCompound("absorbed_block")).orElse(ItemStack.EMPTY);
            if (!absorbedBlock.isEmpty()) {
                this.equipItem(absorbedBlock);
            }
        }

        if (tag.contains("PersistenceRequired")) {
            this.setPersistenceRequired();
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SULFUR_CUBE_BUCKET.get());
    }

    @Override
    protected void addTargetingGoals() {
        // NO-OP
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    protected boolean isDealsDamage() {
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean checkSulfurCubeSpawnRules(EntityType<SulfurCube> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return true;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.hasBodyItem();
    }

    @Override
    public boolean canBeLeashed() {
        return this.hasBodyItem();
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        this.pendingKnockbackSource = null;
        this.pendingKnockbackDamage = 0F;
        this.pendingKnockbackSource = source;
        this.pendingKnockbackDamage = damage;

        if (!this.level().isClientSide() && this.hasBodyItem()) {
            if (this.canExplode() && !this.isPrimed()) {
                Entity attacker = source.getDirectEntity();
                if (source.is(DamageTypeTags.IS_FIRE) || attacker instanceof AbstractArrow projectile && projectile.isOnFire()) {
                    this.primeTime(false);
                } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                    this.primeTime(true);
                }
            }

            if (source.is(ModDamageTypeTags.SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO)) {
                if (!source.is(ModDamageTypeTags.NO_KNOCKBACK)) {
                    // deal default knockback
                    double xd = 0.0;
                    double zd = 0.0;
                    if (source.getDirectEntity() instanceof Projectile projectile) {
                        double dx = projectile.getDeltaMovement().x;
                        double dz = projectile.getDeltaMovement().z;
                        if (projectile instanceof FireworkRocketEntity || projectile instanceof ThrownPotion) {
                            dx = this.position().x - projectile.position().x;
                            dz = this.position().z - projectile.position().z;
                        }

                        DoubleDoubleImmutablePair knockbackDirection = DoubleDoubleImmutablePair.of(dx, dz);
                        xd = -knockbackDirection.leftDouble();
                        zd = -knockbackDirection.rightDouble();
                    } else if (source.getSourcePosition() != null) {
                        xd = source.getSourcePosition().x() - this.getX();
                        zd = source.getSourcePosition().z() - this.getZ();
                    }

                    this.knockback(0.4F, xd, zd);
                }

                return true;
            }
        }

        return super.hurt(source, damage);
    }

    public boolean hasBodyItem() {
        return !this.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    public boolean canExplode() {
        return this.explosionData.isPresent() && this.isAlive() && !this.isPrimed();
    }

    public ItemStack getContainedBlock() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    public List<SulfurCubeArchetype> matchingArchetypes(ItemStack stack) {
        return SulfurCubeArchetypes.REGISTRIES.values()
            .stream()
            .filter(archetype -> stack.is(archetype.items()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void tick() {
        this.tickFuse();
        this.primeWhenOnPoweredPosition();
        super.tick();
    }

    private void tickFuse() {
        if (this.fuse > 0) {
            this.fuse--;
        }

        if (this.explosionData.isPresent()) {
            if (this.fuse == 0) {
                this.dropLeash(true, true);
                this.dead = true;
                if (this.level() instanceof ServerLevel level) {
                    if (VanillaBackport.COMMON_CONFIG.doSulfurCubesExplode.get()) {
                        Level.ExplosionInteraction interaction = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
                            ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
                        level.explode(
                            this,
                            level.damageSources().explosion(this, this),
                            this.getPortalCooldown() > 0 ? USED_PORTAL_DAMAGE_CALCULATOR : null,
                            this.getX(),
                            this.getY(0.0625),
                            this.getZ(),
                            this.explosionData.get().power(),
                            this.explosionData.get().causesFire(),
                            interaction
                        );
                    }

                    this.triggerOnDeathMobEffects(RemovalReason.KILLED);
                }

                this.discard();
            }
        }
    }

    private void primeWhenOnPoweredPosition() {
        if (this.level() instanceof ServerLevel level && this.canExplode()) {
            BlockPos here = BlockPos.containing(this.position());
            if (BlockUtils.getBestOwnOrNeighbourSignal(level, here) != 0) {
                this.primeTime(false);
            }
        }
    }

    private void primeTime(boolean imminent) {
        if (this.explosionData.isPresent()
            && this.isAlive()
            && this.level() instanceof ServerLevel
            && VanillaBackport.COMMON_CONFIG.doSulfurCubesExplode.get()
            && !this.isPrimed()) {
            int fuse = this.explosionData.get().fuse();
            int fuseTime = imminent
                ? this.getRandom().nextInt(fuse / 4) + fuse / 8
                : fuse;
            this.setInvulnerable(true);
            this.setFuse(fuseTime);
            this.entityData.set(MAX_FUSE, fuseTime);
            this.playSound(SoundEvents.TNT_PRIMED);
            this.gameEvent(GameEvent.PRIME_FUSE);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.pickupTimer > 0) {
            this.pickupTimer--;
        }

        if (this.pushSoundCooldown > 0) {
            this.pushSoundCooldown--;
        }
    }

    @Override @Nullable
    public Map<EquipmentSlot, ItemStack> collectEquipmentChanges() {
        ItemStack previous = ((LivingEntityAccessor) this).getLastArmorItemStacks().get(EquipmentSlot.CHEST.getIndex());
        ItemStack current = this.getItemBySlot(EquipmentSlot.CHEST);
        if (this.equipmentHasChanged(previous, current)) {
            if (!current.isEmpty()) {
                this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
                this.removeAllGoals(goal -> true);
                this.setSpeed(0.0F);
            } else {
                this.registerGoals();
            }

            for (SulfurCubeArchetype archetype : this.matchingArchetypes(previous)) {
                for (AttributeEntry entry : archetype.attributeModifiers()) {
                    AttributeInstance attribute = this.getAttribute(entry.attribute());
                    if (attribute != null) {
                        attribute.removeModifier(entry.modifier());
                    }
                }
            }

            this.floatsInLiquids = false;
            this.explosionData = Optional.empty();
            this.contactDamages.clear();
            this.knockbackModifier = DEFAULT_KNOCKBACK_MODIFIERS;
            this.soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;

            for (SulfurCubeArchetype archetype : this.matchingArchetypes(current)) {
                if (archetype.buoyant()) {
                    this.floatsInLiquids = true;
                }

                if (archetype.explosion().isPresent()) {
                    this.explosionData = archetype.explosion();
                }

                if (VanillaBackport.COMMON_CONFIG.doSulfurCubesDealDamage.get() && archetype.contactDamage().isPresent()) {
                    this.contactDamages.add(archetype.contactDamage().get());
                }

                this.knockbackModifier = archetype.knockbackModifiers();
                this.soundSettings = archetype.soundSettings();

                for (AttributeEntry entry : archetype.attributeModifiers()) {
                    AttributeInstance attribute = this.getAttribute(entry.attribute());
                    if (attribute != null) {
                        attribute.addOrUpdateTransientModifier(entry.modifier());
                    }
                }
            }
        }

        return super.collectEquipmentChanges();
    }

    @Override
    public float maxUpStep() {
        return this.hasBodyItem() ? 0.0F : super.maxUpStep();
    }

    @Override
    public boolean omnidirectionalAirMover() {
        return this.hasBodyItem();
    }

    @Override
    public boolean canFreeze() {
        return !this.hasBodyItem() && super.canFreeze();
    }

    public float getFuseRemainingTicks(float partialTicks) {
        return this.isPrimed() ? this.getFuse() - partialTicks + 1.0F : 0.0F;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (this.isBaby()) {
            if (this.isFood(heldItem)) {
                if (!player.getAbilities().instabuild) heldItem.shrink(1);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-this.age), true);
                this.playSound(ModSoundEvents.SULFUR_CUBE_SMALL_EAT.get());
                return InteractionResult.SUCCESS;
            } else {
                return super.mobInteract(player, hand);
            }
        } else if (this.isPrimed()) {
            return InteractionResult.PASS;
        } else if (this.canExplode() && heldItem.is(ItemTags.CREEPER_IGNITERS)) {
            if (!VanillaBackport.COMMON_CONFIG.doSulfurCubesExplode.get()) {
                player.displayClientMessage(Component.translatable("entity.minecraft.sulfur_cube.explosion_disabled"), true);
                return InteractionResult.PASS;
            } else {
                this.primeTime(false);
                if (!heldItem.isDamageableItem()) {
                    heldItem.shrink(1);
                } else {
                    heldItem.hurtAndBreak(1, player, getSlotForHand(hand));
                }

                player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
                return InteractionResult.SUCCESS;
            }
        } else {
            if (heldItem.is(Items.SHEARS) && this.readyForShearing()) {
                if (this.level() instanceof ServerLevel) {
                    ItemStack itemStackToShear = this.getItemBySlot(EquipmentSlot.CHEST);
                    this.shear(SoundSource.PLAYERS);
                    this.gameEvent(GameEvent.SHEAR, player);
                    heldItem.hurtAndBreak(1, player, getSlotForHand(hand));
//                    CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger((ServerPlayer) player, itemStackToShear, this); TODO
                }

                return InteractionResult.SUCCESS;
            } else if (isSwallowableItem(heldItem)) {
                boolean itWorked = this.equipItem(heldItem);
                if (itWorked) {
                    heldItem.consume(1, player);
                    this.gameEvent(GameEvent.ENTITY_INTERACT);
                }

                return itWorked ? InteractionResult.SUCCESS : InteractionResult.PASS;
            } else {
                return bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
            }
        }
    }

    private boolean equipItem(ItemStack heldItem) {
        if (this.isBaby()) {
            return false;
        } else {
            if (this.hasBodyItem()) {
                Item swallowedItem = this.getItemBySlot(EquipmentSlot.CHEST).getItem();
                if (heldItem.is(swallowedItem)) {
                    return false;
                }

                this.spawnAtLocation(this.getItemBySlot(EquipmentSlot.CHEST), this.getBbHeight() * 1.5F);
            }

            this.setItemSlotAndDropWhenKilled(EquipmentSlot.CHEST, heldItem.copyWithCount(1));
            this.playSound(this.getAbsorbSound());
            return true;
        }
    }

    private void applyContactDamage(Entity entity) {
        if (this.level() instanceof ServerLevel level) {
            for (ContactDamage damage : this.contactDamages) {
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolder(damage.damageType())
                    .ifPresent(source -> entity.hurt(new DamageSource(source, damage.attributeToSource() ? this : null), damage.amount().sample(this.getRandom())));
            }
        }
    }

    private static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, T pickupEntity) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == Items.BUCKET && pickupEntity.isAlive()) {
            if (pickupEntity instanceof SulfurCube cube) cube.setPersistenceRequired(); // Prevents bucketed Sulfur Cubes to despawn
            pickupEntity.playSound(pickupEntity.getPickupSound(), 1.0F, 1.0F);
            ItemStack bucket = pickupEntity.getBucketItemStack();
            pickupEntity.saveToBucketTag(bucket);
            ItemStack result = ItemUtils.createFilledResult(itemStack, player, bucket, false);
            player.setItemInHand(hand, result);
            Level level = pickupEntity.level();
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, bucket);
            }

            if (pickupEntity instanceof Leashable leashable) {
                leashable.dropLeash(true, true);
            }

            pickupEntity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.setPersistenceRequired(); // Prevents named Sulfur Cubes to despawn
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.isTiny() ? ModSoundEvents.SULFUR_CUBE_SMALL_HURT.get() : ModSoundEvents.SULFUR_CUBE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTiny() ? ModSoundEvents.SULFUR_CUBE_SMALL_DEATH.get() : ModSoundEvents.SULFUR_CUBE_DEATH.get();
    }

    @Override
    protected SoundEvent getSquishSound() {
        if (this.isTiny()) {
            return ModSoundEvents.SULFUR_CUBE_SMALL_SQUISH.get();
        } else {
            return this.hasBodyItem() ? ModSoundEvents.SULFUR_CUBE_BOUNCE.get() : ModSoundEvents.SULFUR_CUBE_SQUISH.get();
        }
    }

    @Override
    protected SoundEvent getJumpSound() {
        return this.isTiny() ? ModSoundEvents.SULFUR_CUBE_SMALL_JUMP.get() : ModSoundEvents.SULFUR_CUBE_JUMP.get();
    }

    private SoundEvent getAbsorbSound() {
        return ModSoundEvents.SULFUR_CUBE_ABSORB.get();
    }

    private SoundEvent getEjectSound() {
        return ModSoundEvents.SULFUR_CUBE_EJECT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.hasBodyItem()) super.playStepSound(pos, state);
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ModParticles.SULFUR_CUBE_GOO.get();
    }

    public static AttributeSupplier.Builder createSulfurCubeAttributes() {
        return Mob.createMobAttributes();
    }

    @Override
    public void shear(SoundSource source) {
        ItemStack itemStackToShear = this.getItemBySlot(EquipmentSlot.CHEST);
        this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
        this.spawnAtLocation(itemStackToShear, this.getBbHeight() * 1.5F);
        this.playSound(this.getEjectSound());
        this.pickupTimer = 100;
    }

    @Override
    public boolean readyForShearing() {
        return this.hasBodyItem();
    }

    @Override
    public boolean canPickUpLoot() {
        return !this.hasBodyItem();
    }

    private static boolean isSwallowableItem(ItemStack stack) {
        return stack.is(ModItemTags.SULFUR_CUBE_SWALLOWABLE);
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        ItemStack heldItem = this.getItemBySlot(EquipmentSlot.CHEST);
        return heldItem.isEmpty() && isSwallowableItem(stack) && !this.isBaby();
    }

    @Override
    protected void pickUpItem(ItemEntity item) {
        ItemStack itemStack = item.getItem();
        if (this.canHoldItem(itemStack) && this.pickupTimer <= 0) {
            this.onItemPickup(item);
            this.setItemSlot(EquipmentSlot.CHEST, itemStack.split(1));
            this.playSound(this.getAbsorbSound());
            this.setGuaranteedDrop(EquipmentSlot.CHEST);
            this.take(item, 1);
        }
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.isBaby() ? 0 : 1 + this.random.nextInt(2);
    }

    @Override
    protected int getSplitCount() {
        return this.isPrimed() ? 0 : 2;
    }

    @Override
    protected void setSpawnSize(ServerLevelAccessor level, DifficultyInstance difficulty) {
        this.setSize(this.isBaby() ? 1 : 2, true);
    }

    @Override
    public void setSize(int size, boolean updateHealth) {
        super.setSize(size, updateHealth);
        if (updateHealth && size == 1 && !this.isBaby()) {
            this.setBaby(true);
        }
    }

    @Override
    protected void setUpSplitCube(AbstractCubeMob cube, int halfSize, float x, float z) {
        super.setUpSplitCube(cube, halfSize, x, z);
        cube.setBaby(true);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        SulfurCube cube = ModEntityTypes.SULFUR_CUBE.get().create(level);
        if (cube != null) cube.setSize(1, true);
        return cube;
    }

    private boolean isFood(ItemStack heldItem) {
        return heldItem.is(ModItemTags.SULFUR_CUBE_FOOD);
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby()) {
            this.setSize(2, true);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("pickup_timer", this.pickupTimer);
        compound.putBoolean("from_bucket", this.fromBucket());
        compound.putInt("fuse", this.getFuse());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.pickupTimer = compound.getInt("pickup_timer");
        this.setFromBucket(compound.getBoolean("from_bucket"));
        this.setFuse(compound.contains("fuse") ? compound.getInt("fuse") : -1);
        this.entityData.set(MAX_FUSE, this.getFuse());
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void doPush(Entity entity) {
        super.doPush(entity);
        this.applyContactDamage(entity);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        this.playerPush(player);
    }

    private void playerPush(Player player) {
        if (this.hasBodyItem()) {
            Entity pusher = player.isPassenger() ? player.getRootVehicle() : player;
            Vec3 cubeToPusher = this.position().subtract(pusher.position());
            double pusherFeetPosition = pusher.getY();
            double sulfurCubeBottomPosition = this.getY();
            double sulfurCubeTopPosition = sulfurCubeBottomPosition + this.getBbHeight();
            double pusherTopPosition = pusherFeetPosition + pusher.getBbHeight();
            if (cubeToPusher.horizontalDistance() < 1.3F && pusherFeetPosition <= sulfurCubeTopPosition && pusherTopPosition > sulfurCubeBottomPosition) {
                double knockback = Math.max(0.0, 1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                Vec3 pushDirection = VectorUtils.horizontal(cubeToPusher).normalize().scale(knockback);
                float pushSpeedScale = player.isPassenger() ? 0.16F : 0.3F;
                double playerSpeed = ((MotionAwareEntity) player).getKnownSpeed().length() * 2.0 * pushSpeedScale;
                playerSpeed = Mth.clamp(playerSpeed, 0.0, 0.5);
                Vec3 pushVelocity = new Vec3(pushDirection.x, this.onGround() ? knockback * 0.3F : 0.0, pushDirection.z).scale(playerSpeed);
                this.hasImpulse = true;
                float pushSoundThreshold = this.soundSettings.pushSoundImpulseThreshold();
                if (pushVelocity.lengthSqr() > pushSoundThreshold * pushSoundThreshold && this.pushSoundCooldown <= 0) {
                    this.pushSoundCooldown = (int)(this.soundSettings.pushSoundCooldown() * 20.0F);
                    this.playSound(this.soundSettings.pushSound().value());
                }

                this.addDeltaMovement(pushVelocity);
                this.applyContactDamage(player);
            }
        }
    }

    private Vec2 applyHorizontalHitAngleScale(float horizontalAngleScale, Vec2 originalAngle, Vec3 attackerPosition, Vec3 attackerAimDirection, Vec3 targetCenter) {
        Vec3 attackerToTarget = targetCenter.subtract(attackerPosition).normalize();
        float angleDiff = (float) Math.atan2(
            attackerAimDirection.x * attackerToTarget.z - attackerAimDirection.z * attackerToTarget.x,
            attackerAimDirection.x * attackerToTarget.x + attackerAimDirection.z * attackerToTarget.z
        );
        return VectorUtils.rotate(originalAngle, angleDiff * horizontalAngleScale);
    }

    private Vec2 applyVerticalHitAnglePowerTransfer(
        float verticalHitAngleScale,
        float horizontalPower,
        float verticalPower,
        Vec3 attackerPosition,
        Vec3 attackerAimDirection,
        Vec3 targetCenteredPosition,
        float targetHeight
    ) {
        float targetHalfHeight = 0.5F * targetHeight;
        Vec3 targetTopPos = targetCenteredPosition.add(0.0, targetHalfHeight, 0.0);
        Vec3 targetBottomPos = targetCenteredPosition.add(0.0, -targetHalfHeight, 0.0);
        Vec3 attackerToTargetTop = targetTopPos.subtract(attackerPosition).normalize();
        Vec3 attackerToTargetBottom = targetBottomPos.subtract(attackerPosition).normalize();
        float verticalHitAngleFactor = (float) Mth.clampedMap(attackerAimDirection.y, attackerToTargetTop.y, attackerToTargetBottom.y, -1.0, 1.0);
        float transferredPowerRatio = Math.abs(verticalHitAngleFactor * verticalHitAngleScale);
        if (verticalHitAngleFactor < 0.0F) {
            transferredPowerRatio = -transferredPowerRatio;
        }

        float px = horizontalPower * (1.0F - transferredPowerRatio);
        float py = verticalPower * (1.0F + transferredPowerRatio);
        return new Vec2(px, py);
    }

    private Vec2 applyVerticalPositionAnglePowerRotation(
        float verticalPositionAngleScale,
        float horizontalPower,
        float verticalPower,
        float originalHorizontalPower,
        float originalVerticalPower,
        Vec3 attackerFeetPosition,
        Vec3 targetFeetPosition
    ) {
        Vec3 attackerFeetToTargetFeet = targetFeetPosition.subtract(attackerFeetPosition);
        float verticalPositionAngle = (float) Math.atan2(-attackerFeetToTargetFeet.y, attackerFeetToTargetFeet.horizontalDistance());
        Vec2 powerBeforeRotation = new Vec2(horizontalPower, verticalPower);
        Vec2 rotatedPower = VectorUtils.rotate(powerBeforeRotation, -verticalPositionAngle * verticalPositionAngleScale);
        float horizontalRatio = originalHorizontalPower > 0.0F ? Mth.abs(rotatedPower.x) / originalHorizontalPower : 0.0F;
        float verticalRatio = originalVerticalPower > 0.0F ? Mth.abs(rotatedPower.y) / originalVerticalPower : 0.0F;
        float maxRatio = Math.max(horizontalRatio, verticalRatio);
        if (maxRatio > 1.0F) {
            rotatedPower = rotatedPower.scale(1.0F / maxRatio);
        }

        return rotatedPower;
    }

    @Override
    public void knockback(double power, double xd, double zd) {
        Entity attacker = this.pendingKnockbackSource != null ? this.pendingKnockbackSource.getEntity() : null;
        float damage = this.pendingKnockbackDamage;

        if (attacker != null && this.hasBodyItem()) {
            float horizontalPower = this.knockbackModifier.horizontalPower();
            float verticalPower = this.knockbackModifier.verticalPower();
            Vec2 originalAngle = new Vec2((float) xd, (float) zd);
            Vec2 newAngle = this.applyHorizontalHitAngleScale(
                1.6F,
                originalAngle,
                attacker.getEyePosition(),
                attacker.getLookAngle().normalize(),
                this.getBoundingBox().getCenter()
            );
            Vec2 newPower = this.applyVerticalHitAnglePowerTransfer(
                0.5F,
                horizontalPower,
                verticalPower,
                attacker.getEyePosition(),
                attacker.getLookAngle().normalize(),
                this.getBoundingBox().getCenter(),
                this.getBbHeight()
            );
            newPower = this.applyVerticalPositionAnglePowerRotation(
                0.8F,
                newPower.x,
                newPower.y,
                horizontalPower,
                verticalPower,
                attacker.position(),
                this.position()
            );
            horizontalPower = newPower.x;
            verticalPower = newPower.y;
            xd = newAngle.x;
            zd = newAngle.y;
            float powerMultiplier = Mth.sqrt(damage);
            horizontalPower *= powerMultiplier;
            verticalPower *= powerMultiplier;
            float knockbackResistance = (float) (1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            horizontalPower *= knockbackResistance;
            verticalPower *= knockbackResistance;
            this.hasImpulse = true;
            Vec3 deltaMovement = this.getDeltaMovement();
            horizontalPower *= 0.4F;
            horizontalPower = Mth.clamp(horizontalPower, -128.0F, 128.0F);
            verticalPower = Mth.clamp(verticalPower, -128.0F, 128.0F);
            Vec3 horizontalKnockback = new Vec3(xd, 0.0, zd).normalize().scale(horizontalPower);
            this.setDeltaMovement(deltaMovement.x - horizontalKnockback.x, deltaMovement.y + verticalPower * 1.2, deltaMovement.z - horizontalKnockback.z);
            this.playSound(this.soundSettings.hitSound().value());
        } else {
            super.knockback(power, xd, zd);
        }
    }

    @Override
    protected Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getBbHeight() / 2.0F, 0.0);
    }

    @Override @SuppressWarnings("DataFlowIssue")
    protected void setCubeMobHealth(int actualSize) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4 * actualSize);
    }

    private class SulfurCubeLookControl extends LookControl {
        public SulfurCubeLookControl() {
            super(SulfurCube.this);
        }

        @Override
        public void tick() {
            SulfurCube cube = SulfurCube.this;
            if (!cube.hasBodyItem()) {
                super.tick();
            } else {
                float closeAngle = MthUtils.wrapDegrees90(cube.getYRot());
                cube.setYRot(cube.getYRot() - closeAngle);
                cube.setYHeadRot(cube.getYRot());
            }
        }
    }

    private static class SulfurCubeMoveControl<T extends SulfurCube> extends CubeMobMoveControl<T> {
        public SulfurCubeMoveControl(T cube) {
            super(cube);
        }

        @Override
        public void tick() {
            if (!this.cube.hasBodyItem()) {
                super.tick();
            }
        }
    }

    private static class SulfurCubeSearchForItemsGoal extends Goal {
        private final SulfurCube cube;
        @Nullable private ItemEntity targetItem;

        private SulfurCubeSearchForItemsGoal(SulfurCube cube) {
            this.setFlags(EnumSet.of(Flag.LOOK));
            this.cube = cube;
        }

        @Override
        public boolean canUse() {
            if (!this.cube.isBaby() && this.cube.pickupTimer <= 0) {
                this.targetItem = EntityUtils.getNearestEntity(
                    this.cube.level().getEntitiesOfClass(ItemEntity.class, this.cube.getBoundingBox().inflate(8.0), SulfurCube.ALLOWED_ITEMS),
                    this.cube.getX(),
                    this.cube.getY(),
                    this.cube.getZ()
                );
                return this.targetItem != null;
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            this.cube.lookAt(this.targetItem, 10.0F, 10.0F);
            if (this.cube.getMoveControl() instanceof CubeMobMoveControl<?> control) {
                control.setDirection(this.cube.getYRot(), true);
            }
        }
    }

    private static class SulfurCubeTemptGoal extends TemptGoal.ForNonPathfinders {
        public SulfurCubeTemptGoal(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
            super(mob, speedModifier, items, canScare, stopDistance);
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        protected void stopNavigation() {
            if (this.mob.getMoveControl() instanceof CubeMobMoveControl<?> control) {
                control.setWantedMovement(0.0);
            }
        }

        @Override
        protected void navigateTowards(Player player) {
            this.mob.lookAt(player, 10.0F, 10.0F);
            if (this.mob.getMoveControl() instanceof CubeMobMoveControl<?> control) {
                control.setDirection(this.mob.getYRot(), true);
            }
        }
    }
}