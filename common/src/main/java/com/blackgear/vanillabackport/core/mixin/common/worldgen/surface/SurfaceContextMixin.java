package com.blackgear.vanillabackport.core.mixin.common.worldgen.surface;

import com.blackgear.vanillabackport.common.api.worldgen.ContextNoiseSampler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

@Mixin(SurfaceRules.Context.class)
public class SurfaceContextMixin implements ContextNoiseSampler {
    @Shadow @Final public RandomState randomState;
    @Shadow long lastUpdateY;
    @Shadow int blockX;
    @Shadow int blockY;
    @Shadow int blockZ;

    @Unique private final Map<ResourceKey<NormalNoise.NoiseParameters>, DoubleSupplier> noiseSamplers3D = new IdentityHashMap<>();

    @Override
    public DoubleSupplier getNoiseSampler3D(ResourceKey<NormalNoise.NoiseParameters> noise) {
        return this.noiseSamplers3D.computeIfAbsent(noise, this::createNoiseSampler3D);
    }

    @Unique
    private DoubleSupplier createNoiseSampler3D(ResourceKey<NormalNoise.NoiseParameters> key) {
        NormalNoise noise = this.randomState.getOrCreateNoise(key);
        return new DoubleSupplier() {
            private long lastUpdateY;
            private double lastNoise;

            {
                this.lastUpdateY = SurfaceContextMixin.this.lastUpdateY - 1L;
            }

            @Override
            public double getAsDouble() {
                if (this.lastUpdateY != SurfaceContextMixin.this.lastUpdateY) {
                    this.lastNoise = noise.getValue(SurfaceContextMixin.this.blockX, SurfaceContextMixin.this.blockY, SurfaceContextMixin.this.blockZ);
                    this.lastUpdateY = SurfaceContextMixin.this.lastUpdateY;
                }

                return this.lastNoise;
            }
        };
    }
}