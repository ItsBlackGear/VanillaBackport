package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import com.blackgear.vanillabackport.common.level.item.spear.PiercingWeapon;

public interface ServerSpearHandler {
    default void piercingAttack(PiercingWeapon piercingWeapon) {}
}