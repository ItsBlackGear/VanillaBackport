package com.blackgear.vanillabackport.core.mixin.common.access;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Explosion.class)
public interface ExplosionAccessor {
    @Accessor
    ObjectArrayList<BlockPos> getToBlow();
    
    @Accessor
    Explosion.BlockInteraction getBlockInteraction();
}
