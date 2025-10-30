package com.blackgear.vanillabackport.common.level.effect;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IRaidOmenHelper {

    @Nullable BlockPos vb$getRaidOmenPosition();

    void vb$setRaidOmenPosition(BlockPos pos);


    void vb$clearRaidOmenPosition();
}
