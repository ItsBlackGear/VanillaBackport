package com.blackgear.vanillabackport.core.network.handlers;

import com.blackgear.vanillabackport.client.level.gui.inventory.NautilusInventoryScreen;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.inventory.NautilusInventoryMenu;
import com.blackgear.vanillabackport.core.network.ClientboundNautilusScreenOpenPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
public class ClientboundPacketListener {
    public static void handleNautilusScreenOpen(ClientboundNautilusScreenOpenPacket packet, Player player, Level level) {
        Entity entity = level.getEntity(packet.entityId());
        SimpleContainer container = new SimpleContainer(packet.size());
        if (entity instanceof AbstractNautilus nautilus) {
            NautilusInventoryMenu menu = new NautilusInventoryMenu(packet.containerId(), player.getInventory(), container, nautilus);
            player.containerMenu = menu;
            Minecraft.getInstance().setScreen(new NautilusInventoryScreen(menu, player.getInventory(), nautilus));
        }
    }
}