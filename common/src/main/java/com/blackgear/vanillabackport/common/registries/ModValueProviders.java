package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.util.valueproviders.TrapezoidInt;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProviderType;

import java.util.function.Supplier;

public class ModValueProviders {
    public static final CoreRegistry<IntProviderType<?>> VALUE_PROVIDERS = CoreRegistry.create(Registries.INT_PROVIDER_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<IntProviderType<TrapezoidInt>> TRAPEZOID_INT = VALUE_PROVIDERS.register("trapezoid", () -> () -> TrapezoidInt.CODEC);
}