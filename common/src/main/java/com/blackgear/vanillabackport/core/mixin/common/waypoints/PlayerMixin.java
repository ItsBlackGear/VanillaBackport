package com.blackgear.vanillabackport.core.mixin.common.waypoints;

import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {
    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createLivingAttributes(AttributeSupplier.Builder original) {
        original.add(ModAttributes.WAYPOINT_TRANSMIT_RANGE, 6.0E7);
        original.add(ModAttributes.WAYPOINT_RECEIVE_RANGE, 6.0E7);
        return original;
    }
}