package com.blackgear.vanillabackport.common.api.extensions.entity.arms;

import net.minecraft.world.item.UseAnim;

public enum ItemUseAnimations {
    REAL_SPEAR;

    public UseAnim get() {
        return UseAnim.valueOf(this.name());
    }
}