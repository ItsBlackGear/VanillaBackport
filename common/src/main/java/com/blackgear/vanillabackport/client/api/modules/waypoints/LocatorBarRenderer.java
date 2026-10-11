package com.blackgear.vanillabackport.client.api.modules.waypoints;

import com.blackgear.platform.client.event.screen.hud.HudElementRegistryImpl;
import com.blackgear.platform.client.event.screen.hud.VanillaHudElements;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint.*;
import com.blackgear.vanillabackport.common.api.modules.waypoints.Waypoint.Icon;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.util.Utilities.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.Level;

import java.util.*;

@Environment(EnvType.CLIENT)
public class LocatorBarRenderer {
    public static final LocatorBarRenderer INSTANCE = new LocatorBarRenderer();
    
    private static final ResourceLocation LOCATOR_BAR_BACKGROUND = ResourceLocation.withDefaultNamespace("hud/locator_bar_background");
    private static final ResourceLocation LOCATOR_BAR_ARROW_UP = ResourceLocation.withDefaultNamespace("hud/locator_bar_arrow_up");
    private static final ResourceLocation LOCATOR_BAR_ARROW_DOWN = ResourceLocation.withDefaultNamespace("hud/locator_bar_arrow_down");
    
    private final Minecraft minecraft = Minecraft.getInstance();
    
    public static void bootstrap() {
        HudElementRegistryImpl.attachElementBefore(
            VanillaHudElements.EXPERIENCE_LEVEL,
            ResourceLocation.withDefaultNamespace("locator_bar_background"),
            LocatorBarRenderer.INSTANCE::renderBackground
        );
        
        HudElementRegistryImpl.attachElementAfter(
            VanillaHudElements.EXPERIENCE_LEVEL,
            ResourceLocation.withDefaultNamespace("locator_bar_icons"),
            LocatorBarRenderer.INSTANCE::renderWaypoints
        );
    }
    
    public void renderBackground(GuiGraphics graphics, DeltaTracker tracker) {
        if (this.shouldSkipRendering()) return;
        if (VanillaBackport.CLIENT_CONFIG.locatorDisplayInfoBar.get() && this.shouldHideBackgroundForHud()) return;
        
        graphics.blitSprite(LOCATOR_BAR_BACKGROUND, (this.minecraft.getWindow().getGuiScaledWidth() - 182) / 2, this.minecraft.getWindow().getGuiScaledHeight() - 24 - 5, 182, 5);
    }
    
