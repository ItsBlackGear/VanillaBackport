package com.blackgear.vanillabackport.core.mixin.common.straw_bed;

import com.blackgear.vanillabackport.common.level.blocks.StrawBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void vb$onSetRespawnPosition(ResourceKey<Level> dimension, BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (pos != null && player.level().getBlockState(pos).getBlock() instanceof StrawBedBlock) {
            ci.cancel();
        }
    }
}