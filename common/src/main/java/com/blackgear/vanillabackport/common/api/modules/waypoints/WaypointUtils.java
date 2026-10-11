package com.blackgear.vanillabackport.common.api.modules.waypoints;

import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class WaypointUtils {
    private static final AttributeModifier WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER = new AttributeModifier(ResourceLocation.withDefaultNamespace("waypoint_transmit_range_crouch"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER = new AttributeModifier(ResourceLocation.withDefaultNamespace("waypoint_transmit_range_hide"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static void onEntityAttributeUpdate(LivingEntity entity, Holder<Attribute> attribute) {
        if (entity.level() instanceof ServerLevel level) {
            ServerWaypointManager manager = ServerWaypointManager.get(level);
            
            if (entity instanceof ServerPlayer player && attribute.is(ModAttributes.WAYPOINT_RECEIVE_RANGE)) {
                if (entity.getAttributes().getValue(attribute) > 0.0) {
                    manager.addPlayer(player);
                } else {
                    manager.removePlayer(player);
                }
            }
            
            if (attribute.is(ModAttributes.WAYPOINT_TRANSMIT_RANGE)) {
                if (entity.getAttributes().getValue(attribute) > 0.0) {
                    manager.trackWaypoint(WaypointTransmitter.of(entity));
                } else {
                    manager.untrackWaypoint(WaypointTransmitter.of(entity));
                }
            }
        }
    }
    
    public static void onPlayerAttributeUpdate(ServerPlayer player) {
        AttributeInstance attribute = player.getAttribute(ModAttributes.WAYPOINT_TRANSMIT_RANGE);
        if (attribute != null) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(ModItemTags.DISABLES_WAYPOINT_TRACKING)) {
                attribute.addOrUpdateTransientModifier(WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER);
            } else if (player.isCrouching()) {
                attribute.addOrUpdateTransientModifier(WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER);
            } else {
                attribute.removeModifier(WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER);
            }
        }
    }
    
    public static void onEntityTick(LivingEntity entity) {
        if (entity.level().isClientSide()) return;
        AttributeInstance attribute = entity.getAttribute(ModAttributes.WAYPOINT_TRANSMIT_RANGE);
        if (attribute == null) return;
        
        boolean hasInvisibility = entity.hasEffect(MobEffects.INVISIBILITY);
        boolean hasModifier = attribute.hasModifier(WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER.id());
        
        if (hasInvisibility && !hasModifier) {
            attribute.addTransientModifier(WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER);
        } else if (!hasInvisibility && hasModifier) {
            attribute.removeModifier(WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER);
        }
    }
    
    public static boolean isReceivingWaypoints(ServerPlayer player) {
        return player.getAttributeValue(ModAttributes.WAYPOINT_RECEIVE_RANGE) > 0.0;
    }
}