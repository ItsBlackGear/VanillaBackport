package com.blackgear.vanillabackport.common.api.extensions.entity.mounts;

import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public interface MountInventoryHandler {
    MountInventoryHandler DEFAULT = new MountInventoryHandler() { };
    
    static MountInventoryHandler of(Player player) {
        return player instanceof MountInventoryHandler handler ? handler : DEFAULT;
    }
    
    default void openNautilusInventory(AbstractNautilus nautilus, Container container) { /* NO-OP */ }
}