    public void renderWaypoints(GuiGraphics graphics, DeltaTracker tracker) {
        if (this.shouldSkipRendering()) return;
        
        Entity cameraEntity = this.minecraft.cameraEntity;
        if (cameraEntity == null) return;
        
        Level level = cameraEntity.level();
        
        int top = this.minecraft.getWindow().getGuiScaledHeight() - 24 - 5;
        int screenMiddle = Mth.ceil((graphics.guiWidth() - 9) / 2.0F);
        
        ClientWaypointManager.INSTANCE.forEachWaypoint(cameraEntity, waypoint -> {
            if (waypoint.id().left().map(uuid -> uuid.equals(cameraEntity.getUUID())).orElse(false)) return;
            
            double angle = waypoint.yawAngleToCamera(level, (Camera) this.minecraft.gameRenderer.getMainCamera());
            if (angle <= -60.0 || angle > 60.0) return;
            
            Icon icon = waypoint.icon();
            WaypointStyle style = WaypointStyleManager.INSTANCE.get(icon.style);
            
            float distance = Mth.sqrt((float) waypoint.distanceSquared(cameraEntity));
            ResourceLocation sprite = style.sprite(distance);
            int color = icon.color.orElseGet(() -> waypoint.id().map(
                uuid -> ColorUtils.setBrightness(ARGB32.color(255, uuid.hashCode()), 0.9F),
                name -> ColorUtils.setBrightness(ARGB32.color(255, name.hashCode()), 0.9F)
            ));
            
            int dotPosition = Mth.floor(angle * 173.0 / 2.0 / 60.0);
            int x = screenMiddle + dotPosition;
            
            boolean isFirstStyleSprite = sprite.equals(style.spriteLocations().getFirst());
            boolean renderedHead = VanillaBackport.CLIENT_CONFIG.locatorDisplayPlayerHeads.get()
                && isFirstStyleSprite
                && renderPlayerHeads(this.minecraft, graphics, x, top - 2, color, waypoint);
            
            if (!renderedHead) {
                graphics.setColor(ARGB32.red(color) / 255.0F, ARGB32.green(color) / 255.0F, ARGB32.blue(color) / 255.0F, ARGB32.alpha(color) / 255.0F);
                graphics.blit(sprite, x, top - 2, 0, 0, 9, 9, 9, 9);
                graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
            
            PitchDirection pitchDirection = waypoint.pitchDirectionToCamera(level, (Projector) this.minecraft.gameRenderer);
            if (pitchDirection != PitchDirection.NONE) {
                ResourceLocation arrowSprite = (pitchDirection == PitchDirection.DOWN) ? LOCATOR_BAR_ARROW_DOWN : LOCATOR_BAR_ARROW_UP;
                int arrowTop = (pitchDirection == PitchDirection.DOWN) ? 6 : -6;
                
                graphics.blitSprite(arrowSprite, x + 1, top + arrowTop, 7, 5);
            }
        });
    }
    
    private boolean willPrioritizeExperienceInfo() {
        return this.minecraft.player != null && ExperienceDisplay.of(this.minecraft.player).getExperienceDisplayStartTick() + 100 > this.minecraft.player.tickCount;
    }
    
    private boolean willPrioritizeJumpInfo() {
        if (this.minecraft.player == null) return false;
        return this.minecraft.player.getJumpRidingScale() > 0.0F
            || Optionull.mapOrDefault(this.minecraft.player.jumpableVehicle(), PlayerRideableJumping::getJumpCooldown, 0) > 0;
    }

    private boolean shouldHideBackgroundForHud() {
        if (this.minecraft.player == null) return false;
        if (this.minecraft.player.jumpableVehicle() != null) return true;
        return this.minecraft.gameMode != null && this.minecraft.gameMode.hasExperience();
    }
    
    private boolean shouldSkipRendering() {
        if (!VanillaBackport.CLIENT_CONFIG.enableLocatorBar.get()) return true;
        if (!ClientWaypointManager.INSTANCE.hasWaypoints()) return true;
        
        if (this.minecraft.player.jumpableVehicle() != null) return this.willPrioritizeJumpInfo();
        return this.minecraft.gameMode.hasExperience() && this.willPrioritizeExperienceInfo();
    }
    
    private static boolean renderPlayerHeads(Minecraft minecraft, GuiGraphics graphics, int x, int y, int color, TrackedWaypoint waypoint) {
        UUID uuid = waypoint.id().left().orElse(null);
        if (uuid == null || minecraft.getConnection() == null) return false;
        
        PlayerInfo info = minecraft.getConnection().getPlayerInfo(uuid);
        if (info == null) return false;
        
        int headX = x + 2;
        int headY = y + 2;
        int scale = 5;
        
        graphics.fill(headX, headY - 1, headX + scale, headY + scale + 1, color);
        graphics.fill(headX - 1, headY, headX + scale + 1, headY + scale, color);
        
        Player player = minecraft.level != null ? minecraft.level.getPlayerByUUID(uuid) : null;
        boolean isUpsideDown = player != null && PlayerRenderer.isEntityUpsideDown(player);
        boolean showHat = player == null || player.isModelPartShown(PlayerModelPart.HAT);
        
        PlayerFaceRenderer.draw(graphics, info.getSkin().texture(), headX, headY, scale, showHat, isUpsideDown);
        return true;
    }
}