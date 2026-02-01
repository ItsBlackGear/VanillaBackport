package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class CatDataVariants {
    public static final BuiltInCoreRegistry<CatDataVariant> REGISTRY = ModBuiltinRegistries.CAT_VARIANTS;

//    public static final ResourceKey<CatDataVariant> TEST = register(
//        "test",
//        SpawnPrioritySelectors.single(new RawBiomeCheck(BiomeTags.IS_FOREST), 1)
//    );

    private static ResourceKey<CatDataVariant> register(String key) {
        return register(key, SpawnPrioritySelectors.fallback(0));
    }

    private static ResourceKey<CatDataVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = VanillaBackport.vanilla("entity/cat/" + key);
        return REGISTRY.resource(key, new CatDataVariant(new ClientAsset(texture), selectors));
    }
}