package com.blackgear.vanillabackport.common.level.boat;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;

public interface PaleOakBoatBehavior {
    default void fall(Boat boat, double y, boolean onGround) {
        boat.lastYd = boat.getDeltaMovement().y;
        if (!boat.isPassenger()) {
            if (onGround) {
                if (boat.fallDistance > 3.0F) {
                    if (boat.status != Boat.Status.ON_LAND) {
                        boat.resetFallDistance();
                        return;
                    }

                    boat.causeFallDamage(boat.fallDistance, 1.0F, boat.damageSources().fall());
                    if (!boat.level().isClientSide() && !boat.isRemoved()) {
                        boat.kill();
                        if (boat.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int i = 0; i < 3; i++) {
                                boat.spawnAtLocation(ModBlocks.PALE_OAK_PLANKS.get());
                            }

                            for (int i = 0; i < 3; i++) {
                                boat.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                boat.resetFallDistance();
            } else if (!boat.level().getFluidState(boat.blockPosition().below()).is(FluidTags.WATER) && y < 0.0) {
                boat.fallDistance -= (float) y;
            }
        }
    }
}