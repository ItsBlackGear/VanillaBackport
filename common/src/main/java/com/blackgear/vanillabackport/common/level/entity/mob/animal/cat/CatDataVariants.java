package com.blackgear.vanillabackport.common.level.entity.mob.animal.cat;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class CatDataVariants {
    public static final BuiltInCoreRegistry<CatDataVariant> REGISTRIES = BuiltInCoreRegistry.create(ModRegistries.CAT_VARIANT.get(), VanillaBackport.NAMESPACE);

//    public static final ResourceKey<CatDataVariant> TEST = register(
//        "test",
//        SpawnPrioritySelectors.single(new RawBiomeCheck(BiomeTags.IS_FOREST), 1)
//    );

    private static ResourceKey<CatDataVariant> register(String key) {
        return register(key, SpawnPrioritySelectors.fallback(0));
    }

    private static ResourceKey<CatDataVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = new ResourceLocation("entity/cat/" + key);
        return REGISTRIES.resource(key, new CatDataVariant(new ClientAsset(texture), selectors));
    }
}