package com.blackgear.vanillabackport.core.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class VecUtils {
    public static Vec3 horizontal(Vec3 source) {
        return new Vec3(source.x, 0.0, source.z);
    }

    public static Vec2 rotate(Vec2 source, float angleRadians) {
        float cosine = Mth.cos(angleRadians);
        float sine = Mth.sin(angleRadians);
        return new Vec2(source.x * cosine - source.y * sine, source.y * cosine + source.x * sine);
    }
}