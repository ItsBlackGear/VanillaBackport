package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public class BlockFamilies {
    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

    public static final BlockFamily PALE_OAK_PLANKS = familyBuilder(ModBlocks.PALE_OAK_PLANKS.get())
        .button(ModBlocks.PALE_OAK_BUTTON.get())
        .fence(ModBlocks.PALE_OAK_FENCE.get())
        .fenceGate(ModBlocks.PALE_OAK_FENCE_GATE.get())
        .pressurePlate(ModBlocks.PALE_OAK_PRESSURE_PLATE.get())
        .sign(ModBlocks.PALE_OAK_SIGN.getFirst().get(), ModBlocks.PALE_OAK_SIGN.getSecond().get())
        .slab(ModBlocks.PALE_OAK_SLAB.get())
        .stairs(ModBlocks.PALE_OAK_STAIRS.get())
        .door(ModBlocks.PALE_OAK_DOOR.get())
        .trapdoor(ModBlocks.PALE_OAK_TRAPDOOR.get())
        .recipeGroupPrefix("wooden")
        .recipeUnlockedBy("has_planks")
        .getFamily();

    public static final BlockFamily RESIN_BRICKS = familyBuilder(ModBlocks.RESIN_BRICKS.get())
        .wall(ModBlocks.RESIN_BRICK_WALL.get())
        .stairs(ModBlocks.RESIN_BRICK_STAIRS.get())
        .slab(ModBlocks.RESIN_BRICK_SLAB.get())
        .chiseled(ModBlocks.CHISELED_RESIN_BRICKS.get())
        .getFamily();

    public static final BlockFamily SULFUR = familyBuilder(ModBlocks.SULFUR.get())
        .wall(ModBlocks.SULFUR_WALL.get())
        .stairs(ModBlocks.SULFUR_STAIRS.get())
        .slab(ModBlocks.SULFUR_SLAB.get())
        .chiseled(ModBlocks.CHISELED_SULFUR.get())
        .polished(ModBlocks.POLISHED_SULFUR.get())
        .getFamily();

    public static final BlockFamily POLISHED_SULFUR = familyBuilder(ModBlocks.POLISHED_SULFUR.get())
        .wall(ModBlocks.POLISHED_SULFUR_WALL.get())
        .stairs(ModBlocks.POLISHED_SULFUR_STAIRS.get())
        .slab(ModBlocks.POLISHED_SULFUR_SLAB.get())
        .getFamily();

    public static final BlockFamily SULFUR_BRICKS = familyBuilder(ModBlocks.SULFUR_BRICKS.get())
        .wall(ModBlocks.SULFUR_BRICK_WALL.get())
        .stairs(ModBlocks.SULFUR_BRICK_STAIRS.get())
        .slab(ModBlocks.SULFUR_BRICK_SLAB.get())
        .getFamily();
    
    public static final BlockFamily CINNABAR = familyBuilder(ModBlocks.CINNABAR.get())
        .wall(ModBlocks.CINNABAR_WALL.get())
        .stairs(ModBlocks.CINNABAR_STAIRS.get())
        .slab(ModBlocks.CINNABAR_SLAB.get())
        .chiseled(ModBlocks.CHISELED_CINNABAR.get())
        .polished(ModBlocks.POLISHED_CINNABAR.get())
        .getFamily();
    
    public static final BlockFamily POLISHED_CINNABAR = familyBuilder(ModBlocks.POLISHED_CINNABAR.get())
        .wall(ModBlocks.POLISHED_CINNABAR_WALL.get())
        .stairs(ModBlocks.POLISHED_CINNABAR_STAIRS.get())
        .slab(ModBlocks.POLISHED_CINNABAR_SLAB.get())
        .getFamily();
    
    public static final BlockFamily CINNABAR_BRICKS = familyBuilder(ModBlocks.CINNABAR_BRICKS.get())
        .wall(ModBlocks.CINNABAR_BRICK_WALL.get())
        .stairs(ModBlocks.CINNABAR_BRICK_STAIRS.get())
        .slab(ModBlocks.CINNABAR_BRICK_SLAB.get())
        .getFamily();

    private static BlockFamily.Builder familyBuilder(Block block) {
        BlockFamily.Builder builder = new BlockFamily.Builder(block);
        BlockFamily family = MAP.put(block, builder.getFamily());
        if (family != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(block));
        } else {
            return builder;
        }
    }

    public static Stream<BlockFamily> getAllFamilies() {
        return MAP.values().stream();
    }

    @Nullable
    public static BlockFamily getFamily(Block base) {
        return MAP.get(base);
    }
}