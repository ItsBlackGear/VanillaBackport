package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTags {
    public static final TagRegistry<EntityType<?>> TAGS = TagRegistry.create(Registries.ENTITY_TYPE, VanillaBackport.MOD_ID);

    public static final TagKey<EntityType<?>> FOLLOWABLE_FRIENDLY_MOBS = TAGS.register("followable_friendly_mobs");
}