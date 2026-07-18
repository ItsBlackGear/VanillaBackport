package com.blackgear.vanillabackport.core.mixin.client.music_toast;

import com.blackgear.vanillabackport.client.api.modules.music_toast.MusicToastAccess;
import com.blackgear.vanillabackport.client.api.modules.music_toast.MusicToastDisplayState;
import com.blackgear.vanillabackport.client.api.modules.music_toast.NowPlayingToast;
import com.blackgear.vanillabackport.client.api.modules.music_toast.ToastInstance;
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
    
    @Unique private ToastInstance<NowPlayingToast> nowPlayingToast;
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderTail(GuiGraphics graphics, CallbackInfo ci) {
        if (this.minecraft.options.hideGui) return;
        
        if (this.minecraft.options instanceof OptionsAccess access && access.musicToast() != null) {
            MusicToastDisplayState state = access.musicToast().get();
            
            if (this.nowPlayingToast == null && state != MusicToastDisplayState.NEVER) {
                this.initializeMusicToast(state);
            }
            
            if (this.nowPlayingToast != null) {
                this.nowPlayingToast.update();
                
                if (state.renderToast() && (this.minecraft.screen == null || !(this.minecraft.screen instanceof PauseScreen))) {
                    this.nowPlayingToast.render(graphics, graphics.guiWidth());
                }
            }
        }
    }
    
    @Override
    public void showNowPlayingToast() {
        if (this.nowPlayingToast == null && this.minecraft.options instanceof OptionsAccess access && access.musicToast() != null) {
            this.initializeMusicToast(access.musicToast().get());
        }
        
        if (this.nowPlayingToast != null) {
            this.nowPlayingToast.resetToast();
            this.nowPlayingToast.getToast().showToast(this.minecraft.options);
        }
    }
    
    @Override
    public void hideNowPlayingToast() {
        if (this.nowPlayingToast != null) {
            this.nowPlayingToast.getToast().setWantedVisibility(Toast.Visibility.HIDE);
        }
    }
    
    @Override
    public void initializeMusicToast(MusicToastDisplayState state) {
        if (this.nowPlayingToast != null) return;
        
        ToastComponent component = (ToastComponent) (Object) this;
        switch (state) {
            case PAUSE:
            case PAUSE_AND_TOAST:
                this.nowPlayingToast = new ToastInstance<>(component, new NowPlayingToast(), 0);
                break;
        }
    }
    
    @Override
    public void setMusicToastDisplayState(MusicToastDisplayState state) {
        ToastComponent component = (ToastComponent) (Object) this;
        switch (state) {
            case PAUSE:
                this.nowPlayingToast = new ToastInstance<>(component, new NowPlayingToast(), 0);
                break;
            case PAUSE_AND_TOAST:
                this.nowPlayingToast = new ToastInstance<>(component, new NowPlayingToast(), 0);
                if (ToastInstance.getFinalSoundSourceVolume(this.minecraft.options, SoundSource.MUSIC) > 0.0F) {
                    this.nowPlayingToast .getToast().showToast(this.minecraft.options);
                }
                break;
            case NEVER:
                this.nowPlayingToast = null;
        }
    }
}