package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTags {
    public static final TagRegistry<Enchantment> TAGS = TagRegistry.create(Registries.ENCHANTMENT, VanillaBackport.NAMESPACE);

    public static final TagKey<Enchantment> PREVENTS_DECORATED_POT_SHATTERING = TAGS.register("prevents_decorated_pot_shattering");
}
