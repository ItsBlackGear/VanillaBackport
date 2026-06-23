package com.blackgear.vanillabackport.core.mixin.common.extension;

import com.blackgear.vanillabackport.common.api.extensions.entity.EntityRemoval;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements EntityRemoval {
    @Inject(method = "setRemoved", at = @At("TAIL"))
    private void vb$handleEntityRemoval(Entity.RemovalReason reason, CallbackInfo ci) {
        this.onRemoval(reason);
    }
}