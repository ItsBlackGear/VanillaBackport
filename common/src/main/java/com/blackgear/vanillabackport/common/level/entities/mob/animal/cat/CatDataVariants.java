package com.blackgear.vanillabackport.common.level.entities.mob.animal.cat;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;

public class CatDataVariants {
    public static final BuiltInCoreRegistry<CatDataVariant> REGISTRIES = BuiltInCoreRegistry.create(ResourceLocation.withDefaultNamespace("cat_variants"), VanillaBackport.NAMESPACE);

//    public static final ResourceKey<CatDataVariant> TEST = register("test", SpawnPrioritySelectors.single(new RawBiomeCheck(BiomeTags.IS_FOREST), 1));

    private static RegistryKey<CatDataVariant> register(String key) {
        return register(key, SpawnPrioritySelectors.fallback(0));
    }

    private static RegistryKey<CatDataVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = ResourceLocation.withDefaultNamespace("entity/cat/" + key);
        return REGISTRIES.register(key, new CatDataVariant(new ClientAsset(texture), selectors));
    }
}