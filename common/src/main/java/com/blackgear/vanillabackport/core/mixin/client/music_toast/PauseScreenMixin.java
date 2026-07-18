package com.blackgear.vanillabackport.core.mixin.client.music_toast;

import com.blackgear.vanillabackport.client.api.modules.music_toast.NowPlayingToast;
import com.blackgear.vanillabackport.client.api.modules.music_toast.ToastInstance;
import com.blackgear.vanillabackport.client.api.modules.options.OptionsAccess;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    @Shadow @Final private boolean showPauseMenu;
    
    protected PauseScreenMixin(Component title) {
        super(title);
    }
    
    @Inject(method = "render", at = @At(value = "TAIL"))
    private void vb$renderToast(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.rendersNowPlayingToast()) {
            NowPlayingToast.tickMusicNotes();
            NowPlayingToast.renderToast(graphics, this.font);
        }
    }
    
    @Unique
    private boolean rendersNowPlayingToast() {
        Options options = this.minecraft.options;
        if (!(options instanceof OptionsAccess access)) return false;
        
        return access.musicToast().get().renderInPauseScreen() && ToastInstance.getFinalSoundSourceVolume(options, SoundSource.MUSIC) > 0.0F && this.showPauseMenu;
    }
}