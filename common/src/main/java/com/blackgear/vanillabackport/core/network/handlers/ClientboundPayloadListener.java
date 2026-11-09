package com.blackgear.vanillabackport.core.network.handlers;

import com.blackgear.platform.core.networking.PayloadContext;
import com.blackgear.vanillabackport.client.api.LevelEventHandler;
import com.blackgear.vanillabackport.client.level.gui.inventory.NautilusInventoryScreen;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.inventory.NautilusInventoryMenu;
import com.blackgear.vanillabackport.core.network.ClientboundLevelEventsPacket;
import com.blackgear.vanillabackport.core.network.ClientboundNautilusScreenOpenPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ClientboundPayloadListener {
    public static void execute(Runnable run, PayloadContext context) {
        if (Minecraft.getInstance().isSameThread()) {
            run.run();
        } else {
            context.enqueueWork(run);
        }
    }
    
    public static void handleNautilusScreenOpen(ClientboundNautilusScreenOpenPacket packet, Player player) {
        if (player.level().getEntity(packet.entityId()) instanceof AbstractNautilus nautilus) {
            SimpleContainer container = new SimpleContainer(packet.size());
            NautilusInventoryMenu menu = new NautilusInventoryMenu(packet.containerId(), player.getInventory(), container, nautilus);
            player.containerMenu = menu;
            Minecraft.getInstance().setScreen(new NautilusInventoryScreen(menu, player.getInventory(), nautilus));
        }
    }
    
    public static void handleLevelEvents(ClientboundLevelEventsPacket packet, @Nullable Player player, Level level) {
        if (level != null) {
            try {
                LevelEventHandler.INSTANCE.levelEvent(packet.event(), packet.pos(), packet.data());
            } catch (Throwable throwable) {
                CrashReport report = CrashReport.forThrowable(throwable, "Playing level event");
                CrashReportCategory category = report.addCategory("Level event being played");
                category.setDetail("Block coordinates", CrashReportCategory.formatLocation(level, packet.pos()));
                category.setDetail("Event source", player);
                category.setDetail("Event type", packet.type().toString());
                category.setDetail("Event data", packet.data());
                throw new ReportedException(report);
            }
        }
    }
}