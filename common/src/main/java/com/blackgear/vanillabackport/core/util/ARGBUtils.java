package com.blackgear.vanillabackport.core.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
// Can be removed. - Echo2craft.
// FastColor.ARGB32 alternative, with additional methods. - Echo2craft.
public class ARGBUtils {
    public static int alpha(int pColor) {
        return pColor >>> 24;
    }

    public static int red(int pColor) {
        return pColor >> 16 & 255;
    }


    public static int green(int pColor) {
        return pColor >> 8 & 255;
    }

    public static int blue(int pColor) {
        return pColor & 255;
    }

    public static int color(int pAlpha, int pRed, int pGreen, int pBlue) {
        return pAlpha << 24 | pRed << 16 | pGreen << 8 | pBlue;
    }

    public static int color(int pRed, int pGreen, int pBlue) {
        return color(255, pRed, pGreen, pBlue);
    }

    public static int color(Vec3 pColor) {
        return color(as8BitChannel((float)pColor.x()), as8BitChannel((float)pColor.y()), as8BitChannel((float)pColor.z()));
    }

    public static int multiply(int pColor1, int pColor2) {
        if (pColor1 == -1) {
            return pColor2;
        } else {
            return pColor2 == -1 ? pColor1 : color(alpha(pColor1) * alpha(pColor2) / 255, red(pColor1) * red(pColor2) / 255, green(pColor1) * green(pColor2) / 255, blue(pColor1) * blue(pColor2) / 255);
        }
    }

    public static int scaleRGB(int pColor, float pScale) {
        return scaleRGB(pColor, pScale, pScale, pScale);
    }

    public static int scaleRGB(int pColor, float pRedScale, float pGreenScale, float pBlueScale) {
        return color(alpha(pColor), clamp((long)((int)((float)red(pColor) * pRedScale)), 0, 255), clamp((long)((int)((float)green(pColor) * pGreenScale)), 0, 255), clamp((long)((int)((float)blue(pColor) * pBlueScale)), 0, 255));
    }

    public static int scaleRGB(int pColor, int pScale) {
        return color(alpha(pColor), clamp((long)red(pColor) * (long)pScale / 255L, 0, 255), clamp((long)green(pColor) * (long)pScale / 255L, 0, 255), clamp((long)blue(pColor) * (long)pScale / 255L, 0, 255));
    }

    public static int greyscale(int pColor) {
        int $$1 = (int)((float)red(pColor) * 0.3F + (float)green(pColor) * 0.59F + (float)blue(pColor) * 0.11F);
        return color($$1, $$1, $$1);
    }

    public static int lerp(float pDelta, int pColor1, int pColor2) {
        int $$3 = Mth.lerpInt(pDelta, alpha(pColor1), alpha(pColor2));
        int $$4 = Mth.lerpInt(pDelta, red(pColor1), red(pColor2));
        int $$5 = Mth.lerpInt(pDelta, green(pColor1), green(pColor2));
        int $$6 = Mth.lerpInt(pDelta, blue(pColor1), blue(pColor2));
        return color($$3, $$4, $$5, $$6);
    }

    public static int opaque(int pColor) {
        return pColor | -16777216;
    }

    public static int transparent(int pColor) {
        return pColor & 16777215;
    }

    public static int color(int pAlpha, int pColor) {
        return pAlpha << 24 | pColor & 16777215;
    }

    public static int white(float pAlpha) {
        return as8BitChannel(pAlpha) << 24 | 16777215;
    }

    public static int colorFromFloat(float pAlpha, float pRed, float pGreen, float pBlue) {
        return color(as8BitChannel(pAlpha), as8BitChannel(pRed), as8BitChannel(pGreen), as8BitChannel(pBlue));
    }

    public static Vector3f vector3fFromRGB24(int pColor) {
        float $$1 = (float)red(pColor) / 255.0F;
        float $$2 = (float)green(pColor) / 255.0F;
        float $$3 = (float)blue(pColor) / 255.0F;
        return new Vector3f($$1, $$2, $$3);
    }

    public static int average(int pColor1, int pColor2) {
        return color((alpha(pColor1) + alpha(pColor2)) / 2, (red(pColor1) + red(pColor2)) / 2, (green(pColor1) + green(pColor2)) / 2, (blue(pColor1) + blue(pColor2)) / 2);
    }

    public static int as8BitChannel(float pValue) {
        return Mth.floor(pValue * 255.0F);
    }

    public static float alphaFloat(int pColor) {
        return from8BitChannel(alpha(pColor));
    }

    public static float redFloat(int pColor) {
        return from8BitChannel(red(pColor));
    }

    public static float greenFloat(int pColor) {
        return from8BitChannel(green(pColor));
    }

    public static float blueFloat(int pColor) {
        return from8BitChannel(blue(pColor));
    }

    private static float from8BitChannel(int pValue) {
        return (float)pValue / 255.0F;
    }

    public static int toABGR(int pColor) {
        return pColor & -16711936 | (pColor & 16711680) >> 16 | (pColor & 255) << 16;
    }

    public static int fromABGR(int pColor) {
        return toABGR(pColor);
    }

    public static int clamp(long value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(min + " > " + max);
        }
        return (int) Math.min(max, Math.max(value, min));
    }
}
