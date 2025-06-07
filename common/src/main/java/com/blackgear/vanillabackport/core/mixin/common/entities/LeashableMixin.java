package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Leashable.class)
public interface LeashableMixin {
    @Invoker
    static void callRestoreLeashFromSave(Entity entity, Leashable.LeashData leashData) {}

    @Invoker
    static void callDropLeash(Entity entity, boolean broadcast, boolean drop) {}

    @Inject(method = "tickLeash", at = @At("HEAD"), cancellable = true)
    private static <E extends Entity & Leashable> void vb$tickLeash(E entity, CallbackInfo ci) {
        if (entity instanceof HappyGhast) {
            ci.cancel();
            Leashable.LeashData data = entity.getLeashData();
            if (data != null && data.delayedLeashInfo != null) {
                callRestoreLeashFromSave(entity, data);
            }

            if (data != null && data.leashHolder != null) {
                if (!entity.isAlive() || !data.leashHolder.isAlive()) {
                    callDropLeash(entity, true, true);
                }

                Entity holder = entity.getLeashHolder();
                if (holder != null && holder.level() == entity.level()) {
                    float distanceFromHolder = entity.distanceTo(holder);
                    if (!entity.handleLeashAtDistance(holder, distanceFromHolder)) {
                        return;
                    }

                    if ((double) distanceFromHolder > (double) 16.0F) {
                        entity.leashTooFarBehaviour();
                    } else if ((double) distanceFromHolder > (double) 12.0F) {
                        entity.elasticRangeLeashBehaviour(holder, distanceFromHolder);
                        entity.checkSlowFallDistance();
                    } else {
                        entity.closeRangeLeashBehaviour(holder);
                    }
                }
            }
        }
    }
}