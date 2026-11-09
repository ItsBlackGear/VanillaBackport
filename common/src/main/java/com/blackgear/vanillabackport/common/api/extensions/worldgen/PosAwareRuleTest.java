package com.blackgear.vanillabackport.common.api.extensions.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public interface PosAwareRuleTest {
    PosAwareRuleTest DEFAULT = new PosAwareRuleTest() {};
    
    static PosAwareRuleTest of(RuleTest rule) {
        return rule instanceof PosAwareRuleTest instance ? instance : DEFAULT;
    }
    
    default boolean test(BlockState state, BlockPos pos, RandomSource random) {
        return false;
    }
}