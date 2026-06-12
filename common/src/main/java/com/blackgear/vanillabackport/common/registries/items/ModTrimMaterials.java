package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class ModTrimMaterials {
    public static final BootstrapRegistrar<TrimMaterial> REGISTRIES = BootstrapRegistrar.create(Registries.TRIM_MATERIAL, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<TrimMaterial> RESIN = register("resin", ModItems.RESIN_BRICK.get(), Style.EMPTY.withColor(16545810), 0.5F);

    private static ResourceKey<TrimMaterial> register(String key, Item ingredient, Style style, float itemModelIndex) {
        return register(key, ingredient, style, itemModelIndex, Map.of());
    }

    private static ResourceKey<TrimMaterial> register(String key, Item ingredient, Style style, float itemModelIndex, Map<Holder<ArmorMaterial>, String> overrideArmorMaterials) {
        return REGISTRIES.register(
            key,
            context -> TrimMaterial.create(
                key,
                ingredient,
                itemModelIndex,
                Component.translatable(Util.makeDescriptionId("trim_material", ResourceLocation.withDefaultNamespace(key))).withStyle(style),
                overrideArmorMaterials));
    }
}