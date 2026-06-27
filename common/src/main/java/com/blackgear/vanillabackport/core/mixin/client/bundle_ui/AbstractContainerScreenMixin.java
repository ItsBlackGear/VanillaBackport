package com.blackgear.vanillabackport.core.mixin.client.bundle_ui;

import com.blackgear.vanillabackport.client.api.modules.bundle_ui.BundleRenderHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {
    @Shadow protected int imageWidth;
    
    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }
    
    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    private void vb$renderBundleContents(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (BundleRenderHandler.renderBundleContents(guiGraphics, this.font, slot, this.imageWidth)) {
            ci.cancel();
        }
    }
}