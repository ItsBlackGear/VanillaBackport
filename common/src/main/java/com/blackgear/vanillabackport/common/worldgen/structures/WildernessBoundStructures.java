package com.blackgear.vanillabackport.common.worldgen.structures;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.common.level.worldgen.structure.AbandonedCampStructurePools;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

public class WildernessBoundStructures {
    public static final BootstrapRegistrar<Structure> REGISTRIES = BootstrapRegistrar.create(Registries.STRUCTURE, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<Structure> ABANDONED_CAMP_BAMBOO_JUNGLE = REGISTRIES.register("abandoned_camp_bamboo_jungle", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_BAMBOO_JUNGLE))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.BAMBOO_JUNGLE.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_BIRCH_FOREST = REGISTRIES.register("abandoned_camp_birch_forest", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_BIRCH_FOREST))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.BIRCH_FOREST.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_CHERRY_GROVE = REGISTRIES.register("abandoned_camp_cherry_grove", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_CHERRY_GROVE))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.CHERRY_GROVE.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_DAPPLED_FOREST = REGISTRIES.register("abandoned_camp_dappled_forest", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_DAPPLED_FOREST))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.DAPPLED_FOREST.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_FLOWER_FOREST = REGISTRIES.register("abandoned_camp_flower_forest", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_FLOWER_FOREST))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.FLOWER_FOREST.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_FOREST = REGISTRIES.register("abandoned_camp_forest", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_FOREST))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.FOREST.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_MEADOW = REGISTRIES.register("abandoned_camp_meadow", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_MEADOW))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.MEADOW.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_OLD_GROWTH_BIRCH_FOREST = REGISTRIES.register("abandoned_camp_old_growth_birch_forest", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_OLD_GROWTH_BIRCH_FOREST))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.OLD_GROWTH_BIRCH_FOREST.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_OLD_GROWTH_PINE_TAIGA = REGISTRIES.register("abandoned_camp_old_growth_pine_taiga", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_OLD_GROWTH_PINE_TAIGA))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.OLD_GROWTH_PINE_TAIGA.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_OLD_GROWTH_SPRUCE_TAIGA = REGISTRIES.register("abandoned_camp_old_growth_spruce_taiga", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_OLD_GROWTH_SPRUCE_TAIGA))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.OLD_GROWTH_SPRUCE_TAIGA.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_PALE_GARDEN = REGISTRIES.register("abandoned_camp_pale_garden", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_PALE_GARDEN))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.PALE_GARDEN.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_SAVANNA = REGISTRIES.register("abandoned_camp_savanna", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_SAVANNA))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.SAVANNA.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_SNOWY_TAIGA = REGISTRIES.register("abandoned_camp_snowy_taiga", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_SNOWY_TAIGA))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.SNOWY_TAIGA.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_SPARSE_JUNGLE = REGISTRIES.register("abandoned_camp_sparse_jungle", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_SPARSE_JUNGLE))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.SPARSE_JUNGLE.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_SWAMP = REGISTRIES.register("abandoned_camp_swamp", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_SWAMP))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.SWAMP.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_TAIGA = REGISTRIES.register("abandoned_camp_taiga", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_TAIGA))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.TAIGA.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_WINDSWEPT_FOREST = REGISTRIES.register("abandoned_camp_windswept_forest", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_WINDSWEPT_FOREST))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.WINDSWEPT_FOREST.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
    public static final ResourceKey<Structure> ABANDONED_CAMP_WOODED_BADLANDS = REGISTRIES.register("abandoned_camp_wooded_badlands", context -> {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
        return new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(ModBiomeTags.HAS_ABANDONED_CAMP_WOODED_BADLANDS))
                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                .build(),
            templates.getOrThrow(AbandonedCampStructurePools.WOODED_BADLANDS.tentStructureDirectory()),
            2,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            true,
            Heightmap.Types.WORLD_SURFACE_WG
        );
    });
}