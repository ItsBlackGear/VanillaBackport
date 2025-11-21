package com.blackgear.vanillabackport.client;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class ClientConfig {
    // Bats and Pots
    public final ConfigBuilder.ConfigValue<Boolean> hasUpdatedBatModel;

    // Spring to Life
    public final ConfigBuilder.ConfigValue<Boolean> hasFallingLeaves;
    public final ConfigBuilder.ConfigValue<Double> fallingLeavesFrequency;

    public ClientConfig(ConfigBuilder builder) {
        builder.push("Bats and Pots");
        this.hasUpdatedBatModel = builder.comment("Use the updated bat model for bats")
            .define("updated_bat_model", true);
        builder.pop();

        builder.push("Spring to Life");
        this.hasFallingLeaves = builder.comment("allow falling leaves particles to generate")
            .define("falling_leaves", true);
        this.fallingLeavesFrequency = builder.comment("how often should falling leaves particles generate")
            .defineInRange("falling_leaves_frequency", 0.01, 0.0, 1.0);
        builder.pop();
    }
}