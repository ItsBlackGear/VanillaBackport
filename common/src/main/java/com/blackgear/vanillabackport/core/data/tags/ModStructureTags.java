package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModStructureTags {
    public static final TagRegistry<Structure> TAGS = TagRegistry.create(Registries.STRUCTURE, VanillaBackport.NAMESPACE);
    
    public static final TagKey<Structure> ABANDONED_CAMP = TAGS.register("abandoned_camp");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_BAMBOO_JUNGLE_MAPS = TAGS.register("on_abandoned_camp_bamboo_jungle");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_CHERRY_GROVE_MAPS = TAGS.register("on_abandoned_camp_cherry_grove");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_BIRCH_FOREST_MAPS = TAGS.register("on_abandoned_camp_birch_forest");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_DAPPLED_FOREST_MAPS = TAGS.register("on_abandoned_camp_dappled_forest");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_FLOWER_FOREST_MAPS = TAGS.register("on_abandoned_camp_flower_forest");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_PALE_GARDEN_MAPS = TAGS.register("on_abandoned_camp_pale_garden");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_SWAMP_MAPS = TAGS.register("on_abandoned_camp_swamp");
    public static final TagKey<Structure> ON_ABANDONED_CAMP_WINDSWEPT_FOREST_MAPS = TAGS.register("on_abandoned_camp_windswept_forest");
    public static final TagKey<Structure> ON_ANCIENT_CITY_MAPS = TAGS.register("on_ancient_city_maps");
    public static final TagKey<Structure> ON_MINESHAFT_MAPS = TAGS.register("on_mineshaft_maps");
    public static final TagKey<Structure> ON_DESERT_PYRAMID_MAPS = TAGS.register("on_desert_pyramid_maps");
    public static final TagKey<Structure> ON_OCEAN_RUIN_WARM_MAPS = TAGS.register("on_ocean_ruin_warm_maps");
}
