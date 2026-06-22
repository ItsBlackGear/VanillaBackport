package com.blackgear.vanillabackport.core.mixin.client.music_on_pause;

import com.blackgear.vanillabackport.client.api.modules.music_on_pause.MusicTickAccess;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin implements MusicTickAccess {
    @Shadow private boolean loaded;
    @Shadow @Final private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;
    @Shadow protected abstract float getVolume(SoundSource soundSource);
    
    @Unique private boolean vb$isReallyPaused = false;
    
    @Override
    public void pauseAllExcept(SoundSource... sources) {
        if (this.loaded) {
            this.vb$isReallyPaused = true;
            List<SoundSource> whitelist = List.of(sources);
            
            for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry : this.instanceToChannel.entrySet()) {
                SoundInstance sound = entry.getKey();
                if (!whitelist.contains(sound.getSource())) {
                    entry.getValue().execute(channel -> {
                        channel.pause();
                        channel.setVolume(0.0F);
                    });
                }
            }
        }
    }
    
    @Inject(method = "resume", at = @At("HEAD"))
    private void onResume(CallbackInfo ci) {
        if (this.vb$isReallyPaused) {
            this.vb$isReallyPaused = false;
            
            for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry : this.instanceToChannel.entrySet()) {
                SoundInstance sound = entry.getKey();
                if (sound.getSource() != SoundSource.MUSIC) {
                    float volume = this.getVolume(sound.getSource());
                    entry.getValue().execute(channel -> channel.setVolume(volume));
                }
            }
        }
    }
    
    @ModifyVariable(method = "tick", at = @At("HEAD"), argsOnly = true)
    private boolean vb$bypassPauseEngine(boolean isGamePaused) {
        return false;
    }
}