package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

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
}