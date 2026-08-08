package com.blackgear.vanillabackport.core.mixin.common.zombie_horses;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.mounts.ControllableMob;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.DoubleSupplier;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMixin extends AbstractHorse implements ControllableMob {
    @Unique private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    
    protected ZombieHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void vb$createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 25.0));
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.inventory.getItem(1).isEmpty()) {
            compound.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("ArmorItem", 10)) {
            ItemStack stack = ItemStack.of(compound.getCompound("ArmorItem"));
            if (!stack.isEmpty() && this.isArmor(stack)) {
                this.inventory.setItem(1, stack);
            }
        }
        
        this.updateContainerEquipment();
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        this.setPersistenceRequired();
        
        boolean shouldOpenInventory = !this.isBaby() && this.isTamed() && player.isSecondaryUseActive();
        if (!this.isVehicle() && !shouldOpenInventory) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (!heldItem.isEmpty()) {
                if (this.isFood(heldItem)) {
                    return this.fedFood(player, heldItem);
                }
                
                if (!this.isTamed()) {
                    this.makeMad();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        
        return super.mobInteract(player, hand);
    }
    
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return true;
    }
    
    @Override
    public boolean isMobControlled() {
        return this.getFirstPassenger() instanceof Mob;
    }
    
    @Inject(method = "randomizeAttributes", at = @At("TAIL"))
    private void vb$randomizeAttributes(RandomSource random, CallbackInfo ci) {
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateZombieHorseJumpStrength(random::nextDouble));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(generateZombieHorseSpeed(random::nextDouble));
    }
    
    @Unique
    private static double generateZombieHorseJumpStrength(DoubleSupplier probabilityProvider) {
        return 0.5
            + probabilityProvider.getAsDouble() * 0.06666666666666667
            + probabilityProvider.getAsDouble() * 0.06666666666666667
            + probabilityProvider.getAsDouble() * 0.06666666666666667;
    }
    
    @Unique
    private static double generateZombieHorseSpeed(DoubleSupplier probabilityProvider) {
        return (9.0D + probabilityProvider.getAsDouble() + probabilityProvider.getAsDouble() + probabilityProvider.getAsDouble()) / 42.16;
    }
    
    @Override
    protected @Nullable SoundEvent getAngrySound() {
        return ModSoundEvents.ZOMBIE_HORSE_ANGRY.get();
    }
    
    @Override
    protected @Nullable SoundEvent getEatingSound() {
        return ModSoundEvents.ZOMBIE_HORSE_EAT.get();
    }
    
    @Override
    public void containerChanged(Container container) {
        ItemStack oldArmorItem = this.getArmor();
        super.containerChanged(container);
        ItemStack newArmorItem = this.getArmor();
        if (this.tickCount > 20 && this.isArmor(newArmorItem) && oldArmorItem != newArmorItem) {
            this.playSound(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
        }
    }
    
    @Override
    protected void updateContainerEquipment() {
        if (!this.level().isClientSide) {
            super.updateContainerEquipment();
            this.setArmorEquipment(this.inventory.getItem(1));
            this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        }
    }
    
    @Unique
    private void setArmorEquipment(ItemStack stack) {
        this.setArmor(stack);
        if (!this.level().isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            if (this.isArmor(stack)) {
                int protection = ((HorseArmorItem) stack.getItem()).getProtection();
                if (protection != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", protection, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }
    
    @Unique
    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }
    
    @Unique
    private void setArmor(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.CHEST, stack);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }
    
    @Override
    public boolean canWearArmor() {
        return true;
    }
    
    @Override
    public boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof HorseArmorItem;
    }
    
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            Zombie zombie = EntityType.ZOMBIE.create(this.level());
            if (zombie != null) {
                zombie.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                zombie.finalizeSpawn(level, difficulty, reason, null, dataTag);
                zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
                zombie.startRiding((ZombieHorse) (Object) this, false);
            }
        }
        
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    @Override
    public boolean canBeLeashed(Player player) {
        return this.isTamed() || !this.isMobControlled();
    }
    
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItemTags.ZOMBIE_HORSE_FOOD);
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isSunBurnTick()) {
            EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                if (itemStack.isDamageableItem()) {
                    itemStack.setDamageValue(itemStack.getDamageValue() + this.random.nextInt(2));
                    if (itemStack.getDamageValue() >= itemStack.getMaxDamage()) {
                        this.broadcastBreakEvent(equipmentSlot);
                        this.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                    }
                }
            } else {
                this.setSecondsOnFire(8);
            }
        }
    }
    
    @Override
    public float chargeSpeedModifier() {
        return 1.4F;
    }
}