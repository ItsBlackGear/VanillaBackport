package com.blackgear.vanillabackport.client.api.modules.leaf_litter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DryFoliageColor {
    public static final int FOLIAGE_DRY_DEFAULT = -10732494;
    private static int[] pixels = new int[65536];

    public static void init(int[] colors) {
        pixels = colors;
    }

    public static int get(double temperature, double humidity) {
        return get(temperature, humidity, pixels);
    }

    static int get(double temperature, double rain, int[] pixels) {
        rain *= temperature;
        int x = (int) ((1.0 - temperature) * 255.0);
        int y = (int) ((1.0 - rain) * 255.0);
        int index = y << 8 | x;
        return index >= pixels.length ? DryFoliageColor.FOLIAGE_DRY_DEFAULT : pixels[index];
    }
}