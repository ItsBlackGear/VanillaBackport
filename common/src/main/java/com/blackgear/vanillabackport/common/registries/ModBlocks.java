package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.BlockRegistry;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import com.blackgear.vanillabackport.common.level.blocks.*;
import com.blackgear.vanillabackport.common.worldgen.grower.PaleOakTreeGrower;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class ModBlocks {
    public static final BlockRegistry BLOCKS = BlockRegistry.create(VanillaBackport.MOD_ID);

    public static final Supplier<Block> PALE_OAK_LEAVES = BLOCKS.register(
        "pale_oak_leaves",
        properties -> new ParticleLeavesBlock(50, ModParticles.PALE_OAK_LEAVES, properties),
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_STAIRS = BLOCKS.register(
        "pale_oak_stairs",
        properties -> new StairBlock(PALE_OAK_PLANKS.get().defaultBlockState(), properties),
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_SLAB = BLOCKS.register(
        "pale_oak_slab",
        SlabBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_FENCE = BLOCKS.register(
        "pale_oak_fence",
        FenceBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_FENCE_GATE = BLOCKS.register(
        "pale_oak_fence_gate",
        properties -> new FenceGateBlock(properties, ModWoodTypes.PALE_OAK),
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.QUARTZ)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_DOOR = BLOCKS.register(
        "pale_oak_door",
        properties -> new DoorBlock(properties, ModBlockSetTypes.PALE_OAK),
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.1F)
            .sound(SoundType.MOSS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> PALE_MOSS_CARPET = BLOCKS.register(
        "pale_moss_carpet",
        MossyCarpetBlock::new,
        BlockBehaviour.Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.1F)
            .sound(SoundType.MOSS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> PALE_HANGING_MOSS = BLOCKS.register(
        "pale_hanging_moss",
        HangingMossBlock::new,
        BlockBehaviour.Properties.of()
            .ignitedByLava()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .noCollission()
            .sound(SoundType.MOSS_CARPET)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> OPEN_EYEBLOSSOM = BLOCKS.register(
        "open_eyeblossom",
        properties -> new EyeblossomBlock(EyeblossomBlock.Type.OPEN, properties),
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
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
        properties -> new SaplingBlock(new PaleOakTreeGrower(), properties),
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
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
        properties -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, properties, ModBlockSetTypes.PALE_OAK),
        BlockBehaviour.Properties.of()
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
        properties -> new TrapDoorBlock(properties, ModBlockSetTypes.PALE_OAK),
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.QUARTZ)
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0F)
            .noOcclusion()
            .isValidSpawn(BLOCKS::never)
            .ignitedByLava()
    );
    public static final Supplier<Block> PALE_OAK_BUTTON = BLOCKS.register(
        "pale_oak_button",
        properties -> new ButtonBlock(properties, ModBlockSetTypes.PALE_OAK, 30, true),
        buttonProperties()
    );
    public static final Supplier<Block> RESIN_CLUMP = BLOCKS.register(
        "resin_clump",
        ResinClumpBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .replaceable()
            .noCollission()
            .sound(ModSoundTypes.RESIN)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> RESIN_BLOCK = BLOCKS.register(
        "resin_block",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(ModSoundTypes.RESIN)
    );
    public static final Supplier<Block> RESIN_BRICKS = BLOCKS.register(
        "resin_bricks",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> RESIN_BRICK_STAIRS = BLOCKS.register(
        "resin_brick_stairs",
        properties -> new StairBlock(RESIN_BRICKS.get().defaultBlockState(), properties),
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> RESIN_BRICK_SLAB = BLOCKS.register(
        "resin_brick_slab",
        SlabBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> RESIN_BRICK_WALL = BLOCKS.register(
        "resin_brick_wall",
        WallBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );
    public static final Supplier<Block> CHISELED_RESIN_BRICKS = BLOCKS.register(
        "chiseled_resin_bricks",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(ModSoundTypes.RESIN_BRICKS)
            .strength(1.5F, 6.0F)
    );

    public static final Supplier<Block> DRIED_GHAST = BLOCKS.register(
        "dried_ghast",
        DriedGhastBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .instabreak()
            .sound(ModSoundTypes.DRIED_GHAST)
            .noOcclusion()
            .randomTicks()
    );

    public static final Supplier<Block> BUSH = BLOCKS.register(
        "bush",
        BushBlock::new,
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .sound(SoundType.PINK_PETALS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> CACTUS_FLOWER = BLOCKS.register(
        "cactus_flower",
        CactusFlowerBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PINK)
            .noCollission()
            .instabreak()
            .ignitedByLava()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
    );
    public static final Supplier<Block> SHORT_DRY_GRASS = BLOCKS.register(
        "short_dry_grass",
        ShortDryGrassBlock::new,
        BlockBehaviour.Properties.of()
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
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .replaceable()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .pushReaction(PushReaction.DESTROY)
    );

    private static BlockBehaviour.Properties logProperties(MapColor topColor, MapColor sideColor, SoundType sound) {
        return BlockBehaviour.Properties.of()
            .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : sideColor)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(sound)
            .ignitedByLava();
    }

    private static BlockBehaviour.Properties buttonProperties() {
        return BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties flowerPotProperties() {
        return BlockBehaviour.Properties.of()
            .instabreak()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY);
    }

    public static Pair<Supplier<Block>, Supplier<Block>> sign(String name, WoodType woodType, MapColor color) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
            .mapColor(color)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava();
        Supplier<Block> standing = BLOCKS.registerNoItem(name + "_sign", () -> new StandingSignBlock(properties, woodType));
        Supplier<Block> wall = BLOCKS.registerNoItem(name + "_wall_sign", () -> new WallSignBlock(properties.dropsLike(standing.get()), woodType));
        BLOCKS.registerItem(name + "_sign", () -> new SignItem(new Item.Properties().stacksTo(16), standing.get(), wall.get()));
        return new Pair<>(standing, wall);
    }

    public static Pair<Supplier<Block>, Supplier<Block>> hangingSign(String name, WoodType woodType, MapColor color) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
            .mapColor(color)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava();

        Supplier<Block> ceiling = BLOCKS.registerNoItem(name + "_hanging_sign", () -> new CeilingHangingSignBlock(properties, woodType));
        Supplier<Block> wall = BLOCKS.registerNoItem(name + "_wall_hanging_sign", () -> new WallHangingSignBlock(properties.dropsLike(ceiling.get()), woodType));
        BLOCKS.registerItem(name + "_hanging_sign", () -> new HangingSignItem(ceiling.get(), wall.get(), new Item.Properties().stacksTo(16)));
        return new Pair<>(ceiling, wall);
    }
}