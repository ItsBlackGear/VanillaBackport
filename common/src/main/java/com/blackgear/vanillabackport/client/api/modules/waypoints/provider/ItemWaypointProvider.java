package com.blackgear.vanillabackport.client.api.modules.waypoints.provider;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.Waypoint.Icon;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointStyleAssets;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointUtils;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.util.Utilities.ColorUtils;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ItemWaypointProvider implements WaypointProvider {
    private final Map<Either<UUID, String>, TrackedWaypoint> tracked = new HashMap<>();

    @Override
    public void tick(LocalPlayer player, TrackedWaypointManager manager) {
        if (!VanillaBackport.CLIENT_CONFIG.locatorDisplayCompassInfo.get()) return;

        Map<Either<UUID, String>, TrackedWaypoint> discovered = new HashMap<>();
        ResourceKey<Level> dimension = player.level().dimension();

        for (ItemStack stack : player.getInventory().items) {
            process(player, dimension, stack, discovered);
        }
        
        process(player, dimension, player.getOffhandItem(), discovered);

        this.tracked.keySet().removeIf(id -> {
            if (discovered.containsKey(id)) return false;
            manager.untrackWaypoint(this.tracked.get(id));
            return true;
        });

        discovered.forEach((id, waypoint) -> {
            if (this.tracked.put(id, waypoint) == null) {
                manager.trackWaypoint(waypoint);
            } else {
                manager.updateWaypoint(waypoint);
            }
        });
    }

    private void process(Player player, ResourceKey<Level> dimension, ItemStack stack, Map<Either<UUID, String>, TrackedWaypoint> waypoints) {
        if (stack.isEmpty()) return;

        Optional<GlobalPos> lastDeathPos = player.getLastDeathLocation();
        if (lastDeathPos.isPresent() && stack.is(Items.RECOVERY_COMPASS)) {
            GlobalPos pos = lastDeathPos.get();
            if (pos.dimension() == dimension) {
                Either<UUID, String> id = Either.right("death_" + pos);
                waypoints.put(id, createDeathWaypoint(id, pos));
            }
        }

        if (stack.is(Items.COMPASS) && CompassItem.isLodestoneCompass(stack)) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                GlobalPos pos = CompassItem.getLodestonePosition(tag);
                if (pos != null && pos.dimension() == dimension) {
                    Either<UUID, String> id = Either.right("lodestone_" + pos);
                    waypoints.put(id, createLodestoneWaypoint(id, pos));
                }
            }
        }

        if (stack.is(ModItemTags.BUNDLES)) {
            BundleFeatures.getContents(stack).forEach(bundled -> process(player, dimension, bundled, waypoints));
        }
    }

    private static TrackedWaypoint createDeathWaypoint(Either<UUID, String> id, GlobalPos pos) {
        Icon icon = new Icon(WaypointStyleAssets.DEATH, Optional.of(CommonColors.WHITE));
        return new TrackedWaypoint.Vec3iWaypoint(id, icon, positionBuf(pos.pos()));
    }

    private static TrackedWaypoint createLodestoneWaypoint(Either<UUID, String> id, GlobalPos pos) {
        Icon icon = new Icon();
        icon.style = WaypointStyleAssets.LODESTONE;
        icon.color = Optional.of(ColorUtils.setBrightness(ColorUtils.color(255, WaypointUtils.positionHash(pos)), 0.9F));
        return new TrackedWaypoint.Vec3iWaypoint(id, icon, positionBuf(pos.pos()));
    }

    private static FriendlyByteBuf positionBuf(BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(pos.getX());
        buf.writeVarInt(pos.getY());
        buf.writeVarInt(pos.getZ());
        return buf;
    }
}