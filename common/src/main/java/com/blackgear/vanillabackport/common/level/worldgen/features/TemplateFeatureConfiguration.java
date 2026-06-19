package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record TemplateFeatureConfiguration(SimpleWeightedRandomList<TemplateEntry> templates) implements FeatureConfiguration {
    public static final Codec<TemplateFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SimpleWeightedRandomList.wrappedCodecAllowingEmpty(TemplateEntry.CODEC).fieldOf("templates").forGetter(TemplateFeatureConfiguration::templates)
    ).apply(instance, TemplateFeatureConfiguration::new));

    public record TemplateEntry(ResourceLocation template, List<Rotation> rotations) {
        public static final Codec<TemplateEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(TemplateEntry::template),
            Rotation.CODEC.listOf().optionalFieldOf("rotations", List.of(Rotation.values())).forGetter(TemplateEntry::rotations)
        ).apply(instance, TemplateEntry::new));

        public static TemplateEntry of(ResourceLocation template) {
            return new TemplateEntry(template, List.of(Rotation.values()));
        }
    }
}