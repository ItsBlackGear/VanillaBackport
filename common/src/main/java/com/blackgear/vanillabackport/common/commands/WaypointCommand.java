package com.blackgear.vanillabackport.common.commands;

import com.blackgear.vanillabackport.common.api.modules.waypoints.*;
import com.blackgear.vanillabackport.common.api.modules.waypoints.Waypoint.Icon;
import com.blackgear.vanillabackport.common.commands.arguments.HexColorArgument;
import com.blackgear.vanillabackport.common.commands.arguments.ColorArgument;
import com.blackgear.vanillabackport.common.commands.arguments.WaypointArgument;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.entity.LivingEntity;

import java.util.HexFormat;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class WaypointCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
            Commands.literal("waypoint")
                .requires(src -> src.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("list").executes(c -> listWaypoints(c.getSource())))
                .then(
                    Commands.literal("modify")
                        .then(
                            Commands.argument("waypoint", EntityArgument.entity())
                                .then(
                                    Commands.literal("color")
                                        .then(
                                            Commands.argument("color", ColorArgument.color())
                                                .executes(c -> setWaypointColor(c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"), ColorArgument.getTeamColor(c, "color")))
                                        )
                                        .then(
                                            Commands.literal("hex")
                                                .then(
                                                    Commands.argument("color", HexColorArgument.hexColor())
                                                        .executes(c -> setWaypointColor(c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"), HexColorArgument.getHexColor(c, "color")))
                                                )
                                        )
                                        .then(
                                            Commands.literal("reset").executes(c -> resetWaypointColor(c.getSource(), WaypointArgument.getWaypoint(c, "waypoint")))
                                        )
                                )
                                .then(
                                    Commands.literal("style")
                                        .then(
                                            Commands.literal("reset").executes(c -> setWaypointStyle(c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"), WaypointStyleAssets.DEFAULT))
                                        )
                                        .then(
                                            Commands.literal("set")
                                                .then(
                                                    Commands.argument("style", ResourceLocationArgument.id())
                                                        .executes(
                                                            c -> setWaypointStyle(
                                                                c.getSource(),
                                                                WaypointArgument.getWaypoint(c, "waypoint"),
                                                                ResourceKey.create(WaypointStyleAssets.ROOT_ID, ResourceLocationArgument.getId(c, "style"))
                                                            )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
    
    private static int setWaypointStyle(CommandSourceStack source, WaypointTransmitter waypoint, ResourceKey<WaypointStyleAsset> style) {
        mutateIcon(source, waypoint, icon -> icon.style = style);
        source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.style"), false);
        return 0;
    }
    
    private static int setWaypointColor(CommandSourceStack source, WaypointTransmitter waypoint, ChatFormatting color) {
        mutateIcon(source, waypoint, icon -> icon.color = Optional.ofNullable(color.getColor()));
        source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color", Component.literal(color.getName()).withStyle(color)), false);
        return 0;
    }
    
    private static int setWaypointColor(CommandSourceStack source, WaypointTransmitter waypoint, Integer color) {
        mutateIcon(source, waypoint, icon -> icon.color = Optional.of(color));
        source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color", Component.literal(HexFormat.of().withUpperCase().toHexDigits(ARGB32.color(0, color), 6)).withColor(color)), false);
        return 0;
    }
    
    private static int resetWaypointColor(CommandSourceStack source, WaypointTransmitter waypoint) {
        mutateIcon(source, waypoint, icon -> icon.color = Optional.empty());
        source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color.reset"), false);
        return 0;
    }
    
    private static int listWaypoints(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        Set<WaypointTransmitter> waypoints = ServerWaypointManager.get(level).transmitters();
        String dimension = level.dimension().location().toString();
        if (waypoints.isEmpty()) {
            source.sendSuccess(() -> Component.translatable("commands.waypoint.list.empty", dimension), false);
            return 0;
        } else {
            Component waypointNames = ComponentUtils.formatList(
                waypoints.stream()
                    .map(transmitter -> {
                        if (transmitter instanceof LivingEntity entity) {
                            BlockPos pos = entity.blockPosition();
                            return entity.getFeedbackDisplayName().copy()
                                .withStyle(
                                    s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/execute in " + dimension + " run tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip")))
                                        .withColor(transmitter.waypointIcon().color.orElse(-1)));
                        } else {
                            return Component.literal(transmitter.toString());
                        }
                    })
                    .toList(),
                Function.identity()
            );
            source.sendSuccess(() -> Component.translatable("commands.waypoint.list.success", waypoints.size(), dimension, waypointNames), false);
            return waypoints.size();
        }
    }
    
    private static void mutateIcon(CommandSourceStack source, WaypointTransmitter waypoint, Consumer<Icon> consumer) {
        ServerLevel level = source.getLevel();
        ServerWaypointManager.get(level).untrackWaypoint(waypoint);
        consumer.accept(waypoint.waypointIcon());
        ServerWaypointManager.get(level).trackWaypoint(waypoint);
    }
}