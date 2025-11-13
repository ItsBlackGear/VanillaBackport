package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;

public class VariantKeys {
    public static final ResourceKey<ChickenVariant> TEMPERATE_CHICKEN = createChickenKey("temperate");
    public static final ResourceKey<ChickenVariant> COLD_CHICKEN = createChickenKey("cold");
    public static final ResourceKey<ChickenVariant> WARM_CHICKEN = createChickenKey("warm");

    public static final ResourceKey<CowVariant> TEMPERATE_COW = createCowKey("temperate");
    public static final ResourceKey<CowVariant> COLD_COW = createCowKey("cold");
    public static final ResourceKey<CowVariant> WARM_COW = createCowKey("warm");

    public static final ResourceKey<PigVariant> TEMPERATE_PIG = createPigKey("temperate");
    public static final ResourceKey<PigVariant> COLD_PIG = createPigKey("cold");
    public static final ResourceKey<PigVariant> WARM_PIG = createPigKey("warm");

    public static ResourceKey<ChickenVariant> createChickenKey(String name) {
        return ResourceKey.create(ModRegistries.CHICKEN_VARIANT_KEY, VanillaBackport.vanilla(name));
    }

    public static ResourceKey<CowVariant> createCowKey(String name) {
        return ResourceKey.create(ModRegistries.COW_VARIANT_KEY, VanillaBackport.vanilla(name));
    }

    public static ResourceKey<PigVariant> createPigKey(String name) {
        return ResourceKey.create(ModRegistries.PIG_VARIANT_KEY, VanillaBackport.vanilla(name));
    }
}