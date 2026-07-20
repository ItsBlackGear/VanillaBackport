package com.blackgear.vanillabackport.core.mixin.common.zombie_horses;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
    @Final @Mutable @Shadow private MobCategory category;
    
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void vb$makeZombieHorseMonster(CallbackInfo ci) {
        ((EntityTypeMixin) (Object) EntityType.ZOMBIE_HORSE).category = MobCategory.MONSTER;
    }
}