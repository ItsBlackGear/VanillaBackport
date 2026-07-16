package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.common.level.item.spear.AttackRange;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements MobSpearHandler {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(
        method = "doHurtTarget",
        at = @At("TAIL")
    )
    private void vb$doHurtTarget(Entity target, CallbackInfoReturnable<Boolean> cir) {
        this.postPiercingAttack();
    }
    
    @Inject(
        method = "isWithinMeleeAttackRange",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$isWithinMeleeAttackRange(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack activeItem = this.isUsingItem() ? this.getUseItem() : this.getMainHandItem();
        
        AttackRange range = AttackRange.getAttackRange(activeItem);
        if (range == null) return;
        
        double maxRange = range.effectiveMaxRange(this);
        double minRange = range.effectiveMinRange(this);
        AABB hitbox = entity.getBoundingBox();
        
        cir.setReturnValue(this.getAttackBoundingBox(maxRange).intersects(hitbox) && (minRange <= 0.0 || !this.getAttackBoundingBox(minRange).intersects(hitbox)));
    }
    
    @Unique
    protected AABB getAttackBoundingBox(double horizontalExpansion) {
        Entity vehicle = this.getVehicle();
        AABB aabb = this.getBoundingBox();
        if (vehicle != null) {
            AABB mountAabb = vehicle.getBoundingBox();
            AABB ownAabb = this.getBoundingBox();
            aabb = new AABB(
                Math.min(ownAabb.minX, mountAabb.minX),
                ownAabb.minY,
                Math.min(ownAabb.minZ, mountAabb.minZ),
                Math.max(ownAabb.maxX, mountAabb.maxX),
                ownAabb.maxY,
                Math.max(ownAabb.maxZ, mountAabb.maxZ)
            );
        }
        
        return aabb.inflate(horizontalExpansion, 0.0, horizontalExpansion);
    }
}