package com.blackgear.vanillabackport.core.network.handlers;

import com.blackgear.vanillabackport.client.events.LevelEventHandler;
import com.blackgear.vanillabackport.client.level.gui.inventory.NautilusInventoryScreen;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.inventory.NautilusInventoryMenu;
import com.blackgear.vanillabackport.core.network.ClientboundLevelEventsPacket;
import com.blackgear.vanillabackport.core.network.ClientboundNautilusScreenOpenPacket;
import com.blackgear.vanillabackport.core.network.ClientboundWindBurstPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ClientboundPacketListener {
    public static final ClientboundPacketListener INSTANCE = new ClientboundPacketListener(); //TODO: make proxy
    
    public void handleNautilusScreenOpen(ClientboundNautilusScreenOpenPacket packet, Player player, Level level) {
        Entity entity = level.getEntity(packet.entityId());
        if (entity instanceof AbstractNautilus nautilus) {
            SimpleContainer container = new SimpleContainer(packet.size());
            NautilusInventoryMenu menu = new NautilusInventoryMenu(packet.containerId(), player.getInventory(), container, nautilus);
            player.containerMenu = menu;
            Minecraft.getInstance().setScreen(new NautilusInventoryScreen(menu, player.getInventory(), nautilus));
        }
    }
    
    public void handleExplosion(ClientboundWindBurstPacket packet, Player player, Level level) {
        if (level != null && player != null) {
            player.addDeltaMovement(player.getDeltaMovement().add(packet.knockbackX, packet.knockbackY, packet.knockbackZ));

            level.playLocalSound(
                packet.x,
                packet.y,
                packet.z,
                packet.explosionSound.value(),
                SoundSource.BLOCKS,
                4.0F,
                (1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F) * 0.7F,
                false
            );
            
            ParticleOptions options = packet.power < 2.0F || packet.blockInteraction == Explosion.BlockInteraction.KEEP
                ? packet.smallExplosionParticles
                : packet.largeExplosionParticles;
            level.addParticle(options, packet.x, packet.y, packet.z, 1.0, 0.0, 0.0);
        }
    }
    
    public void handleLevelEvents(ClientboundLevelEventsPacket packet, @Nullable Player player, Level level) {
        if (level != null) {
            try {
                LevelEventHandler.INSTANCE.levelEvent(packet.type(), packet.pos(), packet.data());
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