package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import com.blackgear.vanillabackport.common.level.items.spear.AttackRange;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public interface MobSpearHandler {
    AttackRange vb$getAttackRangeWith(ItemStack weapon);
    
    boolean vb$wasRecentlyStabbed(Entity target, int allowedTime);
    
    void vb$rememberStabbedEntity(Entity target);
    
    default int vb$stabbedEntities(Predicate<Entity> filter) {
        return 0;
    }
    
    boolean vb$stabAttack(EquipmentSlot weaponSlot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts);
    
    void vb$causeExtraKnockback(Entity target, float knockback, Vec3 oldMovement);
    
    default void vb$onAttack() {}
    
    void vb$postPiercingAttack();
    
    float vb$getTicksSinceLastKineticHitFeedback(float partial);
    
    float vb$getTicksUsingItem(float partial);
}