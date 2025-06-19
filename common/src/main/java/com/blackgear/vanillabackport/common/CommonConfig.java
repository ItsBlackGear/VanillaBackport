package com.blackgear.vanillabackport.common;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class CommonConfig {
    // The Garden Awakens
    public final ConfigBuilder.ConfigValue<Boolean> generateResin;
    public final ConfigBuilder.ConfigValue<Boolean> spawnCreakingFromHearts;
    public final ConfigBuilder.ConfigValue<Boolean> spawnCreakingHeartsFromSaplings;
    public final ConfigBuilder.ConfigValue<Boolean> generatePaleGarden;
    public final ConfigBuilder.ConfigValue<Boolean> paleTradesFromWanderer;

    // Summer Drop
    public final ConfigBuilder.ConfigValue<Boolean> generateDriedGhasts;
    public final ConfigBuilder.ConfigValue<Boolean> dropLeashConnectionsOnFireworkBoost;

    public CommonConfig(ConfigBuilder builder) {
        builder.push("The Garden Awakens");
        this.generateResin = builder.comment("Determine if resin should generate after hitting a creaking").define("generateResin", true);
        this.spawnCreakingFromHearts = builder.comment("Determine if creakings should spawn from creaking hearts").define("spawnCreakingFromHearts", true);
        this.spawnCreakingHeartsFromSaplings = builder.comment("Determine if creaking hearts should spawn from pale oak saplings").define("spawnCreakingHeartsFromSaplings", false);
        this.generatePaleGarden = builder.comment("Determine if Pale Gardens should generate in the overworld").define("generatePaleGarden", true);
        this.paleTradesFromWanderer = builder.comment("Determine if the wandering trader should have trades from the pale garden").define("paleTradesFromWanderer", true);
        builder.pop();

        builder.push("Summer Drop");
        this.generateDriedGhasts = builder.comment("Determine if dried ghasts should generate in Nether Fossils").define("generateDriedGhasts", true);
        this.dropLeashConnectionsOnFireworkBoost = builder.comment("Determine if all leash connections should be dropped when using a firework rocket while elytra flying").define("dropLeashConnectionsOnFireworkBoost", true);
        builder.pop();
    }
}