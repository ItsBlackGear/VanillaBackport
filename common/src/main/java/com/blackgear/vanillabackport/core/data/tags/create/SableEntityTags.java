package com.blackgear.vanillabackport.core.data.tags.create;

import com.blackgear.platform.common.data.TagRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SableEntityTags {
    public static final TagRegistry<EntityType<?>> TAGS = TagRegistry.create(Registries.ENTITY_TYPE, "sable");

    public static final TagKey<EntityType<?>> RETAIN_IN_SUB_LEVEL = TAGS.register("retain_in_sub_level");
}