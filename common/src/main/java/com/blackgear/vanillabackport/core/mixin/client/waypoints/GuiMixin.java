package com.blackgear.vanillabackport.core.mixin.client.waypoints;

import com.blackgear.vanillabackport.client.api.modules.waypoints.LocatorBarRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(
        method = "renderExperienceBar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V",
            ordinal = 0
        )
    )
    private void renderBackgroundOnExpBar(GuiGraphics guiGraphics, int x, CallbackInfo ci) {
        LocatorBarRenderer.INSTANCE.renderBackground(guiGraphics);
    }

    @Inject(
        method = "renderJumpMeter",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"
        )
    )
    private void renderBackgroundOnJumpBar(PlayerRideableJumping rideable, GuiGraphics guiGraphics, int x, CallbackInfo ci) {
        LocatorBarRenderer.INSTANCE.renderBackground(guiGraphics);
    }
}