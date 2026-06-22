package com.blackgear.vanillabackport.core.mixin.access;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractMinecart.class)
public interface AbstractMinecartAccessor {
    @Invoker static Pair<Vec3i, Vec3i> callExits(RailShape shape) { throw new UnsupportedOperationException(); }
    
    @Invoker boolean callIsRedstoneConductor(BlockPos pos);
    
    @Invoker void callApplyNaturalSlowdown();
    
    @Invoker double callGetMaxSpeed();
    
    @Accessor void setOnRails(boolean onRails);
}