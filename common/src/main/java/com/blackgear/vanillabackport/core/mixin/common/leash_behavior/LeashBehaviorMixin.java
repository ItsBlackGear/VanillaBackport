package com.blackgear.vanillabackport.core.mixin.common.leash_behavior;

import com.blackgear.vanillabackport.common.api.modules.leash_behavior.Leashable;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class LeashBehaviorMixin extends LivingEntity implements Leashable {
    @Unique private double vb$angularMomentum;
    
    protected LeashBehaviorMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public double vb$angularMomentum() {
        return this.vb$angularMomentum;
    }
    
    @Override
    public void vb$setAngularMomentum(double angularMomentum) {
        this.vb$angularMomentum = angularMomentum;
    }
    
    // Exponemos los métodos nativos de Mob a la interfaz Leashable
    @Override
    public Entity vb$getLeashHolder() {
        return ((Mob) (Object) this).getLeashHolder();
    }
    
    @Override
    public void vb$setLeashedTo(Entity entity, boolean sendAttachPacket) {
        ((Mob) (Object) this).setLeashedTo(entity, sendAttachPacket);
    }
    
    @Override
    public void vb$dropLeash(boolean broadcast, boolean dropItem) {
        ((Mob) (Object) this).dropLeash(broadcast, dropItem);
    }
    
    @Inject(method = "tickLeash", at = @At("HEAD"), cancellable = true)
    private void vb$onTickLeash(CallbackInfo ci) {
        // Cancelamos la lógica de seguimiento lineal de vanilla
        ci.cancel();
        
        // MUY IMPORTANTE: Solo restauramos el NBT y soltamos si el holder muere.
        // Simulamos la parte vital del tickLeash original sin mover al mob:
        Mob mob = (Mob) (Object) this;
        if (mob.leashInfoTag != null) {
            mob.restoreLeashFromSave();
        }
        if (mob.getLeashHolder() != null) {
            if (!mob.isAlive() || !mob.getLeashHolder().isAlive()) {
                mob.dropLeash(true, true);
            }
        }
        
        // Ejecutamos nuestra física elástica superior
        Leashable.vb$onTickLeash(this);
    }
}