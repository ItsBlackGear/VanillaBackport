package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class ModPaintingVariants {
    public static final BootstrapRegistrar<PaintingVariant> REGISTRIES = BootstrapRegistrar.create(Registries.PAINTING_VARIANT, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<PaintingVariant> DENNIS = register("dennis", 3, 3);
    
    private static ResourceKey<PaintingVariant> register(String name, int width, int height) {
        return REGISTRIES.resource(
            name,
            (context, key) -> new PaintingVariant(width, height, key));
    }
}