package com.blackgear.vanillabackport.common.registries.blocks;

import com.blackgear.platform.core.helper.BlockEntityRegistry;
import com.blackgear.platform.core.helper.BlockEntityTypeBuilder;
import com.blackgear.vanillabackport.common.level.block_entity.CopperChestBlockEntity;
import com.blackgear.vanillabackport.common.level.block_entity.CreakingHeartBlockEntity;
import com.blackgear.vanillabackport.common.level.block_entity.PotentSulfurBlockEntity;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.experimental.handlers.VanillaBlockEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final BlockEntityRegistry REGISTRIES = BlockEntityRegistry.create(VanillaBackport.NAMESPACE);
    public static final VanillaBlockEntityRegistry HOLDERS = VanillaBlockEntityRegistry.create();

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
    
    // Chaos Cubed
    
    public static final Supplier<BlockEntityType<PotentSulfurBlockEntity>> POTENT_SULFUR = REGISTRIES.register("potent_sulfur",
        BlockEntityTypeBuilder.create(
            PotentSulfurBlockEntity::new,
            ModBlocks.POTENT_SULFUR
        ));
}