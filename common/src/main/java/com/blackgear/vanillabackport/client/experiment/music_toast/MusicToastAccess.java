package com.blackgear.vanillabackport.client.experiment.music_toast;

public interface MusicToastAccess {
    void showNowPlayingToast();
    
    void hideNowPlayingToast();
    
    void initializeMusicToast(MusicToastDisplayState state);
    
    void setMusicToastDisplayState(MusicToastDisplayState state);
}