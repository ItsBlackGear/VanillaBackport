package com.blackgear.vanillabackport.common.api.leash;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public final class QuadLeashRegistry {
    private QuadLeashRegistry() {}

    private static final Map<Predicate<Entity>, Function<Entity, Vec3[]>> OFFSETS = new LinkedHashMap<>();

    static {
        register(entity -> entity instanceof Boat, entity -> LeashPhysics.createQuadOffsets(entity, 0.00, 0.64, 0.382, 0.88));
        register(entity -> entity instanceof Camel, entity -> LeashPhysics.createQuadOffsets(entity, 0.02, 0.48, 0.250, 0.82));
        register(entity -> entity instanceof AbstractChestedHorse, entity -> LeashPhysics.createQuadOffsets(entity, 0.04, 0.41, 0.180, 0.73));
        register(entity -> entity instanceof AbstractHorse, entity -> LeashPhysics.createQuadOffsets(entity, 0.04, 0.52, 0.230, 0.87));
        register(entity -> entity instanceof Sniffer, entity -> LeashPhysics.createQuadOffsets(entity, -0.01, 0.63, 0.380, 1.15));
    }

    public static void register(Predicate<Entity> filter, Function<Entity, Vec3[]> offsets) {
        OFFSETS.put(filter, offsets);
    }

    public static boolean supports(Entity entity) {
        return OFFSETS.keySet().stream().anyMatch(filter -> filter.test(entity));
    }

    @Nullable
    public static Vec3[] getOffsets(Entity entity) {
        for (Map.Entry<Predicate<Entity>, Function<Entity, Vec3[]>> entry : OFFSETS.entrySet()) {
            if (entry.getKey().test(entity)) return entry.getValue().apply(entity);
        }

        return null;
    }

    public static boolean supportsAsHolder(Entity entity) {
        return false;
    }
}