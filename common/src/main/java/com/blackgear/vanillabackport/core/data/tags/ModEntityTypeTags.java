package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTags {
    public static final TagRegistry<EntityType<?>> TAGS = TagRegistry.create(Registries.ENTITY_TYPE, VanillaBackport.NAMESPACE);

    public static final TagKey<EntityType<?>> FOLLOWABLE_FRIENDLY_MOBS = TAGS.register("followable_friendly_mobs");
    public static final TagKey<EntityType<?>> MONSTERS_THAT_SPAWN_ON_PEACEFUL = TAGS.register("monsters_that_spawn_on_peaceful");
    public static final TagKey<EntityType<?>> NOT_AFFECTED_BY_GEYSERS = TAGS.register("not_affected_by_geysers");
    public static final TagKey<EntityType<?>> ACCEPTS_IRON_GOLEM_GIFT = TAGS.register("accepts_iron_golem_gift");
    public static final TagKey<EntityType<?>> CAN_FLOAT_WHILE_RIDDEN = TAGS.register("can_float_while_ridden");
}