package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.vanillabackport.core.mixin.access.EntityAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class InterpolationHandler {
    private final Entity entity;
    private final int interpolationSteps;
    private final InterpolationData data = new InterpolationData(0, Vec3.ZERO, 0.0F, 0.0F);
    @Nullable private Vec3 previousTickPosition;
    @Nullable private Vec2 previousTickRot;

    public InterpolationHandler(Entity entity, int steps) {
        this.interpolationSteps = steps;
        this.entity = entity;
    }

    public void interpolateTo(Vec3 position, float y, float x) {
        if (this.interpolationSteps == 0) {
            this.entity.setPosRaw(position.x, position.y, position.z);
            this.entity.setYRot(y);
            this.entity.setXRot(x);
            this.entity.setOldPosAndRot();
            ((EntityAccessor) this.entity).callReapplyPosition();
            this.cancel();
        } else {
            this.data.steps = this.interpolationSteps;
            this.data.position = position;
            this.data.yRot = y;
            this.data.xRot = x;
            this.previousTickPosition = this.entity.position();
            this.previousTickRot = new Vec2(this.entity.getXRot(), this.entity.getYRot());
        }
    }

    public boolean hasActiveInterpolation() {
        return this.data.steps > 0;
    }

    public void interpolate() {
        if (!this.hasActiveInterpolation()) {
            this.cancel();
        } else {
            double progress = 1.0 / this.data.steps;
            if (this.previousTickPosition != null) {
                Vec3 movement = this.entity.position().subtract(this.previousTickPosition);
                if (this.entity.level().noCollision(this.entity, ((EntityAccessor) this.entity).getDimensions().makeBoundingBox(this.data.position.add(movement)))) {
                    this.data.addDelta(movement);
                }
            }

            if (this.previousTickRot != null) {
                float yRot = this.entity.getYRot() - this.previousTickRot.y;
                float xRot = this.entity.getXRot() - this.previousTickRot.x;
                this.data.addRotation(yRot, xRot);
            }

            double x = Mth.lerp(progress, this.entity.getX(), this.data.position.x);
            double y = Mth.lerp(progress, this.entity.getY(), this.data.position.y);
            double z = Mth.lerp(progress, this.entity.getZ(), this.data.position.z);
            Vec3 position = new Vec3(x, y, z);

            float yRot = Mth.rotLerp((float) progress, this.entity.getYRot(), this.data.yRot);
            float xRot = (float)Mth.lerp(progress, this.entity.getXRot(), this.data.xRot);

            this.entity.setPos(position);
            ((EntityAccessor) this.entity).callSetRot(yRot, xRot);

            this.data.decrease();
            this.previousTickPosition = position;
            this.previousTickRot = new Vec2(this.entity.getXRot(), this.entity.getYRot());
        }
    }

    public void cancel() {
        this.data.steps = 0;
        this.previousTickPosition = null;
        this.previousTickRot = null;
    }

    static class InterpolationData {
        protected int steps;
        Vec3 position;
        float yRot;
        float xRot;

        InterpolationData(int steps, Vec3 position, float yRot, float xRot) {
            this.steps = steps;
            this.position = position;
            this.yRot = yRot;
            this.xRot = xRot;
        }

        public void decrease() {
            this.steps--;
        }

        public void addDelta(Vec3 vec3) {
            this.position = this.position.add(vec3);
        }

        public void addRotation(float f, float g) {
            this.yRot += f;
            this.xRot += g;
        }
    }
}