package com.blackgear.vanillabackport.core.mixin.common.equipment;

import com.blackgear.vanillabackport.common.api.SpawnEquipmentRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin {
    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;getEquipmentForSlot(Lnet/minecraft/world/entity/EquipmentSlot;I)Lnet/minecraft/world/item/Item;"
        )
    )
    private Item vb$wrapGetEquipmentForSlot(EquipmentSlot slot, int type, Operation<Item> original) {
        Mob mob = (Mob) (Object) this;
        Item customItem = SpawnEquipmentRegistry.tryGetCustomEquipment(mob, slot, type);
        return customItem != null ? customItem : original.call(slot, type);
    }
}