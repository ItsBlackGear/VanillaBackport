package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.common.level.entities.sulfurcube.SulfurCube;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class ModEntityTypes {
    public static final LinkedHashMap<ResourceLocation, EntityType<?>> REGISTRIES = new LinkedHashMap<>();

    public static final EntityType<SulfurCube> SULFUR_CUBE = register(
        "sulfur_cube",
        EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER)
            .sized(0.49F, 0.49F)
            .clientTrackingRange(10)
    );

    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> factory) {
        EntityType<T> entry = factory.build(name);
        REGISTRIES.put(new ResourceLocation(name), entry);
        return entry;
    }

    public static void bootstrap(BiConsumer<ResourceLocation, EntityType<?>> consumer) {
        REGISTRIES.forEach(consumer);
    }
}
