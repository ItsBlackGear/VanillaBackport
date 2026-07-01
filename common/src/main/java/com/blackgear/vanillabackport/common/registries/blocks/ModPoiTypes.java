package com.blackgear.vanillabackport.common.registries.blocks;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class ModPoiTypes {
    public static final CoreRegistry<PoiType> REGISTRIES = CoreRegistry.create(Registries.POINT_OF_INTEREST_TYPE, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<PoiType> LIGHTNING_RODS = REGISTRIES.resource("lightning_rods",
        () -> new PoiType(getLightningRodBlocks(), 0, 1));
    
    private static Set<BlockState> getLightningRodBlocks() {
        return ImmutableList.of(
            ModBlocks.EXPOSED_LIGHTNING_ROD,
            ModBlocks.WEATHERED_LIGHTNING_ROD,
            ModBlocks.OXIDIZED_LIGHTNING_ROD,
            ModBlocks.WAXED_LIGHTNING_ROD,
            ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD,
            ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD,
            ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD
        ).stream().flatMap(block -> block.get().getStateDefinition().getPossibleStates().stream())
        .collect(ImmutableSet.toImmutableSet());
    }
}