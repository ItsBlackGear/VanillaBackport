package com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw.RawBiomeCheck;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class WolfDataVariants {
    public static final BuiltInCoreRegistry<WolfDataVariant> REGISTRIES = BuiltInCoreRegistry.create(ResourceLocation.withDefaultNamespace("wolf_variants"), VanillaBackport.NAMESPACE);

//    public static final ResourceKey<WolfDataVariant> TEST = register("test", "test_wolf", SpawnPrioritySelectors.fallback(0));

    private static RegistryKey<WolfDataVariant> register(String key, String assetId, TagKey<Biome> tag) {
        return register(key, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(tag), 1));
    }

    private static RegistryKey<WolfDataVariant> register(String key, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation wild = ResourceLocation.withDefaultNamespace("entity/wolf/" + assetId);
        ResourceLocation tame = ResourceLocation.withDefaultNamespace("entity/wolf/" + assetId + "_tame");
        ResourceLocation angry = ResourceLocation.withDefaultNamespace("entity/wolf/" + assetId + "_angry");
        return REGISTRIES.register(
            key,
            new WolfDataVariant(
                new WolfDataVariant.AssetInfo(new ClientAsset(wild), new ClientAsset(tame), new ClientAsset(angry)),
                selectors
            )
        );
    }
}