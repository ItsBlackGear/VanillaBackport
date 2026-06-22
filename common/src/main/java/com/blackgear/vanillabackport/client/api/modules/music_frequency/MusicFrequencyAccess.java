package com.blackgear.vanillabackport.client.api.modules.music_frequency;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MusicFrequencyAccess {
    void setMinutesBetweenSongs(MusicFrequency frequency);
}