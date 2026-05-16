package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypeTags {
    public static final TagRegistry<DamageType> TAGS = TagRegistry.create(Registries.DAMAGE_TYPE, VanillaBackport.NAMESPACE);

    public static final TagKey<DamageType> SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO = TAGS.register("sulfur_cube_with_block_immune_to");
}