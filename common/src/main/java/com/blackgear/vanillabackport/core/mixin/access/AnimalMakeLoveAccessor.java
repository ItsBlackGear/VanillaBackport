package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(AnimalMakeLove.class)
public interface AnimalMakeLoveAccessor {
    @Accessor float getSpeedModifier();
    
    @Accessor void setSpawnChildAtTime(long spawnChildAtTime);
    @Accessor long getSpawnChildAtTime();
    
    @Invoker Optional<? extends Animal> callFindValidBreedPartner(Animal animal);
    @Invoker Animal callGetBreedTarget(Animal animal);
}
