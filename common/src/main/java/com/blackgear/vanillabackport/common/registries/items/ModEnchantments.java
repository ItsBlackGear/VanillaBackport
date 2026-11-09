package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.items.enchantment.BreachEnchantment;
import com.blackgear.vanillabackport.common.level.items.enchantment.DensityEnchantment;
import com.blackgear.vanillabackport.common.level.items.enchantment.LungeEnchantment;
import com.blackgear.vanillabackport.common.level.items.enchantment.WindBurstEnchantment;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public class ModEnchantments {
    public static final CoreRegistry<Enchantment> REGISTRIES = CoreRegistry.create(Registries.ENCHANTMENT, VanillaBackport.NAMESPACE);

    public static final Supplier<Enchantment> LUNGE = REGISTRIES.register("lunge",
        () -> new LungeEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    
    public static final Supplier<Enchantment> DENSITY = REGISTRIES.register("density",
        () -> new DensityEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    
    public static final Supplier<Enchantment> BREACH = REGISTRIES.register("breach",
        () -> new BreachEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    
    public static final Supplier<Enchantment> WIND_BURST = REGISTRIES.register("wind_burst",
        () -> new WindBurstEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
}