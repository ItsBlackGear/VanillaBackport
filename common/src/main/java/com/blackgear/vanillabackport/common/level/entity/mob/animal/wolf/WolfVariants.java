package com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.*;
import com.blackgear.vanillabackport.common.api.variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class WolfVariants {
    public static final BuiltInCoreRegistry<WolfVariant> REGISTRIES = new BuiltInCoreRegistry<>(ModRegistries.WOLF_VARIANT.get(), VanillaBackport.NAMESPACE);

    public static final ResourceKey<WolfVariant> PALE = register("pale", "wolf", SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<WolfVariant> SPOTTED = register("spotted", "wolf_spotted", ModBiomeTags.SPAWNS_SPOTTED_WOLVES);
    public static final ResourceKey<WolfVariant> SNOWY = register("snowy", "wolf_snowy", ModBiomeTags.SPAWNS_SNOWY_WOLVES);
    public static final ResourceKey<WolfVariant> BLACK = register("black", "wolf_black", ModBiomeTags.SPAWNS_BLACK_WOLVES);
    public static final ResourceKey<WolfVariant> ASHEN = register("ashen", "wolf_ashen", ModBiomeTags.SPAWNS_ASHEN_WOLVES);
    public static final ResourceKey<WolfVariant> RUSTY = register("rusty", "wolf_rusty", ModBiomeTags.SPAWNS_RUSTY_WOLVES);
    public static final ResourceKey<WolfVariant> WOODS = register("woods", "wolf_woods", ModBiomeTags.SPAWNS_WOOD_WOLVES);
    public static final ResourceKey<WolfVariant> CHESTNUT = register("chestnut", "wolf_chestnut", ModBiomeTags.SPAWNS_CHESTNUT_WOLVES);
    public static final ResourceKey<WolfVariant> STRIPED = register("striped", "wolf_striped", ModBiomeTags.SPAWNS_STRIPED_WOLVES);

    private static ResourceKey<WolfVariant> register(String key, String assetId, TagKey<Biome> tag) {
        return register(key, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(tag), 1));
    }

    private static ResourceKey<WolfVariant> register(String key, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation wild = new ResourceLocation("entity/wolf/" + assetId);
        ResourceLocation tame = new ResourceLocation("entity/wolf/" + assetId + "_tame");
        ResourceLocation angry = new ResourceLocation("entity/wolf/" + assetId + "_angry");
        return REGISTRIES.resource(
            key,
            new WolfVariant(
                new WolfVariant.AssetInfo(new ClientAsset(wild), new ClientAsset(tame), new ClientAsset(angry)),
                selectors
            )
        );
    }
}