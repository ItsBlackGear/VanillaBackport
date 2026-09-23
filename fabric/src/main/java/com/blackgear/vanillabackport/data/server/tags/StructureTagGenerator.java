package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.worldgen.structures.WildernessBoundStructures;
import com.blackgear.vanillabackport.core.data.tags.ModStructureTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class StructureTagGenerator extends TagsProvider<Structure> {
    public StructureTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.STRUCTURE, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModStructureTags.ABANDONED_CAMP)
            .add(WildernessBoundStructures.ABANDONED_CAMP_BAMBOO_JUNGLE)
            .add(WildernessBoundStructures.ABANDONED_CAMP_BIRCH_FOREST)
            .add(WildernessBoundStructures.ABANDONED_CAMP_CHERRY_GROVE)
            .add(WildernessBoundStructures.ABANDONED_CAMP_DAPPLED_FOREST)
            .add(WildernessBoundStructures.ABANDONED_CAMP_FLOWER_FOREST)
            .add(WildernessBoundStructures.ABANDONED_CAMP_FOREST)
            .add(WildernessBoundStructures.ABANDONED_CAMP_MEADOW)
            .add(WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_BIRCH_FOREST)
            .add(WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_PINE_TAIGA)
            .add(WildernessBoundStructures.ABANDONED_CAMP_OLD_GROWTH_SPRUCE_TAIGA)
            .add(WildernessBoundStructures.ABANDONED_CAMP_PALE_GARDEN)
            .add(WildernessBoundStructures.ABANDONED_CAMP_SAVANNA)
            .add(WildernessBoundStructures.ABANDONED_CAMP_SNOWY_TAIGA)
            .add(WildernessBoundStructures.ABANDONED_CAMP_SPARSE_JUNGLE)
            .add(WildernessBoundStructures.ABANDONED_CAMP_SWAMP)
            .add(WildernessBoundStructures.ABANDONED_CAMP_TAIGA)
            .add(WildernessBoundStructures.ABANDONED_CAMP_WINDSWEPT_FOREST)
            .add(WildernessBoundStructures.ABANDONED_CAMP_WOODED_BADLANDS);
        
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_BAMBOO_JUNGLE_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_BAMBOO_JUNGLE);
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_CHERRY_GROVE_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_CHERRY_GROVE);
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_BIRCH_FOREST_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_BIRCH_FOREST);
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_DAPPLED_FOREST_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_DAPPLED_FOREST);
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_FLOWER_FOREST_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_FLOWER_FOREST);
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_PALE_GARDEN_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_PALE_GARDEN);
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_SWAMP_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_SWAMP);
        this.tag(ModStructureTags.ON_ABANDONED_CAMP_WINDSWEPT_FOREST_MAPS).add(WildernessBoundStructures.ABANDONED_CAMP_WINDSWEPT_FOREST);
        this.tag(ModStructureTags.ON_ANCIENT_CITY_MAPS).add(BuiltinStructures.ANCIENT_CITY);
        this.tag(ModStructureTags.ON_MINESHAFT_MAPS).add(BuiltinStructures.MINESHAFT);
        this.tag(ModStructureTags.ON_DESERT_PYRAMID_MAPS).add(BuiltinStructures.DESERT_PYRAMID);
        this.tag(ModStructureTags.ON_OCEAN_RUIN_WARM_MAPS).add(BuiltinStructures.OCEAN_RUIN_WARM);
    }
}