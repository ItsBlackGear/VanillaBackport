package com.blackgear.vanillabackport.core.mixin.common.zombie_horses;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
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

import java.util.function.DoubleSupplier;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMixin extends AbstractHorse implements ControllableMob {
    protected ZombieHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void vb$createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 25.0));
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
        ItemStack itemStack = this.getBodyArmorItem();
        super.containerChanged(container);
        ItemStack itemStack2 = this.getBodyArmorItem();
        if (this.tickCount > 20 && this.isBodyArmorItem(itemStack2) && itemStack != itemStack2) {
            this.playSound(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
        }
    }
    
    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }
    
    @Override
    public boolean isBodyArmorItem(ItemStack stack) {
        if (stack.getItem() instanceof AnimalArmorItem animalArmorItem) {
            return animalArmorItem.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN;
        }
        
        return false;
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        if (reason == MobSpawnType.NATURAL) {
            Zombie zombie = EntityType.ZOMBIE.create(this.level());
            if (zombie != null) {
                zombie.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                zombie.finalizeSpawn(level, difficulty, reason, null);
                zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
                zombie.startRiding((ZombieHorse) (Object) this, false);
            }
        }
        
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }
    
    @Override
    public boolean canBeLeashed() {
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
    
    @Override
    public float chargeSpeedModifier() {
        return 1.4F;
    }
}