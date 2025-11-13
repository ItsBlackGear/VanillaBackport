package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class SheepColorSpawnRules {
    private static final SheepColorSpawnConfiguration TEMPERATE_SPAWN_CONFIGURATION = new SheepColorSpawnConfiguration(
        weighted(
            builder()
                .add(single(DyeColor.BLACK), 5)
                .add(single(DyeColor.GRAY), 5)
                .add(single(DyeColor.LIGHT_GRAY), 5)
                .add(single(DyeColor.BROWN), 3)
                .add(commonColors(DyeColor.WHITE), 82)
                .build()
        )
    );
    private static final SheepColorSpawnConfiguration WARM_SPAWN_CONFIGURATION = new SheepColorSpawnConfiguration(
        weighted(
            builder()
                .add(single(DyeColor.GRAY), 5)
                .add(single(DyeColor.LIGHT_GRAY), 5)
                .add(single(DyeColor.WHITE), 5)
                .add(single(DyeColor.BLACK), 3)
                .add(commonColors(DyeColor.BROWN), 82)
                .build()
        )
    );
    private static final SheepColorSpawnConfiguration COLD_SPAWN_CONFIGURATION = new SheepColorSpawnConfiguration(
        weighted(
            builder()
                .add(single(DyeColor.LIGHT_GRAY), 5)
                .add(single(DyeColor.GRAY), 5)
                .add(single(DyeColor.WHITE), 5)
                .add(single(DyeColor.BROWN), 3)
                .add(commonColors(DyeColor.BLACK), 82)
                .build()
        )
    );

    private static SheepColorProvider commonColors(DyeColor color) {
        return weighted(builder().add(single(color), 499).add(single(DyeColor.PINK), 1).build());
    }

    public static DyeColor getRandomSheepColor(DyeColor original, Level level, BlockPos pos, RandomSource random) {
        if (!VanillaBackport.COMMON_CONFIG.hasFarmAnimalVariants.get()) return original;

        SheepColorSpawnConfiguration config = getSheepColorConfiguration(level.getBiome(pos));
        return config.colors().get(random);
    }

    private static SheepColorSpawnConfiguration getSheepColorConfiguration(Holder<Biome> biome) {
        if (biome.is(ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)) {
            return WARM_SPAWN_CONFIGURATION;
        } else {
            return biome.is(ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
                ? COLD_SPAWN_CONFIGURATION
                : TEMPERATE_SPAWN_CONFIGURATION;
        }
    }

    private static SheepColorProvider weighted(SimpleWeightedRandomList<SheepColorProvider> colors) {
        if (colors.isEmpty()) {
            throw new IllegalArgumentException("List must be non-empty");
        } else {
            return random -> colors.getRandomValue(random).orElseThrow(IllegalStateException::new).get(random);
        }
    }

    private static SheepColorProvider single(DyeColor color) {
        return random -> color;
    }

    private static SimpleWeightedRandomList.Builder<SheepColorProvider> builder() {
        return SimpleWeightedRandomList.builder();
    }

    interface SheepColorProvider {
        DyeColor get(RandomSource random);
    }

    record SheepColorSpawnConfiguration(SheepColorProvider colors) {}
}