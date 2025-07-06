package com.blackgear.vanillabackport.common.worldgen.generation;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class FeatureManager {
    private final BiomeContext context;
    private final BiomeWriter writer;

    public FeatureManager(BiomeContext context, BiomeWriter writer) {
        this.context = context;
        this.writer = writer;
    }

    public abstract void bootstrap();

    protected Builder testFor(boolean filter) {
        return new Builder(this.context, ctx -> filter);
    }

    protected Builder testFor(ResourceKey<Biome> biome) {
        return new Builder(this.context, ctx -> ctx.is(biome));
    }

    protected Builder testFor(TagKey<Biome> biome) {
        return new Builder(this.context, ctx -> ctx.is(biome));
    }

    protected Builder testFor(Predicate<BiomeContext> context) {
        return new Builder(this.context, ctx -> ctx.is(context));
    }

    protected void add(BiConsumer<BiomeContext, BiomeWriter> feature) {
        feature.accept(this.context, this.writer);
    }

    protected void addIf(boolean filter, BiConsumer<BiomeContext, BiomeWriter> feature) {
        if (filter) {
            this.add(feature);
        }
    }

    protected void addIf(Predicate<BiomeContext> filter, BiConsumer<BiomeContext, BiomeWriter> feature) {
        this.addIf(filter.test(this.context), feature);
    }

    protected void addVegetation(ResourceKey<PlacedFeature> feature) {
        this.writer.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, feature);
    }

    public class Builder {
        private final BiomeContext context;
        private final Predicate<BiomeContext> filter;

        public Builder(BiomeContext context, Predicate<BiomeContext> filter) {
            this.context = context;
            this.filter = filter;
        }

        public Builder add(Runnable runnable) {
            if (this.filter.test(this.context)) {
                runnable.run();
            }

            return this;
        }

        public Builder addIf(BiConsumer<BiomeContext, BiomeWriter> feature) {
            if (this.filter.test(this.context)) {
                FeatureManager.this.add(feature);
            }

            return this;
        }
    }
}