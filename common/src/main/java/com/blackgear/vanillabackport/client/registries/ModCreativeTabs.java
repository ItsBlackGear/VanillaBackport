package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.client.api.selector.BundledContent;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public interface ModCreativeTabs {
    CoreRegistry<CreativeModeTab> TABS = CoreRegistry.create(Registries.CREATIVE_MODE_TAB, VanillaBackport.MOD_ID);

    Supplier<CreativeModeTab> VANILLA_BACKPORT = TABS.register(
        "vanilla_backport",
        () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
            .title(Component.literal("Vanilla Backport"))
            .icon(() -> new ItemStack(Items.BUNDLE))
            .displayItems((parameters, output) -> {
                List<BundledContent> filters = ModBundledContents.getFilters();
                Collections.reverse(filters);
                filters.stream()
                    .flatMap(filter -> filter.getDisplayItems().stream())
                    .forEach(output::accept);
            })
            .build()
    );
}