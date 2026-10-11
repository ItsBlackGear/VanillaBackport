package com.blackgear.vanillabackport.core.mixin.client.waypoints;

import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Camera.class)
public class CameraMixin implements TrackedWaypoint.Camera {
    @Shadow private Vec3 position;
    @Shadow private float yRot;
    
    @Override
    public float vb$yaw() {
        return Mth.wrapDegrees(this.yRot);
    }
    
    @Override
    public Vec3 vb$position() {
        return this.position;
    }
}