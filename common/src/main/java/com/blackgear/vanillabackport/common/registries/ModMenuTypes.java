package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.inventory.CrafterMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final CoreRegistry<MenuType<?>> REGISTRIES = CoreRegistry.create(Registries.MENU, ResourceLocation.DEFAULT_NAMESPACE);
    
    public static final Supplier<MenuType<CrafterMenu>> CRAFTER_3x3 = REGISTRIES.register("crafter_3x3", () -> new MenuType<>(CrafterMenu::new, FeatureFlags.VANILLA_SET));
}