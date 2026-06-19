package com.blackgear.vanillabackport.common.api.extensions.block.entity;

import com.blackgear.vanillabackport.common.api.extensions.block.BlockContainerSingleItem;
import com.blackgear.vanillabackport.common.level.block_entity.decorated_pot.WobbleStyle;

// Special thanks to Echo2craft
public interface DecoratedPot extends BlockContainerSingleItem {
    void wobble(WobbleStyle style);

    WobbleStyle getLastWobbleStyle();

    long getWobbleStartedAtTick();
}
