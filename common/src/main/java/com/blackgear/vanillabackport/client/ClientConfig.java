package com.blackgear.vanillabackport.client;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class ClientConfig {
    // Bats and Pots
    public final ConfigBuilder.ConfigValue<Boolean> hasUpdatedBatModel;
    
    // Bundles of Bravery
    public final ConfigBuilder.ConfigValue<Boolean> endlessBundleUi;
    public final ConfigBuilder.ConfigValue<Boolean> hasModernBundleModels;
    
    // Spring to Life
    public final ConfigBuilder.ConfigValue<Boolean> hasFallingLeaves;
    public final ConfigBuilder.ConfigValue<Double> fallingLeavesFrequency;
    public final ConfigBuilder.ConfigValue<Boolean> useLegacySpawnEggs;
    public final ConfigBuilder.ConfigValue<Boolean> useSheepWoolUndercoat;
    
    // Chase the Skies
    public final ConfigBuilder.ConfigValue<Boolean> enableLocatorBar;
    public final ConfigBuilder.ConfigValue<Boolean> extendedCloudReach;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayInfoBar;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayCompassInfo;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayPlayerHeads;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayCardinalPoints;
    
    // Copper Age
    public final ConfigBuilder.ConfigValue<Boolean> enableDyePalettes;
    public final ConfigBuilder.ConfigValue<Boolean> enableModernDyeTextures;
    public final ConfigBuilder.ConfigValue<Boolean> endFlashSkyVisuals;
    public final ConfigBuilder.ConfigValue<Boolean> endFlashTerrainVisuals;
    
    public ClientConfig(ConfigBuilder builder) {
        builder.push("Bats and Pots");
        this.hasUpdatedBatModel = builder.comment("Use the updated bat model for bats")
            .define("updated_bat_model", true);
        builder.pop();
        
        builder.push("Bundles of Bravery");
            this.endlessBundleUi = builder.comment("allow access to all items in a Bundle instead of only the first 12")
                .define("endless_bundle_ui", false);
            this.hasModernBundleModels = builder.comment("enable modern bundle models")
                .define("has_modern_bundle_models", true);
        builder.pop();
        
        builder.push("Spring to Life");
            this.hasFallingLeaves = builder.comment("enable falling leaves particles")
                .define("falling_leaves", true);
            this.fallingLeavesFrequency = builder.comment("chance for falling leaf particles to appear")
                .defineInRange("falling_leaves_frequency", 0.01, 0.0, 1.0);
            this.useLegacySpawnEggs = builder.comment("use the legacy spawn egg textures")
                .define("use_legacy_spawn_eggs", false);
            this.useSheepWoolUndercoat = builder.comment("use the modern colored wool undercoat for sheep")
                .define("use_sheep_wool_undercoat", false);
        builder.pop();
        
        builder.push("Chase the Skies");
            this.enableLocatorBar = builder.comment("enable the Locator Bar; disabling it on your client does not make you invisible on the server")
                .define("enable_locator_bar", true);
            this.locatorDisplayInfoBar = builder.comment("keep the experience bar and jump bar visible")
                .define("locator_display_info_bar", false);
            this.locatorDisplayCompassInfo = builder.comment("display compass targets such as Lodestones and death locations")
                .define("locator_display_compass_info", false);
            this.locatorDisplayPlayerHeads = builder.comment("display player heads instead of colored player markers")
                .define("locator_display_player_heads", false);
            this.locatorDisplayCardinalPoints = builder.comment("display cardinal directions [N, S, E, W]")
                .define("locator_display_cardinal_points", false);
            this.extendedCloudReach = builder.comment("extend cloud rendering to match your render distance")
                .define("extended_cloud_reach", true);
        builder.pop();
        
        builder.push("Copper Age");
            this.enableDyePalettes = builder.comment("enable a brown palette to render for each dye")
                .define("enable_dye_palettes", true);
            this.enableModernDyeTextures = builder.comment("enable modern dye textures")
                .define("enable_modern_dye_textures", true);
            this.endFlashSkyVisuals = builder.comment("enable End Flash sky effects")
                .define("end_flash_sky_visuals", true);
            this.endFlashTerrainVisuals = builder.comment("enable End Flash terrain effects")
                .define("end_flash_terrain_visuals", true);
        builder.pop();
    }
}