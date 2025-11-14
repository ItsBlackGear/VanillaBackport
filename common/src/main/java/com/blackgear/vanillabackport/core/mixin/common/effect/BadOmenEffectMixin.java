package com.blackgear.vanillabackport.core.mixin.common.effect;

import com.blackgear.vanillabackport.common.level.effect.IRaidOmenHelper;
import com.blackgear.vanillabackport.common.registries.ModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.world.effect.MobEffects$1")
public class BadOmenEffectMixin {
    @Inject(
            method = "applyEffectTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/raid/Raids;createOrExtendRaid(Lnet/minecraft/server/level/ServerPlayer;)Lnet/minecraft/world/entity/raid/Raid;"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void vb$onApplyEffectTick(LivingEntity livingEntity, int amplifier, CallbackInfo ci, ServerPlayer serverPlayer, ServerLevel serverLevel){
        Raid vRaid = serverLevel.getRaidAt(serverPlayer.blockPosition());
        if (vRaid == null || vRaid.getBadOmenLevel() < vRaid.getMaxBadOmenLevel()) {
            serverPlayer.addEffect(new MobEffectInstance(ModMobEffects.RAID_OMEN.get(), 600, amplifier));
            serverPlayer.removeEffect(MobEffects.BAD_OMEN);
            if(serverPlayer instanceof IRaidOmenHelper vRaidOmenHelper){
                vRaidOmenHelper.vb$setRaidOmenPosition(serverPlayer.blockPosition());
            }
        }
        ci.cancel();
    }
}
