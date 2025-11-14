package com.blackgear.vanillabackport.core.mixin.common.entities.raids;

import com.blackgear.vanillabackport.common.level.effect.IRaidOmenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Raids.class)
public class RaidsMixin {
    @Redirect(
            method = "createOrExtendRaid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;blockPosition()Lnet/minecraft/core/BlockPos;"
            )
    )
    public BlockPos vb$onCreateOrExtendRaid(ServerPlayer instance){
        if(instance instanceof IRaidOmenHelper vIRaidOmenHelper){
            return vIRaidOmenHelper.vb$getRaidOmenPosition();
        }
        return instance.blockPosition();
    }
}
