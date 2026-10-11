package com.blackgear.vanillabackport.core.mixin.client.music_on_pause;

import com.blackgear.vanillabackport.client.api.modules.music_on_pause.MusicTickAccess;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @WrapOperation(method = "pauseGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;pause()V"))
    private void vb$dontPauseMusic(SoundManager instance, Operation<Void> original) {
        ((MusicTickAccess) instance).pauseAllExcept(SoundSource.MUSIC);
    }
    
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/MusicManager;tick()V"))
    private void vb$forceMusicTick(MusicManager instance, Operation<Void> original) {
        instance.tick();
    }
}