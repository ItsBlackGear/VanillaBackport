package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.api.extensions.entity.mounts.MountInventoryHandler;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.inventory.NautilusInventoryMenu;
import com.blackgear.vanillabackport.core.network.ClientboundNautilusScreenOpenPacket;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements MountInventoryHandler {
    @Shadow protected abstract void nextContainerCounter();
    @Shadow private int containerCounter;
    @Shadow protected abstract void initMenu(AbstractContainerMenu menu);
    
    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }
    
    @Override
    public void openNautilusInventory(AbstractNautilus nautilus, Container container) {
        if (this.containerMenu != this.inventoryMenu) {
            this.closeContainer();
        }
        
        this.nextContainerCounter();
        NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(new ClientboundNautilusScreenOpenPacket(this.containerMenu.containerId, container.getContainerSize(), nautilus.getId()), this);
        this.containerMenu = new NautilusInventoryMenu(this.containerCounter, this.getInventory(), container, nautilus);
        this.initMenu(this.containerMenu);
    }
}