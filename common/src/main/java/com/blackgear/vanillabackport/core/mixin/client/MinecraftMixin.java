package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.blackgear.vanillabackport.core.network.ServerboundClientTickEndPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Nullable public ClientLevel level;
    @Shadow private volatile boolean pause;

    @Inject(method = "tick", at = @At("TAIL"))
    private void vb$handleTick(CallbackInfo ci) {
        if (this.level != null && !this.pause) {
            NetworkHandler.DEFAULT_CHANNEL.sendToServer(new ServerboundClientTickEndPacket());
        }
    }
}