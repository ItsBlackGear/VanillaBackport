package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.Mob.class)
public interface MobAccessor {
    @Invoker
    float callGetEquipmentDropChance(EquipmentSlot slot);
}
