package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class ModPaintingVariants {
    public static final CoreRegistry<PaintingVariant> REGISTRIES = CoreRegistry.create(Registries.PAINTING_VARIANT, VanillaBackport.NAMESPACE);

    public static final ResourceKey<PaintingVariant> DENNIS = register("dennis", 3, 3);

    private static ResourceKey<PaintingVariant> register(String name, int width, int height) {
        return REGISTRIES.resource(name, () -> new PaintingVariant(16 * width, 16 * height));
    }
}