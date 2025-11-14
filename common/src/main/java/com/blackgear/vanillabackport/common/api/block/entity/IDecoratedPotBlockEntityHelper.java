package com.blackgear.vanillabackport.common.api.block.entity;

import com.blackgear.vanillabackport.common.api.block.BlockContainerSingleItem;
import com.blackgear.vanillabackport.common.level.blockentities.decoratedpot.WobbleStyle;
import net.minecraft.world.item.ItemStack;

// Helper for Mixin class. - Echo2craft.
public interface IDecoratedPotBlockEntityHelper extends BlockContainerSingleItem {
    void wobble(WobbleStyle pStyle);
    WobbleStyle getLastWobbleStyle();
    long getWobbleStartedAtTick();
}
