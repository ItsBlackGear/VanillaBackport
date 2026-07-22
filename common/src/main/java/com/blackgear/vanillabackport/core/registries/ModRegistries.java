package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.RegistryBuilder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class ModRegistries {
    public static final RegistryBuilder BUILDER = RegistryBuilder.create(VanillaBackport.MOD_ID);

    public static final ResourceKey<Registry<Codec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE_KEY = BUILDER.resource("spawn_condition_type");
    public static final Supplier<Registry<Codec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE = BUILDER.registry(SPAWN_CONDITION_TYPE_KEY);
}