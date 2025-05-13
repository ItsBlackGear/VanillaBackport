package com.blackgear.vanillabackport.common.level.entities.happyghast;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class HappyGhastTemptGoal extends Goal {
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10.0).ignoreLineOfSight();
    private final TargetingConditions targetingConditions;
    protected final HappyGhast ghast;
    private final double speedModifier;
    private double px;
    private double py;
    private double pz;
    private double pRotX;
    private double pRotY;
    @Nullable protected Player player;
    private int calmDown;
    private final Predicate<ItemStack> items;
    private final boolean canScare;
    private final double stopDistance;

    public HappyGhastTemptGoal(HappyGhast ghast, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
        this.ghast = ghast;
        this.speedModifier = speedModifier;
        this.items = items;
        this.canScare = canScare;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
    }

    @Override
    public boolean canUse() {
        if (this.calmDown > 0) {
            this.calmDown--;
            return false;
        } else {
            this.player = this.ghast.level().getNearestPlayer(this.targetingConditions.range(16), this.ghast);
            return this.player != null;
        }
    }

    private boolean shouldFollow(LivingEntity entity) {
        return this.items.test(entity.getMainHandItem()) || this.items.test(entity.getOffhandItem());
    }

    @Override
    public boolean canContinueToUse() {
        if (this.canScare() && this.player != null) {
            if (this.ghast.distanceToSqr(this.player) < 36.0) {
                if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.01) {
                    return false;
                }

                if (Math.abs((double) this.player.getXRot() - this.pRotX) > 5.0 || Math.abs((double)this.player.getYRot() - this.pRotY) > 5.0) {
                    return false;
                }
            } else {
                this.px = this.player.getX();
                this.py = this.player.getY();
                this.pz = this.player.getZ();
            }

            this.pRotX = this.player.getXRot();
            this.pRotY = this.player.getYRot();
        }

        return this.canUse();
    }

    protected boolean canScare() {
        return this.canScare;
    }

    @Override
    public void start() {
        if (this.player == null) return;

        this.px = this.player.getX();
        this.py = this.player.getY();
        this.pz = this.player.getZ();
    }

    @Override
    public void stop() {
        this.player = null;
        this.ghast.getNavigation().stop();
        this.calmDown = reducedTickDelay(100);
    }

    @Override
    public void tick() {
        if (this.player == null) return;

        this.ghast.getLookControl().setLookAt(this.player, (float)(this.ghast.getMaxHeadYRot() + 20), (float) this.ghast.getMaxHeadXRot());
        if (this.ghast.distanceToSqr(this.player) < this.stopDistance * this.stopDistance) {
            ((HappyGhast.GhastMoveControl) this.ghast.getMoveControl()).setWait();
        } else {
            Vec3 vec3 = player.getEyePosition().subtract(this.ghast.position()).scale(this.ghast.getRandom().nextDouble()).add(this.ghast.position());
            this.ghast.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, this.speedModifier);
        }
    }
}