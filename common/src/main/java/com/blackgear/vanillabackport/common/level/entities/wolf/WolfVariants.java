package com.blackgear.vanillabackport.common.level.entities.wolf;

import com.blackgear.platform.core.Environment;
import com.blackgear.vanillabackport.common.api.variant.*;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class WolfVariants {
    public static final ResourceKey<WolfVariant> PALE = register("pale", "wolf", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<WolfVariant> SPOTTED = register("spotted", "wolf_spotted", BiomeTags.IS_SAVANNA);
    public static final ResourceKey<WolfVariant> SNOWY = register("snowy", "wolf_snowy", Biomes.GROVE);
    public static final ResourceKey<WolfVariant> BLACK = register("black", "wolf_black", Biomes.OLD_GROWTH_PINE_TAIGA);
    public static final ResourceKey<WolfVariant> ASHEN = register("ashen", "wolf_ashen", Biomes.SNOWY_TAIGA);
    public static final ResourceKey<WolfVariant> RUSTY = register("rusty", "wolf_rusty", BiomeTags.IS_JUNGLE);
    public static final ResourceKey<WolfVariant> WOODS = register("woods", "wolf_woods", Biomes.FOREST);
    public static final ResourceKey<WolfVariant> CHESTNUT = register("chestnut", "wolf_chestnut", Biomes.OLD_GROWTH_SPRUCE_TAIGA);
    public static final ResourceKey<WolfVariant> STRIPED = register("striped", "wolf_striped", BiomeTags.IS_BADLANDS);

    public static void bootstrap(RegistryAccess access) {
        register("pale", "wolf", SpawnPrioritySelectors.fallback(0));
        register(access, "spotted", "wolf_spotted", BiomeTags.IS_SAVANNA);
        register(access, "snowy", "wolf_snowy", Biomes.GROVE);
        register(access, "black", "wolf_black", Biomes.OLD_GROWTH_PINE_TAIGA);
        register(access, "ashen", "wolf_ashen", Biomes.SNOWY_TAIGA);
        register(access, "rusty", "wolf_rusty", BiomeTags.IS_JUNGLE);
        register(access, "woods", "wolf_woods", Biomes.FOREST);
        register(access, "chestnut", "wolf_chestnut", Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        register(access, "striped", "wolf_striped", BiomeTags.IS_BADLANDS);
    }

    private static HolderSet<Biome> getRequiredBiomes(TagKey<Biome> biome) {
        return Environment.isClientSide()
            ? ClientsideSpawnPrioritySelector.getRequiredBiomes(biome)
            : Environment.getCurrentServer().get().registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
    }

    private static Holder<Biome> getRequiredBiomes(ResourceKey<Biome> biome) {
        return Environment.isClientSide()
            ? ClientsideSpawnPrioritySelector.getRequiredBiomes(biome)
            : Environment.getCurrentServer().get().registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
    }

    private static SpawnPrioritySelectors highPriorityBiome(HolderSet<Biome> biomes) {
        return SpawnPrioritySelectors.single(new BiomeCheck(biomes), 1);
    }

    private static void register(RegistryAccess access, String key, String assetId, ResourceKey<Biome> biome) {
        access.lookup(Registries.BIOME).ifPresent(lookup -> register(key, assetId, highPriorityBiome(HolderSet.direct(lookup.getOrThrow(biome)))));
    }

    private static void register(RegistryAccess access, String key, String assetId, TagKey<Biome> biome) {
        access.lookup(Registries.BIOME).ifPresent(lookup -> register(key, assetId, highPriorityBiome(lookup.getOrThrow(biome))));
    }

    private static ResourceKey<WolfVariant> register(String key, String assetId, ResourceKey<Biome> biome) {
        return register(key, assetId, highPriorityBiome(HolderSet.direct(getRequiredBiomes(biome))));
    }

    private static ResourceKey<WolfVariant> register(String key, String assetId, TagKey<Biome> biome) {
        return register(key, assetId, highPriorityBiome(getRequiredBiomes(biome)));
    }

    private static ResourceKey<WolfVariant> register(String key, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation wild = VanillaBackport.vanilla("entity/wolf/" + assetId);
        ResourceLocation tame = VanillaBackport.vanilla("entity/wolf/" + assetId + "_tame");
        ResourceLocation angry = VanillaBackport.vanilla("entity/wolf/" + assetId + "_angry");
        return ModBuiltinRegistries.WOLF_VARIANTS.resource(
            key,
            new WolfVariant(
                new WolfVariant.AssetInfo(new ClientAsset(wild), new ClientAsset(tame), new ClientAsset(angry)),
                selectors
            )
        );
    }
}