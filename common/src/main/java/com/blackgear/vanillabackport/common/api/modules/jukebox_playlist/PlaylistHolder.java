package com.blackgear.vanillabackport.common.api.modules.jukebox_playlist;

public interface PlaylistHolder {
    boolean vb$shouldLoop();
    
    void vb$tryToAdvancePlaylist(boolean replayCurrentSongOtherwise);
}