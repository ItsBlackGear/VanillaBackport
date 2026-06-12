package com.blackgear.vanillabackport.common.api.variants.spawn;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.api.variants.spawn.check.BiomeCheck;
import com.blackgear.vanillabackport.common.api.variants.spawn.check.MoonBrightnessCheck;
import com.blackgear.vanillabackport.common.api.variants.spawn.check.StructureCheck;
import com.blackgear.vanillabackport.common.api.variants.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.variants.spawn.check.raw.RawStructureCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public class SpawnConditions {
    public static final CoreRegistry<MapCodec<? extends SpawnCondition>> CONDITIONS = CoreRegistry.create(ModRegistries.SPAWN_CONDITION_TYPE.get(), VanillaBackport.NAMESPACE);

    public static final Supplier<MapCodec<? extends SpawnCondition>> STRUCTURE = CONDITIONS.register("structure", () -> StructureCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> MOON_BRIGHTNESS = CONDITIONS.register("moon_brightness", () -> MoonBrightnessCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> BIOME = CONDITIONS.register("biome", () -> BiomeCheck.CODEC);

    public static final Supplier<MapCodec<? extends SpawnCondition>> RAW_BIOME = CONDITIONS.register("raw_biome", () -> RawBiomeCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> RAW_STRUCTURE = CONDITIONS.register("raw_structure", () -> RawStructureCheck.CODEC);
}