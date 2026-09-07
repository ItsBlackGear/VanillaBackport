package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.common.api.modules.waypoints.ServerWaypointManager;
import com.blackgear.vanillabackport.core.mixin.common.access.BooleanValueAccessor;
import com.blackgear.vanillabackport.core.mixin.common.access.GameRulesAccessor;
import net.minecraft.world.level.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> RULE_LOCATOR_BAR = GameRulesAccessor.callRegister("locatorBar",
        GameRules.Category.PLAYER,
        BooleanValueAccessor.callCreate(true, (server, value) -> server.getAllLevels().forEach(level -> {
            ServerWaypointManager manager = ServerWaypointManager.get(level);
            if (value.get()) {
                level.players().forEach(manager::updatePlayer);
            } else {
                manager.breakAllConnections();
            }
        }))
    );
    
    public static void bootstrap() {}
}