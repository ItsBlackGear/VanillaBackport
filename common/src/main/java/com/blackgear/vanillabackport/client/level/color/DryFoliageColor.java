package com.blackgear.vanillabackport.client.level.color;

public class DryFoliageColor {
    public static final int FOLIAGE_DRY_DEFAULT = -10732494;
    private static int[] pixels = new int[65536];

    public static void init(int[] colors) {
        pixels = colors;
    }

    public static int get(double temperature, double humidity) {
        return get(temperature, humidity, pixels);
    }

    static int get(double temperature, double humidity, int[] pixels) {
        humidity *= temperature;
        int tempOffset = (int) ((1.0 - temperature) * 255.0);
        int humidityOffset = (int) ((1.0 - humidity) * 255.0);
        int index = humidityOffset << 8 | tempOffset;
        return index >= pixels.length ? DryFoliageColor.FOLIAGE_DRY_DEFAULT : pixels[index];
    }
}