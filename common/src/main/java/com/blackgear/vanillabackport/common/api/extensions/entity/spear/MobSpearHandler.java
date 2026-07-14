package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import com.blackgear.vanillabackport.common.level.components.AttackRange;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public interface MobSpearHandler {
    AttackRange getAttackRangeWith(ItemStack weapon);

    boolean wasRecentlyStabbed(Entity target, int allowedTime);

    void rememberStabbedEntity(Entity target);

    default int stabbedEntities(Predicate<Entity> filter) {
        return 0;
    }

    boolean stabAttack(EquipmentSlot weaponSlot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts);

    void causeExtraKnockback(Entity target, float knockback, Vec3 oldMovement);

    default void onAttack() {}

    void postPiercingAttack();

    float getTicksSinceLastKineticHitFeedback(float partial);

    float getTicksUsingItem(float partial);
}