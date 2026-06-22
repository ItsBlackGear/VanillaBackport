package com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.BiomeCheck;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.MoonBrightnessCheck;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.StructureCheck;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawStructureCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public class SpawnConditions {
    public static final CoreRegistry<MapCodec<? extends SpawnCondition>> REGISTRIES = CoreRegistry.create(ModRegistries.SPAWN_CONDITION_TYPE.get(), VanillaBackport.NAMESPACE);

    public static final Supplier<MapCodec<? extends SpawnCondition>> STRUCTURE = REGISTRIES.register("structure", () -> StructureCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> MOON_BRIGHTNESS = REGISTRIES.register("moon_brightness", () -> MoonBrightnessCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> BIOME = REGISTRIES.register("biome", () -> BiomeCheck.CODEC);

    public static final Supplier<MapCodec<? extends SpawnCondition>> RAW_BIOME = REGISTRIES.register("raw_biome", () -> RawBiomeCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> RAW_STRUCTURE = REGISTRIES.register("raw_structure", () -> RawStructureCheck.CODEC);
}