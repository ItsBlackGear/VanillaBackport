package com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ZombieNautilusVariants {
    public static final BuiltInCoreRegistry<ZombieNautilusVariant> REGISTRIES = new BuiltInCoreRegistry<>(ModRegistries.ZOMBIE_NAUTILUS_VARIANT.get(), VanillaBackport.NAMESPACE);

    public static final ResourceKey<ZombieNautilusVariant> TEMPERATE = register("temperate",
        ZombieNautilusVariant.ModelType.NORMAL,
        "zombie_nautilus",
        SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<ZombieNautilusVariant> WARM = register("warm",
        ZombieNautilusVariant.ModelType.WARM,
        "zombie_nautilus_coral",
        ModBiomeTags.SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS);

    private static ResourceKey<ZombieNautilusVariant> register(String key, ZombieNautilusVariant.ModelType type, String identifier, TagKey<Biome> biome) {
        return register(key, type, identifier, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<ZombieNautilusVariant> register(String key, ZombieNautilusVariant.ModelType type, String identifier, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = new ResourceLocation("entity/nautilus/" + identifier);
        return REGISTRIES.resource(key, new ZombieNautilusVariant(new ModelAndTexture<>(type, texture), selectors));
    }
}