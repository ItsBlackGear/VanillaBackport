package com.blackgear.vanillabackport.client.api.color;

import com.blackgear.vanillabackport.client.resources.LeafColorReloadListener;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class LeafColors {
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
                .orElseGet(() -> new BlockTintCache(value -> level.calculateBlockTint(value, DRY_FOLIAGE_COLOR_RESOLVER)).getColor(pos));
        } else {
            return DryFoliageColor.FOLIAGE_DRY_DEFAULT;
        }
    }

    public static int getClientLeafTintColor(BlockPos pos) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return 0;

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (LeafColorReloadListener.hasCustomColor(block)) {
            return LeafColorReloadListener.getCustomColor(block);
        }

        return Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
    }

    static {
        COLOR_MAP.put(holder -> holder.is(ModBiomeTags.HAS_PALE_LEAF_LITTER), 10528412);
        COLOR_MAP.put(holder -> holder.is(ModBiomeTags.HAS_DARK_LEAF_LITTER), 8082228);
    }
}