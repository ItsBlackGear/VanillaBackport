package com.blackgear.vanillabackport.common.registries.blocks;

import com.blackgear.platform.core.helper.BlockRegistry;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import com.blackgear.vanillabackport.common.level.block.*;
import com.blackgear.vanillabackport.common.level.block.properties.SharedBlockProperties;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.datafixers.util.Pair;
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
    public static final BlockRegistry REGISTRIES = BlockRegistry.create(VanillaBackport.NAMESPACE);

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
        properties -> new ButtonBlock(ModBlockSetTypes.PALE_OAK, 30, properties),
        SharedBlockProperties.buttonProperties());
    public static final Supplier<Block> PALE_OAK_PRESSURE_PLATE = REGISTRIES.register("pale_oak_pressure_plate",
        properties -> new PressurePlateBlock(ModBlockSetTypes.PALE_OAK, properties),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY));
    public static final Supplier<Block> PALE_OAK_DOOR = REGISTRIES.register("pale_oak_door",
        properties -> new DoorBlock(ModBlockSetTypes.PALE_OAK, properties),
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
        properties -> new FenceGateBlock(ModWoodTypes.PALE_OAK, properties),
        Properties.of()
            .mapColor(MapColor.QUARTZ)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .ignitedByLava());
    public static final Supplier<Block> PALE_OAK_TRAPDOOR = REGISTRIES.register("pale_oak_trapdoor",
        properties -> new TrapDoorBlock(ModBlockSetTypes.PALE_OAK, properties),
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
        properties -> new SaplingBlock(new TreeGrower("pale_oak", Optional.of(TheGardenAwakensFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty()), properties),
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
        Supplier<Block> standing = REGISTRIES.registerNoItem(name + "_sign", () -> new StandingSignBlock(woodType, properties));
        Supplier<Block> wall = REGISTRIES.registerNoItem(name + "_wall_sign", () -> new WallSignBlock(woodType, properties.dropsLike(standing.get())));
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

        Supplier<Block> ceiling = REGISTRIES.registerNoItem(name + "_hanging_sign", () -> new CeilingHangingSignBlock(woodType, properties));
        Supplier<Block> wall = REGISTRIES.registerNoItem(name + "_wall_hanging_sign", () -> new WallHangingSignBlock(woodType, properties.dropsLike(ceiling.get())));
        REGISTRIES.registerItem(name + "_hanging_sign", () -> new HangingSignItem(ceiling.get(), wall.get(), new Item.Properties().stacksTo(16)));
        return new Pair<>(ceiling, wall);
    }
}