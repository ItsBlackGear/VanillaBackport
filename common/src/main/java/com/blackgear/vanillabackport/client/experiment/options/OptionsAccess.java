package com.blackgear.vanillabackport.client.experiment.options;

import com.blackgear.vanillabackport.client.experiment.music_frequency.MusicFrequency;
import com.blackgear.vanillabackport.client.experiment.music_toast.MusicToastDisplayState;
import net.minecraft.client.OptionInstance;

public interface OptionsAccess {
    OptionInstance<MusicFrequency> musicFrequency();
    
    OptionInstance<MusicToastDisplayState> musicToast();
}