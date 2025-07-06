package com.blackgear.vanillabackport.client.level.color;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.core.mixin.access.BiomeAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class LeafLitterColors {
    private static final Map<Predicate<Holder<Biome>>, Integer> COLOR_MAP = new ConcurrentHashMap<>();

    public static final ColorResolver DRY_FOLIAGE_COLOR_RESOLVER = (biome, d, e) -> {
        Biome.ClimateSettings settings = ((BiomeAccessor)(Object)biome).getClimateSettings();
        double temperature = Mth.clamp(settings.temperature(), 0.0F, 1.0F);
        double humidity = Mth.clamp(settings.downfall(), 0.0F, 1.0F);
        return DryFoliageColor.get(temperature, humidity);
    };

    public static int getAverageDryFoliageColor(BlockPos pos) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            Holder<Biome> biome = level.getBiome(pos);

            return COLOR_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().test(biome))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseGet(() -> new BlockTintCache(value -> level.calculateBlockTint(value, LeafLitterColors.DRY_FOLIAGE_COLOR_RESOLVER)).getColor(pos));
        } else {
            return DryFoliageColor.FOLIAGE_DRY_DEFAULT;
        }
    }

    static {
        COLOR_MAP.put(holder -> holder.is(ModBiomes.PALE_GARDEN), 10528412);
        COLOR_MAP.put(holder -> holder.is(Biomes.DARK_FOREST), 8082228);
        COLOR_MAP.put(holder -> holder.is(Biomes.SWAMP), 8082228);
        COLOR_MAP.put(holder -> holder.is(Biomes.MANGROVE_SWAMP), 8082228);
    }
}