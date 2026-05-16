package com.blackgear.vanillabackport.core.mixin.leash;

import com.blackgear.vanillabackport.common.api.leash.LeashDataAccess;
import com.blackgear.vanillabackport.common.api.leash.LeashHolderCallback;
import com.blackgear.vanillabackport.common.api.leash.LeashPhysics;
import com.blackgear.vanillabackport.common.api.leash.LeashableCallback;
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
        ci.cancel();

        Leashable.LeashData data = entity.getLeashData();
        if (data != null && data.delayedLeashInfo != null) {
            restoreLeashFromSave(entity, data);
        }

        if (data != null && data.leashHolder != null) {
            if (!entity.isAlive() || !data.leashHolder.isAlive()) {
                entity.dropLeash(true, entity.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS));
                return;
            }

            Entity holder = entity.getLeashHolder();
            if (holder != null && holder.level() == entity.level()) {
                if (holder instanceof LeashHolderCallback callback) {
                    callback.vb$notifyLeashHolder(entity);
                }

                double distanceTo = LeashPhysics.distanceBetween(entity, holder);
                if (distanceTo > LeashPhysics.snapDistance(entity)) {
                    entity.level().playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    entity.leashTooFarBehaviour();
                } else if (distanceTo > LeashPhysics.elasticDistance(entity) - holder.getBbWidth() - entity.getBbWidth()) {
                    if (LeashPhysics.checkElasticInteractions(entity, holder, data)) {
                        if (entity instanceof LeashableCallback callback) {
                            callback.vb$onElasticLeashPull();
                        } else {
                            entity.checkSlowFallDistance();
                        }
                    }
                } else {
                    entity.closeRangeLeashBehaviour(holder);
                }

                LeashDataAccess access = (LeashDataAccess)(Object) data;
                entity.setYRot((float)(entity.getYRot() - access.vb$getAngularMomentum()));
                access.vb$setAngularMomentum(access.vb$getAngularMomentum() * LeashPhysics.angularFriction(entity));
            }
        }
    }
}
