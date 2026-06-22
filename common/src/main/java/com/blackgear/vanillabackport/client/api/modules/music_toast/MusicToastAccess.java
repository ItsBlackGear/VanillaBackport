package com.blackgear.vanillabackport.client.api.modules.music_toast;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MusicToastAccess {
    void showNowPlayingToast();
    
    void hideNowPlayingToast();
    
    void initializeMusicToast(MusicToastDisplayState state);
    
    void setMusicToastDisplayState(MusicToastDisplayState state);
}