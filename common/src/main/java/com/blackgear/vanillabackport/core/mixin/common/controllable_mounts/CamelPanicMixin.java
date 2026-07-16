package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelAi;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CamelAi.CamelPanic.class)
public class CamelPanicMixin extends AnimalPanic<Camel> {
    public CamelPanicMixin(float speedMultiplier) {
        super(speedMultiplier);
    }
    
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Camel camel) {
        return super.checkExtraStartConditions(level, camel) && !ControllableMob.of(camel).isMobControlled();
    }
}