package com.blackgear.vanillabackport.client.api.modules.music_on_pause;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sounds.SoundSource;

@Environment(EnvType.CLIENT)
public interface MusicTickAccess {
    void pauseAllExcept(SoundSource... source);
}