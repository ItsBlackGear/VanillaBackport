package com.blackgear.vanillabackport.core.mixin.client.waypoints;

import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements TrackedWaypoint.Projector {
    @Shadow @Final private Camera mainCamera;
    @Shadow protected abstract double getFov(Camera activeRenderInfo, float partialTicks, boolean useFOVSetting);
    @Shadow public abstract Matrix4f getProjectionMatrix(double fov);
    
    @Override
    public Vec3 vb$projectPointToScreen(Vec3 point) {
        Matrix4f projection = this.getProjectionMatrix(this.getFov(this.mainCamera, 0.0F, true));
        Quaternionf rotation = this.mainCamera.rotation().conjugate(new Quaternionf());
        Matrix4f viewMatrix = new Matrix4f().rotation(rotation);
        Matrix4f viewProjection = projection.mul(viewMatrix);
        Vec3 camPos = this.mainCamera.getPosition();
        Vec3 offset = point.subtract(camPos);
        Vector3f vector3f = viewProjection.transformProject(offset.toVector3f());
        return new Vec3(vector3f);
    }
    
    @Override
    public double vb$projectHorizonToScreen() {
        float xRot = this.mainCamera.getXRot();
        if (xRot <= -90.0F) {
            return Double.NEGATIVE_INFINITY;
        } else if (xRot >= 90.0F) {
            return Double.POSITIVE_INFINITY;
        } else {
            float g = (float) this.getFov(this.mainCamera, 0.0F, true);
            return Math.tan(xRot * Mth.DEG_TO_RAD) / Math.tan(g / 2.0F * Mth.DEG_TO_RAD);
        }
    }
}