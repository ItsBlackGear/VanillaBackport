package com.blackgear.vanillabackport.client;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class ClientConfig {
    // Bats and Pots
    public final ConfigBuilder.ConfigValue<Boolean> hasUpdatedBatModel;
    
    // Bundles of Bravery
    public final ConfigBuilder.ConfigValue<Boolean> endlessBundleUi;
    
    // Spring to Life
    public final ConfigBuilder.ConfigValue<Boolean> hasFallingLeaves;
    public final ConfigBuilder.ConfigValue<Double> fallingLeavesFrequency;
    public final ConfigBuilder.ConfigValue<Boolean> useLegacySpawnEggs;
    public final ConfigBuilder.ConfigValue<Boolean> useSheepWoolUndercoat;
    
    // Chase the Skies
    public final ConfigBuilder.ConfigValue<Boolean> renderLocatorBar;
    public final ConfigBuilder.ConfigValue<Boolean> extendedCloudReach;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayXpBar;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayCompassInfo;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayPlayerHeads;
    public final ConfigBuilder.ConfigValue<Boolean> locatorDisplayCardinalPoints;
    
    // Copper Age
    public final ConfigBuilder.ConfigValue<Boolean> endFlashSkyVisuals;
    public final ConfigBuilder.ConfigValue<Boolean> endFlashTerrainVisuals;

    public ClientConfig(ConfigBuilder builder) {
        builder.push("Bats and Pots");
        this.hasUpdatedBatModel = builder.comment("Use the updated bat model for bats")
            .define("updated_bat_model", true);
        builder.pop();
        
        builder.push("Bundles of Bravery");
        this.endlessBundleUi = builder.comment("makes all items in a bundle accessible not only the first 12")
            .define("endless_bundle_ui", false);
        builder.pop();

        builder.push("Spring to Life");
        this.hasFallingLeaves = builder.comment("allow falling leaves particles to generate")
            .define("falling_leaves", true);
        this.fallingLeavesFrequency = builder.comment("how often should falling leaves particles generate")
            .defineInRange("falling_leaves_frequency", 0.01, 0.0, 1.0);
        this.useLegacySpawnEggs = builder.comment("use the legacy spawn egg textures")
            .define("use_legacy_spawn_eggs", false);
        this.useSheepWoolUndercoat = builder.comment("toggle the colored sheep wool undercoat")
            .define("use_sheep_wool_undercoat", false);
        builder.pop();
        
        builder.push("Chase the Skies");
        this.renderLocatorBar = builder.comment("toggle the locator bar rendering via client side")
            .define("render_locator_bar", true);
        this.locatorDisplayXpBar = builder.comment("toggle whether avoid hiding the experience bar")
            .define("locator_display_xp_bar", false);
        this.locatorDisplayCompassInfo = builder.comment("toggle whether to display compass info; like lodestones or death location")
            .define("locator_display_compass_info", false);
        this.locatorDisplayPlayerHeads = builder.comment("toggle whether to display player heads instead of the coloured icons")
            .define("locator_display_player_heads", false);
        this.locatorDisplayCardinalPoints = builder.comment("toggle whether to display the cardinal points [N, S, W, E]")
            .define("locator_display_cardinal_points", false);
        this.extendedCloudReach = builder.comment("toggle whether clouds render all the way through your render distance")
            .define("extended_cloud_reach", true);
        builder.pop();
        
        builder.push("Copper Age");
        this.endFlashSkyVisuals = builder.comment("toggle the end flash sky visuals")
            .define("end_flash_sky_visuals", true);
        this.endFlashTerrainVisuals = builder.comment("toggle the end flash terrain visuals")
            .define("end_flash_terrain_visuals", true);
        builder.pop();
    }
}