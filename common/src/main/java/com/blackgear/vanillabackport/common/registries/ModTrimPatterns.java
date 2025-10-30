package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;

public class ModTrimPatterns {
    public static final ResourceKey<TrimPattern> FLOW = registryKey("flow");
    public static final ResourceKey<TrimPattern> BOLT = registryKey("bolt");

    public static void bootstrap(BootstapContext<TrimPattern> context) {
        register(context, ModItems.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE.get(), FLOW);
        register(context, ModItems.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), BOLT);
    }

    private static void register(BootstapContext<TrimPattern> pContext, Item pTemplateItem, ResourceKey<TrimPattern> pTrimPatternKey) {
        TrimPattern trimpattern = new TrimPattern(
                pTrimPatternKey.location(),
                BuiltInRegistries.ITEM.wrapAsHolder(pTemplateItem),
                Component.translatable(Util.makeDescriptionId("trim_pattern", pTrimPatternKey.location()))
        );
        pContext.register(pTrimPatternKey, trimpattern);
    }

    private static ResourceKey<TrimPattern> registryKey(String name) {
        return ResourceKey.create(Registries.TRIM_PATTERN, VanillaBackport.vanilla(name));
    }
}
