package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.common.value_providers.TrapezoidInt;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProviderType;

import java.util.function.Supplier;

public class ModValueProviders {
    public static final CoreRegistry<IntProviderType<?>> REGISTRIES = CoreRegistry.create(Registries.INT_PROVIDER_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<IntProviderType<TrapezoidInt>> TRAPEZOID_INT = REGISTRIES.register("trapezoid", () -> () -> TrapezoidInt.CODEC);
}