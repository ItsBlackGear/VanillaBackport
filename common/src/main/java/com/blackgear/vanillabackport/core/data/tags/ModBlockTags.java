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
    public static final TagKey<Block> ARMADILLO_SPAWNABLE_ON = TAGS.register("armadillo_spawnable_on");
}