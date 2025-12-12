package com.blackgear.vanillabackport.common.api.variant.vanilla;

import com.blackgear.vanillabackport.common.api.variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.WolfVariant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that maps vanilla WolfVariant entries to spawn selectors.
 * This is used during wolf spawning to select the appropriate vanilla variant based on biome.
 *
 * Spawn selectors are registered via the WolfVariantReloadListener when JSON files are loaded.
 */
public class VanillaWolfVariantRegistry {
    private static final Map<ResourceKey<WolfVariant>, VanillaWolfVariantSpawnSelector> SELECTORS = new ConcurrentHashMap<>();

    /**
     * Registers a custom vanilla wolf variant spawn selector.
     * Can be used by external mods to add custom spawn conditions for vanilla or modded wolf variants.
     */
    public static void register(ResourceKey<WolfVariant> key, VanillaWolfVariantSpawnSelector selector) {
        SELECTORS.put(key, selector);
    }

    /**
     * Gets all registered spawn selectors.
     */
    public static Collection<VanillaWolfVariantSpawnSelector> getSelectors() {
        return Collections.unmodifiableCollection(SELECTORS.values());
    }

    /**
     * Gets a spawn selector for a specific vanilla wolf variant.
     */
    public static Optional<VanillaWolfVariantSpawnSelector> getSelector(ResourceKey<WolfVariant> key) {
        return Optional.ofNullable(SELECTORS.get(key));
    }

    /**
     * Selects the appropriate vanilla wolf variant for a spawn context (position and biome).
     * Uses the priority-based selection system to pick the best matching variant.
     *
     * @param context The spawn context containing position, level, and biome information
     * @param random  The random source for selection when multiple variants match with equal priority
     * @return An optional containing the selected vanilla wolf variant holder, or empty if no match
     */
    public static Optional<Holder<WolfVariant>> selectVariantForSpawn(SpawnContext context, RandomSource random) {
        if (SELECTORS.isEmpty()) {
            return Optional.empty();
        }

        return PriorityProvider.pick(
            SELECTORS.values().stream(),
            selector -> selector,
            random,
            context
        ).map(VanillaWolfVariantSpawnSelector::vanillaVariant);
    }

    /**
     * Clears all registered selectors. Called during reload to allow re-registration.
     */
    public static void clear() {
        SELECTORS.clear();
    }

    /**
     * Checks if the registry has any selectors registered.
     */
    public static boolean isEmpty() {
        return SELECTORS.isEmpty();
    }
}

