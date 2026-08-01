package com.blackgear.vanillabackport.common.registries.enchantment;

import com.blackgear.vanillabackport.core.mixin.common.access.EnchantmentAccessor;
import com.blackgear.vanillabackport.core.mixin.common.access.EnchantmentHelperAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentUtils {
    public static void doPostPiercingAttackEffects(ServerLevel server, LivingEntity user) {
        EnchantmentHelperAccessor.callRunIterationOnItem(user.getWeaponItem(), EquipmentSlot.MAINHAND, user,
            (enchantment, level, item) -> doPostPiercingAttack(enchantment.value(), server, level, item, user));
    }
    
    private static void doPostPiercingAttack(Enchantment enchantment, ServerLevel server, int level, EnchantedItemInUse item, Entity user) {
        EnchantmentAccessor.callApplyEffects(
            enchantment.getEffects(ModEnchantmentEffectComponents.POST_PIERCING_ATTACK.get()),
            EnchantmentAccessor.callEntityContext(server, level, user, user.position()),
            e -> e.apply(server, level, item, user, user.position())
        );
    }
}