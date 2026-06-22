package com.blackgear.vanillabackport.client.api.modules.music_frequency;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public enum MusicFrequency implements StringRepresentable {
    DEFAULT("DEFAULT", "options.music_frequency.default", 20),
    FREQUENT("FREQUENT", "options.music_frequency.frequent", 10),
    CONSTANT("CONSTANT", "options.music_frequency.constant", 0);
    
    public static final Codec<MusicFrequency> CODEC = StringRepresentable.fromEnum(MusicFrequency::values);
    private final String name;
    private final int maxFrequency;
    private final Component caption;
    
    MusicFrequency(String name, String translationKey, int maxFrequency) {
        this.name = name;
        this.maxFrequency = maxFrequency * 1200;
        this.caption = Component.translatable(translationKey);
    }
    
    public int getNextSongDelay(@Nullable Music music, RandomSource random) {
        if (music == null) {
            return this.maxFrequency;
        } else if (this == CONSTANT) {
            return 100;
        } else {
            int minFrequency = Math.min(music.getMinDelay(), this.maxFrequency);
            int maxFrequency = Math.min(music.getMaxDelay(), this.maxFrequency);
            return Mth.nextInt(random, minFrequency, maxFrequency);
        }
    }
    
    public Component caption() {
        return this.caption;
    }
    
    @Override
    public String getSerializedName() {
        return this.name;
    }
}