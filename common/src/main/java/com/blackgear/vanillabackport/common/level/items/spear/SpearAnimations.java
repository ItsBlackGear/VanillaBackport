package com.blackgear.vanillabackport.common.level.items.spear;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.common.level.components.KineticWeapon;
import com.blackgear.vanillabackport.common.level.components.KineticWeapon.Condition;
import com.blackgear.vanillabackport.core.util.Ease;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class SpearAnimations {
    private static float progress(float time, float start, float end) {
        return Mth.clamp(Mth.inverseLerp(time, start, end), 0.0F, 1.0F);
    }

    public static <T extends LivingEntity> void thirdPersonHandUse(
        ModelPart arm,
        ModelPart head,
        boolean holdingInRightArm,
        ItemStack item,
        T entity
    ) {
        float partial = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        MobSpearHandler handler = (MobSpearHandler) entity;

        int invert = holdingInRightArm ? 1 : -1;
        arm.yRot = -0.1F * invert + head.yRot;
        arm.xRot = -Mth.HALF_PI + head.xRot + 0.8F;
        if (entity.isFallFlying() || entity.getSwimAmount(partial) > 0.0F) {
            arm.xRot -= 0.9599311F;
        }

        arm.yRot = Mth.DEG_TO_RAD * Mth.clamp(Mth.RAD_TO_DEG * arm.yRot, -60.0F, 60.0F);
        arm.xRot = Mth.DEG_TO_RAD * Mth.clamp(Mth.RAD_TO_DEG * arm.xRot, -120.0F, 30.0F);
        if (handler.vb$getTicksUsingItem(partial) > 0.0F && (!entity.isUsingItem() || entity.getUsedItemHand() == (holdingInRightArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND))) {
            KineticWeapon kineticWeapon = KineticWeapon.get(item);
            if (kineticWeapon != null) {
                UseParams params = UseParams.fromKineticWeapon(kineticWeapon, handler.vb$getTicksUsingItem(partial));
                arm.yRot = arm.yRot + -invert * params.swayScaleFast() * Mth.DEG_TO_RAD * params.swayIntensity() * 1.0F;
                arm.zRot = arm.zRot + -invert * params.swayScaleSlow() * Mth.DEG_TO_RAD * params.swayIntensity() * 0.5F;
                arm.xRot = arm.xRot + Mth.DEG_TO_RAD * (
                    -40.0F * params.raiseProgressStart() +
                    30.0F * params.raiseProgressMiddle() +
                    -20.0F * params.raiseProgressEnd() +
                    20.0F * params.lowerProgress() +
                    10.0F * params.raiseBackProgress() +
                    0.6F * params.swayScaleSlow() * params.swayIntensity()
                );
            }
        }
    }

    public static <T extends LivingEntity> void thirdPersonUseItem(
        T entity,
        PoseStack pose,
        float timeHeld,
        HumanoidArm arm,
        ItemStack actualItem
    ) {
        float partial = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);

        KineticWeapon kineticWeapon = KineticWeapon.get(actualItem);
        if (kineticWeapon != null && timeHeld != 0.0F) {
            float attack = Ease.inQuad(progress(entity.getAttackAnim(partial), 0.05F, 0.2F));
            float retract = Ease.inOutExpo(progress(entity.getAttackAnim(partial), 0.4F, 1.0F));
            UseParams params = UseParams.fromKineticWeapon(kineticWeapon, timeHeld);
            int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
            float raiseProgressModified = 1.0F - Ease.outBack(1.0F - params.raiseProgress());
            float hitFeedback = hitFeedbackAmount(((MobSpearHandler)entity).vb$getTicksSinceLastKineticHitFeedback(partial));
            pose.translate(0.0, -hitFeedback * 0.4, -kineticWeapon.forwardMovement() * (raiseProgressModified - params.raiseBackProgress()) + hitFeedback);
            pose.rotateAround(Axis.XN.rotationDegrees(70.0F * (params.raiseProgress() - params.raiseBackProgress()) - 40.0F * (attack - retract)), 0.0F, -0.03125F, 0.125F);
            pose.rotateAround(Axis.YP.rotationDegrees(invert * 90 * (params.raiseProgress() - params.swayProgress() + 3.0F * retract + attack)), 0.0F, 0.0F, 0.125F);
        }
    }

    public static <T extends LivingEntity> void thirdPersonAttackHand(
        HumanoidModel<T> model,
        T entity
    ) {
        float partial = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        float attackTime = entity.getAttackAnim(partial);
        HumanoidArm attackArm = entity.swingingArm == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();

        model.rightArm.yRot = model.rightArm.yRot - model.body.yRot;
        model.leftArm.yRot = model.leftArm.yRot - model.body.yRot;
        model.leftArm.xRot = model.leftArm.xRot - model.body.yRot;
        float prepare = Ease.inOutSine(progress(attackTime, 0.0F, 0.05F));
        float attack = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
        float retract = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
        ModelPart arm = attackArm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        arm.xRot += (90.0F * prepare - 120.0F * attack + 30.0F * retract) * Mth.DEG_TO_RAD;
    }

    public static <T extends LivingEntity> void thirdPersonAttackItem(
        T entity,
        PoseStack pose
    ) {
        float partial = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        float attackTime = entity.getAttackAnim(partial);

        if (!(attackTime <= 0.0F)) {
            KineticWeapon kineticWeapon = KineticWeapon.get(entity.getMainHandItem());
            float jetForward = kineticWeapon != null ? kineticWeapon.forwardMovement() : 0.0F;
            float attack = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
            float retract = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
            pose.rotateAround(Axis.XN.rotationDegrees(70.0F * (attack - retract)), 0.0F, -0.125F, 0.125F);
            pose.translate(0.0F, jetForward * (attack - retract), 0.0F);
        }
    }

    private static float hitFeedbackAmount(float ticksSinceFeedbackStart) {
        return 0.4F * (Ease.outQuart(progress(ticksSinceFeedbackStart, 1.0F, 3.0F)) - Ease.inOutSine(progress(ticksSinceFeedbackStart, 3.0F, 10.0F)));
    }

    public static void firstPersonUse(
        float ticksSinceKineticHitFeedback,
        PoseStack pose,
        float timeHeld,
        HumanoidArm arm,
        ItemStack stack
    ) {
        KineticWeapon kineticWeapon = KineticWeapon.get(stack);
        if (kineticWeapon != null) {
            UseParams params = UseParams.fromKineticWeapon(kineticWeapon, timeHeld);
            int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
            pose.translate(
                invert * (params.raiseProgress() * 0.15F + params.raiseProgressEnd() * -0.05F + params.swayProgress() * -0.1F + params.swayScaleSlow() * 0.005F),
                params.raiseProgress() * -0.075F + params.raiseProgressMiddle() * 0.075F + params.swayScaleFast() * 0.01F,
                params.raiseProgressStart() * 0.05 + params.raiseProgressEnd() * -0.05 + params.swayScaleSlow() * 0.005F
            );
            pose.rotateAround(
                Axis.XP.rotationDegrees(-65.0F * Ease.inOutBack(params.raiseProgress()) - 35.0F * params.lowerProgress() + 100.0F * params.raiseBackProgress() + -0.5F * params.swayScaleFast()),
                0.0F,
                0.1F,
                0.0F
            );
            pose.rotateAround(
                Axis.YN.rotationDegrees(invert * (-90.0F * progress(params.raiseProgress(), 0.5F, 0.55F) + 90.0F * params.swayProgress() + 2.0F * params.swayScaleSlow())),
                invert * 0.15F,
                0.0F,
                0.0F
            );
            pose.translate(0.0F, -hitFeedbackAmount(ticksSinceKineticHitFeedback), 0.0F);
        }
    }

    public static void firstPersonAttack(
        float attack,
        PoseStack pose,
        int invert
    ) {
        float startingAmount = Ease.inOutSine(progress(attack, 0.0F, 0.05F));
        float middleAmount = Ease.outBack(progress(attack, 0.05F, 0.2F));
        float endingAmount = Ease.inOutExpo(progress(attack, 0.4F, 1.0F));
        pose.translate(invert * 0.1F * (startingAmount - middleAmount), -0.075F * (startingAmount - endingAmount), 0.65F * (startingAmount - middleAmount));
        pose.mulPose(Axis.XP.rotationDegrees(-70.0F * (startingAmount - endingAmount)));
        pose.translate(0.0, 0.0, -0.25 * (endingAmount - middleAmount));
    }

    record UseParams(
        float raiseProgress,
        float raiseProgressStart,
        float raiseProgressMiddle,
        float raiseProgressEnd,
        float swayProgress,
        float lowerProgress,
        float raiseBackProgress,
        float swayIntensity,
        float swayScaleSlow,
        float swayScaleFast
    ) {
        public static UseParams fromKineticWeapon(KineticWeapon kineticWeapon, float time) {
            int finishRaisingTick = kineticWeapon.delayTicks();
            int finishSwayingTick = kineticWeapon.dismountConditions().map(Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
            int startSwayingTick = finishSwayingTick - 20;
            int finishLoweringTick = kineticWeapon.knockbackConditions().map(Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
            int startLoweringTick = finishLoweringTick - 40;
            int finishRaisingBackTick = kineticWeapon.damageConditions().map(Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
            float raiseProgress = SpearAnimations.progress(time, 0.0F, (float) finishRaisingTick);
            float raiseProgressStart = SpearAnimations.progress(raiseProgress, 0.0F, 0.5F);
            float raiseProgressMiddle = SpearAnimations.progress(raiseProgress, 0.5F, 0.8F);
            float raiseProgressEnd = SpearAnimations.progress(raiseProgress, 0.8F, 1.0F);
            float swayProgress = SpearAnimations.progress(time, (float) startSwayingTick, (float) finishSwayingTick);
            float lowerProgress = Ease.outCubic(Ease.inOutElastic(SpearAnimations.progress(time - 20.0F, (float) startLoweringTick, (float) finishLoweringTick)));
            float raiseBackProgress = SpearAnimations.progress(time, (float) (finishRaisingBackTick - 5), (float) finishRaisingBackTick);
            float swayIntensity = 2.0F * Ease.outCirc(swayProgress) - 2.0F * Ease.inCirc(raiseBackProgress);
            float swayScaleSlow = Mth.sin(time * 19.0F * Mth.DEG_TO_RAD) * swayIntensity;
            float swayScaleFast = Mth.sin(time * 30.0F * Mth.DEG_TO_RAD) * swayIntensity;

            return new UseParams(
                raiseProgress,
                raiseProgressStart,
                raiseProgressMiddle,
                raiseProgressEnd,
                swayProgress,
                lowerProgress,
                raiseBackProgress,
                swayIntensity,
                swayScaleSlow,
                swayScaleFast
            );
        }
    }
}