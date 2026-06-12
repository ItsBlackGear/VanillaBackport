package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final CoreRegistry<DataComponentType<?>> REGISTRIES = CoreRegistry.create(BuiltInRegistries.DATA_COMPONENT_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<DataComponentType<ResourceKey<ChickenVariant>>> CHICKEN_VARIANT = register("chicken/variant",
        builder ->
            builder.persistent(ResourceKey.codec(ModRegistries.CHICKEN_VARIANT_KEY))
                .networkSynchronized(ResourceKey.streamCodec(ModRegistries.CHICKEN_VARIANT_KEY)));

    public static <T> Supplier<DataComponentType<T>> register(String key, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return REGISTRIES.register(key, () -> operator.apply(DataComponentType.builder()).build());
    }
}