package com.blackgear.vanillabackport.client.api.modules.music_toast;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.Toast.Visibility;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ToastInstance<T extends Toast> {
    private final ToastComponent component;
    private final T toast;
    private final int firstSlotIndex;
    private long animationStartTime;
    private long becameFullyVisibleAt;
    private Visibility visibility;
    private long fullyVisibleFor;
    private float visiblePortion;
    private boolean hasFinishedRendering;
    
    public ToastInstance(ToastComponent component, T toast, int firstSlotIndex) {
        this.component = component;
        this.toast = toast;
        this.firstSlotIndex = firstSlotIndex;
        this.resetToast();
    }
    
    public T getToast() {
        return this.toast;
    }
    
    public void resetToast() {
        this.animationStartTime = -1L;
        this.becameFullyVisibleAt = -1L;
        this.visibility = Visibility.HIDE;
        this.fullyVisibleFor = 0L;
        this.visiblePortion = 0.0F;
        this.hasFinishedRendering = false;
    }
    
    public boolean hasFinishedRendering() {
        return this.hasFinishedRendering;
    }
    
    public void calculateVisiblePortion(long now) {
        float animationProgress = Mth.clamp((float) (now - this.animationStartTime) / 600.0F, 0.0F, 1.0F);
        animationProgress *= animationProgress;
        this.visiblePortion = this.visibility == Visibility.HIDE
            ? 1.0F - animationProgress
            : animationProgress;
    }
    
    public void update() {
        long now = Util.getMillis();
        if (this.animationStartTime == -1L) {
            this.animationStartTime = now;
            this.visibility = Visibility.SHOW;
        }
        
        if (this.visibility == Visibility.SHOW && now - this.animationStartTime <= 600L) {
            this.becameFullyVisibleAt = now;
        }
        
        ToastModifier toast = ToastModifier.of(this.toast);
        this.fullyVisibleFor = now - this.becameFullyVisibleAt;
        this.calculateVisiblePortion(now);
        toast.update(this.component, this.fullyVisibleFor);
        Visibility wantedVisibility = toast.getWantedVisibility();
        if (wantedVisibility != this.visibility) {
            this.animationStartTime = now - (int) ((1.0F - this.visiblePortion) * 600.0F);
            this.visibility = wantedVisibility;
        }
        
        boolean wasAlreadyFinishedRendering = this.hasFinishedRendering;
        this.hasFinishedRendering = this.visibility == Visibility.HIDE && now - this.animationStartTime > 600L;
        if (this.hasFinishedRendering && !wasAlreadyFinishedRendering) {
            toast.onFinishedRendering();
        }
    }
    
    public void render(GuiGraphics graphics, int screenWidth) {
        if (!this.hasFinishedRendering) {
            ToastModifier toast = ToastModifier.of(this.toast);
            graphics.pose().pushPose();
            graphics.pose().translate(toast.xPos(screenWidth, this.visiblePortion), toast.yPos(this.firstSlotIndex), 800.0);
            this.toast.render(graphics, this.component, this.fullyVisibleFor);
            graphics.pose().popPose();
        }
    }
    
    public static float getFinalSoundSourceVolume(Options options, SoundSource source) {
        return source == SoundSource.MASTER ? options.getSoundSourceVolume(source) : options.getSoundSourceVolume(source) * options.getSoundSourceVolume(SoundSource.MASTER);
    }
}