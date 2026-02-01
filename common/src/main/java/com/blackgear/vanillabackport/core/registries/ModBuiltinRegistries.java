package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.*;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class ModBuiltinRegistries {
    public static final BuiltInCoreRegistry<WolfSoundVariant> WOLF_SOUND_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.WOLF_SOUND_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<CowVariant> COW_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.COW_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<ChickenVariant> CHICKEN_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.CHICKEN_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<PigVariant> PIG_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.PIG_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<FrogDataVariant> FROG_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.FROG_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<WolfDataVariant> WOLF_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.WOLF_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<CatDataVariant> CAT_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.CAT_VARIANT.get(), VanillaBackport.NAMESPACE);
}