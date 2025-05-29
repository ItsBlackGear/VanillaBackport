package com.blackgear.vanillabackport.common.level.entities;

import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum AnimalVariant {
    DEFAULT("default", null),
    COLD("cold", ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS),
    WARM("warm", ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);

    private final String name;
    private final @Nullable TagKey<Biome> tag;

    AnimalVariant(String name, @Nullable TagKey<Biome> tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public static AnimalVariant getByName(String name) {
        for (AnimalVariant variant : values()) {
            if (variant.name.equals(name)) {
                return variant;
            }
        }

        return DEFAULT;
    }

    public static <T extends LivingEntity & AnimalVariantHolder> void selectVariantToSpawn(ServerLevelAccessor level, T entity, MobSpawnType spawnType) {
        if (spawnType != MobSpawnType.BREEDING && spawnType != MobSpawnType.COMMAND) {
            Holder<Biome> biome = level.getBiome(entity.blockPosition());

            AnimalVariant variant = Arrays.stream(AnimalVariant.values())
                .filter(value -> value.tag != null && biome.is(value.tag))
                .findFirst()
                .orElse(DEFAULT);

            entity.setVariant(variant);
        }
    }
}