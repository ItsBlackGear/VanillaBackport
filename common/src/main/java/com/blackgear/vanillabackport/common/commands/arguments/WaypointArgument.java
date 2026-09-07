package com.blackgear.vanillabackport.common.commands.arguments;

import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointTransmitter;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;

public class WaypointArgument {
    public static final SimpleCommandExceptionType ERROR_NOT_A_WAYPOINT = new SimpleCommandExceptionType(Component.translatable("argument.waypoint.invalid"));
    
    public static WaypointTransmitter getWaypoint(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        if (context.getArgument(name, EntitySelector.class).findSingleEntity(context.getSource()) instanceof WaypointTransmitter waypoint) {
            return waypoint;
        } else {
            throw ERROR_NOT_A_WAYPOINT.create();
        }
    }
}