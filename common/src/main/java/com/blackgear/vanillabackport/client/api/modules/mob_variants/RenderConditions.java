package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.vanillabackport.core.ModChecker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT) @FunctionalInterface
public interface RenderConditions {
    RenderConditions DEFAULT = () -> true;
    RenderConditions FARM_ANIMALS = () -> VanillaBackport.COMMON_CONFIG.hasFarmAnimalVariants.get() && !ModChecker.MIXED_LITTER;
    RenderConditions SHEEP_UNDERCOAT = () -> VanillaBackport.CLIENT_CONFIG.useSheepWoolUndercoat.get() && !ModChecker.MIXED_LITTER;

    boolean apply();
}