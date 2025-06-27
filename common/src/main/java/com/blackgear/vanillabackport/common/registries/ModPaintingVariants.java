package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class ModPaintingVariants {
    public static final ResourceKey<PaintingVariant> DENNIS = registryKey("dennis");

    public static void bootstrap(BootstrapContext<PaintingVariant> context) {
        register(context, DENNIS, 3,3);
    }

    private static void register(BootstrapContext<PaintingVariant> context, ResourceKey<PaintingVariant> key, int width, int height) {
        context.register(key, new PaintingVariant(width, height, key.location()));
    }

    private static ResourceKey<PaintingVariant> registryKey(String name) {
        return ResourceKey.create(Registries.PAINTING_VARIANT, VanillaBackport.resource(name));
    }
}