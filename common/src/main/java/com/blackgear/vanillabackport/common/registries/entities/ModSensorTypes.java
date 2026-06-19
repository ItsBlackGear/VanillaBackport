package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo.MobSensor;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entity.ai.sensor_types.AdultSensorAnyType;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;

import java.util.function.Supplier;

public class ModSensorTypes {
    public static final EntityRegistry REGISTRIES = EntityRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<SensorType<AdultSensorAnyType>> NEAREST_ADULT_ANY_TYPE = REGISTRIES.sensor(
        "nearest_adult_any_type",
        AdultSensorAnyType::new
    );
    public static final Supplier<SensorType<TemptingSensor>> HAPPY_GHAST_TEMPTATIONS = REGISTRIES.sensor(
        "happy_ghast_temptations",
        () -> new TemptingSensor(HappyGhast.IS_FOOD)
    );
    public static final Supplier<SensorType<TemptingSensor>> ARMADILLO_TEMPTATIONS = REGISTRIES.sensor(
        "armadillo_temptations",
        () -> new TemptingSensor(Armadillo.IS_FOOD)
    );
    public static final Supplier<SensorType<MobSensor<Armadillo>>> ARMADILLO_SCARE_DETECTED = REGISTRIES.sensor(
        "armadillo_scare_detected",
        () -> new MobSensor<>(5, Armadillo::isScaredBy, Armadillo::canStayRolledUp, ModMemoryModules.DANGER_DETECTED_RECENTLY.get(), 80)
    );
}