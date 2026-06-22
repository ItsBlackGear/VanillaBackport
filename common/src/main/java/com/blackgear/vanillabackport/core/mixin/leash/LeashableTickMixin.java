package com.blackgear.vanillabackport.core.mixin.leash;

import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashDataExtension;
import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashPhysics;
import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashableCallback;
import com.blackgear.vanillabackport.core.ModChecker;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Leashable.class)
public interface LeashableTickMixin {
    @Shadow private static <E extends Entity & Leashable> void restoreLeashFromSave(E entity, Leashable.LeashData leashData) {}

    @Inject(method = "tickLeash", at = @At("HEAD"), cancellable = true)
    private static <E extends Entity & Leashable> void vb$tickLeash(E entity, CallbackInfo ci) {
        if (ModChecker.SABLE_LOADED) return;

        Leashable.LeashData data = entity.getLeashData();
        if (data != null && data.delayedLeashInfo != null) {
            restoreLeashFromSave(entity, data);
        }

        if (data != null && data.leashHolder != null) {
            if (!entity.isAlive() || !data.leashHolder.isAlive()) {
                entity.dropLeash(true, entity.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS));
            }

            Entity holder = entity.getLeashHolder();
            LeashableCallback callback = (LeashableCallback) entity;
            if (holder != null && holder.level() == entity.level()) {
                double distanceTo = LeashPhysics.distanceBetween(entity, holder);
                callback.vb$whenLeashedTo(holder);
                if (distanceTo > callback.vb$leashSnapDistance()) {
                    entity.level().playSound(null, holder.blockPosition(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    entity.leashTooFarBehaviour();
                } else if (distanceTo > callback.vb$leashElasticDistance() - holder.getBbWidth() - entity.getBbWidth() && LeashPhysics.checkElasticInteractions(entity, holder, data)) {
                    callback.vb$onElasticLeashPull();
                } else {
                    entity.closeRangeLeashBehaviour(holder);
                }

                LeashDataExtension leashData = (LeashDataExtension) (Object) data;
                entity.setYRot((float) (entity.getYRot() - leashData.angularMomentum()));
                leashData.setAngularMomentum(leashData.angularMomentum() * LeashPhysics.angularFriction(entity));
            }
        }

        ci.cancel();
    }
}
