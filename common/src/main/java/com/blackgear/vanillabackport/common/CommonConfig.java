package com.blackgear.vanillabackport.common;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class CommonConfig {
    // Armored Paws
    public final ConfigBuilder.ConfigValue<Boolean> hasArmadillos;
    public final ConfigBuilder.ConfigValue<Boolean> hasWolfVariants;
    public final ConfigBuilder.ConfigValue<Boolean> updatedWolfSpawns;

    // Bundles of Bravery
    public final ConfigBuilder.ConfigValue<Boolean> hasModernBundles;
    public final ConfigBuilder.ConfigValue<Boolean> hasBundleLoot;
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
    public final ConfigBuilder.ConfigValue<Double> mobVariantSpawnChance;
    public final ConfigBuilder.ConfigValue<Double> cactusFlowerSpawnChance;

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
    public final ConfigBuilder.ConfigValue<Integer> golemWeatheringTickFrom;
    public final ConfigBuilder.ConfigValue<Integer> golemWeatheringTickTo;
    public final ConfigBuilder.ConfigValue<Integer> golemItemCarryAmount;
    
    // Mounts of Mayhem
    public final ConfigBuilder.ConfigValue<Boolean> hasParchedSkeletons;
    public final ConfigBuilder.ConfigValue<Boolean> hasCamelHusks;
    public final ConfigBuilder.ConfigValue<Boolean> hasNautilus;
    public final ConfigBuilder.ConfigValue<Boolean> hasZombieHorses;
    public final ConfigBuilder.ConfigValue<Boolean> hasZombieNautilus;
    public final ConfigBuilder.ConfigValue<Boolean> hasNautilusArmorLoot;
    public final ConfigBuilder.ConfigValue<Boolean> canMonstersSpawnWithSpears;
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
            this.hasModernBundles = builder.comment("enable the modern bundle interface and controls")
                .define("has_updated_bundles", true);
            this.hasBundleLoot = builder.comment("allow Bundles to generate in Village loot chests")
                .define("bundle_loot", true);
            this.hasDyeableBundleRecipe = builder.comment("allow bundles to be dyed")
                .define("has_dyeable_bundle_recipe", true);
        builder.pop();

        builder.push("The Garden Awakens");
            this.hasResin = builder.comment("allow Resin to be obtained from Creaking Hearts")
                .define("resin", true);
            this.hasResinLoot = builder.comment("allow Resin to generate in Woodland Mansion loot chests")
                .define("resin_loot", true);
            this.hasCreaking = builder.comment("allow Creakings to spawn from Creaking Hearts")
                .define("creaking", true);
            this.hasPaleGarden = builder.comment("allow Pale Garden biomes to generate")
                .define("pale_garden", true);
            this.doMerchantTradeTheGardenAwakensContents = builder.comment("Wandering Traders to offer items from The Garden Awakens")
                .define("pale_trades", true);
            this.creakingParticleColor = builder.comment("primary color of Creaking Heart trail particles")
                .define("creaking_particle_color", 6250335);
            this.creakingParticleReverseColor = builder.comment("secondary color of Creaking Heart trail particles")
                .define("creaking_particle_reverse_color", 16545810);
            this.doCreakingHeartsWorkOnDay = builder.comment("allow Creaking Hearts to spawn Creakings during the day")
                .define("do_creaking_hearts_work_on_day", false);
        builder.pop();

        builder.push("Spring to Life");
            this.hasBushes = builder.comment("allow Bushes to generate")
                .define("bushes", true);
            this.hasFireflyBushes = builder.comment("allow Firefly Bushes to generate")
                .define("firefly_bushes", true);
            this.hasWildflowers = builder.comment("allow Wildflowers to generate")
                .define("wildflowers", true);
            this.hasDryGrass = builder.comment("allow Dry Grass to generate")
                .define("dry_grass", true);
            this.hasFallenTrees = builder.comment("allow Fallen Trees to generate")
                .define("fallen_trees", true);
            this.hasLeafLitter = builder.comment("allow Leaf Litter to generate")
                .define("leaf_litter", true);
            this.hasCactusFlowers = builder.comment("allow Cactus Flowers to generate")
                .define("cactus_flowers", true);
            this.hasFarmAnimalVariants = builder.comment("allow Chicken, Cow, and Pig variants to spawn")
                .define("farm_animal_variants", true);
            this.hasWolfSoundVariants = builder.comment("allow wolves to have sound variants")
                .define("wolf_sound_variants", true);
            this.hasCamelSpawns = builder.comment("allow Camels to spawn naturally")
                .define("camel_spawns", true);
            this.doMerchantTradeSpringToLifeContents = builder.comment("allow Wandering Traders to offer items from Spring to Life")
                .define("spring_trades", true);
            this.hasLodestoneLoot = builder.comment("allow Lodestones to generate in Ruined Portal loot chests")
                .define("lodestone_loot", true);
            this.mobVariantSpawnChance = builder.comment("global spawn chance for mob variants; use datapacks for per-variant weighting")
                .defineInRange("mob_variants_spawn_chance", 1.0, 0.0, 1.0);
            this.cactusFlowerSpawnChance = builder.comment("chance for a cactus to generate with a Cactus Flower")
                .defineInRange("cactus_flower_spawn_chance", 0.25, 0.0, 1.0);
        builder.pop();

        builder.push("Chase the Skies");
            this.hasDriedGhasts = builder.comment("allow Dried Ghasts to be obtainable through Nether Fossils and Piglin bartering")
                .define("dried_ghasts", true);
            this.leashDropConnections = builder.comment("drop leash connections when using a Firework Rocket boost")
                .define("leash_drop_connections", true);
            this.hasTearsMusicDisc = builder.comment("allow Ghasts to drop the Tears music disc")
                .define("tears_music_disc", true);
            this.happyGhastSpeedModifier = builder.comment("movement speed multiplier for ridden Happy Ghasts")
                .define("happy_ghast_speed_modifier", 1.0);
        builder.pop();

        builder.push("Hot as Lava");
            this.hasLavaChickenMusicDisc = builder.comment("allow Chicken jockeys to drop the Lava Chicken music disc")
                .define("lava_chicken_music_disc", true);
        builder.pop();
        
        builder.push("Copper Age");
            this.hasCopperHorseArmorLoot = builder.comment("allow Copper Horse Armor to generate in loot chests")
                .define("has_copper_horse_armor_loot", true);
            this.hasCopperGolems = builder.comment("allow Copper Golems to be built using a Carved Pumpkin")
                .define("has_copper_golems", true);
            this.golemWeatheringTickFrom = builder.comment("minimum ticks before a Copper Golem advances to the next weathering stage")
                .defineInRange("golem_weathering_tick_from", 504000, 0, Integer.MAX_VALUE);
            this.golemWeatheringTickTo = builder.comment("maximum ticks before a Copper Golem advances to the next weathering stage")
                .defineInRange("golem_weathering_tick_to", 552000, 0, Integer.MAX_VALUE);
            this.golemItemCarryAmount = builder.comment("maximum number of items a Copper Golem can carry")
                .defineInRange("golem_item_carry_amount", 16, 1, Integer.MAX_VALUE);
        builder.pop();

        builder.push("Mounts of Mayhem");
            this.hasParchedSkeletons = builder.comment("allow Parched Skeletons to spawn naturally")
                .define("has_parched_skeletons", true);
            this.hasCamelHusks = builder.comment("allow Camel Husks to spawn naturally")
                .define("has_camel_husks", true);
            this.hasNautilus = builder.comment("allow Nautilus to spawn naturally")
                .define("has_nautilus", true);
            this.hasZombieHorses = builder.comment("allow Zombie Horses to spawn naturally")
                .define("has_zombie_horses", true);
            this.hasZombieNautilus = builder.comment("allow Zombie Nautilus to spawn naturally")
                .define("has_zombie_nautilus", true);
            this.hasNautilusArmorLoot = builder.comment("allow Nautilus Armor to generate in loot chests")
                .define("has_nautilus_armor_loot", true);
            this.canMonstersSpawnWithSpears = builder.comment("allow Zombies, Husks, Piglins and Zombified Piglins to spawn holding spears")
                .define("can_monsters_spawn_with_spears", true);
            this.canMountsFloatWhileRidden = builder.comment("allow mounts to float while ridden")
                .define("can_mounts_float_while_ridden", true);
        builder.pop();
        
        builder.push("Chaos Cubed");
            this.hasSulfurCaves = builder.comment("allow Sulfur Cave biomes to generate")
                .define("has_sulfur_caves", true);
            this.hasSulfurSprings = builder.comment("allow Sulfur Springs to generate in Sulfur Caves")
                .define("has_sulfur_springs", true);
            this.doGeysersErupt = builder.comment("allow Potent Sulfur blocks to generate Geysers")
                .define("do_geysers_erupt", true);
            this.hasSulfurCubes = builder.comment("allow Sulfur Cubes to spawn in Sulfur Caves")
                .define("has_sulfur_cubes", true);
            this.doSulfurCubesExplode = builder.comment("allow explosive Sulfur Cube archetypes to explode")
                .define("do_sulfur_cubes_explode", true);
            this.doSulfurCubesDealDamage = builder.comment("allow damaging Sulfur Cube archetypes to deal damage")
                .define("do_sulfur_cubes_damage", true);
            this.doMerchantTradeChaosCubedContents = builder.comment("allow Wandering Traders to offer items from Chaos Cubed")
                .define("do_merchant_trade_chaos_cubed_contents", true);
            this.hasBounceMusicDisc = builder.comment("allows the Bounce music disc to generate in Abandoned Mineshafts loot chests")
                .define("has_bounce_music_disc", true);
        builder.pop();
    }
}