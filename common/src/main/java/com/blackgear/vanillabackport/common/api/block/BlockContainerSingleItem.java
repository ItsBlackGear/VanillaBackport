package com.blackgear.vanillabackport.common.api.block;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.ticks.ContainerSingleItem;

public interface BlockContainerSingleItem extends ContainerSingleItem {
    BlockEntity getContainerBlockEntity();

    @Override
    default boolean stillValid(Player p_335018_) {
        return Container.stillValidBlockEntity(this.getContainerBlockEntity(), p_335018_);
    }
}
