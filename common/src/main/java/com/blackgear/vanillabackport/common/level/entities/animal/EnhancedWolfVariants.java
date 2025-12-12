package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.internal.IBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class EnhancedWolfVariants {
    public static final ResourceKey<EnhancedWolfVariant> PALE = register("pale", "wolf", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<EnhancedWolfVariant> SPOTTED = register("spotted", "wolf_spotted", ModBiomeTags.SPAWNS_SPOTTED_WOLVES);
    public static final ResourceKey<EnhancedWolfVariant> SNOWY = register("snowy", "wolf_snowy", ModBiomeTags.SPAWNS_SNOWY_WOLVES);
    public static final ResourceKey<EnhancedWolfVariant> BLACK = register("black", "wolf_black", ModBiomeTags.SPAWNS_BLACK_WOLVES);
    public static final ResourceKey<EnhancedWolfVariant> ASHEN = register("ashen", "wolf_ashen", ModBiomeTags.SPAWNS_ASHEN_WOLVES);
    public static final ResourceKey<EnhancedWolfVariant> RUSTY = register("rusty", "wolf_rusty", ModBiomeTags.SPAWNS_RUSTY_WOLVES);
    public static final ResourceKey<EnhancedWolfVariant> WOODS = register("woods", "wolf_woods", ModBiomeTags.SPAWNS_WOOD_WOLVES);
    public static final ResourceKey<EnhancedWolfVariant> CHESTNUT = register("chestnut", "wolf_chestnut", ModBiomeTags.SPAWNS_CHESTNUT_WOLVES);
    public static final ResourceKey<EnhancedWolfVariant> STRIPED = register("striped", "wolf_striped", ModBiomeTags.SPAWNS_STRIPED_WOLVES);

    private static ResourceKey<EnhancedWolfVariant> register(String key, String assetId, TagKey<Biome> tag) {
        return register(key, assetId, SpawnPrioritySelectors.single(new IBiomeCheck(tag), 1));
    }

    private static ResourceKey<EnhancedWolfVariant> register(String key, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation wild = VanillaBackport.vanilla("entity/wolf/" + assetId);
        ResourceLocation tame = VanillaBackport.vanilla("entity/wolf/" + assetId + "_tame");
        ResourceLocation angry = VanillaBackport.vanilla("entity/wolf/" + assetId + "_angry");
        return ModBuiltinRegistries.WOLF_VARIANTS.resource(
            key,
            new EnhancedWolfVariant(
                new EnhancedWolfVariant.AssetInfo(new ClientAsset(wild), new ClientAsset(tame), new ClientAsset(angry)),
                selectors
            )
        );
    }
}