package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class ModBuiltinRegistries {
    public static final BuiltInCoreRegistry<WolfSoundVariant> VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.WOLF_SOUND_VARIANT.get(), VanillaBackport.NAMESPACE);
}