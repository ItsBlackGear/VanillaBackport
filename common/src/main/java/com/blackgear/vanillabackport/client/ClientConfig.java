package com.blackgear.vanillabackport.client;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class ClientConfig {
    // Spring to Life
    public final ConfigBuilder.ConfigValue<Boolean> hasFallingLeaves;
    public final ConfigBuilder.ConfigValue<Double> fallingLeavesFrequency;
    public final ConfigBuilder.ConfigValue<Boolean> useLegacySpawnEggs;
    public final ConfigBuilder.ConfigValue<Boolean> useSheepWoolUndercoat;
    
    // Copper Age
    public final ConfigBuilder.ConfigValue<Boolean> endFlashSkyVisuals;
    public final ConfigBuilder.ConfigValue<Boolean> endFlashTerrainVisuals;

    public ClientConfig(ConfigBuilder builder) {
        builder.push("Spring to Life");
        this.hasFallingLeaves = builder.comment("allow falling leaves particles to generate")
            .define("falling_leaves", true);
        this.fallingLeavesFrequency = builder.comment("how often should falling leaves particles generate")
            .defineInRange("falling_leaves_frequency", 0.01, 0.0, 1.0);
        this.useLegacySpawnEggs = builder.comment("use the legacy spawn egg textures")
            .define("use_legacy_spawn_eggs", false);
        this.useSheepWoolUndercoat = builder.comment("toggle the colored sheep wool undercoat")
            .define("use_sheep_wool_undercoat", true);
        builder.pop();
        
        builder.push("Copper Age");
        this.endFlashSkyVisuals = builder.comment("toggle the end flash sky visuals")
            .define("end_flash_sky_visuals", true);
        this.endFlashTerrainVisuals = builder.comment("toggle the end flash terrain visuals")
            .define("end_flash_terrain_visuals", true);
        builder.pop();
    }
}