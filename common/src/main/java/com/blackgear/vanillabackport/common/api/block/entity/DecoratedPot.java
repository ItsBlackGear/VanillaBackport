package com.blackgear.vanillabackport.common.api.block.entity;

import com.blackgear.vanillabackport.common.api.block.BlockContainerSingleItem;
import com.blackgear.vanillabackport.common.level.blockentities.decoratedpot.WobbleStyle;

// Special thanks to Echo2craft
public interface DecoratedPot extends BlockContainerSingleItem {
    void wobble(WobbleStyle style);

    WobbleStyle getLastWobbleStyle();

    long getWobbleStartedAtTick();
}
