package com.blackgear.vanillabackport.core.util;

import net.minecraft.util.Mth;

public class MathUtils {
    public static float wrapDegrees90(float angle) {
        float normalizedAngle = angle % 90.0F;
        if (normalizedAngle >= 45.0F) {
            normalizedAngle -= 90.0F;
        }

        if (normalizedAngle < -45.0F) {
            normalizedAngle += 90.0F;
        }

        return normalizedAngle;
    }

    public static byte packDegrees(float angle) {
        return (byte) Mth.floor(angle * 256.0F / 360.0F);
    }

    public static float unpackDegrees(byte rot) {
        return rot * 360 / 256.0F;
    }
}