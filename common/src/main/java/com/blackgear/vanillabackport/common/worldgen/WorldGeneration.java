package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.worldgen.generation.SpringToLifeFeatureManager;

public class WorldGeneration {
    public static void bootstrap(BiomeWriter writer, BiomeContext context) {
        new SpringToLifeFeatureManager(context, writer).bootstrap();
    }
}