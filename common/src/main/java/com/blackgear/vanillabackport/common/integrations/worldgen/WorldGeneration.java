package com.blackgear.vanillabackport.common.integrations.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.worldgen.*;

public class WorldGeneration {
    public static void bootstrap(BiomeWriter writer, BiomeContext context) {
        new SpringToLifeFeatureManager(context, writer).bootstrap();
        new ArmoredPawsFeatureManager(context, writer).bootstrap();
        new MountsOfMayhemFeatureManager(context, writer).bootstrap();
        new ChaosCubedFeatureManager(context, writer).bootstrap();
        new WildernessBoundFeatureManager(context, writer).bootstrap();
    }
}