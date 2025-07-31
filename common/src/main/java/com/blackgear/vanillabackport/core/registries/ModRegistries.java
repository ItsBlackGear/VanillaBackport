package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.RegistryBuilder;
import com.blackgear.vanillabackport.common.api.variant.SpawnCondition;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class ModRegistries {
    public static final RegistryBuilder BUILDER = RegistryBuilder.create(VanillaBackport.NAMESPACE);

    public static final ResourceKey<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT_KEY = BUILDER.resource("wolf_sound_variant");
    public static final Supplier<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT = BUILDER.registry(WOLF_SOUND_VARIANT_KEY);

    public static final ResourceKey<Registry<CowVariant>> COW_VARIANT_KEY = BUILDER.resource("cow_variant");
    public static final Supplier<Registry<CowVariant>> COW_VARIANT = BUILDER.registry(COW_VARIANT_KEY);

    public static final ResourceKey<Registry<Codec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE_KEY = BUILDER.resource("spawn_condition_type");
    public static final Supplier<Registry<Codec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE = BUILDER.registry(SPAWN_CONDITION_TYPE_KEY);
}