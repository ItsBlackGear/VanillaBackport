package com.blackgear.vanillabackport.core.mixin.common.leash_behavior;

import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashableCallback;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin implements LeashableCallback { /* NO-OP */ }