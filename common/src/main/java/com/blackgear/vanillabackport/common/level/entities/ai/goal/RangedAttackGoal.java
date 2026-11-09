package com.blackgear.vanillabackport.common.level.entities.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.IntSupplier;

public class RangedAttackGoal extends Goal {
	private final Mob mob;
	private final RangedAttackMob rangedAttackMob;
	@Nullable private LivingEntity target;
	private int attackTime = -1;
	private final double speedModifier;
	private int seeTime;
	private final IntSupplier attackIntervalMin;
	private final IntSupplier attackIntervalMax;
	private final float attackRadius;
	private final float attackRadiusSqr;

	public RangedAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) {
		this(mob, speedModifier, () -> attackInterval, () -> attackInterval, attackRadius);
	}

	public RangedAttackGoal(RangedAttackMob mob, double speedModifier, IntSupplier attackInterval, float attackRadius) {
		this(mob, speedModifier, attackInterval, attackInterval, attackRadius);
	}

	public RangedAttackGoal(RangedAttackMob mob, double speedModifier, IntSupplier attackIntervalMin, IntSupplier attackIntervalMax, float attackRadius) {
		if (!(mob instanceof LivingEntity)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		} else {
			this.rangedAttackMob = mob;
			this.mob = (Mob)mob;
			this.speedModifier = speedModifier;
			this.attackIntervalMin = attackIntervalMin;
			this.attackIntervalMax = attackIntervalMax;
			this.attackRadius = attackRadius;
			this.attackRadiusSqr = attackRadius * attackRadius;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}
	}

	@Override
	public boolean canUse() {
		LivingEntity bestTarget = this.mob.getTarget();
		if (bestTarget != null && bestTarget.isAlive()) {
			this.target = bestTarget;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canContinueToUse() {
		if (this.target == null) return false;
		return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
	}

	@Override
	public void stop() {
		this.target = null;
		this.seeTime = 0;
		this.attackTime = -1;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		if (this.target == null) return;
		double targetDistSqr = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
		boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(this.target);
		if (hasLineOfSight) {
			this.seeTime++;
		} else {
			this.seeTime = 0;
		}

		boolean targetIsOutOfRange = targetDistSqr > this.attackRadiusSqr;
		if (!targetIsOutOfRange && this.seeTime >= 5 && this.stopWhenInRange()) {
			this.mob.getNavigation().stop();
		} else {
			this.mob.getNavigation().moveTo(this.target, this.speedModifier);
		}

		this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
		if (--this.attackTime == 0) {
			if (!hasLineOfSight || targetIsOutOfRange) return;

			float dist = (float)Math.sqrt(targetDistSqr) / this.attackRadius;
			float power = Mth.clamp(dist, 0.1F, 1.0F);
			this.rangedAttackMob.performRangedAttack(this.target, power);
			this.attackTime = Mth.floor(dist * (this.attackIntervalMax.getAsInt() - this.attackIntervalMin.getAsInt()) + this.attackIntervalMin.getAsInt());
		} else if (this.attackTime < 0) {
			this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(targetDistSqr) / this.attackRadius, this.attackIntervalMin.getAsInt(), this.attackIntervalMax.getAsInt()));
		}
	}

	protected boolean stopWhenInRange() {
		return true;
	}
}
