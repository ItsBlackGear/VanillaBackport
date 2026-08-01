package com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus;

import java.util.UUID;
import java.util.function.Predicate;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.mounts.ControllableMob;
import com.blackgear.vanillabackport.common.api.extensions.entity.mounts.MountInventoryHandler;
import com.blackgear.vanillabackport.common.level.item.NautilusArmorItem;
import com.blackgear.vanillabackport.common.registries.entities.ModMobEffects;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNautilus extends TamableAnimal implements ContainerListener, HasCustomInventoryScreen, OwnableEntity, PlayerRideableJumping, Saddleable, ControllableMob {
	private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
	private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
	private int dashCooldown;
	protected float playerJumpPendingScale;
	protected SimpleContainer inventory;

	protected boolean isJumping;
	@Nullable private UUID owner;

	protected AbstractNautilus(EntityType<? extends AbstractNautilus> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.011F, 0.0F, true);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		this.createInventory();
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getOwnerUUID() != null) {
			compound.putUUID("Owner", this.getOwnerUUID());
		}
		
		if (!this.inventory.getItem(0).isEmpty()) {
			compound.put("SaddleItem", this.inventory.getItem(0).save(new CompoundTag()));
		}
		
		if (!this.inventory.getItem(1).isEmpty()) {
			compound.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		UUID uUID;
		if (compound.hasUUID("Owner")) {
			uUID = compound.getUUID("Owner");
		} else {
			String string = compound.getString("Owner");
			uUID = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string);
		}
		
		if (uUID != null) {
			this.setOwnerUUID(uUID);
		}
		
		if (compound.contains("SaddleItem", 10)) {
			ItemStack itemStack = ItemStack.of(compound.getCompound("SaddleItem"));
			if (itemStack.is(Items.SADDLE)) {
				this.inventory.setItem(0, itemStack);
			}
		}
		
		if (compound.contains("ArmorItem", 10)) {
			ItemStack itemstack = ItemStack.of(compound.getCompound("ArmorItem"));
			if (!itemstack.isEmpty() && this.isArmor(itemstack)) {
				this.inventory.setItem(1, itemstack);
			}
		}
		
		this.updateContainerEquipment();
	}
	
	@Override
	public boolean isFood(ItemStack stack) {
		return !this.isTame() && !this.isBaby() ? stack.is(ModItemTags.NAUTILUS_TAMING_ITEMS) : stack.is(ModItemTags.NAUTILUS_FOOD);
	}
	
	@Override
	protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
		if (stack.is(ModItemTags.NAUTILUS_BUCKET_FOOD)) {
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
		} else {
			super.usePlayerItem(player, hand, stack);
		}
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Animal.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 15.0)
			.add(Attributes.MOVEMENT_SPEED, 1.0)
			.add(Attributes.ATTACK_DAMAGE, 3.0)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.3);
	}
	
	@Override
	public int getArmorValue() {
		return super.getArmorValue();
	}
	
	@Override
	public boolean isPushedByFluid() {
		return false;
	}
	
	@Override
	protected PathNavigation createNavigation(Level level) {
		return new WaterBoundPathNavigation(this, level);
	}
	
	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level) {
		return 0.0F;
	}
	
	public static boolean checkNautilusSpawnRules(EntityType<? extends AbstractNautilus> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		int seaLevel = level.getSeaLevel();
		int minSpawnLevel = seaLevel - 25;
		return pos.getY() >= minSpawnLevel
			&& pos.getY() <= seaLevel - 5
			&& level.getFluidState(pos.below()).is(FluidTags.WATER)
			&& level.getBlockState(pos.above()).is(Blocks.WATER);
	}
	
	@Override
	public boolean checkSpawnObstruction(LevelReader level) {
		return level.isUnobstructed(this);
	}
	
	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return !this.isVehicle();
	}
	
	@Override
	public @Nullable LivingEntity getControllingPassenger() {
		return this.isSaddled() && this.getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger();
	}
	
	@Override
	protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
		float strafe = controller.xxa;
		float forward = 0.0F;
		float up = 0.0F;
		
		if (controller.zza != 0.0F) {
			float forwardLook = Mth.cos(controller.getXRot() * Mth.DEG_TO_RAD);
			float upLook = -Mth.sin(controller.getXRot() * Mth.DEG_TO_RAD);
			if (controller.zza < 0.0F) {
				forwardLook *= -0.5F;
				upLook *= -0.5F;
			}
			
			up = upLook;
			forward = forwardLook;
		}
		
		return new Vec3(strafe, up, forward);
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
		float turnSpeed = 0.5F;
		yRot += diff * turnSpeed;
		this.setRot(yRot, rotation.x);
		this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
		if (this.isControlledByLocalInstance()) {
			if (this.playerJumpPendingScale > 0.0F && !this.isJumping) {
				this.executeRidersJump(this.playerJumpPendingScale, controller);
			}
			
			this.playerJumpPendingScale = 0.0F;
		}
	}
	
	@Override
	public void travel(Vec3 input) {
		if (this.isAlive() && this.isInWater()) {
			float speed = this.getSpeed();
			this.moveRelative(speed, input);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
		} else {
			super.travel(input);
		}
	}
	
	@Override
	protected float getRiddenSpeed(Player controller) {
		return this.isInWater()
			? 0.0325F * (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED)
			: 0.02F * (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
	}
	
	protected void doPlayerRide(Player player) {
		if (!this.level().isClientSide()) {
			player.startRiding(this);
			if (!this.isVehicle()) {
				this.clearRestriction();
			}
		}
	}
	
	private int getNautilusRestrictionRadius() {
		return !this.isBaby() && !this.isSaddled() ? 32 : 16;
	}
	
	protected void checkRestriction() {
		if (!this.isLeashed() && !this.isVehicle() && this.isTame()) {
			int radius = this.getNautilusRestrictionRadius();
			if (!this.hasRestriction() || !this.getRestrictCenter().closerThan(this.blockPosition(), radius + 8) || radius != this.getRestrictRadius()) {
				this.restrictTo(this.blockPosition(), radius);
			}
		}
	}
	
	@Override
	protected void customServerAiStep() {
		this.checkRestriction();
		super.customServerAiStep();
	}
	
	private void applyEffects(Level level) {
		if (this.getFirstPassenger() instanceof Player player) {
			boolean hasEffect = player.hasEffect(ModMobEffects.BREATH_OF_THE_NAUTILUS);
			boolean shouldRefresh = level.getGameTime() % 40L == 0L;
			if (!hasEffect || shouldRefresh) {
				player.addEffect(new MobEffectInstance(ModMobEffects.BREATH_OF_THE_NAUTILUS, 60, 0, true, true, true));
			}
		}
	}
	
	private void spawnBubbles() {
		double speed = this.getDeltaMovement().length();
		double bubbleProbability = Mth.clamp(speed * 2.0, 0.15F, 1.0);
		if (this.random.nextFloat() < bubbleProbability) {
			float yRot = this.getYRot();
			float xRot = Mth.clamp(this.getXRot(), -10.0F, 10.0F);
			Vec3 mouthDirectionVector = this.calculateViewVector(xRot, yRot);
			double spread = this.random.nextDouble() * 0.8 * (1.0 + speed);
			double dx = (this.random.nextFloat() - 0.5) * spread;
			double dy = (this.random.nextFloat() - 0.5) * spread;
			double dz = (this.random.nextFloat() - 0.5) * spread;
			this.level().addParticle(
				ParticleTypes.BUBBLE,
				this.getX() - mouthDirectionVector.x * 1.1,
				this.getY() - mouthDirectionVector.y + 0.25,
				this.getZ() - mouthDirectionVector.z * 1.1,
				dx, dy, dz
			);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			this.applyEffects(this.level());
		}
		
		if (this.isDashing() && this.dashCooldown < 35) {
			this.setDashing(false);
		}
		
		if (this.dashCooldown > 0) {
			this.dashCooldown--;
			if (this.dashCooldown == 0) {
				this.playSound(this.getDashReadySound());
			}
		}
		
		if (this.isInWater()) {
			this.spawnBubbles();
		}
	}
	
	@Override
	public boolean canJump() {
		return this.isSaddled();
	}
	
	@Override
	public void onPlayerJump(int jumpAmount) {
		if (this.isSaddled() && this.dashCooldown <= 0) {
			this.playerJumpPendingScale = jumpAmount >= 90 ? 1.0F : 0.4F + 0.4F * jumpAmount / 90.0F;
		}
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DASH, false);
		this.entityData.define(SADDLED, false);
	}
	
	public boolean isDashing() {
		return this.entityData.get(DASH);
	}
	
	public void setDashing(boolean isDashing) {
		this.entityData.set(DASH, isDashing);
	}
	
	protected void executeRidersJump(float amount, Player controller) {
		this.addDeltaMovement(controller.getLookAngle().scale((this.isInWater() ? 1.2F : 0.5F) * amount * this.getAttributeValue(Attributes.MOVEMENT_SPEED) * this.getBlockSpeedFactor()));
		this.dashCooldown = 40;
		this.setDashing(true);
		this.hasImpulse = true;
	}
	
	@Override
	public void handleStartJump(int jumpScale) {
		this.playSound(this.getDashSound());
		this.gameEvent(GameEvent.ENTITY_INTERACT);
		this.setDashing(true);
	}
	
	@Override
	public int getJumpCooldown() {
		return this.dashCooldown;
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (!this.firstTick && DASH.equals(key)) {
			this.dashCooldown = this.dashCooldown == 0 ? 40 : this.dashCooldown;
		}
		
		super.onSyncedDataUpdated(key);
	}
	
	@Override
	public void handleStopJump() {
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
	}
	
	protected @Nullable SoundEvent getDashSound() {
		return null;
	}
	
	protected @Nullable SoundEvent getDashReadySound() {
		return null;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		this.setPersistenceRequired();
		
		if (this.isBaby()) {
			return super.mobInteract(player, hand);
		} else if (this.isTame() && player.isSecondaryUseActive()) {
			this.openCustomInventoryScreen(player);
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		} else {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.isEmpty()) {
				if (!this.level().isClientSide() && !this.isTame() && this.isFood(stack)) {
					this.usePlayerItem(player, hand, stack);
					this.tryToTame(player);
					return InteractionResult.SUCCESS;
				}

				if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
					FoodProperties properties = stack.getItem().getFoodProperties();
					this.usePlayerItem(player, hand, stack);
					this.heal(properties != null ? 2.0F * properties.getNutrition() : 1.0F);
					this.playEatingSound();
					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}
				
				InteractionResult interactionResult = stack.interactLivingEntity(player, this, hand);
				if (interactionResult.consumesAction()) {
					return interactionResult;
				}
				
				if (this.canWearArmor() && this.isArmor(stack) && !this.isWearingArmor()) {
					this.equipArmor(player, stack);
					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}
			}
			
			if (this.isTame() && !player.isSecondaryUseActive() && !this.isFood(stack)) {
				this.doPlayerRide(player);
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			} else {
				return super.mobInteract(player, hand);
			}
		}
	}
	
	protected void playEatingSound() {
	}
    
    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0) {
			this.tame(player);
			this.navigation.stop();
			this.level().broadcastEntityEvent(this, (byte) 7);
		} else {
			this.level().broadcastEntityEvent(this, (byte) 6);
		}
		
		this.playEatingSound();
    }
	
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return true;
	}
	
	@Override
	public boolean hurt(DamageSource source, float damage) {
		boolean wasHurt = super.hurt(source, damage);
		if (wasHurt && source.getEntity() instanceof LivingEntity attacker) {
			NautilusAi.setAngerTarget(this, attacker);
		}
		
		return wasHurt;
	}
	
	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
	}
	
	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		RandomSource random = level.getRandom();
		NautilusAi.initMemories(this, random);
		return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
	}
	
	@Override
	public SoundEvent getSaddleSoundEvent() {
		return this.isUnderWater() ? ModSoundEvents.NAUTILUS_SADDLE_UNDERWATER_EQUIP.get() : ModSoundEvents.NAUTILUS_SADDLE_EQUIP.get();
	}
	
	protected int getInventorySize() {
		return 2;
	}
	
	protected void createInventory() {
		SimpleContainer old = this.inventory;
		this.inventory = new SimpleContainer(this.getInventorySize());
		if (old != null) {
			old.removeListener(this);
			int max = Math.min(old.getContainerSize(), this.inventory.getContainerSize());
			
			for (int slot = 0; slot < max; slot++) {
				ItemStack stack = old.getItem(slot);
				if (!stack.isEmpty()) {
					this.inventory.setItem(slot, stack.copy());
				}
			}
		}
		
		this.inventory.addListener(this);
		this.updateContainerEquipment();
	}
	
	@Override
	public void openCustomInventoryScreen(Player player) {
		if (!this.level().isClientSide() && (!this.isVehicle() || this.hasPassenger(player)) && this.isTame()) {
			MountInventoryHandler.of(player).openNautilusInventory(this, this.inventory);
		}
	}
	
	@Override
	public SlotAccess getSlot(int slot) {
		int inventorySlot = slot - 400;
		if (inventorySlot >= 0 && inventorySlot < 2 && inventorySlot < this.inventory.getContainerSize()) {
			if (inventorySlot == 0) {
				return this.createEquipmentSlotAccess(inventorySlot, stack -> stack.isEmpty() || stack.is(Items.SADDLE));
			}
			
			if (inventorySlot == 1) {
				if (!this.canWearArmor()) {
					return SlotAccess.NULL;
				}
				
				return this.createEquipmentSlotAccess(inventorySlot, stack -> stack.isEmpty() || this.isArmor(stack));
			}
		}
		
		int j = slot - 500 + 2;
		return j >= 2 && j < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, j) : super.getSlot(slot);
	}
	
	public boolean hasInventoryChanged(Container inventory) {
		return this.inventory != inventory;
	}
	
	@Override
	public boolean isMobControlled() {
		return this.getFirstPassenger() instanceof Mob;
	}
	
	protected boolean isAggravated() {
		return this.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT) || this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
	}
	
	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.isTame();
	}
	
	@Override
	public @Nullable UUID getOwnerUUID() {
		return this.owner;
	}
	
	public void setOwnerUUID(@Nullable UUID uuid) {
		this.owner = uuid;
	}
	
	@Override
	public boolean isSaddleable() {
		return this.isAlive() && !this.isBaby() && this.isTame();
	}
	
	@Override
	public void equipSaddle(@Nullable SoundSource source) {
		this.inventory.setItem(0, new ItemStack(Items.SADDLE));
	}
	
	public void equipArmor(Player player, ItemStack armor) {
		if (this.isArmor(armor)) {
			this.inventory.setItem(1, armor.copyWithCount(1));
			if (!player.getAbilities().instabuild) {
				armor.shrink(1);
			}
		}
	}
	
	@Override
	public boolean isSaddled() {
		return this.entityData.get(SADDLED);
	}
	
	protected void updateContainerEquipment() {
		if (!this.level().isClientSide) {
			this.entityData.set(SADDLED, !this.inventory.getItem(0).isEmpty());
			this.setArmorEquipment(this.inventory.getItem(1));
			this.setDropChance(EquipmentSlot.CHEST, 0.0F);
		}
	}
	
	private void setArmorEquipment(ItemStack stack) {
		this.setArmor(stack);
		if (!this.level().isClientSide()) {
			this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
			if (this.isArmor(stack)) {
				int protection = ((NautilusArmorItem) stack.getItem()).getProtection();
				if (protection != 0) {
					this.getAttribute(Attributes.ARMOR)
						.addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Nautilus armor bonus", protection, AttributeModifier.Operation.ADDITION));
				}
			}
		}
	}
	
	@Override
	public void containerChanged(Container container) {
		ItemStack oldArmor = this.getArmor();
		boolean isSaddled = this.isSaddled();
		this.updateContainerEquipment();
		ItemStack newArmor = this.getArmor();
		if (this.tickCount > 20) {
			if (!isSaddled && this.isSaddled()) {
				this.playSound(this.getSaddleSoundEvent(), 0.5F, 1.0F);
			}
			
			if (this.isArmor(newArmor) && oldArmor != newArmor) {
				this.playSound(ModSoundEvents.ARMOR_EQUIP_NAUTILUS.get(), 0.5F, 1.0F);
			}
		}
	}
	
	public ItemStack getArmor() {
		return this.getItemBySlot(EquipmentSlot.CHEST);
	}
	
	private void setArmor(ItemStack stack) {
		this.setItemSlot(EquipmentSlot.CHEST, stack);
		this.setDropChance(EquipmentSlot.CHEST, 0.0F);
	}
	
	@Override
	protected void dropEquipment() {
		super.dropEquipment();
		if (this.inventory != null) {
			for (int size = 0; size < this.inventory.getContainerSize(); size++) {
				ItemStack stack = this.inventory.getItem(size);
				if (!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack)) {
					this.spawnAtLocation(stack);
				}
			}
		}
	}

	public boolean canWearArmor() {
		return true;
	}

	public boolean isWearingArmor() {
		return !this.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
	}

	public boolean isArmor(ItemStack stack) {
		return stack.getItem() instanceof NautilusArmorItem;
	}

	private SlotAccess createEquipmentSlotAccess(int slot, Predicate<ItemStack> stackFilter) {
		return new SlotAccess() {
			@Override
			public ItemStack get() {
				return AbstractNautilus.this.inventory.getItem(slot);
			}

			@Override
			public boolean set(ItemStack carried) {
				if (!stackFilter.test(carried)) {
					return false;
				} else {
					AbstractNautilus.this.inventory.setItem(slot, carried);
					AbstractNautilus.this.updateContainerEquipment();
					return true;
				}
			}
		};
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public MobType getMobType() {
		return MobType.WATER;
	}
	
	@Override
	public double getPassengersRidingOffset() {
		return this.dimensions.height * 0.9;
	}
}