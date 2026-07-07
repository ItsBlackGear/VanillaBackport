package com.blackgear.vanillabackport.common.registries.blocks;

import com.blackgear.platform.core.helper.BlockRegistry;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import com.blackgear.vanillabackport.common.level.block.*;
import com.blackgear.vanillabackport.common.level.block.properties.SharedBlockProperties;
import com.blackgear.vanillabackport.common.registries.worldgen.ModTreeGrowers;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.experimental.handlers.VanillaBlockRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class ModBlocks {
    public static final BlockRegistry REGISTRIES = BlockRegistry.create(VanillaBackport.NAMESPACE);
    public static final VanillaBlockRegistry HOLDERS = VanillaBlockRegistry.create();

    // The Garden Awakens
    
    public static final Supplier<Block> CREAKING_HEART = REGISTRIES.register("creaking_heart",
        CreakingHeartBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(10.0F)
            .sound(ModSoundTypes.CREAKING_HEART));
    
    public static final Supplier<Block> PALE_OAK_LOG = REGISTRIES.register("pale_oak_log",
        RotatedPillarBlock::new,
        SharedBlockProperties.logProperties(MapColor.QUARTZ, MapColor.STONE, SoundType.WOOD));
    public static final Supplier<Block> STRIPPED_PALE_OAK_LOG = REGISTRIES.register("stripped_pale_oak_log",
        RotatedPillarBlock::new,
        SharedBlockProperties.logProperties(MapColor.QUARTZ, MapColor.QUARTZ, SoundType.WOOD));
    public static final Supplier<Block> PALE_OAK_WOOD = REGISTRIES.register("pale_oak_wood",
        RotatedPillarBlock::new,
        Properties.of()
            .mapColor(MapColor.STONE)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Supplier<Block> STRIPPED_PALE_OAK_WOOD = REGISTRIES.register("stripped_pale_oak_wood",
        RotatedPillarBlock::new,
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    
    public static final Supplier<Block> PALE_OAK_PLANKS = REGISTRIES.register("pale_oak_planks", SharedBlockProperties.PALE_OAK);
    public static final Supplier<Block> PALE_OAK_SLAB = REGISTRIES.register("pale_oak_slab", SlabBlock::new, SharedBlockProperties.PALE_OAK);
    public static final Supplier<Block> PALE_OAK_STAIRS = REGISTRIES.register("pale_oak_stairs", () -> new StairBlock(PALE_OAK_PLANKS.get().defaultBlockState(), SharedBlockProperties.PALE_OAK));
    
    public static final Pair<Supplier<Block>, Supplier<Block>> PALE_OAK_SIGN = sign("pale_oak",
        ModWoodTypes.PALE_OAK,
        MapColor.QUARTZ);
    public static final Pair<Supplier<Block>, Supplier<Block>> PALE_OAK_HANGING_SIGN = hangingSign("pale_oak",
        ModWoodTypes.PALE_OAK,
        MapColor.QUARTZ);
    public static final Supplier<Block> PALE_OAK_BUTTON = REGISTRIES.register("pale_oak_button",
        properties -> new ButtonBlock(properties, ModBlockSetTypes.PALE_OAK, 30, true),
        SharedBlockProperties.buttonProperties());
    public static final Supplier<Block> PALE_OAK_PRESSURE_PLATE = REGISTRIES.register("pale_oak_pressure_plate",
        properties -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, properties, ModBlockSetTypes.PALE_OAK),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> PALE_OAK_DOOR = REGISTRIES.register("pale_oak_door",
        properties -> new DoorBlock(properties, ModBlockSetTypes.PALE_OAK),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0F)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> PALE_OAK_FENCE = REGISTRIES.register("pale_oak_fence",
        FenceBlock::new,
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Supplier<Block> PALE_OAK_FENCE_GATE = REGISTRIES.register("pale_oak_fence_gate",
        properties -> new FenceGateBlock(properties, ModWoodTypes.PALE_OAK),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .ignitedByLava());
    public static final Supplier<Block> PALE_OAK_TRAPDOOR = REGISTRIES.register("pale_oak_trapdoor",
        properties -> new TrapDoorBlock(properties, ModBlockSetTypes.PALE_OAK),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0F)
            .noOcclusion()
            .isValidSpawn(REGISTRIES::never)
            .ignitedByLava());
    
    public static final Supplier<Block> PALE_HANGING_MOSS = REGISTRIES.register("pale_hanging_moss",
        HangingMossBlock::new,
        Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .noCollission()
            .sound(SoundType.MOSS_CARPET)
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> PALE_MOSS_BLOCK = REGISTRIES.register("pale_moss_block",
        PaleMossBlock::new,
        Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.1F)
            .sound(SoundType.MOSS)
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> PALE_MOSS_CARPET = REGISTRIES.register("pale_moss_carpet",
        MossyCarpetBlock::new,
        Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.1F)
            .sound(SoundType.MOSS)
            .pushReaction(PushReaction.DESTROY));
    
    public static final Supplier<Block> PALE_OAK_LEAVES = REGISTRIES.register("pale_oak_leaves",
        properties -> new ParticleLeavesBlock(50, ModParticles.PALE_OAK_LEAVES, properties),
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_GREEN)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(REGISTRIES::ocelotOrParrot)
            .isSuffocating(REGISTRIES::never)
            .isViewBlocking(REGISTRIES::never)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(REGISTRIES::never));
    
    public static final Supplier<Block> PALE_OAK_SAPLING = REGISTRIES.register("pale_oak_sapling",
        properties -> new SaplingBlock(ModTreeGrowers.PALE_OAK_TREE, properties),
        Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> POTTED_PALE_OAK_SAPLING = REGISTRIES.registerNoItem("potted_pale_oak_sapling",
        properties -> new FlowerPotBlock(PALE_OAK_SAPLING.get(), properties),
        SharedBlockProperties.flowerPotProperties());
    
    public static final Supplier<Block> CLOSED_EYEBLOSSOM = REGISTRIES.register("closed_eyeblossom",
        properties -> new EyeblossomBlock(EyeblossomBlock.Type.CLOSED, properties),
        Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY)
            .randomTicks());
    public static final Supplier<Block> POTTED_CLOSED_EYEBLOSSOM = REGISTRIES.registerNoItem("potted_closed_eyeblossom",
        properties -> new EyeblossomFlowerPotBlock(CLOSED_EYEBLOSSOM.get(), properties),
        SharedBlockProperties.flowerPotProperties().randomTicks());
    
    public static final Supplier<Block> OPEN_EYEBLOSSOM = REGISTRIES.register("open_eyeblossom",
        properties -> new EyeblossomBlock(EyeblossomBlock.Type.OPEN, properties),
        Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY)
            .randomTicks());
    public static final Supplier<Block> POTTED_OPEN_EYEBLOSSOM = REGISTRIES.registerNoItem("potted_open_eyeblossom",
        properties -> new EyeblossomFlowerPotBlock(OPEN_EYEBLOSSOM.get(), properties),
        SharedBlockProperties.flowerPotProperties().randomTicks());
    
    public static final Supplier<Block> RESIN_BLOCK = REGISTRIES.register("resin_block",
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(ModSoundTypes.RESIN));
    
    public static final Supplier<Block> RESIN_BRICKS = REGISTRIES.register("resin_bricks", SharedBlockProperties.RESIN_BRICKS);
    public static final Supplier<Block> CHISELED_RESIN_BRICKS = REGISTRIES.register("chiseled_resin_bricks", SharedBlockProperties.RESIN_BRICKS);
    public static final Supplier<Block> RESIN_BRICK_SLAB = REGISTRIES.register("resin_brick_slab", SlabBlock::new, SharedBlockProperties.RESIN_BRICKS);
    public static final Supplier<Block> RESIN_BRICK_STAIRS = REGISTRIES.register("resin_brick_stairs", () -> new StairBlock(RESIN_BRICKS.get().defaultBlockState(), SharedBlockProperties.RESIN_BRICKS));
    public static final Supplier<Block> RESIN_BRICK_WALL = REGISTRIES.register("resin_brick_wall", WallBlock::new, SharedBlockProperties.RESIN_BRICKS);
    
    public static final Supplier<Block> RESIN_CLUMP = REGISTRIES.register("resin_clump",
        ResinClumpBlock::new,
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .replaceable()
            .noCollission()
            .sound(ModSoundTypes.RESIN)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY));
    
    // Spring to Life
    
    public static final Supplier<Block> LEAF_LITTER = REGISTRIES.register("leaf_litter",
        LeafLitterBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_BROWN)
            .replaceable()
            .noCollission()
            .sound(ModSoundTypes.LEAF_LITTER)
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> WILDFLOWERS = REGISTRIES.register("wildflowers",
        PinkPetalsBlock::new,
        Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .sound(SoundType.PINK_PETALS)
            .pushReaction(PushReaction.DESTROY));
    
    public static final Supplier<Block> BUSH = REGISTRIES.register("bush",
        ActualBushBlock::new,
        Properties.of()
            .mapColor(MapColor.PLANT)
            .replaceable()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> FIREFLY_BUSH = REGISTRIES.register("firefly_bush",
        FireflyBushBlock::new,
        Properties.of()
            .mapColor(MapColor.PLANT)
            .ignitedByLava()
            .lightLevel(state -> 2)
            .noCollission()
            .instabreak()
            .sound(SoundType.SWEET_BERRY_BUSH)
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> CACTUS_FLOWER = REGISTRIES.register("cactus_flower",
        CactusFlowerBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_PINK)
            .noCollission()
            .instabreak()
            .ignitedByLava()
            .sound(ModSoundTypes.CACTUS_FLOWER)
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> SHORT_DRY_GRASS = REGISTRIES.register("short_dry_grass",
        ShortDryGrassBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .replaceable()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> TALL_DRY_GRASS = REGISTRIES.register("tall_dry_grass",
        TallDryGrassBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .replaceable()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .pushReaction(PushReaction.DESTROY));
    
    // Chase the Skies
    
    public static final Supplier<Block> DRIED_GHAST = REGISTRIES.register("dried_ghast",
        DriedGhastBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .instabreak()
            .sound(ModSoundTypes.DRIED_GHAST)
            .noOcclusion()
            .randomTicks());
    
    // Copper Age
    
    public static final Supplier<Block> COPPER_CHEST = REGISTRIES.register("copper_chest",
        () -> new WeatheringCopperChestBlock(
            WeatherState.UNAFFECTED,
            ModSoundEvents.COPPER_CHEST_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(Blocks.COPPER_BLOCK.defaultMapColor())));
    public static final Supplier<Block> EXPOSED_COPPER_CHEST = REGISTRIES.register("exposed_copper_chest",
        () -> new WeatheringCopperChestBlock(
            WeatherState.EXPOSED,
            ModSoundEvents.COPPER_CHEST_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final Supplier<Block> WEATHERED_COPPER_CHEST = REGISTRIES.register("weathered_copper_chest",
        () -> new WeatheringCopperChestBlock(
            WeatherState.WEATHERED,
            ModSoundEvents.COPPER_CHEST_WEATHERED_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_WEATHERED_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(MapColor.WARPED_STEM)));
    public static final Supplier<Block> OXIDIZED_COPPER_CHEST = REGISTRIES.register("oxidized_copper_chest",
        () -> new WeatheringCopperChestBlock(
            WeatherState.OXIDIZED,
            ModSoundEvents.COPPER_CHEST_OXIDIZED_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_OXIDIZED_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(MapColor.WARPED_NYLIUM)));
    
    public static final Supplier<Block> WAXED_COPPER_CHEST = REGISTRIES.register("waxed_copper_chest",
        () -> new CopperChestBlock(
            WeatherState.UNAFFECTED,
            ModSoundEvents.COPPER_CHEST_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(Blocks.COPPER_BLOCK.defaultMapColor())));
    public static final Supplier<Block> WAXED_EXPOSED_COPPER_CHEST = REGISTRIES.register("waxed_exposed_copper_chest",
        () -> new CopperChestBlock(
            WeatherState.EXPOSED,
            ModSoundEvents.COPPER_CHEST_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final Supplier<Block> WAXED_WEATHERED_COPPER_CHEST = REGISTRIES.register("waxed_weathered_copper_chest",
        () -> new CopperChestBlock(
            WeatherState.WEATHERED,
            ModSoundEvents.COPPER_CHEST_WEATHERED_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_WEATHERED_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(MapColor.WARPED_STEM)));
    public static final Supplier<Block> WAXED_OXIDIZED_COPPER_CHEST = REGISTRIES.register("waxed_oxidized_copper_chest",
        () -> new CopperChestBlock(
            WeatherState.OXIDIZED,
            ModSoundEvents.COPPER_CHEST_OXIDIZED_OPEN.get(),
            ModSoundEvents.COPPER_CHEST_OXIDIZED_CLOSE.get(),
            SharedBlockProperties.COPPER_CHEST.mapColor(MapColor.WARPED_NYLIUM)));
    
    public static final Supplier<Block> ACACIA_SHELF = REGISTRIES.register("acacia_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.ACACIA_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> BAMBOO_SHELF = REGISTRIES.register("bamboo_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.BAMBOO_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> BIRCH_SHELF = REGISTRIES.register("birch_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.BIRCH_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> CHERRY_SHELF = REGISTRIES.register("cherry_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.CHERRY_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> CRIMSON_SHELF = REGISTRIES.register("crimson_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.CRIMSON_PLANKS.defaultMapColor()));
    public static final Supplier<Block> DARK_OAK_SHELF = REGISTRIES.register("dark_oak_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.DARK_OAK_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> JUNGLE_SHELF = REGISTRIES.register("jungle_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.JUNGLE_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> MANGROVE_SHELF = REGISTRIES.register("mangrove_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.MANGROVE_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> OAK_SHELF = REGISTRIES.register("oak_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> PALE_OAK_SHELF = REGISTRIES.register("pale_oak_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(MapColor.QUARTZ)
            .ignitedByLava());
    public static final Supplier<Block> SPRUCE_SHELF = REGISTRIES.register("spruce_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.SPRUCE_PLANKS.defaultMapColor())
            .ignitedByLava());
    public static final Supplier<Block> WARPED_SHELF = REGISTRIES.register("warped_shelf",
        ShelfBlock::new,
        SharedBlockProperties.SHELF
            .mapColor(Blocks.WARPED_PLANKS.defaultMapColor()));
    
    public static final Supplier<Block> EXPOSED_LIGHTNING_ROD = REGISTRIES.register("exposed_lightning_rod",
        properties -> new WeatheringLightningRodBlock(WeatherState.EXPOSED, properties),
        Properties.copy(Blocks.LIGHTNING_ROD).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY));
    public static final Supplier<Block> WEATHERED_LIGHTNING_ROD = REGISTRIES.register("weathered_lightning_rod",
        properties -> new WeatheringLightningRodBlock(WeatherState.WEATHERED, properties),
        Properties.copy(Blocks.LIGHTNING_ROD).mapColor(MapColor.WARPED_STEM));
    public static final Supplier<Block> OXIDIZED_LIGHTNING_ROD = REGISTRIES.register("oxidized_lightning_rod",
        properties -> new WeatheringLightningRodBlock(WeatherState.OXIDIZED, properties),
        Properties.copy(Blocks.LIGHTNING_ROD).mapColor(MapColor.WARPED_NYLIUM));
    
    public static final Supplier<Block> WAXED_LIGHTNING_ROD = REGISTRIES.register("waxed_lightning_rod",
        LightningRodBlock::new,
        Properties.copy(Blocks.LIGHTNING_ROD));
    public static final Supplier<Block> WAXED_EXPOSED_LIGHTNING_ROD = REGISTRIES.register("waxed_exposed_lightning_rod",
        LightningRodBlock::new,
        Properties.copy(Blocks.LIGHTNING_ROD).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY));
    public static final Supplier<Block> WAXED_WEATHERED_LIGHTNING_ROD = REGISTRIES.register("waxed_weathered_lightning_rod",
        LightningRodBlock::new,
        Properties.copy(Blocks.LIGHTNING_ROD).mapColor(MapColor.WARPED_STEM));
    public static final Supplier<Block> WAXED_OXIDIZED_LIGHTNING_ROD = REGISTRIES.register("waxed_oxidized_lightning_rod",
        LightningRodBlock::new,
        Properties.copy(Blocks.LIGHTNING_ROD).mapColor(MapColor.WARPED_NYLIUM));
    
    public static final Supplier<Block> COPPER_GOLEM_STATUE = REGISTRIES.register("copper_golem_statue",
        () -> new WeatheringCopperGolemStatueBlock(
            WeatherState.UNAFFECTED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(Blocks.COPPER_BLOCK.defaultMapColor())));
    public static final Supplier<Block> EXPOSED_COPPER_GOLEM_STATUE = REGISTRIES.register("exposed_copper_golem_statue",
        () -> new WeatheringCopperGolemStatueBlock(
            WeatherState.EXPOSED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final Supplier<Block> WEATHERED_COPPER_GOLEM_STATUE = REGISTRIES.register("weathered_copper_golem_statue",
        () -> new WeatheringCopperGolemStatueBlock(
            WeatherState.WEATHERED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(MapColor.WARPED_STEM)));
    public static final Supplier<Block> OXIDIZED_COPPER_GOLEM_STATUE = REGISTRIES.register("oxidized_copper_golem_statue",
        () -> new WeatheringCopperGolemStatueBlock(
            WeatherState.OXIDIZED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(MapColor.WARPED_NYLIUM)));
    
    public static final Supplier<Block> WAXED_COPPER_GOLEM_STATUE = REGISTRIES.register("waxed_copper_golem_statue",
        () -> new CopperGolemStatueBlock(
            WeatherState.UNAFFECTED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(Blocks.COPPER_BLOCK.defaultMapColor())));
    public static final Supplier<Block> WAXED_EXPOSED_COPPER_GOLEM_STATUE = REGISTRIES.register("waxed_exposed_copper_golem_statue",
        () -> new CopperGolemStatueBlock(
            WeatherState.EXPOSED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final Supplier<Block> WAXED_WEATHERED_COPPER_GOLEM_STATUE = REGISTRIES.register("waxed_weathered_copper_golem_statue",
        () -> new CopperGolemStatueBlock(
            WeatherState.WEATHERED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(MapColor.WARPED_STEM)));
    public static final Supplier<Block> WAXED_OXIDIZED_COPPER_GOLEM_STATUE = REGISTRIES.register("waxed_oxidized_copper_golem_statue",
        () -> new CopperGolemStatueBlock(
            WeatherState.OXIDIZED,
            SharedBlockProperties.COPPER_GOLEM_STATUE.mapColor(MapColor.WARPED_NYLIUM)));
    
    public static final Pair<Supplier<Block>, Supplier<Block>> COPPER_TORCH = torch("copper", ModParticles.COPPER_FIRE_FLAME);
    public static final WeatheringCopperBlocks COPPER_LANTERN = WeatheringCopperBlocks.create("copper_lantern",
        REGISTRIES::register,
        LanternBlock::new,
        WeatheringLanternBlock::new,
        properties -> Properties.of()
            .mapColor(MapColor.METAL)
            .forceSolidOn()
            .strength(3.5F)
            .sound(SoundType.LANTERN)
            .lightLevel(state -> 15)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY));
    public static final WeatheringCopperBlocks COPPER_BARS = WeatheringCopperBlocks.create("copper_bars",
        REGISTRIES::register,
        IronBarsBlock::new,
        WeatheringCopperBarsBlock::new,
        properties -> Properties.of()
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.COPPER)
            .noOcclusion());
    public static final WeatheringCopperBlocks COPPER_CHAIN = WeatheringCopperBlocks.create("copper_chain",
        REGISTRIES::register,
        ChainBlock::new,
        WeatheringCopperChainBlock::new,
        properties -> Properties.of()
            .forceSolidOn()
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.COPPER)
            .noOcclusion());
    
    // Chaos Cubed
    
    public static final Supplier<Block> CINNABAR = REGISTRIES.register("cinnabar", SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_SLAB = REGISTRIES.register("cinnabar_slab", SlabBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_STAIRS = REGISTRIES.register("cinnabar_stairs", () -> new StairBlock(CINNABAR.get().defaultBlockState(), SharedBlockProperties.CINNABAR));
    public static final Supplier<Block> CINNABAR_WALL = REGISTRIES.register("cinnabar_wall", WallBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> POLISHED_CINNABAR = REGISTRIES.register("polished_cinnabar", SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> POLISHED_CINNABAR_SLAB = REGISTRIES.register("polished_cinnabar_slab", SlabBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> POLISHED_CINNABAR_STAIRS = REGISTRIES.register("polished_cinnabar_stairs", () -> new StairBlock(POLISHED_CINNABAR.get().defaultBlockState(), SharedBlockProperties.CINNABAR));
    public static final Supplier<Block> POLISHED_CINNABAR_WALL = REGISTRIES.register("polished_cinnabar_wall", WallBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_BRICKS = REGISTRIES.register("cinnabar_bricks", SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_BRICK_SLAB = REGISTRIES.register("cinnabar_brick_slab", SlabBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_BRICK_STAIRS = REGISTRIES.register("cinnabar_brick_stairs", () -> new StairBlock(CINNABAR_BRICKS.get().defaultBlockState(), SharedBlockProperties.CINNABAR));
    public static final Supplier<Block> CINNABAR_BRICK_WALL = REGISTRIES.register("cinnabar_brick_wall", WallBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CHISELED_CINNABAR = REGISTRIES.register("chiseled_cinnabar", SharedBlockProperties.CINNABAR);

    public static final Supplier<Block> SULFUR = REGISTRIES.register("sulfur", SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_SLAB = REGISTRIES.register("sulfur_slab", SlabBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_STAIRS = REGISTRIES.register("sulfur_stairs", () -> new StairBlock(SULFUR.get().defaultBlockState(), SharedBlockProperties.SULFUR));
    public static final Supplier<Block> SULFUR_WALL = REGISTRIES.register("sulfur_wall", WallBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> POLISHED_SULFUR = REGISTRIES.register("polished_sulfur", SharedBlockProperties.SULFUR);
    public static final Supplier<Block> POLISHED_SULFUR_SLAB = REGISTRIES.register("polished_sulfur_slab", SlabBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> POLISHED_SULFUR_STAIRS = REGISTRIES.register("polished_sulfur_stairs", () -> new StairBlock(POLISHED_SULFUR.get().defaultBlockState(), SharedBlockProperties.SULFUR));
    public static final Supplier<Block> POLISHED_SULFUR_WALL = REGISTRIES.register("polished_sulfur_wall", WallBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_BRICKS = REGISTRIES.register("sulfur_bricks", SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_BRICK_SLAB = REGISTRIES.register("sulfur_brick_slab", SlabBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_BRICK_STAIRS = REGISTRIES.register("sulfur_brick_stairs", () -> new StairBlock(SULFUR_BRICKS.get().defaultBlockState(), SharedBlockProperties.SULFUR));
    public static final Supplier<Block> SULFUR_BRICK_WALL = REGISTRIES.register("sulfur_brick_wall", WallBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> CHISELED_SULFUR = REGISTRIES.register("chiseled_sulfur", SharedBlockProperties.SULFUR);

    public static final Supplier<Block> POTENT_SULFUR = REGISTRIES.register("potent_sulfur", PotentSulfurBlock::new, SharedBlockProperties.SULFUR.sound(ModSoundTypes.POTENT_SULFUR));
    public static final Supplier<Block> SULFUR_SPIKE = REGISTRIES.register("sulfur_spike",
        () -> new SpeleothemBlock(
            SULFUR.get().defaultBlockState(),
            Properties.of()
                .mapColor(MapColor.COLOR_YELLOW)
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASEDRUM)
                .noOcclusion()
                .sound(SoundType.POINTED_DRIPSTONE)
                .randomTicks()
                .strength(1.5F, 3.0F)
                .dynamicShape()
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(REGISTRIES::never)
                .noOcclusion()));
    
    // Helper
    
    public static Pair<Supplier<Block>, Supplier<Block>> sign(String name, WoodType woodType, MapColor color) {
        Properties properties = Properties.of()
            .mapColor(color)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava();
        Supplier<Block> standing = REGISTRIES.registerNoItem(name + "_sign", () -> new StandingSignBlock(properties, woodType));
        Supplier<Block> wall = REGISTRIES.registerNoItem(name + "_wall_sign", () -> new WallSignBlock(properties.dropsLike(standing.get()), woodType));
        REGISTRIES.registerItem(name + "_sign", () -> new SignItem(new Item.Properties().stacksTo(16), standing.get(), wall.get()));
        return new Pair<>(standing, wall);
    }
    
    public static Pair<Supplier<Block>, Supplier<Block>> hangingSign(String name, WoodType woodType, MapColor color) {
        Properties properties = Properties.of()
            .mapColor(color)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava();

        Supplier<Block> ceiling = REGISTRIES.registerNoItem(name + "_hanging_sign", () -> new CeilingHangingSignBlock(properties, woodType));
        Supplier<Block> wall = REGISTRIES.registerNoItem(name + "_wall_hanging_sign", () -> new WallHangingSignBlock(properties.dropsLike(ceiling.get()), woodType));
        REGISTRIES.registerItem(name + "_hanging_sign", () -> new HangingSignItem(ceiling.get(), wall.get(), new Item.Properties().stacksTo(16)));
        return new Pair<>(ceiling, wall);
    }
    
    public static Pair<Supplier<Block>, Supplier<Block>> torch(String name, Supplier<SimpleParticleType> particle) {
        Properties properties = Properties.of()
            .noCollission()
            .instabreak()
            .lightLevel(state -> 14)
            .sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY);
        
        Supplier<Block> torch = REGISTRIES.registerNoItem(name + "_torch", () -> new LazyTorchBlock(particle, properties));
        Supplier<Block> wall_torch = REGISTRIES.registerNoItem(name + "_wall_torch", () -> new LazyWallTorchBlock(particle, properties.dropsLike(torch.get())));
        REGISTRIES.registerItem(name + "_torch", () -> new StandingAndWallBlockItem(torch.get(), wall_torch.get(), new Item.Properties(), Direction.DOWN));
        return new Pair<>(torch, wall_torch);
    }
}