package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.platform.common.worldgen.modifier.FeatureManager;
import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.common.worldgen.placements.WildernessBoundPlacements;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class WildernessBoundFeatureManager extends FeatureManager {
    public WildernessBoundFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }

    @Override
    public void bootstrap() {
        this.addIf(VanillaBackport.COMMON_CONFIG.hasFallenTrees.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomes.DAPPLED_FOREST)
                .add(() -> this.addVegetation(WildernessBoundPlacements.FALLEN_POPLAR_TREE));
        });
    }
}