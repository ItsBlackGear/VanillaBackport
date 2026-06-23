package com.blackgear.vanillabackport.core.mixin.common.attributes;

import com.blackgear.vanillabackport.core.mixin.common.access.RangedAttributeAccessor;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedAttribute.class)
public class RangedAttributeMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$fixKnockbackResistanceRange(
        String descriptionId,
        double defaultValue,
        double min,
        double max,
        CallbackInfo ci
    ) {
        RangedAttribute self = (RangedAttribute)(Object)this;
        if (descriptionId.equals("attribute.name.knockback_resistance")
            || descriptionId.equals("attribute.name.generic.knockback_resistance")) {
            ((RangedAttributeAccessor) self).setMinValue(-2.0);
        }
    }
}