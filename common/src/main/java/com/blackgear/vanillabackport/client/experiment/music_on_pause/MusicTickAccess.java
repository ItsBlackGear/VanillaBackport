package com.blackgear.vanillabackport.client.experiment.music_on_pause;

import net.minecraft.sounds.SoundSource;

public interface MusicTickAccess {
    void pauseAllExcept(SoundSource... source);
}