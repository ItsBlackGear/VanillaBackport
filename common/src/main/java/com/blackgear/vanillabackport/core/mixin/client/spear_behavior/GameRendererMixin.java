package com.blackgear.vanillabackport.core.mixin.client.spear_behavior;

import com.blackgear.vanillabackport.common.level.components.AttackRange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final Minecraft minecraft;
    
    @Inject(method = "pick(F)V", at = @At("HEAD"), cancellable = true)
    private void vb$onPick(float partialTicks, CallbackInfo ci) {
        Entity cameraEntity = this.minecraft.getCameraEntity();
        if (cameraEntity != null && this.minecraft.level != null && this.minecraft.player != null) {
            ItemStack activeStack = this.minecraft.player.getMainHandItem();
            AttackRange attackRange = AttackRange.get(activeStack);
            
            if (attackRange != null) {
                ci.cancel();
                
                this.minecraft.getProfiler().push("pick");
                
                HitResult hitResult = vb$raycastHitResult(this.minecraft.player, partialTicks, cameraEntity, attackRange);
                this.minecraft.hitResult = hitResult;
                
                if (hitResult instanceof EntityHitResult entityHitResult) {
                    Entity entity = entityHitResult.getEntity();
                    if (entity instanceof LivingEntity || entity instanceof ItemFrame) {
                        this.minecraft.crosshairPickEntity = entity;
                    } else {
                        this.minecraft.crosshairPickEntity = null;
                    }
                } else {
                    this.minecraft.crosshairPickEntity = null;
                }
                
                this.minecraft.getProfiler().pop();
            }
        }
    }
    
    @Unique
    private HitResult vb$raycastHitResult(Player player, float partialTicks, Entity cameraEntity, AttackRange itemAttackRange) {
        double blockReach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        HitResult hitResult = null;
        
        if (itemAttackRange != null) {
            hitResult = itemAttackRange.getClosestHit(cameraEntity, partialTicks, Entity::isPickable);
            if (hitResult instanceof BlockHitResult) {
                hitResult = vb$filterHitResult(hitResult, cameraEntity.getEyePosition(partialTicks), blockReach);
            }
        }
        
        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
            double entityReach = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
            hitResult = vb$pick(cameraEntity, blockReach, entityReach, partialTicks);
        }
        
        return hitResult;
    }
    
    @Unique
    private static HitResult vb$pick(Entity cameraEntity, double blockReach, double entityReach, float partialTicks) {
        double maxDistance = Math.max(blockReach, entityReach);
        double maxDistanceSq = maxDistance * maxDistance;
        Vec3 from = cameraEntity.getEyePosition(partialTicks);
        HitResult blockHitResult = cameraEntity.pick(maxDistance, partialTicks, false);
        double blockDistanceSq = blockHitResult.getLocation().distanceToSqr(from);
        
        if (blockHitResult.getType() != HitResult.Type.MISS) {
            maxDistanceSq = blockDistanceSq;
            maxDistance = Math.sqrt(blockDistanceSq);
        }
        
        Vec3 direction = cameraEntity.getViewVector(partialTicks);
        Vec3 to = from.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
        var box = cameraEntity.getBoundingBox().expandTowards(direction.scale(maxDistance)).inflate(1.0, 1.0, 1.0);
        
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, Entity::isPickable, maxDistanceSq);
        
        return entityHitResult != null && entityHitResult.getLocation().distanceToSqr(from) < blockDistanceSq
            ? vb$filterHitResult(entityHitResult, from, entityReach)
            : vb$filterHitResult(blockHitResult, from, blockReach);
    }
    
    @Unique
    private static HitResult vb$filterHitResult(HitResult hitResult, Vec3 from, double maxRange) {
        Vec3 hitLocation = hitResult.getLocation();
        if (!hitLocation.closerThan(from, maxRange)) {
            Vec3 location = hitResult.getLocation();
            Direction direction = Direction.getNearest(location.x - from.x, location.y - from.y, location.z - from.z);
            return BlockHitResult.miss(location, direction, BlockPos.containing(location));
        } else {
            return hitResult;
        }
    }
}
