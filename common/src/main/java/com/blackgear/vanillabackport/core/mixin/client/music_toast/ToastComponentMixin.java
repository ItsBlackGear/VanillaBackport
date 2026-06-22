package com.blackgear.vanillabackport.core.mixin.client.music_toast;

import com.blackgear.vanillabackport.client.api.modules.music_toast.MusicToastAccess;
import com.blackgear.vanillabackport.client.api.modules.music_toast.MusicToastDisplayState;
import com.blackgear.vanillabackport.client.api.modules.music_toast.NowPlayingToast;
import com.blackgear.vanillabackport.client.api.modules.options.OptionsAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastComponent.class)
public abstract class ToastComponentMixin implements MusicToastAccess {
    @Shadow @Final Minecraft minecraft;
    
    @Unique private NowPlayingToast vb$nowPlayingToast;
    @Unique private ToastComponent.ToastInstance<NowPlayingToast> vb$nowPlayingToastInstance;
    @Unique private boolean vb$inGameToastShowed = false;
    @Unique private NowPlayingToast vb$pauseToast;
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderTail(GuiGraphics graphics, CallbackInfo ci) {
        if (this.minecraft.options.hideGui) return;
        
        if (this.minecraft.screen instanceof PauseScreen) {
            if (this.vb$nowPlayingToastInstance != null) {
                this.vb$nowPlayingToastInstance = null;
            }
            
            if (this.vb$nowPlayingToast != null) {
                this.vb$inGameToastShowed = true;
            }
            
            if (this.minecraft.options instanceof OptionsAccess access && access.musicToast() != null) {
                MusicToastDisplayState state = access.musicToast().get();
                
                if (state == MusicToastDisplayState.PAUSE || state == MusicToastDisplayState.PAUSE_AND_TOAST) {
                    if (this.vb$pauseToast == null) {
                        this.vb$pauseToast = new NowPlayingToast();
                    }
                    
                    graphics.pose().pushPose();
                    graphics.pose().translate(0.0F, 0.0F, 800.0F);
                    
                    this.vb$pauseToast.render(graphics, (ToastComponent)(Object)this, 600L);
                    graphics.pose().popPose();
                }
            }
        } else {
            if (this.minecraft.options instanceof OptionsAccess access && access.musicToast() != null) {
                if (access.musicToast().get() == MusicToastDisplayState.PAUSE_AND_TOAST) {
                    if (this.vb$nowPlayingToast != null && this.vb$nowPlayingToastInstance == null && !this.vb$inGameToastShowed) {
                        this.vb$nowPlayingToastInstance = ((ToastComponent) (Object) this).new ToastInstance<>(this.vb$nowPlayingToast, 0, 1);
                        this.vb$inGameToastShowed = true;
                    }
                }
            }
            
            if (this.vb$nowPlayingToastInstance != null) {
                boolean finished = this.vb$nowPlayingToastInstance.render(graphics.guiWidth(), graphics);
                if (finished) {
                    this.vb$nowPlayingToastInstance = null;
                }
            }
        }
    }
    
    @Inject(method = "clear", at = @At("HEAD"))
    private void onClear(CallbackInfo ci) {
        this.vb$nowPlayingToast = null;
        this.vb$nowPlayingToastInstance = null;
        this.vb$pauseToast = null;
        this.vb$inGameToastShowed = false;
    }
    
    @Override
    public void showNowPlayingToast() {
        if (this.vb$nowPlayingToast == null) {
            this.vb$nowPlayingToast = new NowPlayingToast();
        }
        
        this.vb$nowPlayingToast.showToast(this.minecraft.options);
        this.vb$inGameToastShowed = false;
        
        if (this.minecraft.options instanceof OptionsAccess access && access.musicToast() != null) {
            MusicToastDisplayState state = access.musicToast().get();
            if (state == MusicToastDisplayState.PAUSE_AND_TOAST) {
                float musicVolume = this.minecraft.options.getSoundSourceVolume(SoundSource.MUSIC);
                float masterVolume = this.minecraft.options.getSoundSourceVolume(SoundSource.MASTER);
                
                if (musicVolume * masterVolume > 0.0F) {
                    if (!(this.minecraft.screen instanceof PauseScreen)) {
                        this.vb$nowPlayingToastInstance = ((ToastComponent) (Object) this).new ToastInstance<>(this.vb$nowPlayingToast, 0, 1);
                    }
                    
                    this.vb$inGameToastShowed = true;
                }
            }
        }
    }
    
    @Override
    public void hideNowPlayingToast() {
        if (this.vb$nowPlayingToast != null) {
            this.vb$nowPlayingToast.setWantedVisibility(Toast.Visibility.HIDE);
        }
    }
    
    @Override
    public void initializeMusicToast(MusicToastDisplayState state) {
        if (this.vb$nowPlayingToast == null) {
            this.vb$nowPlayingToast = new NowPlayingToast();
        }
    }
    
    @Override
    public void setMusicToastDisplayState(MusicToastDisplayState state) {
        this.initializeMusicToast(state);
        if (this.vb$nowPlayingToast == null) return;
        
        switch (state) {
            case PAUSE:
                this.vb$nowPlayingToastInstance = null;
                break;
            case PAUSE_AND_TOAST:
                if (this.minecraft.options.getSoundSourceVolume(SoundSource.MUSIC) * this.minecraft.options.getSoundSourceVolume(SoundSource.MASTER) > 0.0F) {
                    this.vb$nowPlayingToast.showToast(this.minecraft.options);
                    if (!(this.minecraft.screen instanceof PauseScreen)) {
                        this.vb$nowPlayingToastInstance = ((ToastComponent) (Object) this).new ToastInstance<>(this.vb$nowPlayingToast, 0, 1);
                    }
                    
                    this.vb$inGameToastShowed = true;
                }
                break;
            case NEVER:
                this.vb$nowPlayingToastInstance = null;
                this.vb$nowPlayingToast = null;
                this.vb$pauseToast = null;
                this.vb$inGameToastShowed = false;
                break;
        }
    }
}