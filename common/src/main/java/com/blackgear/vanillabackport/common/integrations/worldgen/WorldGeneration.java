package com.blackgear.vanillabackport.common.integrations.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.worldgen.ChaosCubedFeatureManager;
import com.blackgear.vanillabackport.common.worldgen.MountsOfMayhemFeatureManager;
import com.blackgear.vanillabackport.common.worldgen.SpringToLifeFeatureManager;

public class WorldGeneration {
    public static void bootstrap(BiomeWriter writer, BiomeContext context) {
        new SpringToLifeFeatureManager(context, writer).bootstrap();
        new MountsOfMayhemFeatureManager(context, writer).bootstrap();
        new ChaosCubedFeatureManager(context, writer).bootstrap();
    }
}