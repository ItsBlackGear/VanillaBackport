package com.blackgear.vanillabackport.core.mixin.access;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VoxelShape.class)
public interface VoxelShapeAccessor {
    @Invoker
    DoubleList callGetCoords(Direction.Axis axis);
}
