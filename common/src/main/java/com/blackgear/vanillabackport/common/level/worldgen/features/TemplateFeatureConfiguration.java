package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.List;
import java.util.Optional;

public record TemplateFeatureConfiguration(SimpleWeightedRandomList<TemplateEntry> templates, Optional<Holder<StructureProcessorList>> processors) implements FeatureConfiguration {
    public static final Codec<TemplateFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SimpleWeightedRandomList.wrappedCodecAllowingEmpty(TemplateEntry.CODEC).fieldOf("templates").forGetter(TemplateFeatureConfiguration::templates),
        StructureProcessorType.LIST_CODEC.optionalFieldOf("processors").forGetter(TemplateFeatureConfiguration::processors)
    ).apply(instance, TemplateFeatureConfiguration::new));

    public TemplateFeatureConfiguration(SimpleWeightedRandomList<TemplateEntry> templates) {
        this(templates, Optional.empty());
    }
    
    public static TemplateFeatureConfiguration simple(ResourceLocation id, Holder<StructureProcessorList> processors) {
        return new TemplateFeatureConfiguration(SimpleWeightedRandomList.single(TemplateEntry.of(id)), Optional.of(processors));
    }
    
    public static TemplateFeatureConfiguration simple(ResourceLocation id) {
        return new TemplateFeatureConfiguration(SimpleWeightedRandomList.single(TemplateEntry.of(id)), Optional.empty());
    }
    
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