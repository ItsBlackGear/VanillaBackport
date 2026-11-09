package com.blackgear.vanillabackport.core.mixin.common.mace;

import com.blackgear.vanillabackport.common.level.items.MaceItem;
import com.blackgear.vanillabackport.common.level.items.enchantment.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    @WrapOperation(
        method = "attack",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    private boolean vb$onMaceAttack(Entity target, DamageSource source, float amount, Operation<Boolean> original) {
        ItemStack heldItem = this.getMainHandItem();
        Item item = heldItem.getItem();
        if (item instanceof MaceItem mace) {
            amount += mace.getAttackDamageBonus(target, amount, source);
        }
        
        return original.call(target, source, amount);
    }
    
    @WrapOperation(
        method = "actuallyHurt",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F")
    )
    private float vb$onArmorAbsorb(Player player, DamageSource source, float amount, Operation<Float> original) {
        float result = original.call(player, source, amount);
        Entity attacker = source.getDirectEntity();
        ItemStack heldItem = attacker instanceof LivingEntity living ? living.getMainHandItem() : null;
        
        if (heldItem != null) {
            float reductionFactor = EnchantmentUtils.getReducedArmorEffectiveness(heldItem, this);
            reductionFactor = Mth.clamp(reductionFactor, 0.0F, 1.0F);
            result = Mth.lerp(reductionFactor, amount, result);
        }
        
        return result;
    }
    
    @Inject(
        at = {@At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostDamageEffects(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;)V",
            ordinal = 0,
            shift = At.Shift.BEFORE
        )},
        method = {"attack(Lnet/minecraft/world/entity/Entity;)V"}
    )
    private void doPreAttack(Entity entity, CallbackInfo info) {
        if (this.level() instanceof ServerLevel && entity instanceof LivingEntity living) {
            ItemStack weapon = this.getMainHandItem();
            Item var6 = weapon.getItem();
            if (var6 instanceof MaceItem mace) {
                mace.preHurtEnemy(weapon, living, this);
            }
        }
    }
}