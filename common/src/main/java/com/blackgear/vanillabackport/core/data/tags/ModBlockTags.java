package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagRegistry<Block> TAGS = TagRegistry.create(Registries.BLOCK, VanillaBackport.MOD_ID);

    public static final TagKey<Block> PALE_OAK_LOGS = TAGS.register("pale_oak_logs");
    public static final TagKey<Block> HAPPY_GHAST_AVOIDS = TAGS.register("happy_ghast_avoids");
}