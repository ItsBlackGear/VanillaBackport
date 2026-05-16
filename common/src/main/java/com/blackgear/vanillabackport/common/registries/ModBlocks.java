package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.BlockRegistry;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import com.blackgear.vanillabackport.common.level.blocks.*;
import com.blackgear.vanillabackport.common.level.blocks.properties.SharedBlockProperties;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;
import java.util.function.Supplier;

public class ModBlocks {
    public static final BlockRegistry BLOCKS = BlockRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<Block> PALE_OAK_LEAVES = BLOCKS.register(
        "pale_oak_leaves",
        properties -> new ParticleLeavesBlock(50, ModParticles.PALE_OAK_LEAVES, properties),
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_GREEN)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(BLOCKS::ocelotOrParrot)
            .isSuffocating(BLOCKS::never)
            .isViewBlocking(BLOCKS::never)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(BLOCKS::never)
    );
    public static final Supplier<Block> PALE_OAK_PLANKS = BLOCKS.register(
        "pale_oak_planks",
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_STAIRS = BLOCKS.register(
        "pale_oak_stairs",
        properties -> new StairBlock(PALE_OAK_PLANKS.get().defaultBlockState(), properties),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_SLAB = BLOCKS.register(
        "pale_oak_slab",
        SlabBlock::new,
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_FENCE = BLOCKS.register(
        "pale_oak_fence",
        FenceBlock::new,
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_FENCE_GATE = BLOCKS.register(
        "pale_oak_fence_gate",
        properties -> new FenceGateBlock(ModWoodTypes.PALE_OAK, properties),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_DOOR = BLOCKS.register(
        "pale_oak_door",
        properties -> new DoorBlock(ModBlockSetTypes.PALE_OAK, properties),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0F)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> PALE_OAK_WOOD = BLOCKS.register(
        "pale_oak_wood",
        RotatedPillarBlock::new,
        Properties.of()
            .mapColor(MapColor.STONE)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_LOG = BLOCKS.register(
        "pale_oak_log",
        RotatedPillarBlock::new,
        logProperties(MapColor.QUARTZ, MapColor.STONE, SoundType.WOOD)
    );
    public static final Supplier<Block> STRIPPED_PALE_OAK_WOOD = BLOCKS.register(
        "stripped_pale_oak_wood",
        RotatedPillarBlock::new,
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> STRIPPED_PALE_OAK_LOG = BLOCKS.register(
        "stripped_pale_oak_log",
        RotatedPillarBlock::new,
        logProperties(MapColor.QUARTZ, MapColor.QUARTZ, SoundType.WOOD)
    );
    public static final Supplier<Block> PALE_MOSS_BLOCK = BLOCKS.register(
        "pale_moss_block",
        PaleMossBlock::new,
        Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.1F)
            .sound(SoundType.MOSS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> PALE_MOSS_CARPET = BLOCKS.register(
        "pale_moss_carpet",
        MossyCarpetBlock::new,
        Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.1F)
            .sound(SoundType.MOSS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> PALE_HANGING_MOSS = BLOCKS.register(
        "pale_hanging_moss",
        HangingMossBlock::new,
        Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .noCollission()
            .sound(SoundType.MOSS_CARPET)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> OPEN_EYEBLOSSOM = BLOCKS.register(
        "open_eyeblossom",
        properties -> new EyeblossomBlock(EyeblossomBlock.Type.OPEN, properties),
        Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY)
            .randomTicks()
    );
    public static final Supplier<Block> CLOSED_EYEBLOSSOM = BLOCKS.register(
        "closed_eyeblossom",
        properties -> new EyeblossomBlock(EyeblossomBlock.Type.CLOSED, properties),
        Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY)
            .randomTicks()
    );
    public static final Supplier<Block> PALE_OAK_SAPLING = BLOCKS.register(
        "pale_oak_sapling",
        properties -> new SaplingBlock(new TreeGrower("pale_oak", Optional.of(TheGardenAwakensFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty()), properties),
        Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> POTTED_OPEN_EYEBLOSSOM = BLOCKS.registerNoItem(
        "potted_open_eyeblossom",
        properties -> new EyeblossomFlowerPotBlock(OPEN_EYEBLOSSOM.get(), properties),
        flowerPotProperties().randomTicks()
    );
    public static final Supplier<Block> POTTED_CLOSED_EYEBLOSSOM = BLOCKS.registerNoItem(
        "potted_closed_eyeblossom",
        properties -> new EyeblossomFlowerPotBlock(CLOSED_EYEBLOSSOM.get(), properties),
        flowerPotProperties().randomTicks()
    );
    public static final Supplier<Block> POTTED_PALE_OAK_SAPLING = BLOCKS.registerNoItem(
        "potted_pale_oak_sapling",
        properties -> new FlowerPotBlock(PALE_OAK_SAPLING.get(), properties),
        flowerPotProperties()
    );
    public static final Supplier<Block> CREAKING_HEART = BLOCKS.register(
        "creaking_heart",
        CreakingHeartBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(10.0F)
            .sound(ModSoundTypes.CREAKING_HEART)
    );
    public static final Pair<Supplier<Block>, Supplier<Block>> PALE_OAK_SIGN = sign(
        "pale_oak",
        ModWoodTypes.PALE_OAK,
        MapColor.QUARTZ
    );
    public static final Pair<Supplier<Block>, Supplier<Block>> PALE_OAK_HANGING_SIGN = hangingSign(
        "pale_oak",
        ModWoodTypes.PALE_OAK,
        MapColor.QUARTZ
    );
    public static final Supplier<Block> PALE_OAK_PRESSURE_PLATE = BLOCKS.register(
        "pale_oak_pressure_plate",
        properties -> new PressurePlateBlock(ModBlockSetTypes.PALE_OAK, properties),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> PALE_OAK_TRAPDOOR = BLOCKS.register(
        "pale_oak_trapdoor",
        properties -> new TrapDoorBlock(ModBlockSetTypes.PALE_OAK, properties),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0F)
            .noOcclusion()
            .isValidSpawn(BLOCKS::never)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_BUTTON = BLOCKS.register(
        "pale_oak_button",
        properties -> new ButtonBlock(ModBlockSetTypes.PALE_OAK, 30, properties),
        buttonProperties()
    );
    public static final Supplier<Block> RESIN_CLUMP = BLOCKS.register(
        "resin_clump",
        ResinClumpBlock::new,
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .replaceable()
            .noCollission()
            .sound(ModSoundTypes.RESIN)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> RESIN_BLOCK = BLOCKS.register(
        "resin_block",
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(ModSoundTypes.RESIN)
    );
    public static final Supplier<Block> RESIN_BRICKS = BLOCKS.register(
        "resin_bricks",
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> RESIN_BRICK_STAIRS = BLOCKS.register(
        "resin_brick_stairs",
        properties -> new StairBlock(RESIN_BRICKS.get().defaultBlockState(), properties),
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> RESIN_BRICK_SLAB = BLOCKS.register(
        "resin_brick_slab",
        SlabBlock::new,
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> RESIN_BRICK_WALL = BLOCKS.register(
        "resin_brick_wall",
        WallBlock::new,
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> CHISELED_RESIN_BRICKS = BLOCKS.register(
        "chiseled_resin_bricks",
        Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );

    public static final Supplier<Block> DRIED_GHAST = BLOCKS.register(
        "dried_ghast",
        DriedGhastBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .instabreak()
            .sound(ModSoundTypes.DRIED_GHAST)
            .noOcclusion()
            .randomTicks()
    );

    public static final Supplier<Block> BUSH = BLOCKS.register(
        "bush",
        ActualBushBlock::new,
        Properties.of()
            .mapColor(MapColor.PLANT)
            .replaceable()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> FIREFLY_BUSH = BLOCKS.register(
        "firefly_bush",
        FireflyBushBlock::new,
        Properties.of()
            .mapColor(MapColor.PLANT)
            .ignitedByLava()
            .lightLevel(state -> 2)
            .noCollission()
            .instabreak()
            .sound(SoundType.SWEET_BERRY_BUSH)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> WILDFLOWERS = BLOCKS.register(
        "wildflowers",
        PinkPetalsBlock::new,
        Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .sound(SoundType.PINK_PETALS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> LEAF_LITTER = BLOCKS.register(
        "leaf_litter",
        LeafLitterBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_BROWN)
            .replaceable()
            .noCollission()
            .sound(ModSoundTypes.LEAF_LITTER)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> CACTUS_FLOWER = BLOCKS.register(
        "cactus_flower",
        CactusFlowerBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_PINK)
            .noCollission()
            .instabreak()
            .ignitedByLava()
            .sound(ModSoundTypes.CACTUS_FLOWER)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> SHORT_DRY_GRASS = BLOCKS.register(
        "short_dry_grass",
        ShortDryGrassBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .replaceable()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> TALL_DRY_GRASS = BLOCKS.register(
        "tall_dry_grass",
        TallDryGrassBlock::new,
        Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .replaceable()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Supplier<Block> CINNABAR = BLOCKS.register("cinnabar", SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_SLAB = BLOCKS.register("cinnabar_slab", SlabBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_STAIRS = BLOCKS.register("cinnabar_stairs", () -> new StairBlock(CINNABAR.get().defaultBlockState(), SharedBlockProperties.CINNABAR));
    public static final Supplier<Block> CINNABAR_WALL = BLOCKS.register("cinnabar_wall", WallBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> POLISHED_CINNABAR = BLOCKS.register("polished_cinnabar", SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> POLISHED_CINNABAR_SLAB = BLOCKS.register("polished_cinnabar_slab", SlabBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> POLISHED_CINNABAR_STAIRS = BLOCKS.register("polished_cinnabar_stairs", () -> new StairBlock(POLISHED_CINNABAR.get().defaultBlockState(), SharedBlockProperties.CINNABAR));
    public static final Supplier<Block> POLISHED_CINNABAR_WALL = BLOCKS.register("polished_cinnabar_wall", WallBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_BRICKS = BLOCKS.register("cinnabar_bricks", SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_BRICK_SLAB = BLOCKS.register("cinnabar_brick_slab", SlabBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CINNABAR_BRICK_STAIRS = BLOCKS.register("cinnabar_brick_stairs", () -> new StairBlock(CINNABAR_BRICKS.get().defaultBlockState(), SharedBlockProperties.CINNABAR));
    public static final Supplier<Block> CINNABAR_BRICK_WALL = BLOCKS.register("cinnabar_brick_wall", WallBlock::new, SharedBlockProperties.CINNABAR);
    public static final Supplier<Block> CHISELED_CINNABAR = BLOCKS.register("chiseled_cinnabar", SharedBlockProperties.CINNABAR);

    public static final Supplier<Block> SULFUR = BLOCKS.register("sulfur", SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_SLAB = BLOCKS.register("sulfur_slab", SlabBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_STAIRS = BLOCKS.register("sulfur_stairs", () -> new StairBlock(SULFUR.get().defaultBlockState(), SharedBlockProperties.SULFUR));
    public static final Supplier<Block> SULFUR_WALL = BLOCKS.register("sulfur_wall", WallBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> POLISHED_SULFUR = BLOCKS.register("polished_sulfur", SharedBlockProperties.SULFUR);
    public static final Supplier<Block> POLISHED_SULFUR_SLAB = BLOCKS.register("polished_sulfur_slab", SlabBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> POLISHED_SULFUR_STAIRS = BLOCKS.register("polished_sulfur_stairs", () -> new StairBlock(POLISHED_SULFUR.get().defaultBlockState(), SharedBlockProperties.SULFUR));
    public static final Supplier<Block> POLISHED_SULFUR_WALL = BLOCKS.register("polished_sulfur_wall", WallBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_BRICKS = BLOCKS.register("sulfur_bricks", SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_BRICK_SLAB = BLOCKS.register("sulfur_brick_slab", SlabBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> SULFUR_BRICK_STAIRS = BLOCKS.register("sulfur_brick_stairs", () -> new StairBlock(SULFUR_BRICKS.get().defaultBlockState(), SharedBlockProperties.SULFUR));
    public static final Supplier<Block> SULFUR_BRICK_WALL = BLOCKS.register("sulfur_brick_wall", WallBlock::new, SharedBlockProperties.SULFUR);
    public static final Supplier<Block> CHISELED_SULFUR = BLOCKS.register("chiseled_sulfur", SharedBlockProperties.SULFUR);

    public static final Supplier<Block> POTENT_SULFUR = BLOCKS.register("potent_sulfur", PotentSulfurBlock::new, SharedBlockProperties.SULFUR.sound(ModSoundTypes.POTENT_SULFUR));
    public static final Supplier<Block> SULFUR_SPIKE = BLOCKS.register(
        "sulfur_spike",
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
                .isRedstoneConductor(BLOCKS::never)
                .noOcclusion()
        )
    );

    private static Properties logProperties(MapColor topColor, MapColor sideColor, SoundType sound) {
        return Properties.of()
            .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : sideColor)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(sound)
            .ignitedByLava();
    }

    private static Properties buttonProperties() {
        return Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }

    private static Properties flowerPotProperties() {
        return Properties.of()
            .instabreak()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY);
    }

    public static Pair<Supplier<Block>, Supplier<Block>> sign(String name, WoodType woodType, MapColor color) {
        Properties properties = Properties.of()
            .mapColor(color)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava();
        Supplier<Block> standing = BLOCKS.registerNoItem(name + "_sign", () -> new StandingSignBlock(woodType, properties));
        Supplier<Block> wall = BLOCKS.registerNoItem(name + "_wall_sign", () -> new WallSignBlock(woodType, properties.dropsLike(standing.get())));
        BLOCKS.registerItem(name + "_sign", () -> new SignItem(new Item.Properties().stacksTo(16), standing.get(), wall.get()));
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

        Supplier<Block> ceiling = BLOCKS.registerNoItem(name + "_hanging_sign", () -> new CeilingHangingSignBlock(woodType, properties));
        Supplier<Block> wall = BLOCKS.registerNoItem(name + "_wall_hanging_sign", () -> new WallHangingSignBlock(woodType, properties.dropsLike(ceiling.get())));
        BLOCKS.registerItem(name + "_hanging_sign", () -> new HangingSignItem(ceiling.get(), wall.get(), new Item.Properties().stacksTo(16)));
        return new Pair<>(ceiling, wall);
    }
}