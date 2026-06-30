package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagRegistry<Block> TAGS = TagRegistry.create(Registries.BLOCK, VanillaBackport.NAMESPACE);

    public static final TagKey<Block> PALE_OAK_LOGS = TAGS.register("pale_oak_logs");
    public static final TagKey<Block> CREAKING_HEART_HOLDERS = TAGS.register("creaking_heart_holders");

    public static final TagKey<Block> HAPPY_GHAST_AVOIDS = TAGS.register("happy_ghast_avoids");

    public static final TagKey<Block> TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS = TAGS.register("triggers_ambient_desert_sand_block_sounds");
    public static final TagKey<Block> TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS = TAGS.register("triggers_ambient_desert_dry_vegetation_block_sounds");
    public static final TagKey<Block> TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS = TAGS.register("triggers_ambient_dried_ghast_block_sounds");

    public static final TagKey<Block> ALLOWS_LEAF_LITTER = TAGS.register("allows_leaf_litter");
    public static final TagKey<Block> SUPPORTS_CACTUS = TAGS.register("supports_cactus");

    public static final TagKey<Block> SPAWN_FALLING_LEAVES = TAGS.register("spawn_falling_leaves");
    public static final TagKey<Block> SPAWN_FALLING_NEEDLES = TAGS.register("spawn_falling_needles");

    public static final TagKey<Block> CAMELS_SPAWNABLE_ON = TAGS.register("camel_spawnable_on");
    public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = TAGS.register("incorrect_for_copper_tool");

    public static final TagKey<Block> CAUSES_PERIODIC_GEYSER_ERUPTIONS = TAGS.register("causes_periodic_geyser_eruptions");
    public static final TagKey<Block> CAUSES_CONTINUOUS_GEYSER_ERUPTIONS = TAGS.register("causes_continuous_geyser_eruptions");

    public static final TagKey<Block> SPELEOTHEMS = TAGS.register("speleothems");
    public static final TagKey<Block> SULFUR_SPIKE_REPLACEABLE = TAGS.register("sulfur_spike_replaceable");
    
    public static final TagKey<Block> COPPER_GOLEM_STATUES = TAGS.register("copper_golem_statues");
    public static final TagKey<Block> COPPER_CHESTS = TAGS.register("copper_chests");
    public static final TagKey<Block> COPPER_GOLEM_DESTINATION_TARGETS = TAGS.register("copper_golem_destination_targets");
    public static final TagKey<Block> COPPER = TAGS.register("copper");
    
    public static final TagKey<Block> BARS = TAGS.register("bars");
    public static final TagKey<Block> CHAINS = TAGS.register("chains");
    public static final TagKey<Block> LANTERNS = TAGS.register("lanterns");
    public static final TagKey<Block> LIGHTNING_RODS = TAGS.register("lightning_rods");
    
    public static final TagKey<Block> WOODEN_SHELVES = TAGS.register("wooden_shelves");
}