package com.blackgear.vanillabackport.core.mixin.client.music_toast;

import com.blackgear.vanillabackport.client.experiment.music_toast.NowPlayingToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ToastComponent.ToastInstance.class, priority = 500)
public abstract class ToastInstanceMixin {
    @Shadow public abstract Toast getToast();
    
    @Redirect(
        method = "render(ILnet/minecraft/client/gui/GuiGraphics;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/toasts/Toast$Visibility;playSound(Lnet/minecraft/client/sounds/SoundManager;)V"
        )
    )
    private void vb$redirectPlaySound(Toast.Visibility visibility, SoundManager soundManager) {
        if (this.getToast() instanceof NowPlayingToast) return;
        visibility.playSound(soundManager);
    }
}