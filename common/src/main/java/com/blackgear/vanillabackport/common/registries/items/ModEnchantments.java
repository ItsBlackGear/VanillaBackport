package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.items.enchantment.LungeEnchantment;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public class ModEnchantments {
    public static final CoreRegistry<Enchantment> REGISTRIES = CoreRegistry.create(Registries.ENCHANTMENT, VanillaBackport.NAMESPACE);

    public static final Supplier<Enchantment> LUNGE = REGISTRIES.register("lunge",
        () -> new LungeEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
}