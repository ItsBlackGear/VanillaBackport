package com.blackgear.vanillabackport.common.level.blocks;

import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;

public class ResinClumpBlock extends MultifaceBlock {
    public ResinClumpBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return null;
    }
}