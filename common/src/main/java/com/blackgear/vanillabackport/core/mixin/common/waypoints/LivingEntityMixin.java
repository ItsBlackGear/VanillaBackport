package com.blackgear.vanillabackport.core.mixin.common.waypoints;

import com.blackgear.vanillabackport.common.api.modules.waypoints.ServerWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointTransmitter;
import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements WaypointTransmitter {
    @Shadow public abstract AttributeMap getAttributes();
    @Shadow public abstract double getAttributeValue(Holder<Attribute> attribute);
    
    @Unique private Icon locatorBarIcon = new Icon();
    
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createLivingAttributes(AttributeSupplier.Builder original) {
        original.add(ModAttributes.WAYPOINT_TRANSMIT_RANGE);
        
        return original;
    }
    
    @Override
    public boolean isTransmittingWaypoint() {
        return this.getAttributeValue(ModAttributes.WAYPOINT_TRANSMIT_RANGE) > 0.0;
    }
    
    @Override
    public Optional<Connection> makeWaypointConnectionWith(ServerPlayer player) {
        LivingEntity self = (LivingEntity)(Object) this;
        if (this.firstTick || player == self) {
            return Optional.empty();
        } else if (WaypointTransmitter.doesSourceIgnoreReceiver(self, player)) {
            return Optional.empty();
        } else {
            Icon icon = this.locatorBarIcon.cloneAndAssignStyle(self);
            if (WaypointTransmitter.isReallyFar(self, player)) {
                return Optional.of(new EntityAzimuthConnection(self, icon, player));
            } else {
                return !WaypointTransmitter.isChunkVisible(self.chunkPosition(), player)
                    ? Optional.of(new EntityChunkConnection(self, icon, player))
                    : Optional.of(new EntityBlockConnection(self, icon, player));
            }
        }
    }
    
    @Override
    public Icon waypointIcon() {
        return this.locatorBarIcon;
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void vb$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (this.locatorBarIcon.hasData()) {
            Icon.CODEC.encodeStart(NbtOps.INSTANCE, this.locatorBarIcon).resultOrPartial().ifPresent(tag -> compound.put("locator_bar_icon", tag));
        }
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void vb$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("locator_bar_icon")) {
            this.locatorBarIcon = Icon.CODEC.parse(NbtOps.INSTANCE, compound.get("locator_bar_icon")).resultOrPartial().orElseGet(Icon::new);
        } else {
            this.locatorBarIcon = new Icon();
        }
    }
    
    @Inject(method = "remove", at = @At("TAIL"))
    private void vb$onRemoval(Entity.RemovalReason reason, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object) this;
        if (self.level() instanceof ServerLevel level) {
            ServerWaypointManager.get(level).untrackWaypoint(WaypointTransmitter.of(self));
        }
    }
}