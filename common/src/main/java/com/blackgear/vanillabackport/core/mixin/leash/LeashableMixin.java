package com.blackgear.vanillabackport.core.mixin.leash;

import com.blackgear.vanillabackport.common.api.leash.LeashDataExtension;
import com.blackgear.vanillabackport.common.api.leash.LeashExtension;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Leashable.class)
public interface LeashableMixin extends LeashExtension {
    @Shadow private static <E extends Entity & Leashable> void restoreLeashFromSave(E entity, Leashable.LeashData leashData) { }

    @Inject(method = "tickLeash", at = @At("HEAD"), cancellable = true)
    private static <E extends Entity & Leashable> void onTickLeash(E entity, CallbackInfo ci) {
        Leashable.LeashData data = entity.getLeashData();
        if (data != null && data.delayedLeashInfo != null) {
            restoreLeashFromSave(entity, data);
        }

        if (data != null && data.leashHolder != null) {
            if (!entity.isAlive() || !data.leashHolder.isAlive()) {
                entity.dropLeash(true, entity.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS));
            }

            Entity holder = entity.getLeashHolder();
            LeashExtension leashed = (LeashExtension) entity;
            if (holder != null && holder.level() == entity.level()) {
                double distance = leashed.leashDistanceTo(holder);
                leashed.whenLeashedTo(holder);
                if (distance > leashed.leashSnapDistance()) {
                    entity.level().playSound(null, holder.blockPosition(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    entity.leashTooFarBehaviour();
                } else if (distance > leashed.leashElasticDistance() - holder.getBbWidth() - entity.getBbWidth() && leashed.checkElasticInteractions(holder, data)) {
                    leashed.onElasticLeashPull();
                } else {
                    entity.closeRangeLeashBehaviour(holder);
                }

                LeashDataExtension leashData = (LeashDataExtension) (Object) data;
                entity.setYRot((float) (entity.getYRot() - leashData.angularMomentum()));
                leashData.setAngularMomentum(leashData.angularMomentum() * LeashExtension.angularFriction(entity));
            }
        }

        ci.cancel();
    }
}