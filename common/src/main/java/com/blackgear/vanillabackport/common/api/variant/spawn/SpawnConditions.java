package com.blackgear.vanillabackport.common.api.variant.spawn;

import com.blackgear.platform.core.api.registrar.Registrar;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.BiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.MoonBrightnessCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.StructureCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.raw.RawStructureCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.Codec;

public class SpawnConditions {
    public static final Registrar<Codec<? extends SpawnCondition>> REGISTRIES = Registrar.create(ModRegistries.SPAWN_CONDITION_TYPE_KEY, VanillaBackport.NAMESPACE);

    public static final Codec<? extends SpawnCondition> STRUCTURE = REGISTRIES.register("structure", StructureCheck.CODEC);
    public static final Codec<? extends SpawnCondition> MOON_BRIGHTNESS = REGISTRIES.register("moon_brightness", MoonBrightnessCheck.CODEC);
    public static final Codec<? extends SpawnCondition> BIOME = REGISTRIES.register("biome", BiomeCheck.CODEC);

    public static final Codec<? extends SpawnCondition> RAW_BIOME = REGISTRIES.register("raw_biome", RawBiomeCheck.CODEC);
    public static final Codec<? extends SpawnCondition> RAW_STRUCTURE = REGISTRIES.register("raw_structure", RawStructureCheck.CODEC);
}