package com.blackgear.vanillabackport.client;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class ClientConfig {
    public final ConfigBuilder.ConfigValue<Boolean> fadeMusicOnPaleGarden;
    public final ConfigBuilder.ConfigValue<Boolean> fallingLeaves;
    public final ConfigBuilder.ConfigValue<Double> fallingLeavesFrequency;
    public final ConfigBuilder.ConfigValue<Boolean> updatedBatModel;

    public ClientConfig(ConfigBuilder builder) {
        builder.push("The Garden Awakens");
        this.fadeMusicOnPaleGarden = builder.comment("Determine if the music should fade out when entering a pale garden").define("fadeMusicOnPaleGarden", true);
        builder.pop();

        builder.push("Falling Leaves");
        this.fallingLeaves = builder.comment("Enable falling leaves particles in the world").define("falling_leaves", true);
        this.fallingLeavesFrequency = builder.comment("The frequency of falling leaves particles in the world, lower values mean more particles").defineInRange("falling_leaves_frequency", 0.01, 0.0, 1.0);
        builder.pop();

        builder.push("Bats and Pots");
        this.updatedBatModel = builder.comment("Use the updated bat model for bats").define("updated_bat_model", true);
        builder.pop();
    }
}