package com.blackgear.vanillabackport.common.level.effect;

import com.blackgear.vanillabackport.common.api.effect.AdvanceMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class RaidOmenMobEffect extends AdvanceMobEffect {
    public RaidOmenMobEffect(MobEffectCategory pCategory, int pColor, Supplier<ParticleOptions> particleSupplier) {
        super(pCategory, pColor, particleSupplier);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer vServerPlayer) {
            if (!livingEntity.isSpectator()) {
                ServerLevel vServerLevel = vServerPlayer.serverLevel();
                if(vServerPlayer instanceof IRaidOmenHelper vRaidOmenHelper){
                    BlockPos vPos = vRaidOmenHelper.vb$getRaidOmenPosition();
                    if(vPos != null){
                        vServerLevel.getRaids().createOrExtendRaid(vServerPlayer);
                        vRaidOmenHelper.vb$clearRaidOmenPosition();
                    }
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration == 1;
    }
}
