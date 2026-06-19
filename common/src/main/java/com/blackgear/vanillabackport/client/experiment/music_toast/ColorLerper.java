package com.blackgear.vanillabackport.client.experiment.music_toast;

import com.blackgear.vanillabackport.core.util.Utilities.*;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class ColorLerper {
    public static final DyeColor[] MUSIC_NOTE_COLORS = new DyeColor[]{
        DyeColor.WHITE,
        DyeColor.LIGHT_GRAY,
        DyeColor.LIGHT_BLUE,
        DyeColor.BLUE,
        DyeColor.CYAN,
        DyeColor.GREEN,
        DyeColor.LIME,
        DyeColor.YELLOW,
        DyeColor.ORANGE,
        DyeColor.PINK,
        DyeColor.RED,
        DyeColor.MAGENTA
    };
    
    public static int getLerpedColor(final Type type, final float tick) {
        int tickCount = Mth.floor(tick);
        int value = tickCount / type.colorDuration;
        int colorCount = type.colors.length;
        int c1 = value % colorCount;
        int c2 = (value + 1) % colorCount;
        float subStep = (tickCount % type.colorDuration + Mth.frac(tick)) / type.colorDuration;
        int color1 = type.getColor(type.colors[c1]);
        int color2 = type.getColor(type.colors[c2]);
        return ColorUtils.srgbLerp(subStep, color1, color2);
    }
    
    public static int getModifiedColor(DyeColor dyeColor, float brightness) {
        if (dyeColor == DyeColor.WHITE) {
            return -1644826; // El entero por defecto de Mojang para el blanco modificado
        } else {
            // 1.20.1 usa un float[] normalizado de 0.0F a 1.0F
            float[] afloat = dyeColor.getTextureDiffuseColors();
            
            // Escalamos a bytes (0-255) aplicando la multiplicación del brillo
            int r = Mth.clamp(Mth.floor(afloat[0] * brightness * 255.0F), 0, 255);
            int g = Mth.clamp(Mth.floor(afloat[1] * brightness * 255.0F), 0, 255);
            int b = Mth.clamp(Mth.floor(afloat[2] * brightness * 255.0F), 0, 255);
            
            // Empaquetamos en formato ARGB con Alpha opaco (255)
            return (255 << 24) | (r << 16) | (g << 8) | b;
        }
    }
    
    @Environment(EnvType.CLIENT)
    public enum Type {
        MUSIC_NOTE(30, ColorLerper.MUSIC_NOTE_COLORS, 1.25F);
        
        private final int colorDuration;
        private final Map<DyeColor, Integer> colorByDye;
        private final DyeColor[] colors;
        
        Type(final int colorDuration, final DyeColor[] colors, final float brightness) {
            this.colorDuration = colorDuration;
            this.colorByDye = Maps.newHashMap(
                Arrays.stream(colors)
                    .collect(Collectors.toMap(d -> d, color -> ColorLerper.getModifiedColor(color, brightness)))
            );
            this.colors = colors;
        }
        
        public final int getColor(final DyeColor dyeColor) {
            return this.colorByDye.get(dyeColor);
        }
    }
}