package com.blackgear.vanillabackport.core.mixin.leashable.common.entities;

import com.blackgear.vanillabackport.common.api.leash.Leashable;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

public class LeashablesMixin {
    @Mixin(Camel.class)
    public static class CamelMixin implements Leashable {
        @Override
        public Vec3[] getQuadLeashOffsets() {
            return Leashable.createQuadLeashOffsets((Camel)(Object)this, 0.02, 0.48, 0.25, 0.82);
        }
    }

    @Mixin(AbstractHorse.class)
    public static class AbstractHorseMixin implements Leashable {
        @Override
        public boolean supportQuadLeash() {
            return true;
        }

        @Override
        public Vec3[] getQuadLeashOffsets() {
            return Leashable.createQuadLeashOffsets((AbstractHorse)(Object)this, 0.04, 0.52, 0.23, 0.87);
        }
    }
}