package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.RegistryBuilder;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class ModRegistries {
    public static final RegistryBuilder BUILDER = RegistryBuilder.create(VanillaBackport.MOD_ID);

    public static final ResourceKey<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT_KEY = BUILDER.resource("wolf_sound_variant");
    public static final Supplier<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT = BUILDER.registry(WOLF_SOUND_VARIANT_KEY);
}