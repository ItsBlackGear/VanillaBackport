package com.blackgear.vanillabackport.common.level.entities.wolf;

import com.blackgear.vanillabackport.common.api.variant.*;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.blackgear.vanillabackport.core.util.Utils;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class WolfVariants {
    public static final ResourceKey<WolfVariant> PALE = register("pale", "wolf", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<WolfVariant> SPOTTED = register("spotted", "wolf_spotted", ModBiomeTags.SPAWNS_SPOTTED_WOLVES);
    public static final ResourceKey<WolfVariant> SNOWY = register("snowy", "wolf_snowy", ModBiomeTags.SPAWNS_SNOWY_WOLVES);
    public static final ResourceKey<WolfVariant> BLACK = register("black", "wolf_black", ModBiomeTags.SPAWNS_BLACK_WOLVES);
    public static final ResourceKey<WolfVariant> ASHEN = register("ashen", "wolf_ashen", ModBiomeTags.SPAWNS_ASHEN_WOLVES);
    public static final ResourceKey<WolfVariant> RUSTY = register("rusty", "wolf_rusty", ModBiomeTags.SPAWNS_RUSTY_WOLVES);
    public static final ResourceKey<WolfVariant> WOODS = register("woods", "wolf_woods", ModBiomeTags.SPAWNS_WOOD_WOLVES);
    public static final ResourceKey<WolfVariant> CHESTNUT = register("chestnut", "wolf_chestnut", ModBiomeTags.SPAWNS_CHESTNUT_WOLVES);
    public static final ResourceKey<WolfVariant> STRIPED = register("striped", "wolf_striped", ModBiomeTags.SPAWNS_STRIPED_WOLVES);


    private static SpawnPrioritySelectors highPriorityBiome(HolderSet<Biome> biomes) {
        return SpawnPrioritySelectors.single(new BiomeCheck(biomes), 1);
    }

    private static ResourceKey<WolfVariant> register(String key, String assetId, TagKey<Biome> biome) {
        HolderSet<Biome> biomes = Utils.getRegistryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
        return register(key, assetId, highPriorityBiome(biomes));
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