package com.blackgear.vanillabackport.core.mixin.leash;

import com.blackgear.vanillabackport.common.api.leash.LeashDataExtension;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Leashable.LeashData.class)
public class LeashDataMixin implements LeashDataExtension {
    @Unique private double angularMomentum;

    @Override
    public double angularMomentum() {
        return this.angularMomentum;
    }

    @Override
    public void setAngularMomentum(double angularMomentum) {
        this.angularMomentum = angularMomentum;
    }
}