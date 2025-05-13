package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.BlockEntityRegistry;
import com.blackgear.platform.core.helper.BlockEntityTypeBuilder;
import com.blackgear.vanillabackport.common.level.blockentities.CreakingHeartBlockEntity;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final BlockEntityRegistry BLOCK_ENTITIES = BlockEntityRegistry.create(VanillaBackport.MOD_ID);

    public static final Supplier<BlockEntityType<CreakingHeartBlockEntity>> CREAKING_HEART = BLOCK_ENTITIES.register(
        "creaking_heart",
        BlockEntityTypeBuilder.create(
            CreakingHeartBlockEntity::new,
            ModBlocks.CREAKING_HEART
        )
    );
}