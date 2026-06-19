package com.blackgear.vanillabackport.core.mixin.client.music_on_pause;

import com.blackgear.vanillabackport.client.experiment.music_on_pause.MusicTickAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundManager.class)
public class SoundManagerMixin implements MusicTickAccess {
    @Shadow @Final private SoundEngine soundEngine;
    
    @Override
    public void pauseAllExcept(SoundSource... source) {
        ((MusicTickAccess) this.soundEngine).pauseAllExcept(source);
    }
}