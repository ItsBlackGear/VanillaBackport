package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.blackgear.vanillabackport.common.level.worldgen.features.TemplateFeatureConfiguration.TemplateEntry;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class TemplateFeature extends Feature<TemplateFeatureConfiguration> {
    public TemplateFeature(Codec<TemplateFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TemplateFeatureConfiguration> context) {
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        TemplateFeatureConfiguration config = context.config();
        TemplateEntry entry = config.templates().getRandomValue(random).orElseThrow();
        Rotation rotation = Util.getRandom(entry.rotations(), random);
        StructureTemplateManager manager = level.getLevel().getStructureManager();
        StructureTemplate template = manager.getOrCreate(entry.template());
        Vec3i offsetX = this.getRotatedOffset(rotation, Direction.Axis.X, template);
        Vec3i offsetZ = this.getRotatedOffset(rotation, Direction.Axis.Z, template);
        BlockPos pos = context.origin().offset(offsetX).offset(offsetZ);
        StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setRandom(random);
        return template.placeInWorld(level, pos, pos, settings, random, 3);
    }

    private Vec3i getRotatedOffset(Rotation rotation, Direction.Axis axis, StructureTemplate template) {
        return rotation.rotate(DirectionUtils.getNegative(axis)).getNormal().multiply(template.getSize().get(axis) / 2);
    }
}