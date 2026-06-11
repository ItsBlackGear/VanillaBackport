package com.blackgear.vanillabackport.core.mixin.common.spawning;

import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> {
    @Shadow @Final private  EntityType.EntityFactory<T> factory;
    
    @Shadow public abstract boolean is(TagKey<EntityType<?>> tag);
    
//    @Inject(method = "create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;", at = @At("RETURN"), cancellable = true)
//    private void a(Level level, CallbackInfoReturnable<T> cir) {
//        if (cir.getReturnValue() == null && this.is(ModEntityTypeTags.MONSTERS_THAT_SPAWN_ON_PEACEFUL)) {
//            this.factory.create((EntityType<T>)(Object)this, level);
//        }
//    }
}