package com.blackgear.vanillabackport.client.api.modules.options;

import com.blackgear.vanillabackport.client.api.modules.music_frequency.MusicFrequency;
import com.blackgear.vanillabackport.client.api.modules.music_toast.MusicToastDisplayState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;

@Environment(EnvType.CLIENT)
public interface OptionsAccess {
    OptionInstance<MusicFrequency> musicFrequency();
    
    OptionInstance<MusicToastDisplayState> musicToast();
}