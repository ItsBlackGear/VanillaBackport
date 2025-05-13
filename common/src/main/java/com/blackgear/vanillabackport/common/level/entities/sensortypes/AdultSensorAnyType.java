package com.blackgear.vanillabackport.common.level.entities.sensortypes;

import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Optional;
import java.util.Set;

public class AdultSensorAnyType extends Sensor<AgeableMob> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    protected void doTick(ServerLevel level, AgeableMob entity) {
        entity.getBrain()
            .getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
            .ifPresent(entities -> this.setNearestVisibleAdult(entity, entities));
    }

    private void setNearestVisibleAdult(AgeableMob mob, NearestVisibleLivingEntities entities) {
        Optional<AgeableMob> adult = entities.findClosest(entity -> entity.getType().is(ModEntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS) && !entity.isBaby())
            .map(AgeableMob.class::cast);
        mob.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, adult);
    }
}