package com.blackgear.vanillabackport.common.level.entities.armadillo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MobSensor<T extends LivingEntity> extends Sensor<T> {
    private final BiPredicate<T, LivingEntity> mobTest;
    private final Predicate<T> readyTest;
    private final MemoryModuleType<Boolean> toSet;
    private final int memoryTimeToLive;

    public MobSensor(int scanRate, BiPredicate<T, LivingEntity> mobTest, Predicate<T> readyTest, MemoryModuleType<Boolean> toSet, int memoryTimeToLive) {
        super(scanRate);
        this.mobTest = mobTest;
        this.readyTest = readyTest;
        this.toSet = toSet;
        this.memoryTimeToLive = memoryTimeToLive;
    }

    @Override
    protected void doTick(ServerLevel level, T entity) {
        if (!this.readyTest.test(entity)) {
            this.clearMemory(entity);
        } else {
            this.checkForMobsNearby(entity);
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(MemoryModuleType.NEAREST_LIVING_ENTITIES);
    }

    private void checkForMobsNearby(T entity) {
        Optional<List<LivingEntity>> entities = entity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
        if (entities.isPresent()) {
            boolean mobDetected = entities.get().stream().anyMatch(mob -> this.mobTest.test(entity, mob));
            if (mobDetected) this.mobDetected(entity);
        }
    }

    private void mobDetected(T entity) {
        entity.getBrain().setMemoryWithExpiry(this.toSet, true, this.memoryTimeToLive);
    }

    private void clearMemory(T entity) {
        entity.getBrain().eraseMemory(this.toSet);
    }
}