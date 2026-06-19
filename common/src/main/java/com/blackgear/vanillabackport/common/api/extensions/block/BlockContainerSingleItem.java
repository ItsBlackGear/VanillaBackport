package com.blackgear.vanillabackport.common.api.extensions.block;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.ticks.ContainerSingleItem;

// Special thanks to Echo2craft
public interface BlockContainerSingleItem extends ContainerSingleItem {
    BlockEntity getContainerBlockEntity();

    @Override
    default boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.getContainerBlockEntity(), player);
    }
}
