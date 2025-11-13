package com.blackgear.vanillabackport.core.data.tags.forge;

import com.blackgear.platform.common.data.TagRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ForgeBiomeTags {
    public static final TagRegistry<Biome> TAGS = TagRegistry.create(Registries.BIOME, "forge");

    public static final TagKey<Biome> IS_HOT = TAGS.register("is_hot");
    public static final TagKey<Biome> IS_HOT_OVERWORLD = TAGS.register("is_hot/overworld");
    public static final TagKey<Biome> IS_HOT_NETHER = TAGS.register("is_hot/nether");
    public static final TagKey<Biome> IS_HOT_END = TAGS.register("is_hot/end");
    public static final TagKey<Biome> IS_COLD = TAGS.register("is_cold");
    public static final TagKey<Biome> IS_COLD_OVERWORLD = TAGS.register("is_cold/overworld");
    public static final TagKey<Biome> IS_COLD_NETHER = TAGS.register("is_cold/nether");
    public static final TagKey<Biome> IS_COLD_END = TAGS.register("is_cold/end");
    public static final TagKey<Biome> IS_SPARSE = TAGS.register("is_sparse");
    public static final TagKey<Biome> IS_SPARSE_OVERWORLD = TAGS.register("is_sparse/overworld");
    public static final TagKey<Biome> IS_SPARSE_NETHER = TAGS.register("is_sparse/nether");
    public static final TagKey<Biome> IS_SPARSE_END = TAGS.register("is_sparse/end");
    public static final TagKey<Biome> IS_DENSE = TAGS.register("is_dense");
    public static final TagKey<Biome> IS_DENSE_OVERWORLD = TAGS.register("is_dense/overworld");
    public static final TagKey<Biome> IS_DENSE_NETHER = TAGS.register("is_dense/nether");
    public static final TagKey<Biome> IS_DENSE_END = TAGS.register("is_dense/end");
    public static final TagKey<Biome> IS_WET = TAGS.register("is_wet");
    public static final TagKey<Biome> IS_WET_OVERWORLD = TAGS.register("is_wet/overworld");
    public static final TagKey<Biome> IS_WET_NETHER = TAGS.register("is_wet/nether");
    public static final TagKey<Biome> IS_WET_END = TAGS.register("is_wet/end");
    public static final TagKey<Biome> IS_DRY = TAGS.register("is_dry");
    public static final TagKey<Biome> IS_DRY_OVERWORLD = TAGS.register("is_dry/overworld");
    public static final TagKey<Biome> IS_DRY_NETHER = TAGS.register("is_dry/nether");
    public static final TagKey<Biome> IS_DRY_END = TAGS.register("is_dry/end");
    public static final TagKey<Biome> IS_CONIFEROUS = TAGS.register("is_coniferous");
    public static final TagKey<Biome> IS_SPOOKY = TAGS.register("is_spooky");
    public static final TagKey<Biome> IS_DEAD = TAGS.register("is_dead");
    public static final TagKey<Biome> IS_LUSH = TAGS.register("is_lush");
    public static final TagKey<Biome> IS_MUSHROOM = TAGS.register("is_mushroom");
    public static final TagKey<Biome> IS_MAGICAL = TAGS.register("is_magical");
    public static final TagKey<Biome> IS_RARE = TAGS.register("is_rare");
    public static final TagKey<Biome> IS_PLATEAU = TAGS.register("is_plateau");
    public static final TagKey<Biome> IS_MODIFIED = TAGS.register("is_modified");
    public static final TagKey<Biome> IS_WATER = TAGS.register("is_water");
    public static final TagKey<Biome> IS_DESERT = TAGS.register("is_desert");
    public static final TagKey<Biome> IS_PLAINS = TAGS.register("is_plains");
    public static final TagKey<Biome> IS_SWAMP = TAGS.register("is_swamp");
    public static final TagKey<Biome> IS_SANDY = TAGS.register("is_sandy");
    public static final TagKey<Biome> IS_SNOWY = TAGS.register("is_snowy");
    public static final TagKey<Biome> IS_WASTELAND = TAGS.register("is_wasteland");
    public static final TagKey<Biome> IS_VOID = TAGS.register("is_void");
    public static final TagKey<Biome> IS_UNDERGROUND = TAGS.register("is_underground");
    public static final TagKey<Biome> IS_CAVE = TAGS.register("is_cave");
    public static final TagKey<Biome> IS_PEAK = TAGS.register("is_peak");
    public static final TagKey<Biome> IS_SLOPE = TAGS.register("is_slope");
    public static final TagKey<Biome> IS_MOUNTAIN = TAGS.register("is_mountain");
}