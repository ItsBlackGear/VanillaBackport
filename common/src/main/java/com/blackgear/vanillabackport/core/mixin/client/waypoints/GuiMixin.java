package com.blackgear.vanillabackport.core.mixin.client.waypoints;

import com.blackgear.vanillabackport.client.api.modules.waypoints.LocatorBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow @Final private Minecraft minecraft;

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

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;)V"
        )
    )
    private void renderBackgroundFallback(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        boolean isMounted = this.minecraft.player != null && this.minecraft.player.jumpableVehicle() != null;
        boolean hasExp = this.minecraft.gameMode != null && this.minecraft.gameMode.hasExperience();

        if (!isMounted && !hasExp) {
            LocatorBarRenderer.INSTANCE.renderBackground(guiGraphics);
        }
    }
}