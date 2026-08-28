package com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantSpawner;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ZombieNautilus extends AbstractNautilus implements VariantDataHolder<ZombieNautilusVariant> {
    private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(ZombieNautilus.class, EntityDataSerializers.STRING);
    
    public ZombieNautilus(EntityType<? extends AbstractNautilus> entityType, Level level) {
        super(entityType, level);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractNautilus.createAttributes().add(Attributes.MOVEMENT_SPEED, 1.1F);
    }
    
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }
    
    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return ZombieNautilusAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }
    
    @Override
    protected Brain.Provider<ZombieNautilus> brainProvider() {
        return ZombieNautilusAi.brainProvider();
    }
    
    @Override @SuppressWarnings("unchecked")
    public Brain<ZombieNautilus> getBrain() {
        return (Brain<ZombieNautilus>) super.getBrain();
    }
    
    
    @Override
    protected void customServerAiStep() {
        ProfilerFiller profiler = this.level().getProfiler();
        profiler.push("zombieNautilusBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        profiler.popPush("zombieNautilusActivityUpdate");
        ZombieNautilusAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep();
    }
    
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isUnderWater() ? ModSoundEvents.ZOMBIE_NAUTILUS_AMBIENT.get() : ModSoundEvents.ZOMBIE_NAUTILUS_AMBIENT_ON_LAND.get();
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isUnderWater() ? ModSoundEvents.ZOMBIE_NAUTILUS_HURT.get() : ModSoundEvents.ZOMBIE_NAUTILUS_HURT_ON_LAND.get();
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return this.isUnderWater() ? ModSoundEvents.ZOMBIE_NAUTILUS_DEATH.get() : ModSoundEvents.ZOMBIE_NAUTILUS_DEATH_ON_LAND.get();
    }
    
    @Override
    protected SoundEvent getDashSound() {
        return this.isUnderWater() ? ModSoundEvents.ZOMBIE_NAUTILUS_DASH.get() : ModSoundEvents.ZOMBIE_NAUTILUS_DASH_ON_LAND.get();
    }
    
    @Override
    protected SoundEvent getDashReadySound() {
        return this.isUnderWater() ? ModSoundEvents.ZOMBIE_NAUTILUS_DASH_READY.get() : ModSoundEvents.ZOMBIE_NAUTILUS_DASH_READY_ON_LAND.get();
    }
    
    @Override
    protected void playEatingSound() {
        this.playSound(ModSoundEvents.ZOMBIE_NAUTILUS_EAT.get());
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return ModSoundEvents.ZOMBIE_NAUTILUS_SWIM.get();
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, "minecraft:temperate");
    }
    
    @Override
    public void setVariantData(ZombieNautilusVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(ZombieNautilusVariants.REGISTRIES, variant));
    }
    
    @Override
    public Optional<ZombieNautilusVariant> getVariantData() {
        return VariantUtils.getOrDefault(ZombieNautilusVariants.REGISTRIES, this.entityData.get(DATA_VARIANT_ID));
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        VariantUtils.readVariantSaveData(this, compound, ZombieNautilusVariants.REGISTRIES);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        VariantUtils.addVariantSaveData(this, compound, ZombieNautilusVariants.REGISTRIES);
    }
    
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ZombieNautilusVariants.REGISTRIES, VariantSpawner.DEFAULT)
            .ifPresent(this::setVariantData);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }
    
    @Override
    public boolean canBeLeashed() {
        return !this.isAggravated() && !this.isMobControlled();
    }
    
    @Override
    public void setBaby(boolean baby) {
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isSunBurnTick()) {
            EquipmentSlot equipmentSlot = EquipmentSlot.BODY;
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                if (itemStack.isDamageableItem()) {
                    Item item = itemStack.getItem();
                    itemStack.setDamageValue(itemStack.getDamageValue() + this.random.nextInt(2));
                    if (itemStack.getDamageValue() >= itemStack.getMaxDamage()) {
                        this.onEquippedItemBroken(item, equipmentSlot);
                        this.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                    }
                }
                
            } else {
                this.igniteForSeconds(8.0F);
            }
        }
    }
}