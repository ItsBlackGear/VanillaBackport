package com.blackgear.vanillabackport.common;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class CommonConfig {
    // Bundles of Bravery
    public final ConfigBuilder.ConfigValue<Boolean> hasBundles;

    // The Garden Awakens
    public final ConfigBuilder.ConfigValue<Boolean> hasResin;
    public final ConfigBuilder.ConfigValue<Boolean> hasCreaking;
    public final ConfigBuilder.ConfigValue<Boolean> hasPaleGarden;
    public final ConfigBuilder.ConfigValue<Boolean> hasPaleTrades;

    // Spring to Life
    public final ConfigBuilder.ConfigValue<Boolean> hasBushes;
    public final ConfigBuilder.ConfigValue<Boolean> hasFireflyBushes;
    public final ConfigBuilder.ConfigValue<Boolean> hasWildflowers;
    public final ConfigBuilder.ConfigValue<Boolean> hasDryGrass;
    public final ConfigBuilder.ConfigValue<Boolean> hasFallenTrees;
    public final ConfigBuilder.ConfigValue<Boolean> hasLeafLitter;
    public final ConfigBuilder.ConfigValue<Boolean> hasFarmAnimalVariants;

    // Chase the Skies
    public final ConfigBuilder.ConfigValue<Boolean> hasDriedGhasts;
    public final ConfigBuilder.ConfigValue<Boolean> leashDropConnections;
    public final ConfigBuilder.ConfigValue<Boolean> hasTearsMusicDisc;

    // Hot as Lava
    public final ConfigBuilder.ConfigValue<Boolean> hasLavaChickenMusicDisc;

    public CommonConfig(ConfigBuilder builder) {
        builder.push("Bundles of Bravery");
        this.hasBundles = builder.comment("allow bundles to generate in village chests")
            .define("bundles", true);
        builder.pop();

        builder.push("The Garden Awakens");
        this.hasResin = builder.comment("allow resin to be obtainable from creaking hearts and woodland mansions")
            .define("resin", true);
        this.hasCreaking = builder.comment("allow creakings to spawn from creaking hearts")
            .define("creaking", true);
        this.hasPaleGarden = builder.comment("allow the pale garden to generate in the overworld")
            .define("pale_garden", true);
        this.hasPaleTrades = builder.comment("allow features from the garden awakens to be obtainable through wandering traders")
            .define("pale_trades", true);
        builder.pop();

        builder.push("Spring to Life");
        this.hasBushes = builder.comment("allow bushes to generate in the overworld")
            .define("bushes", true);
        this.hasFireflyBushes = builder.comment("allow firefly bushes to generate in the overworld")
            .define("firefly_bushes", true);
        this.hasWildflowers = builder.comment("allow firefly bushes to generate in the overworld")
            .define("wildflowers", true);
        this.hasDryGrass = builder.comment("allow dry grass to generate in the overworld")
            .define("dry_grass", true);
        this.hasFallenTrees = builder.comment("allow fallen trees to generate in the overworld")
            .define("fallen_trees", true);
        this.hasLeafLitter = builder.comment("allow leaf litter to generate in the overworld")
            .define("leaf_litter", true);
        this.hasFarmAnimalVariants = builder.comment("allow variants for pigs, cows and chickens to generate")
            .define("farm_animal_variants", true);
        builder.pop();

        builder.push("Chase the Skies");
        this.hasDriedGhasts = builder.comment("allow dried ghasts to be obtainable through nether fossils and piglin bartering")
            .define("dried_ghasts", true);
        this.leashDropConnections = builder.comment("toggle whether leashes drop their connections when boosting with a firework rocket")
            .define("leash_drop_connections", true);
        this.hasTearsMusicDisc = builder.comment("allows ghasts drop the tears music disc")
            .define("tears_music_disc", true);
        builder.pop();

        builder.push("Hot as Lava");
        this.hasLavaChickenMusicDisc = builder.comment("allows chicken jockeys to drop the lava chicken music disc")
            .define("lava_chicken_music_disc", true);
        builder.pop();
    }
}