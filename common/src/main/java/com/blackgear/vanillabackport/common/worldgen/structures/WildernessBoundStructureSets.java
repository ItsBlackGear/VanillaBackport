package com.blackgear.vanillabackport.common.worldgen.structures;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

public class WildernessBoundStructureSets {
    public static final BootstrapRegistrar<StructureSet> REGISTRIES = BootstrapRegistrar.create(Registries.STRUCTURE_SET, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<StructureSet> ABANDONED_CAMP = REGISTRIES.register("abandoned_camp", context -> {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        return new StructureSet(
            List.of(
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_BAMBOO_JUNGLE)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_BIRCH_FOREST)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_CHERRY_GROVE)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_DAPPLED_FOREST)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_FLOWER_FOREST)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_FOREST)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_MEADOW)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_PINE_TAIGA)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_BIRCH_FOREST)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_SPRUCE_TAIGA)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_PALE_GARDEN)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_SAVANNA)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_SNOWY_TAIGA)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_SPARSE_JUNGLE)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_SWAMP)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_TAIGA)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_WINDSWEPT_FOREST)),
                StructureSet.entry(structures.getOrThrow(WildernessBoundStructures.ABANDONED_CAMP_WOODED_BADLANDS))
            ),
            new RandomSpreadStructurePlacement(37, 8, RandomSpreadType.LINEAR, 91231127)
        );
    });
}