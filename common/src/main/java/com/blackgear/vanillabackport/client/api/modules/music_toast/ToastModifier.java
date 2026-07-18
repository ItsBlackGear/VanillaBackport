package com.blackgear.vanillabackport.client.api.modules.music_toast;

import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;

public interface ToastModifier {
    ToastModifier DEFAULT = new ToastModifier() {};
    
    static ToastModifier of(Toast toast) {
        return toast instanceof ToastModifier modifier ? modifier : DEFAULT;
    }
    
    default Toast.Visibility getWantedVisibility() {
        return Toast.Visibility.HIDE;
    }
    
    default void update(ToastComponent component, long fullyVisibleForMs) { /* NO-OP */ }
    
    default float xPos(int screenWidth, float visiblePortion) {
        return 0.0F;
    }
    
    default float yPos(int firstSlotIndex) {
        return 0.0F;
    }
    
    default void onFinishedRendering() { /* NO-OP */ }
}