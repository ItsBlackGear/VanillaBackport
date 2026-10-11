package com.blackgear.vanillabackport.common.api;

import com.blackgear.vanillabackport.core.util.Utilities.HashUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;

import java.util.*;
import java.util.function.Supplier;

public class SpawnEquipmentRegistry {
    private static final Map<EquipmentSlot, Map<Integer, TierConfig>> TIERS = new EnumMap<>(EquipmentSlot.class);
    
    public static void register(EquipmentSlot slot, int tier, float chance, Supplier<Item> itemSupplier) {
        TIERS.computeIfAbsent(slot, k -> new HashMap<>())
             .computeIfAbsent(tier, k -> new TierConfig(chance, new ArrayList<>()))
             .items().add(itemSupplier);
    }

    public static Item tryGetCustomEquipment(Mob mob, EquipmentSlot slot, int tier) {
        Map<Integer, TierConfig> tiers = TIERS.get(slot);
        if (tiers == null) return null;
        
        TierConfig config = tiers.get(tier);
        if (config == null || config.items().isEmpty()) return null;
        
        long seed = mob.getUUID().getLeastSignificantBits();
        float hash = HashUtils.toFloat(seed);
        
        if (hash < config.chance()) {
            List<Supplier<Item>> items = config.items();
            int itemIndex = items.size() == 1 ? 0 : Math.abs((int) HashUtils.toLong(seed ^ 0x9E3779B97F4A7C15L)) % items.size();
            return items.get(itemIndex).get();
        }
        
        return null;
    }
    
    public record TierConfig(float chance, List<Supplier<Item>> items) {}
}