package com.blackgear.vanillabackport.common.api.variant.spawn;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.BiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.MoonBrightnessCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.StructureCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.internal.IBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public class SpawnConditions {
    public static final CoreRegistry<MapCodec<? extends SpawnCondition>> CONDITIONS = CoreRegistry.create(ModRegistries.SPAWN_CONDITION_TYPE.get(), VanillaBackport.NAMESPACE);
    public static final CoreRegistry<MapCodec<? extends SpawnCondition>> MINECRAFT_CONDITIONS = CoreRegistry.create(ModRegistries.SPAWN_CONDITION_TYPE.get(), "minecraft");

    public static final Supplier<MapCodec<? extends SpawnCondition>> STRUCTURE = CONDITIONS.register("structure", () -> StructureCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> MOON_BRIGHTNESS = CONDITIONS.register("moon_brightness", () -> MoonBrightnessCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> BIOME = CONDITIONS.register("biome", () -> BiomeCheck.CODEC);

    public static final Supplier<MapCodec<? extends SpawnCondition>> I_BIOME = MINECRAFT_CONDITIONS.register("internal_biome", () -> IBiomeCheck.CODEC);
    public static final Supplier<MapCodec<? extends SpawnCondition>> I_STRUCTURE = MINECRAFT_CONDITIONS.register("internal_structure", () -> IBiomeCheck.CODEC);
}