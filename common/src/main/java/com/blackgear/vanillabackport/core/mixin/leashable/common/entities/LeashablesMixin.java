package com.blackgear.vanillabackport.core.mixin.leashable.common.entities;

import com.blackgear.vanillabackport.common.api.leash.Leashable;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
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

    @Mixin(AbstractChestedHorse.class)
    public static class AbstractChestedHorseMixin implements Leashable {
        @Override
        public boolean supportQuadLeash() {
            return true;
        }

        @Override
        public Vec3[] getQuadLeashOffsets() {
            return Leashable.createQuadLeashOffsets((AbstractChestedHorse)(Object)this, 0.04, 0.41, 0.18, 0.73);
        }
    }

    @Mixin(Sniffer.class)
    public static class SnifferMixin implements Leashable {
        @Override
        public boolean supportQuadLeash() {
            return true;
        }

        @Override
        public Vec3[] getQuadLeashOffsets() {
            return Leashable.createQuadLeashOffsets((Sniffer)(Object)this, -0.01, 0.63, 0.38, 1.15);
        }
    }
}