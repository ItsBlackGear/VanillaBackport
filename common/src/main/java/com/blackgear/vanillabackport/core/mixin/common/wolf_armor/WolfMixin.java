package com.blackgear.vanillabackport.core.mixin.common.wolf_armor;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.ModCrackiness;
import com.blackgear.vanillabackport.common.level.item.WolfArmorItem;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.mixin.common.extension.TamableAnimalMixin;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimalMixin implements NeutralMob {
    protected WolfMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void vb$getHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
        if (this.canArmorAbsorb(damageSource)) {
            cir.setReturnValue(ModSoundEvents.WOLF_ARMOR_DAMAGE.get());
        }
    }
    
    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!this.canArmorAbsorb(damageSource)) {
            super.actuallyHurt(damageSource, damageAmount);
        } else {
            ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
            int prevDamage = stack.getDamageValue();
            int max = stack.getMaxDamage();
            stack.hurtAndBreak(Mth.ceil(damageAmount), this, wolf -> wolf.broadcastBreakEvent(EquipmentSlot.CHEST));
            if (ModCrackiness.WOLF_ARMOR.byDamage(prevDamage, max) != ModCrackiness.WOLF_ARMOR.byDamage(stack)) {
                this.playSound(ModSoundEvents.WOLF_ARMOR_CRACK.get());
                if (this.level() instanceof ServerLevel level) {
                    level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, ModItems.ARMADILLO_SCUTE.get().getDefaultInstance()), this.getX(), this.getY() + 1.0, this.getZ(), 20, 0.2, 0.1, 0.2, 0.1);
                }
            }
        }
    }

    @Unique
    private boolean canArmorAbsorb(DamageSource source) {
        return this.hasArmor() && !source.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    @Override
    protected void applyTamingSideEffects() {
        if (this.isTame()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0);
            this.setHealth(40.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0);
        }
    }

    @Override
    public void hurtArmor(DamageSource damageSource, float damageAmount) {
        if (damageAmount > 0.0F) {
            int i = (int) Math.max(1.0F, damageAmount / 4.0F);

            ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
            if (stack.getItem() instanceof WolfArmorItem) {
                stack.hurtAndBreak(i, this, wolf -> wolf.broadcastBreakEvent(EquipmentSlot.CHEST));
                if (stack.isEmpty()) {
                    this.playSound(ModSoundEvents.WOLF_ARMOR_BREAK.get());
                    this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                }
            }
        }
    }

    @Unique
    private boolean hasArmor() {
        ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
        return !stack.isEmpty() && stack.is(ModItems.WOLF_ARMOR.get());
    }
}