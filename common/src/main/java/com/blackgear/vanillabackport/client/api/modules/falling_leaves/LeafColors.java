package com.blackgear.vanillabackport.client.api.modules.falling_leaves;

import com.blackgear.vanillabackport.client.api.modules.leaf_litter.DryFoliageColor;
import com.blackgear.vanillabackport.client.api.modules.leaf_litter.DryLeafColorReloadListener;
import com.blackgear.vanillabackport.core.mixin.common.access.BiomeAccessor;
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

import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
public class LeafColors {
    public static final ColorResolver DRY_FOLIAGE_COLOR_RESOLVER = (biome, d, e) -> {
        Biome.ClimateSettings settings = ((BiomeAccessor) (Object) biome).getClimateSettings();
        double temperature = Mth.clamp(settings.temperature(), 0.0F, 1.0F);
        double humidity = Mth.clamp(settings.downfall(), 0.0F, 1.0F);
        return DryFoliageColor.get(temperature, humidity);
    };

    public static int getAverageDryFoliageColor(BlockPos pos) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return DryFoliageColor.FOLIAGE_DRY_DEFAULT;
        
        Holder<Biome> biome = level.getBiome(pos);
        OptionalInt customColor = DryLeafColorReloadListener.getColorForBiome(biome);
        if (customColor.isPresent()) return customColor.getAsInt();
        
        return new BlockTintCache(value -> level.calculateBlockTint(value, DRY_FOLIAGE_COLOR_RESOLVER)).getColor(pos);
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
}