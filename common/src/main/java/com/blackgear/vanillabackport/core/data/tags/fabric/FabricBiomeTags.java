package com.blackgear.vanillabackport.core.data.tags.fabric;

import com.blackgear.platform.common.data.TagRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class FabricBiomeTags {
    public static final TagRegistry<Biome> TAGS = TagRegistry.create(Registries.BIOME, "c");

    public static final TagKey<Biome> IN_OVERWORLD = TAGS.register("in_overworld");
    public static final TagKey<Biome> IN_THE_END = TAGS.register("in_the_end");
    public static final TagKey<Biome> IN_NETHER = TAGS.register("in_nether");
    public static final TagKey<Biome> TAIGA = TAGS.register("taiga");
    public static final TagKey<Biome> EXTREME_HILLS = TAGS.register("extreme_hills");
    public static final TagKey<Biome> WINDSWEPT = TAGS.register("windswept");
    public static final TagKey<Biome> JUNGLE = TAGS.register("jungle");
    public static final TagKey<Biome> MESA = TAGS.register("mesa");
    public static final TagKey<Biome> PLAINS = TAGS.register("plains");
    public static final TagKey<Biome> SAVANNA = TAGS.register("savanna");
    public static final TagKey<Biome> ICY = TAGS.register("icy");
    public static final TagKey<Biome> AQUATIC_ICY = TAGS.register("aquatic_icy");
    public static final TagKey<Biome> BEACH = TAGS.register("beach");
    public static final TagKey<Biome> FOREST = TAGS.register("forest");
    public static final TagKey<Biome> BIRCH_FOREST = TAGS.register("birch_forest");
    public static final TagKey<Biome> OCEAN = TAGS.register("ocean");
    public static final TagKey<Biome> DESERT = TAGS.register("desert");
    public static final TagKey<Biome> RIVER = TAGS.register("river");
    public static final TagKey<Biome> SWAMP = TAGS.register("swamp");
    public static final TagKey<Biome> MUSHROOM = TAGS.register("mushroom");
    public static final TagKey<Biome> UNDERGROUND = TAGS.register("underground");
    public static final TagKey<Biome> MOUNTAIN = TAGS.register("mountain");
    public static final TagKey<Biome> CLIMATE_HOT = TAGS.register("climate_hot");
    public static final TagKey<Biome> CLIMATE_TEMPERATE = TAGS.register("climate_temperate");
    public static final TagKey<Biome> CLIMATE_COLD = TAGS.register("climate_cold");
    public static final TagKey<Biome> CLIMATE_WET = TAGS.register("climate_wet");
    public static final TagKey<Biome> CLIMATE_DRY = TAGS.register("climate_dry");
    public static final TagKey<Biome> VEGETATION_SPARSE = TAGS.register("vegetation_sparse");
    public static final TagKey<Biome> VEGETATION_DENSE = TAGS.register("vegetation_dense");
    public static final TagKey<Biome> TREE_CONIFEROUS = TAGS.register("tree_coniferous");
    public static final TagKey<Biome> TREE_SAVANNA = TAGS.register("tree_savanna");
    public static final TagKey<Biome> TREE_JUNGLE = TAGS.register("tree_jungle");
    public static final TagKey<Biome> TREE_DECIDUOUS = TAGS.register("tree_deciduous");
    public static final TagKey<Biome> VOID = TAGS.register("void");
    public static final TagKey<Biome> MOUNTAIN_PEAK = TAGS.register("mountain_peak");
    public static final TagKey<Biome> MOUNTAIN_SLOPE = TAGS.register("mountain_slope");
    public static final TagKey<Biome> AQUATIC = TAGS.register("aquatic");
    public static final TagKey<Biome> WASTELAND = TAGS.register("wasteland");
    public static final TagKey<Biome> DEAD = TAGS.register("dead");
    public static final TagKey<Biome> FLORAL = TAGS.register("floral");
    public static final TagKey<Biome> SNOWY = TAGS.register("snowy");
    public static final TagKey<Biome> BADLANDS = TAGS.register("badlands");
    public static final TagKey<Biome> CAVES = TAGS.register("caves");
    public static final TagKey<Biome> END_ISLANDS = TAGS.register("end_islands");
    public static final TagKey<Biome> NETHER_FORESTS = TAGS.register("nether_forests");
    public static final TagKey<Biome> SNOWY_PLAINS = TAGS.register("snowy_plains");
    public static final TagKey<Biome> STONY_SHORES = TAGS.register("stony_shores");
    public static final TagKey<Biome> FLOWER_FORESTS = TAGS.register("flower_forests");
    public static final TagKey<Biome> DEEP_OCEAN = TAGS.register("deep_ocean");
    public static final TagKey<Biome> SHALLOW_OCEAN = TAGS.register("shallow_ocean");
}