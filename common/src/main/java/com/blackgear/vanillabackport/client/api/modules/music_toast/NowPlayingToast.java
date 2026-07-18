package com.blackgear.vanillabackport.client.api.modules.music_toast;

import com.blackgear.vanillabackport.core.mixin.client.access.MusicManagerAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class NowPlayingToast implements Toast, ToastModifier {
    private static final ResourceLocation NOW_PLAYING_BACKGROUND_SPRITE = new ResourceLocation("textures/gui/toast/now_playing.png");
    private static final ResourceLocation MUSIC_NOTES_SPRITE = new ResourceLocation("textures/gui/icon/music_notes.png");
    private static final int TEXT_COLOR = DyeColor.LIGHT_GRAY.getTextColor();
    
    private static int musicNoteColorTick;
    private static long lastMusicNoteColorChange;
    private static int musicNoteColor = -1;
    
    private boolean updateToast;
    private double notificationDisplayTimeMultiplier;
    private final Minecraft minecraft;
    private Visibility wantedVisibility = Visibility.HIDE;
    
    public NowPlayingToast() {
        this.minecraft = Minecraft.getInstance();
    }
    
    public static void renderToast(GuiGraphics graphics, Font font) {
        ResourceLocation currentSong = getCurrentSongName();
        if (currentSong == null) return;
        
        graphics.pose().pushPose();
        graphics.blitNineSliced(NOW_PLAYING_BACKGROUND_SPRITE, 0, 0, getWidth(currentSong, font), 30, 4, 160, 32, 0, 0);
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        float r = ((musicNoteColor >> 16) & 0xFF) / 255.0F;
        float g = ((musicNoteColor >> 8) & 0xFF) / 255.0F;
        float b = (musicNoteColor & 0xFF) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        
        int frameIndex = (musicNoteColorTick / 2) % 8;
        int offset = frameIndex * 16;
        
        graphics.blit(MUSIC_NOTES_SPRITE, 7, 7, 0, 0, offset, 16, 16, 16, 128);
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        
        graphics.drawString(font, getNowPlayingString(currentSong), 30, 15 - 9 / 2, TEXT_COLOR | 0xFF000000, false);
        graphics.pose().popPose();
    }
    
    @Nullable
    private static ResourceLocation getCurrentSongName() {
        MusicManagerAccessor accessor = (MusicManagerAccessor) Minecraft.getInstance().getMusicManager();
        if (accessor.getCurrentMusic() != null) {
            Sound sound = accessor.getCurrentMusic().getSound();
            if (sound != null) return sound.getLocation();
        }
        return null;
    }
    
    public static void tickMusicNotes() {
        if (getCurrentSongName() != null) {
            long now = System.currentTimeMillis();
            if (now > lastMusicNoteColorChange + 25L) {
                musicNoteColorTick++;
                lastMusicNoteColorChange = now;
                musicNoteColor = ColorLerper.getLerpedColor(ColorLerper.Type.MUSIC_NOTE, musicNoteColorTick);
            }
        }
    }
    
    private static Component getNowPlayingString(@Nullable ResourceLocation location) {
        if (location == null) return Component.empty();
        String path = location.getPath();
        String namespace = location.getNamespace();
        String translationKey = path.replace("/", ".");
        return Component.translatableWithFallback(translationKey, formatFallbackName(namespace, path));
    }
    
    private static String formatFallbackName(String namespace, String path) {
        String songNameRaw = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;
        return capitalizeString(namespace) + " - " + capitalizeString(songNameRaw);
    }
    
    private static String capitalizeString(String input) {
        if (input == null || input.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char ch : input.toCharArray()) {
            if (ch == '_') {
                result.append(' ');
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(ch));
                capitalizeNext = false;
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
    
    public void showToast(Options options) {
        this.updateToast = true;
        this.notificationDisplayTimeMultiplier = options.notificationDisplayTime().get();
        this.setWantedVisibility(Visibility.SHOW);
    }
    
    @Override
    public void update(ToastComponent component, long fullyVisibleForMs) {
        if (this.updateToast) {
            this.wantedVisibility = fullyVisibleForMs < 5000.0 * this.notificationDisplayTimeMultiplier ? Visibility.SHOW : Visibility.HIDE;
            tickMusicNotes();
        }
    }
    
    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent component, long timeSinceLastVisible) {
        renderToast(graphics, component.getMinecraft().font);
        return this.wantedVisibility;
    }
    
    @Override
    public void onFinishedRendering() {
        this.updateToast = false;
    }
    
    @Override
    public int width() {
        return getWidth(getCurrentSongName(), this.minecraft.font);
    }
    
    private static int getWidth(@Nullable ResourceLocation currentSong, Font font) {
        return 30 + font.width(getNowPlayingString(currentSong)) + 7;
    }
    
    @Override
    public int height() {
        return 30;
    }
    
    @Override
    public float xPos(int screenWidth, float visiblePortion) {
        return this.width() * visiblePortion - this.width();
    }
    
    @Override
    public Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }
    
    public void setWantedVisibility(Visibility visibility) {
        this.wantedVisibility = visibility;
    }
}