package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.level.worldgen.structure.AbandonedCampStructurePools;
import com.blackgear.vanillabackport.common.worldgen.structures.WildernessBoundStructureSets;
import com.blackgear.vanillabackport.common.worldgen.structures.WildernessBoundStructures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.concurrent.CompletableFuture;

public class StructureGenerator extends FabricDynamicRegistryProvider {
    public StructureGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        // Wilderness Bound
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_BAMBOO_JUNGLE);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_BIRCH_FOREST);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_CHERRY_GROVE);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_DAPPLED_FOREST);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_FLOWER_FOREST);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_FOREST);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_MEADOW);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_PINE_TAIGA);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_BIRCH_FOREST);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_SPRUCE_TAIGA);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_PALE_GARDEN);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_SAVANNA);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_SNOWY_TAIGA);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_SPARSE_JUNGLE);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_SWAMP);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_TAIGA);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_WINDSWEPT_FOREST);
        this.structure(provider, entries, WildernessBoundStructures.ABANDONED_CAMP_WOODED_BADLANDS);
        
        this.set(provider, entries, WildernessBoundStructureSets.ABANDONED_CAMP);
        
        this.pool(provider, entries, AbandonedCampStructurePools.SAVANNA.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.FLOWER_FOREST.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.BIRCH_FOREST.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.FOREST.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.SNOWY_TAIGA.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.BAMBOO_JUNGLE.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.SPARSE_JUNGLE.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.CHERRY_GROVE.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.MEADOW.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.OLD_GROWTH_BIRCH_FOREST.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.OLD_GROWTH_SPRUCE_TAIGA.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.OLD_GROWTH_PINE_TAIGA.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.SWAMP.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.TAIGA.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.WINDSWEPT_FOREST.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.DAPPLED_FOREST.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.WOODED_BADLANDS.campStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.PALE_GARDEN.campStructureDirectory());
        
        this.pool(provider, entries, AbandonedCampStructurePools.SAVANNA.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.FLOWER_FOREST.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.BIRCH_FOREST.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.FOREST.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.SNOWY_TAIGA.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.BAMBOO_JUNGLE.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.SPARSE_JUNGLE.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.CHERRY_GROVE.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.MEADOW.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.OLD_GROWTH_BIRCH_FOREST.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.OLD_GROWTH_SPRUCE_TAIGA.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.OLD_GROWTH_PINE_TAIGA.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.SWAMP.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.TAIGA.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.WINDSWEPT_FOREST.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.DAPPLED_FOREST.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.WOODED_BADLANDS.tentStructureDirectory());
        this.pool(provider, entries, AbandonedCampStructurePools.PALE_GARDEN.tentStructureDirectory());
    }

    private void structure(HolderLookup.Provider provider, Entries entries, ResourceKey<Structure> key) {
        final HolderLookup.RegistryLookup<Structure> registry = provider.lookupOrThrow(Registries.STRUCTURE);
        entries.add(key, registry.getOrThrow(key).value());
    }

    private void set(HolderLookup.Provider provider, Entries entries, ResourceKey<StructureSet> key) {
        final HolderLookup.RegistryLookup<StructureSet> registry = provider.lookupOrThrow(Registries.STRUCTURE_SET);
        entries.add(key, registry.getOrThrow(key).value());
    }

    private void pool(HolderLookup.Provider provider, Entries entries, ResourceKey<StructureTemplatePool> key) {
        final HolderLookup.RegistryLookup<StructureTemplatePool> registry = provider.lookupOrThrow(Registries.TEMPLATE_POOL);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/structure";
    }
}