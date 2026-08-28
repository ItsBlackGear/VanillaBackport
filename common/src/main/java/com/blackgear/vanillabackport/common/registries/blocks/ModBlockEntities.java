package com.blackgear.vanillabackport.common.registries.blocks;

import com.blackgear.platform.core.helper.BlockEntityRegistry;
import com.blackgear.platform.core.helper.BlockEntityTypeBuilder;
import com.blackgear.vanillabackport.common.level.block_entities.*;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final BlockEntityRegistry REGISTRIES = BlockEntityRegistry.create(VanillaBackport.NAMESPACE);
    
    // The Garden Awakens
    
    public static final Supplier<BlockEntityType<CreakingHeartBlockEntity>> CREAKING_HEART = REGISTRIES.register("creaking_heart",
        BlockEntityTypeBuilder.create(
            CreakingHeartBlockEntity::new,
            ModBlocks.CREAKING_HEART
        ));
    
    // Copper Age
    
    public static final Supplier<BlockEntityType<CopperChestBlockEntity>> COPPER_CHEST = REGISTRIES.register("copper_chest",
        BlockEntityTypeBuilder.create(
            CopperChestBlockEntity::new,
            ModBlocks.COPPER_CHEST,
            ModBlocks.EXPOSED_COPPER_CHEST,
            ModBlocks.WEATHERED_COPPER_CHEST,
            ModBlocks.OXIDIZED_COPPER_CHEST,
            ModBlocks.WAXED_COPPER_CHEST,
            ModBlocks.WAXED_EXPOSED_COPPER_CHEST,
            ModBlocks.WAXED_WEATHERED_COPPER_CHEST,
            ModBlocks.WAXED_OXIDIZED_COPPER_CHEST));
    
    public static final Supplier<BlockEntityType<CopperGolemStatueBlockEntity>> COPPER_GOLEM_STATUE = REGISTRIES.register("copper_golem_statue",
        BlockEntityTypeBuilder.create(
            CopperGolemStatueBlockEntity::new,
            ModBlocks.COPPER_GOLEM_STATUE,
            ModBlocks.EXPOSED_COPPER_GOLEM_STATUE,
            ModBlocks.WEATHERED_COPPER_GOLEM_STATUE,
            ModBlocks.OXIDIZED_COPPER_GOLEM_STATUE,
            ModBlocks.WAXED_COPPER_GOLEM_STATUE,
            ModBlocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE,
            ModBlocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE,
            ModBlocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE));
    
    public static final Supplier<BlockEntityType<ShelfBlockEntity>> SHELF = REGISTRIES.register("shelf",
        BlockEntityTypeBuilder.create(
            ShelfBlockEntity::new,
            ModBlocks.ACACIA_SHELF,
            ModBlocks.BAMBOO_SHELF,
            ModBlocks.BIRCH_SHELF,
            ModBlocks.CHERRY_SHELF,
            ModBlocks.CRIMSON_SHELF,
            ModBlocks.DARK_OAK_SHELF,
            ModBlocks.JUNGLE_SHELF,
            ModBlocks.MANGROVE_SHELF,
            ModBlocks.OAK_SHELF,
            ModBlocks.PALE_OAK_SHELF,
            ModBlocks.SPRUCE_SHELF,
            ModBlocks.WARPED_SHELF));
    
    // Chaos Cubed
    
    public static final Supplier<BlockEntityType<PotentSulfurBlockEntity>> POTENT_SULFUR = REGISTRIES.register("potent_sulfur",
        BlockEntityTypeBuilder.create(
            PotentSulfurBlockEntity::new,
            ModBlocks.POTENT_SULFUR
        ));
}