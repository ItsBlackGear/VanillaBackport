package com.blackgear.vanillabackport.client.experiment.music_toast;

import com.blackgear.vanillabackport.core.mixin.access.MusicManagerAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

//TODO: FLICKERING MAY STILL OCURR
public class NowPlayingToast implements Toast {
    private static final ResourceLocation NOW_PLAYING_BACKGROUND_SPRITE = new ResourceLocation("textures/gui/toast/now_playing.png");
    private static final ResourceLocation MUSIC_NOTES_SPRITE = new ResourceLocation("textures/gui/icon/music_notes.png");
    private static final int TEXT_COLOR = DyeColor.LIGHT_GRAY.getTextColor();
    
    private static final long BASE_DISPLAY_DURATION = 5000L;
    private static final float ANIMATION_DURATION = 600.0F;
    private static final int TOAST_HEIGHT = 30;
    
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
    
    public void showToast(Options options) {
        this.updateToast = true;
        this.notificationDisplayTimeMultiplier = options.notificationDisplayTime().get();
        this.setWantedVisibility(Visibility.SHOW);
    }
    
    public void setWantedVisibility(Visibility visibility) {
        this.wantedVisibility = visibility;
    }
    
    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent toastComponent, long timeSinceLastVisible) {
        long maxDisplayTime = (long) (BASE_DISPLAY_DURATION * this.notificationDisplayTimeMultiplier);
        boolean isStaticPause = (timeSinceLastVisible == 600L);
        
        if (!isStaticPause && this.minecraft.screen instanceof PauseScreen) {
            return Toast.Visibility.HIDE;
        }
        
        if (timeSinceLastVisible == 0L) {
            this.wantedVisibility = Toast.Visibility.SHOW;
        } else if (isStaticPause) {
            this.wantedVisibility = Toast.Visibility.SHOW;
            tickMusicNotes();
        } else if (this.updateToast) {
            this.wantedVisibility = (double) timeSinceLastVisible < maxDisplayTime ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
            tickMusicNotes();
        }
        
        ResourceLocation currentSong = getCurrentSongName();
        if (currentSong == null) {
            return Toast.Visibility.HIDE;
        }
        
        Font font = toastComponent.getMinecraft().font;
        int toastWidth = this.width();
        int screenWidth = graphics.guiWidth();
        
        float visiblePortion = getVisiblePortion(timeSinceLastVisible, isStaticPause, maxDisplayTime);
        
        graphics.pose().pushPose();
        
        if (isStaticPause) {
            graphics.pose().translate(0.0F, 0.0F, 0.0F);
        } else {
            float originalX = (float) screenWidth - (float) toastWidth * visiblePortion;
            float targetLeftX = ((float) toastWidth * visiblePortion - (float) toastWidth);
            graphics.pose().translate(-originalX + targetLeftX, 0.0F, 0.0F);
        }
        
        graphics.blitNineSliced(
            NOW_PLAYING_BACKGROUND_SPRITE,
            0, 0, toastWidth, TOAST_HEIGHT,
            4, 160, 32, 0, 0
        );
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        float r = ((musicNoteColor >> 16) & 0xFF) / 255.0F;
        float g = ((musicNoteColor >> 8) & 0xFF) / 255.0F;
        float b = (musicNoteColor & 0xFF) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        
        int totalFrames = 8;
        int frameIndex = (musicNoteColorTick / 2) % totalFrames;
        int vOffset = frameIndex * 16;
        
        graphics.blit(
            MUSIC_NOTES_SPRITE,
            7, 7,
            0,
            0.0F, (float)vOffset,
            16, 16,
            16, 128
        );
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        
        graphics.drawString(font, getNowPlayingString(currentSong), 30, 15 - 9 / 2, TEXT_COLOR | 0xFF000000, false);
        graphics.pose().popPose();
        
        return this.wantedVisibility;
    }
    
    private float getVisiblePortion(long timeSinceLastVisible, boolean isStaticPause, long maxDisplayTime) {
        if (isStaticPause) {
            return 1.0F;
        } else if (this.wantedVisibility == Visibility.SHOW) {
            float progress = Mth.clamp((float) timeSinceLastVisible / ANIMATION_DURATION, 0.0F, 1.0F);
            return progress * progress;
        } else {
            long exitTime = timeSinceLastVisible - maxDisplayTime;
            float progress = Mth.clamp((float) exitTime / ANIMATION_DURATION, 0.0F, 1.0F);
            return 1.0F - (progress * progress);
        }
    }
    
    @Nullable
    private static ResourceLocation getCurrentSongName() {
        MusicManagerAccessor accessor = (MusicManagerAccessor) Minecraft.getInstance().getMusicManager();
        if (accessor.getCurrentMusic() != null) {
            Sound sound = accessor.getCurrentMusic().getSound();
            return sound.getLocation();
        }
        return null;
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
    
    private static int getWidth(@Nullable ResourceLocation currentSong, Font font) {
        return 30 + font.width(getNowPlayingString(currentSong)) + 7;
    }
    
    @Override
    public int width() {
        return getWidth(getCurrentSongName(), this.minecraft.font);
    }
    
    @Override
    public int height() {
        return TOAST_HEIGHT;
    }
}