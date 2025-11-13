package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.blackgear.vanillabackport.common.level.entities.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entities.armadillo.ArmadilloAi;
import com.blackgear.vanillabackport.common.level.entities.armadillo.MobSensor;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entities.sensortypes.AdultSensorAnyType;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;

import java.util.function.Supplier;

public class ModSensorTypes {
    public static final EntityRegistry SENSOR_TYPES = EntityRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<SensorType<AdultSensorAnyType>> NEAREST_ADULT_ANY_TYPE = SENSOR_TYPES.sensor(
        "nearest_adult_any_type",
        AdultSensorAnyType::new
    );
    public static final Supplier<SensorType<TemptingSensor>> HAPPY_GHAST_TEMPTATIONS = SENSOR_TYPES.sensor(
        "happy_ghast_temptations",
        () -> new TemptingSensor(HappyGhast.IS_FOOD)
    );
    public static final Supplier<SensorType<TemptingSensor>> ARMADILLO_TEMPTATIONS = SENSOR_TYPES.sensor(
        "armadillo_temptations",
        () -> new TemptingSensor(Armadillo.IS_FOOD)
    );
    public static final Supplier<SensorType<MobSensor<Armadillo>>> ARMADILLO_SCARE_DETECTED = SENSOR_TYPES.sensor(
        "armadillo_scare_detected",
        () -> new MobSensor<>(5, Armadillo::isScaredBy, Armadillo::canStayRolledUp, ModMemoryModules.DANGER_DETECTED_RECENTLY.get(), 80)
    );
}