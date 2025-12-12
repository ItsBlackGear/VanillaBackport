package com.blackgear.vanillabackport.common.api.variant.vanilla;

import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Optional;

/**
 * Utility class for working with vanilla wolf variant selection during spawn.
 * Provides convenience methods to select and apply vanilla wolf variants based on spawn context.
 */
public final class VanillaWolfVariantUtils {

    private VanillaWolfVariantUtils() {
        // Utility class
    }

    /**
     * Selects a vanilla wolf variant appropriate for the given spawn location.
     * Uses biome-based spawn conditions to determine the best variant.
     *
     * @param level The server level accessor
     * @param pos   The spawn position
     * @return An optional containing the selected variant holder
     */
    public static Optional<Holder<WolfVariant>> selectVariantForSpawn(ServerLevelAccessor level, BlockPos pos) {
        SpawnContext context = SpawnContext.create(level, pos);
        return VanillaWolfVariantRegistry.selectVariantForSpawn(context, level.getRandom());
    }

    /**
     * Selects a vanilla wolf variant appropriate for the given spawn context.
     *
     * @param context The spawn context
     * @param random  The random source
     * @return An optional containing the selected variant holder
     */
    public static Optional<Holder<WolfVariant>> selectVariantForSpawn(SpawnContext context, RandomSource random) {
        return VanillaWolfVariantRegistry.selectVariantForSpawn(context, random);
    }

    /**
     * Applies a selected vanilla wolf variant to a wolf entity.
     * This method should be called during wolf spawning after the variant is selected.
     *
     * @param wolf    The wolf entity to apply the variant to
     * @param variant The variant holder to apply
     */
    public static void applyVariantToWolf(Wolf wolf, Holder<WolfVariant> variant) {
        wolf.setVariant(variant);
    }

    /**
     * Selects and applies a vanilla wolf variant to a wolf entity based on its spawn location.
     * This is a convenience method that combines selection and application.
     *
     * @param wolf  The wolf entity
     * @param level The server level accessor
     * @param pos   The spawn position
     * @return true if a variant was selected and applied, false otherwise
     */
    public static boolean selectAndApplyVariant(Wolf wolf, ServerLevelAccessor level, BlockPos pos) {
        Optional<Holder<WolfVariant>> variant = selectVariantForSpawn(level, pos);
        variant.ifPresent(v -> applyVariantToWolf(wolf, v));
        return variant.isPresent();
    }

    /**
     * Gets a random vanilla wolf variant from a parent wolf for breeding.
     * Uses the same biome-based logic but can also consider parent variants.
     *
     * @param parent1 First parent wolf
     * @param parent2 Second parent wolf
     * @param random  Random source
     * @return The selected variant for the offspring
     */
    public static Holder<WolfVariant> selectOffspringVariant(Wolf parent1, Wolf parent2, RandomSource random) {
        // For offspring, randomly inherit from one of the parents
        return random.nextBoolean() ? parent1.getVariant() : parent2.getVariant();
    }
}

