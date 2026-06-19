package com.blackgear.vanillabackport.common.registries.blocks;

import com.blackgear.platform.core.helper.BlockEntityRegistry;
import com.blackgear.platform.core.helper.BlockEntityTypeBuilder;
import com.blackgear.vanillabackport.common.level.block_entity.CreakingHeartBlockEntity;
import com.blackgear.vanillabackport.common.level.block_entity.PotentSulfurBlockEntity;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final BlockEntityRegistry REGISTRIES = BlockEntityRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<BlockEntityType<CreakingHeartBlockEntity>> CREAKING_HEART = REGISTRIES.register(
        "creaking_heart",
        BlockEntityTypeBuilder.create(
            CreakingHeartBlockEntity::new,
            ModBlocks.CREAKING_HEART
        )
    );
    public static final Supplier<BlockEntityType<PotentSulfurBlockEntity>> POTENT_SULFUR = REGISTRIES.register(
        "potent_sulfur",
        BlockEntityTypeBuilder.create(
            PotentSulfurBlockEntity::new,
            ModBlocks.POTENT_SULFUR
        )
    );
}