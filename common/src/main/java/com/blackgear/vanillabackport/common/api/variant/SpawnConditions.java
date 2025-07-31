package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public class SpawnConditions {
    public static final CoreRegistry<Codec<? extends SpawnCondition>> CONDITIONS = CoreRegistry.create(ModRegistries.SPAWN_CONDITION_TYPE.get(), VanillaBackport.NAMESPACE);

    public static final Supplier<Codec<? extends SpawnCondition>> STRUCTURE = CONDITIONS.register("structure", () -> StructureCheck.CODEC);
    public static final Supplier<Codec<? extends SpawnCondition>> MOON_BRIGHTNESS = CONDITIONS.register("moon_brightness", () -> MoonBrightnessCheck.CODEC);
    public static final Supplier<Codec<? extends SpawnCondition>> BIOME = CONDITIONS.register("biome", () -> BiomeCheck.CODEC);
}