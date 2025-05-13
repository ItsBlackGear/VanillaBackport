package com.blackgear.vanillabackport.core.util;

import net.minecraft.util.Mth;

import static net.minecraft.util.FastColor.ARGB32.*;

public class ColorUtils {
    public static int scaleRGB(int color, float red, float green, float blue) {
        return color(
            alpha(color),
            Mth.clamp((int) (red(color) * red), 0, 255),
            Mth.clamp((int) (green(color) * green), 0, 255),
            Mth.clamp((int) (blue(color) * blue), 0, 255)
        );
    }

    public static int colorFromFloat(float alpha, float red, float green, float blue) {
        return color(
            (int) (alpha * 255),
            (int) (red * 255),
            (int) (green * 255),
            (int) (blue * 255)
        );
    }
}