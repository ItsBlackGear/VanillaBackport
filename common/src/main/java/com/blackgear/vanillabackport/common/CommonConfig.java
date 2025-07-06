package com.blackgear.vanillabackport.common;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class CommonConfig {
    // The Garden Awakens
    public final ConfigBuilder.ConfigValue<Boolean> generateResin;
    public final ConfigBuilder.ConfigValue<Boolean> spawnCreakingFromHearts;
    public final ConfigBuilder.ConfigValue<Boolean> spawnCreakingHeartsFromSaplings;
    public final ConfigBuilder.ConfigValue<Boolean> generatePaleGarden;
    public final ConfigBuilder.ConfigValue<Boolean> paleTradesFromWanderer;

    // Chase the Skies
    public final ConfigBuilder.ConfigValue<Boolean> generateDriedGhasts;
    public final ConfigBuilder.ConfigValue<Boolean> dropLeashConnectionsOnFireworkBoost;

    // Spring to Life
    public final ConfigBuilder.ConfigValue<Boolean> generateBushes;
    public final ConfigBuilder.ConfigValue<Boolean> generateFireflyBushes;
    public final ConfigBuilder.ConfigValue<Boolean> generateWildflowers;
    public final ConfigBuilder.ConfigValue<Boolean> generateDryGrass;
    public final ConfigBuilder.ConfigValue<Boolean> generateFallenTrees;
    public final ConfigBuilder.ConfigValue<Boolean> generateLeafLitter;

    public CommonConfig(ConfigBuilder builder) {
        builder.push("The Garden Awakens");
        this.generateResin = builder.comment("Determine if resin should generate after hitting a creaking").define("generateResin", true);
        this.spawnCreakingFromHearts = builder.comment("Determine if creakings should spawn from creaking hearts").define("spawnCreakingFromHearts", true);
        this.spawnCreakingHeartsFromSaplings = builder.comment("Determine if creaking hearts should spawn from pale oak saplings").define("spawnCreakingHeartsFromSaplings", false);
        this.generatePaleGarden = builder.comment("Determine if Pale Gardens should generate in the overworld").define("generatePaleGarden", true);
        this.paleTradesFromWanderer = builder.comment("Determine if the wandering trader should have trades from the pale garden").define("paleTradesFromWanderer", true);
        builder.pop();

        builder.push("Chase the Skies");
        this.generateDriedGhasts = builder.comment("Determine if dried ghasts should generate in Nether Fossils").define("generateDriedGhasts", true);
        this.dropLeashConnectionsOnFireworkBoost = builder.comment("Determine if all leash connections should be dropped when using a firework rocket while elytra flying").define("dropLeashConnectionsOnFireworkBoost", true);
        builder.pop();

        builder.push("Spring to Life");
        this.generateBushes = builder.comment("Determine if bushes should generate in the overworld").define("bushes", true);
        this.generateFireflyBushes = builder.comment("Determine if firefly bushes should generate in the overworld").define("firefly_bushes", true);
        this.generateWildflowers = builder.comment("Determine if wildflowers should generate in the overworld").define("wildflowers", true);
        this.generateDryGrass = builder.comment("Determine if dry grass should generate in the overworld").define("dry_grass", true);
        this.generateFallenTrees = builder.comment("Determine if fallen trees should generate in the overworld").define("fallen_trees", true);
        this.generateLeafLitter = builder.comment("Determine if leaf litter should generate in the overworld").define("leaf_litter", true);
        builder.pop();
    }
}