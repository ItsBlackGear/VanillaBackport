package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.blocks.CopperGolemStatueBlock;
import com.blackgear.vanillabackport.common.level.block_entities.CopperGolemStatueBlockEntity;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityDataSerializers;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.util.WorldUtilities.EntityUtils;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CopperGolem extends AbstractGolem implements ContainerUser, Shearable {
    private static final EntityDataAccessor<WeatheredState> DATA_WEATHER_STATE = SynchedEntityData.defineId(CopperGolem.class, ModEntityDataSerializers.WEATHERING_COPPER_STATE.get());
    private static final EntityDataAccessor<CopperGolemState> COPPER_GOLEM_STATE = SynchedEntityData.defineId(CopperGolem.class, ModEntityDataSerializers.COPPER_GOLEM_STATE.get());
    @Nullable private BlockPos openedChestPos;
    @Nullable private UUID lastLightningBoltUUID;
    private long nextWeatheringTick = -1L;
    private int idleAnimationStartTick = 0;
    private final AnimationState idleAnimationState = new AnimationState();
    private final AnimationState interactionGetItemAnimationState = new AnimationState();
    private final AnimationState interactionGetNoItemAnimationState = new AnimationState();
    private final AnimationState interactionDropItemAnimationState = new AnimationState();
    private final AnimationState interactionDropNoItemAnimationState = new AnimationState();
    public static final EquipmentSlot EQUIPMENT_SLOT_ANTENNA = EquipmentSlot.HEAD;
    
    public CopperGolem(EntityType<? extends AbstractGolem> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.setState(CopperGolemState.IDLE);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.getBrain().setMemory(ModMemoryModuleTypes.TRANSPORT_ITEMS_COOLDOWN_TICKS.get(), this.getRandom().nextInt(60, 100));
        this.setMaxUpStep(1.0F);
    }
    
    @Override
    protected PathNavigation createNavigation(Level level) {
        CopperGolemPathNavigation navigation = new CopperGolemPathNavigation(this, level);
        navigation.setCanOpenDoors(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MOVEMENT_SPEED, 0.2F)
            .add(Attributes.MAX_HEALTH, 12.0);
    }
    
    @Override
    public float getEyeHeight(Pose pose) {
        return 0.8125F;
    }
    
    public CopperGolemState getState() {
        return this.entityData.get(COPPER_GOLEM_STATE);
    }
    
    public void setState(CopperGolemState state) {
        this.entityData.set(COPPER_GOLEM_STATE, state);
    }
    
    public WeatheredState getWeatherState() {
        return this.entityData.get(DATA_WEATHER_STATE);
    }
    
    public void setWeatherState(WeatheredState state) {
        this.entityData.set(DATA_WEATHER_STATE, state);
    }
    
    public void setOpenedChestPos(BlockPos openedChestPos) {
        this.openedChestPos = openedChestPos;
    }
    
    public void clearOpenedChestPos() {
        this.openedChestPos = null;
    }
    
    public AnimationState getIdleAnimationState() {
        return this.idleAnimationState;
    }
    
    public AnimationState getInteractionGetItemAnimationState() {
        return this.interactionGetItemAnimationState;
    }
    
    public AnimationState getInteractionGetNoItemAnimationState() {
        return this.interactionGetNoItemAnimationState;
    }
    
    public AnimationState getInteractionDropItemAnimationState() {
        return this.interactionDropItemAnimationState;
    }
    
    public AnimationState getInteractionDropNoItemAnimationState() {
        return this.interactionDropNoItemAnimationState;
    }
    
    @Override
    protected Brain.Provider<CopperGolem> brainProvider() {
        return CopperGolemAi.brainProvider();
    }
    
    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return CopperGolemAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }
    
    @Override @SuppressWarnings("unchecked")
    public Brain<CopperGolem> getBrain() {
        return (Brain<CopperGolem>) super.getBrain();
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WEATHER_STATE, WeatheredState.UNAFFECTED);
        this.entityData.define(COPPER_GOLEM_STATE, CopperGolemState.IDLE);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putLong("next_weather_age", this.nextWeatheringTick);
        compound.putString("weather_state", this.getWeatherState().getSerializedName());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.nextWeatheringTick = compound.getLong("next_weather_age");
        this.setWeatherState(WeatheredState.fromName(compound.getString("weather_state")));
    }
    
    @Override
    protected void customServerAiStep() {
        ServerLevel level = (ServerLevel) this.level();
        ProfilerFiller profiler = level.getProfiler();
        profiler.push("copperGolemBrain");
        this.getBrain().tick(level, this);
        profiler.popPush("copperGolemActivityUpdate");
        CopperGolemAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (!this.isNoAi()) {
                this.setupAnimationStates();
            }
        } else {
            this.updateWeathering((ServerLevel) this.level(), this.level().getRandom(), this.level().getGameTime());
        }
    }
    
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Level level = this.level();
        if (stack.isEmpty()) {
            ItemStack equippedItem = this.getMainHandItem();
            if (!equippedItem.isEmpty()) {
                BehaviorUtils.throwItem(this, equippedItem, player.position());
                this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        if (stack.is(Items.SHEARS) && this.readyForShearing()) {
            if (level instanceof ServerLevel) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (level.isClientSide) {
            return InteractionResult.PASS;
        } else if (stack.is(Items.HONEYCOMB) && this.nextWeatheringTick != -2L) {
            level.levelEvent(null, 3003, this.blockPosition(), 0);
            this.nextWeatheringTick = -2L;
            if (!player.getAbilities().instabuild) stack.shrink(1);
            
            return InteractionResult.SUCCESS;
        } else if (stack.is(ItemTags.AXES) && this.nextWeatheringTick == -2L) {
            level.playSound(null, this, SoundEvents.AXE_SCRAPE, this.getSoundSource(), 1.0F, 1.0F);
            level.levelEvent(null, 3004, this.blockPosition(), 0);
            this.nextWeatheringTick = -1L;
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            return InteractionResult.SUCCESS;
        } else {
            if (stack.is(ItemTags.AXES)) {
                WeatheredState weatherState = this.getWeatherState();
                if (weatherState != WeatheredState.UNAFFECTED) {
                    level.playSound(null, this, SoundEvents.AXE_SCRAPE, this.getSoundSource(), 1.0F, 1.0F);
                    level.levelEvent(null, 3005, this.blockPosition(), 0);
                    this.nextWeatheringTick = -1L;
                    this.entityData.set(DATA_WEATHER_STATE, WeatheredState.previous(weatherState), true);
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    return InteractionResult.SUCCESS;
                }
            }
            
            return super.mobInteract(player, hand);
        }
    }
    
    private void updateWeathering(ServerLevel level, RandomSource random, long gameTime) {
        if (this.nextWeatheringTick != -2L) {
            if (this.nextWeatheringTick == -1L) {
                this.nextWeatheringTick = gameTime + random.nextIntBetweenInclusive(504000, 552000);
            } else {
                WeatheredState weatherState = this.getWeatherState();
                boolean isFullyOxidized = weatherState.equals(WeatheredState.OXIDIZED);
                if (gameTime >= this.nextWeatheringTick && !isFullyOxidized) {
                    WeatheredState newState = WeatheredState.next(weatherState);
                    boolean isNewStateFullyOxidized = newState.equals(WeatheredState.OXIDIZED);
                    this.setWeatherState(newState);
                    this.nextWeatheringTick = isNewStateFullyOxidized ? 0L : this.nextWeatheringTick + random.nextIntBetweenInclusive(504000, 552000);
                }
                
                if (isFullyOxidized && this.canTurnToStatue(level)) {
                    this.turnToStatue(level);
                }
            }
        }
    }
    
    private boolean canTurnToStatue(Level level) {
        return level.getBlockState(this.blockPosition()).isAir() && level.getRandom().nextFloat() <= 0.0058F;
    }
    
    private void turnToStatue(ServerLevel level) {
        BlockPos pos = this.blockPosition();
        
        level.setBlock(
            pos,
            ModBlocks.OXIDIZED_COPPER_GOLEM_STATUE.get()
                .defaultBlockState()
                .setValue(CopperGolemStatueBlock.POSE, CopperGolemStatueBlock.Pose.values()[this.random.nextInt(0, CopperGolemStatueBlock.Pose.values().length)])
                .setValue(CopperGolemStatueBlock.FACING, Direction.fromYRot(this.getYRot())),
            3
        );
        
        if (level.getBlockEntity(pos) instanceof CopperGolemStatueBlockEntity golem) {
            golem.createStatue(this);
            EntityUtils.dropPreservedEquipment(this);
            this.discard();
            this.playSound(ModSoundEvents.COPPER_GOLEM_BECOME_STATUE.get());
            if (this.isLeashed()) {
                this.dropLeash(true, level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS));
            }
        }
    }
    
    private void setupAnimationStates() {
        switch (this.getState()) {
            case IDLE:
                this.interactionGetNoItemAnimationState.stop();
                this.interactionGetItemAnimationState.stop();
                this.interactionDropItemAnimationState.stop();
                this.interactionDropNoItemAnimationState.stop();
                if (this.idleAnimationStartTick == this.tickCount) {
                    this.idleAnimationState.start(this.tickCount);
                } else if (this.idleAnimationStartTick == 0) {
                    this.idleAnimationStartTick = this.tickCount + this.random.nextInt(200, 240);
                }
                
                if (this.tickCount == this.idleAnimationStartTick + 10.0F) {
                    this.playHeadSpinSound();
                    this.idleAnimationStartTick = 0;
                }
                break;
            case GETTING_ITEM:
                this.idleAnimationState.stop();
                this.idleAnimationStartTick = 0;
                this.interactionGetNoItemAnimationState.stop();
                this.interactionDropItemAnimationState.stop();
                this.interactionDropNoItemAnimationState.stop();
                this.interactionGetItemAnimationState.startIfStopped(this.tickCount);
                break;
            case GETTING_NO_ITEM:
                this.idleAnimationState.stop();
                this.idleAnimationStartTick = 0;
                this.interactionGetItemAnimationState.stop();
                this.interactionDropNoItemAnimationState.stop();
                this.interactionDropItemAnimationState.stop();
                this.interactionGetNoItemAnimationState.startIfStopped(this.tickCount);
                break;
            case DROPPING_ITEM:
                this.idleAnimationState.stop();
                this.idleAnimationStartTick = 0;
                this.interactionGetItemAnimationState.stop();
                this.interactionGetNoItemAnimationState.stop();
                this.interactionDropNoItemAnimationState.stop();
                this.interactionDropItemAnimationState.startIfStopped(this.tickCount);
                break;
            case DROPPING_NO_ITEM:
                this.idleAnimationState.stop();
                this.idleAnimationStartTick = 0;
                this.interactionGetItemAnimationState.stop();
                this.interactionGetNoItemAnimationState.stop();
                this.interactionDropItemAnimationState.stop();
                this.interactionDropNoItemAnimationState.startIfStopped(this.tickCount);
        }
    }
    
    public void spawn(WeatheredState state) {
        this.setWeatherState(state);
        this.playSpawnSound();
    }
    
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.playSpawnSound();
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    public void playSpawnSound() {
        this.playSound(ModSoundEvents.COPPER_GOLEM_SPAWN.get());
    }
    
    private void playHeadSpinSound() {
        if (!this.isSilent()) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), this.getSpinHeadSound(), this.getSoundSource(), 0.5F, 1.0F, false);
        }
    }
    
    @Override @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return CopperGolemOxidationLevels.getOxidationLevel(this.getWeatherState()).hurtSound();
    }
    
    @Override @Nullable
    protected SoundEvent getDeathSound() {
        return CopperGolemOxidationLevels.getOxidationLevel(this.getWeatherState()).deathSound();
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(CopperGolemOxidationLevels.getOxidationLevel(this.getWeatherState()).stepSound());
    }
    
    private SoundEvent getSpinHeadSound() {
        return CopperGolemOxidationLevels.getOxidationLevel(this.getWeatherState()).spinHeadSound();
    }
    
    @Override
    protected Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.75F * this.getEyeHeight(), 0.0);
    }
    
    @Override
    public boolean hasContainerOpen(ContainerOpenersCounter container, BlockPos blockPos) {
        if (this.openedChestPos == null) {
            return false;
        } else {
            BlockState blockState = this.level().getBlockState(this.openedChestPos);
            return this.openedChestPos.equals(blockPos)
                || blockState.getBlock() instanceof ChestBlock
                && blockState.getValue(ChestBlock.TYPE) != ChestType.SINGLE
                && ContainerHandler.getConnectedBlockPos(this.openedChestPos, blockState).equals(blockPos);
        }
    }
    
    @Override
    public void shear(SoundSource source) {
        this.level().playSound(null, this, ModSoundEvents.COPPER_GOLEM_SHEAR.get(), source, 1.0F, 1.0F);
        ItemStack itemStack = this.getItemBySlot(EQUIPMENT_SLOT_ANTENNA);
        this.setItemSlot(EQUIPMENT_SLOT_ANTENNA, ItemStack.EMPTY);
        this.spawnAtLocation(itemStack, 1.5F);
    }
    
    @Override
    public boolean readyForShearing() {
        return this.isAlive() && this.getItemBySlot(EQUIPMENT_SLOT_ANTENNA).is(ModItemTags.SHEARABLE_FROM_COPPER_GOLEM);
    }
    
    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        EntityUtils.dropPreservedEquipment(this);
    }
    
    @Override
    protected void actuallyHurt(DamageSource source, float damage) {
        super.actuallyHurt(source, damage);
        this.setState(CopperGolemState.IDLE);
    }
    
    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        super.thunderHit(level, lightning);
        UUID lightningBoltUUID = lightning.getUUID();
        if (!lightningBoltUUID.equals(this.lastLightningBoltUUID)) {
            this.lastLightningBoltUUID = lightningBoltUUID;
            WeatheredState weatherState = this.getWeatherState();
            if (weatherState != WeatheredState.UNAFFECTED) {
                this.nextWeatheringTick = -1L;
                this.entityData.set(DATA_WEATHER_STATE, WeatheredState.previous(weatherState), true);
            }
        }
    }
}