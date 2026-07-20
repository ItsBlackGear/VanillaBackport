package com.blackgear.vanillabackport.common;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class CommonConfig {
    // Armored Paws
    public final ConfigBuilder.ConfigValue<Boolean> hasArmadillos;
    public final ConfigBuilder.ConfigValue<Boolean> hasWolfVariants;
    public final ConfigBuilder.ConfigValue<Boolean> updatedWolfSpawns;

    // Bundles of Bravery
    public final ConfigBuilder.ConfigValue<Boolean> hasUpdatedBundles;
    public final ConfigBuilder.ConfigValue<Boolean> hasBundleLoot;
    public final ConfigBuilder.ConfigValue<Boolean> hasModernBundleGraphics;
    public final ConfigBuilder.ConfigValue<Boolean> hasDyeableBundleRecipe;

    // The Garden Awakens
    public final ConfigBuilder.ConfigValue<Boolean> hasResin;
    public final ConfigBuilder.ConfigValue<Boolean> hasResinLoot;
    public final ConfigBuilder.ConfigValue<Boolean> hasCreaking;
    public final ConfigBuilder.ConfigValue<Boolean> hasPaleGarden;
    public final ConfigBuilder.ConfigValue<Boolean> doMerchantTradeTheGardenAwakensContents;
    public final ConfigBuilder.ConfigValue<Integer> creakingParticleColor;
    public final ConfigBuilder.ConfigValue<Integer> creakingParticleReverseColor;
    public final ConfigBuilder.ConfigValue<Boolean> doCreakingHeartsWorkOnDay;

    // Spring to Life
    public final ConfigBuilder.ConfigValue<Boolean> hasBushes;
    public final ConfigBuilder.ConfigValue<Boolean> hasFireflyBushes;
    public final ConfigBuilder.ConfigValue<Boolean> hasWildflowers;
    public final ConfigBuilder.ConfigValue<Boolean> hasDryGrass;
    public final ConfigBuilder.ConfigValue<Boolean> hasFallenTrees;
    public final ConfigBuilder.ConfigValue<Boolean> hasLeafLitter;
    public final ConfigBuilder.ConfigValue<Boolean> hasCactusFlowers;
    public final ConfigBuilder.ConfigValue<Boolean> hasFarmAnimalVariants;
    public final ConfigBuilder.ConfigValue<Boolean> hasWolfSoundVariants;
    public final ConfigBuilder.ConfigValue<Boolean> hasCamelSpawns;
    public final ConfigBuilder.ConfigValue<Boolean> doMerchantTradeSpringToLifeContents;
    public final ConfigBuilder.ConfigValue<Boolean> hasLodestoneLoot;

    // Chase the Skies
    public final ConfigBuilder.ConfigValue<Boolean> hasDriedGhasts;
    public final ConfigBuilder.ConfigValue<Boolean> leashDropConnections;
    public final ConfigBuilder.ConfigValue<Boolean> hasTearsMusicDisc;
    public final ConfigBuilder.ConfigValue<Double> happyGhastSpeedModifier;

    // Hot as Lava
    public final ConfigBuilder.ConfigValue<Boolean> hasLavaChickenMusicDisc;

    // Copper Age
    public final ConfigBuilder.ConfigValue<Boolean> hasCopperHorseArmorLoot;
    public final ConfigBuilder.ConfigValue<Boolean> hasCopperGolems;

    // Mounts of Mayhem
    public final ConfigBuilder.ConfigValue<Boolean> hasParchedSkeletons;
    public final ConfigBuilder.ConfigValue<Boolean> hasCamelHusks;
    public final ConfigBuilder.ConfigValue<Boolean> hasNautilus;
    public final ConfigBuilder.ConfigValue<Boolean> hasZombieHorses;
    public final ConfigBuilder.ConfigValue<Boolean> hasZombieNautilus;
    public final ConfigBuilder.ConfigValue<Boolean> hasNautilusArmorLoot;
    public final ConfigBuilder.ConfigValue<Boolean> canMobsSpawnWithSpears;
    public final ConfigBuilder.ConfigValue<Boolean> canMountsFloatWhileRidden;
    
    // Chaos Cubed
    public final ConfigBuilder.ConfigValue<Boolean> hasSulfurCaves;
    public final ConfigBuilder.ConfigValue<Boolean> hasSulfurSprings;
    public final ConfigBuilder.ConfigValue<Boolean> doGeysersErupt;
    public final ConfigBuilder.ConfigValue<Boolean> hasSulfurCubes;
    public final ConfigBuilder.ConfigValue<Boolean> doSulfurCubesExplode;
    public final ConfigBuilder.ConfigValue<Boolean> doSulfurCubesDealDamage;
    public final ConfigBuilder.ConfigValue<Boolean> doMerchantTradeChaosCubedContents;
    public final ConfigBuilder.ConfigValue<Boolean> hasBounceMusicDisc;

    public CommonConfig(ConfigBuilder builder) {
        builder.push("Armored Paws");
        this.hasArmadillos = builder.comment("allow armadillos to generate in the overworld")
            .define("armadillo", true);
        this.hasWolfVariants = builder.comment("allow variants for wolves to generate")
            .define("wolf_variants", true);
        this.updatedWolfSpawns = builder.comment("adds wolves to more biomes for natural spawning")
            .define("updated_wolf_spawns", true);
        builder.pop();

        builder.push("Bundles of Bravery");
        this.hasUpdatedBundles = builder.comment("toggle the updated controls and UI for bundles")
            .define("has_updated_bundles", true);
        this.hasBundleLoot = builder.comment("allow bundles to appear on chests at villages")
            .define("bundle_loot", true);
        this.hasModernBundleGraphics = builder.comment("toggle the modern rendering for bundles")
            .define("has_modern_bundle_graphics", true);
        this.hasDyeableBundleRecipe = builder.comment("toggle whether bundles should be dyeable")
            .define("has_dyeable_bundle_recipe", true);
        builder.pop();

        builder.push("The Garden Awakens");
        this.hasResin = builder.comment("allow resin to be obtainable from creaking hearts")
            .define("resin", true);
        this.hasResinLoot = builder.comment("allow resin to appear on chests at woodland mansions")
            .define("resin_loot", true);
        this.hasCreaking = builder.comment("allow creakings to spawn from creaking hearts")
            .define("creaking", true);
        this.hasPaleGarden = builder.comment("allow the pale garden to generate in the overworld")
            .define("pale_garden", true);
        this.doMerchantTradeTheGardenAwakensContents = builder.comment("allow features from 'The Garden Awakens' to be obtainable through wandering traders")
            .define("pale_trades", true);
        this.creakingParticleColor = builder.comment("creaking heart trail particle color (gray by default)")
            .define("creaking_particle_color", 6250335);
        this.creakingParticleReverseColor = builder.comment("creaking heart trail particle reverse color (orange by default)")
            .define("creaking_particle_reverse_color", 16545810);
        this.doCreakingHeartsWorkOnDay = builder.comment("allow Creaking Hearts to generate Creakings even during the day")
            .define("do_creaking_hearts_work_on_day", false);
        builder.pop();

        builder.push("Spring to Life");
        this.hasBushes = builder.comment("allow bushes to generate in the overworld")
            .define("bushes", true);
        this.hasFireflyBushes = builder.comment("allow firefly bushes to generate in the overworld")
            .define("firefly_bushes", true);
        this.hasWildflowers = builder.comment("allow wildflowers to generate in the overworld")
            .define("wildflowers", true);
        this.hasDryGrass = builder.comment("allow dry grass to generate in the overworld")
            .define("dry_grass", true);
        this.hasFallenTrees = builder.comment("allow fallen trees to generate in the overworld")
            .define("fallen_trees", true);
        this.hasLeafLitter = builder.comment("allow leaf litter to generate in the overworld")
            .define("leaf_litter", true);
        this.hasCactusFlowers = builder.comment("allow cactus flowers to generate")
            .define("cactus_flowers", true);
        this.hasFarmAnimalVariants = builder.comment("allow variants for pigs, cows and chickens to generate")
            .define("farm_animal_variants", true);
        this.hasWolfSoundVariants = builder.comment("allow wolfs to have variants for their sounds")
            .define("wolf_sound_variants", true);
        this.doMerchantTradeSpringToLifeContents = builder.comment("allow features from 'Spring to Life' to be obtainable through wandering traders")
            .define("spring_trades", true);
        this.hasCamelSpawns = builder.comment("allow camels to spawn outside of villages")
            .define("camel_spawns", true);
        this.hasLodestoneLoot = builder.comment("allow lodestones to appear on chests at ruined portals")
            .define("lodestone_loot", true);
        builder.pop();

        builder.push("Chase the Skies");
        this.hasDriedGhasts = builder.comment("allow dried ghasts to be obtainable through nether fossils and piglin bartering")
            .define("dried_ghasts", true);
        this.leashDropConnections = builder.comment("toggle whether leashes drop their connections when boosting with a firework rocket")
            .define("leash_drop_connections", true);
        this.hasTearsMusicDisc = builder.comment("allows ghasts drop the tears music disc")
            .define("tears_music_disc", true);
        this.happyGhastSpeedModifier = builder.comment("apply a modifier to the speed of happy ghasts when ridden, 1.0 is default speed")
            .define("happy_ghast_speed_modifier", 1.0);
        builder.pop();

        builder.push("Hot as Lava");
        this.hasLavaChickenMusicDisc = builder.comment("allows chicken jockeys to drop the lava chicken music disc")
            .define("lava_chicken_music_disc", true);
        builder.pop();

        builder.push("Copper Age");
        this.hasCopperHorseArmorLoot = builder.comment("allow Copper Horse Armor to generate on chests").define("has_copper_horse_armor_loot", true);
        this.hasCopperGolems = builder.comment("allow Copper Golems to be built from Carved Pumpkins").define("has_copper_golems", true);
        builder.pop();

        builder.push("Mounts of Mayhem");
        this.hasParchedSkeletons = builder.comment("allow Parched Skeletons to spawn naturally").define("has_parched_skeletons", true);
        this.hasCamelHusks = builder.comment("allow Camel Husks to spawn naturally").define("has_camel_husks", true);
        this.hasNautilus = builder.comment("allow Nautilus to spawn naturally").define("has_nautilus", true);
        this.hasZombieHorses = builder.comment("allow Zombie Horses to spawn naturally").define("has_zombie_horses", true);
        this.hasZombieNautilus = builder.comment("allow Zombie Nautilus to spawn naturally").define("has_zombie_nautilus", true);
        this.hasNautilusArmorLoot = builder.comment("allow Nautilus Armor to generate on chests").define("has_nautilus_armor_loot", true);
        this.canMobsSpawnWithSpears = builder.comment("allow Mobs to spawn holding spears").define("can_mobs_spawn_with_spears", true);
        this.canMountsFloatWhileRidden = builder.comment("allow Mount Mobs to float while ridden").define("can_mobs_spawn_with_spears", true);
        builder.pop();
        
        builder.push("Chaos Cubed");
        this.hasSulfurCaves = builder.comment("allow the generation of Sulfur Cave biomes").define("has_sulfur_caves", true);
        this.hasSulfurSprings = builder.comment("allow the generation of Sulfur Springs on top of Sulfur Caves").define("has_sulfur_springs", true);
        this.doGeysersErupt = builder.comment("allow Potent Sulfur Blocks to generate Geysers").define("do_geysers_erupt", true);
        this.hasSulfurCubes = builder.comment("allow Sulfur Cubes to spawn on Sulfur Caves").define("has_sulfur_cubes", true);
        this.doSulfurCubesExplode = builder.comment("allow Sulfur Cubes with exploding archetypes to explode").define("do_sulfur_cubes_explode", true);
        this.doSulfurCubesDealDamage = builder.comment("allow Sulfur Cubes with damaging archetypes to deal damage").define("do_sulfur_cubes_damage", true);
        this.doMerchantTradeChaosCubedContents = builder.comment("allow Wanderer traders to sell contents from Chaos Cubed").define("do_merchant_trade_chaos_cubed_contents", true);
        this.hasBounceMusicDisc = builder.comment("allows the bounce music disc to generate on abandoned mineshafts").define("has_bounce_music_disc", true);
        builder.pop();
    }
}