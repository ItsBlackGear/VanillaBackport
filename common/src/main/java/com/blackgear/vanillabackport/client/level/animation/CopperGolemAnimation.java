package com.blackgear.vanillabackport.client.level.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

@Environment(EnvType.CLIENT)
public class CopperGolemAnimation {
	public static final AnimationDefinition COPPER_GOLEM_WALK = AnimationDefinition.Builder.withLength(0.8333F)
		.looping()
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(10.0F, -1.87F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(10.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, -0.82F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(10.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 1.87F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.625F, KeyframeAnimations.degreeVec(-10.0F, 0.82F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.build();
	public static final AnimationDefinition COPPER_GOLEM_IDLE = AnimationDefinition.Builder.withLength(3.5F)
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, -35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(0.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.75F, KeyframeAnimations.degreeVec(-25.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(-25.0F, 300.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.build();
	public static final AnimationDefinition COPPER_GOLEM_WALK_ITEM = AnimationDefinition.Builder.withLength(0.8333F)
		.looping()
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(10.0F, -1.87F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(10.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, -0.82F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(10.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 1.87F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.625F, KeyframeAnimations.degreeVec(-10.0F, 0.82F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(-59.78638F, -6.49053F, -3.76613F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION, new Keyframe(0.0F, KeyframeAnimations.degreeVec(-59.78638F, 6.49053F, 3.76613F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION, new Keyframe(0.0F, KeyframeAnimations.posVec(-0.21129F, -0.0212F, -0.07004F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			)
		)
		.build();
	public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_ITEM_DROP = AnimationDefinition.Builder.withLength(3.0F)
		.looping()
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.9167F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.125F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7083F, KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.degreeVec(10.15255F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(17.86088F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.875F, KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9167F, KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-0.26F, -1.93F, -3.73F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.875F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9167F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.posVec(-0.39F, 0.52F, -2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-117.82767F, 2.94538F, 0.22703F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-97.7902F, 0.73403F, 1.39387F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-44.60123F, 10.14454F, 8.66307F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2083F, KeyframeAnimations.degreeVec(-113.7629F, 21.38835F, 15.48184F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-113.76F, 21.39F, 15.48F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-39.99304F, 7.3511F, 14.05666F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(68.07913F, -3.61348F, 1.39182F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.degreeVec(193.16708F, -1.90441F, -0.43495F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(250.66708F, -1.90441F, -0.43495F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7917F, KeyframeAnimations.degreeVec(264.19006F, -4.41519F, -0.66792F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.degreeVec(270.53693F, 16.03493F, 0.47968F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(319.31668F, 17.97846F, 1.34328F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5417F, KeyframeAnimations.posVec(-0.79739F, -0.10573F, 1.70592F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-88.58526F, -17.10045F, 11.7676F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-88.59F, -17.1F, 11.77F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-46.59531F, -16.13694F, -3.85578F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0512F, -0.76176F, 3.12882F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5833F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.posVec(0.0F, 0.0909F, -0.10834F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5833F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.posVec(0.0F, 0.0909F, -0.10834F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.build();
	public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_ITEM_NODROP = AnimationDefinition.Builder.withLength(3.0F)
		.looping()
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.9167F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.125F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7083F, KeyframeAnimations.degreeVec(9.73588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.degreeVec(10.15255F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(17.86088F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.875F, KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9167F, KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(17.23588F, -1.93433F, -3.73384F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-0.26F, -1.93F, -3.73F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(1.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.875F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9167F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.posVec(-0.39F, 0.52F, -2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, -0.01091F, -0.02988F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-117.82767F, 2.94538F, 0.22703F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-97.7902F, 0.73403F, 1.39387F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-44.60123F, 10.14454F, 8.66307F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-4.31506F, 6.54961F, 13.21388F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(-6.53898F, 13.96898F, 14.34786F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(3.50393F, -4.70737F, 8.3608F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(3.50393F, -4.70737F, 8.3608F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(3.90089F, -4.3843F, 3.35549F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(3.90089F, -4.3843F, 3.35549F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5417F, KeyframeAnimations.posVec(-0.79739F, -0.10573F, 1.70592F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(-0.51052F, -0.38088F, 0.79745F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(-0.46F, -0.34F, 0.72F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -20.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(2.47864F, -0.32621F, -12.50706F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(2.47864F, -0.32621F, -12.50706F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(2.41492F, -0.64686F, -5.01363F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(2.41492F, -0.64686F, -5.01363F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.03F, -0.76F, 0.45F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.SCALE,
				new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.build();
	public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_NOITEM_GET = AnimationDefinition.Builder.withLength(3.0F)
		.looping()
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.9167F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.125F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.degreeVec(21.43F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(24.14867F, -20.70481F, -9.00717F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(24.14867F, -20.70481F, -9.00717F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.75F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.posVec(-0.01805F, 0.88303F, -0.09783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.posVec(0.0F, 0.46194F, -0.19134F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.posVec(0.0F, 0.46194F, -0.19134F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9167F, KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9583F, KeyframeAnimations.degreeVec(10.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0F, KeyframeAnimations.degreeVec(5.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(0.16381F, -16.71134F, -6.35306F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.degreeVec(0.15732F, -4.21139F, -6.31751F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.degreeVec(0.07901F, 5.3943F, -3.15187F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(4.53867F, 7.47675F, 0.59181F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-2.53852F, 9.99038F, -0.44067F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-12.68664F, 9.76061F, -2.18558F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(-15.19938F, 22.36971F, -3.52259F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-3.02173F, 22.37156F, -2.41802F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(-0.52173F, 22.37156F, -2.41802F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.75F, KeyframeAnimations.degreeVec(-12.40598F, -0.4674F, -1.79838F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9167F, KeyframeAnimations.posVec(-0.22438F, 0.82319F, -1.27252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9583F, KeyframeAnimations.posVec(-0.52521F, 0.96725F, -0.32978F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0F, KeyframeAnimations.posVec(-0.52521F, 0.96725F, -0.32978F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(-0.5345F, 1.16541F, -0.37206F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.posVec(-0.5345F, 1.16541F, -0.37206F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-82.47403F, 17.82361F, 2.17224F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5F, KeyframeAnimations.degreeVec(-85.08388F, 14.26971F, 1.99595F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-85.16266F, 13.19102F, 2.43976F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-98.33F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-84.12204F, 8.95753F, 14.11779F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2083F, KeyframeAnimations.degreeVec(-84.12204F, 8.95753F, 14.11779F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(-93.6065F, 13.90544F, 15.98524F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-124.48661F, 66.29146F, -7.28605F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.degreeVec(-129.4866F, 66.29146F, -7.28605F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-108.91607F, 1.79762F, 20.93924F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(-102.18303F, 4.35881F, 17.40962F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-98.33642F, -0.70114F, 4.09322F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(-98.39385F, 6.71929F, 3.00137F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-98.33981F, 1.77244F, 3.7307F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(-100.70987F, 3.48829F, 7.1138F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.75F, KeyframeAnimations.degreeVec(-97.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7917F, KeyframeAnimations.degreeVec(-87.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.degreeVec(-97.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(-102.95F, 6.92F, 13.88F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.degreeVec(-76.475F, 3.46F, 6.94F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(-26.475F, 3.46F, 6.94F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5417F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.375F, KeyframeAnimations.posVec(-0.51F, -0.38F, 0.8F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.posVec(-2.14094F, 0.69619F, 1.23422F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(-0.97932F, 0.38244F, 0.12884F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(-1.55232F, 1.79904F, 0.37956F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.posVec(-1.53125F, 1.64598F, 1.41168F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.posVec(-1.57256F, 1.05375F, 1.32469F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.75F, KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7917F, KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.posVec(-1.33F, 0.16F, 1.02F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.posVec(-0.5748F, 0.38848F, 1.45646F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(-0.67F, 0.08F, 0.51F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.9583F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2083F, KeyframeAnimations.degreeVec(-88.58526F, -17.10045F, 11.7676F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-88.59F, -17.1F, 11.77F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-46.59531F, -16.13694F, -3.85578F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(-24.5317F, -19.0214F, -13.70805F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0512F, -0.76176F, 3.12882F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7917F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.7917F, KeyframeAnimations.posVec(0.0F, 0.09F, -0.11F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.build();
	public static final AnimationDefinition COPPER_GOLEM_CHEST_INTERACTION_NOITEM_NOGET = AnimationDefinition.Builder.withLength(3.0F)
		.looping()
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(24.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.9167F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(14.72765F, -31.63886F, -7.85085F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.125F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(12.40525F, -4.4E-4F, 0.00829F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(13.92716F, 26.80536F, 6.38918F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(13.93F, 26.81F, 6.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7083F, KeyframeAnimations.degreeVec(12.40725F, 0.00444F, 0.00783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(12.40725F, 0.00444F, 0.00783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.125F, KeyframeAnimations.degreeVec(12.40725F, 0.0F, 0.00783F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"body",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.4F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7083F, KeyframeAnimations.posVec(0.0F, 0.34F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(0.0F, 0.34F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7083F, KeyframeAnimations.degreeVec(0.57F, -1.25F, 0.07F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.89798F, -18.12465F, -0.16276F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7917F, KeyframeAnimations.degreeVec(1.21328F, -21.15422F, -0.2148F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.875F, KeyframeAnimations.degreeVec(1.21328F, -21.15422F, -0.2148F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0F, KeyframeAnimations.degreeVec(1.21328F, -21.15422F, -0.2148F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(2.56546F, 0.76525F, 0.57246F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(4.53867F, 7.47675F, 0.59181F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(4.53867F, 7.47675F, 0.59181F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"head",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -0.15451F, 0.47553F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, -0.01F, -0.03F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-7.38733F, 1.29876F, 9.91615F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-34.55418F, 11.73507F, 36.8361F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-82.47403F, 17.82361F, 2.17224F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5F, KeyframeAnimations.degreeVec(-85.08388F, 14.26971F, 1.99595F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-85.16266F, 13.19102F, 2.43976F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.degreeVec(-92.79F, 0.73F, 1.39F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-95.83405F, 33.18639F, -0.40081F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.degreeVec(-95.83F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-98.33F, 33.19F, -0.4F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-56.46674F, 3.3853F, 14.45894F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.degreeVec(3.9F, -4.38F, 3.36F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(3.90089F, -4.3843F, 3.35549F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.8333F, KeyframeAnimations.posVec(0.25358F, -0.20153F, 2.21248F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.posVec(0.25F, -0.2F, 2.21F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5417F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(-0.26323F, -1.46323F, 0.66566F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.posVec(-0.46F, -0.88F, -0.3F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(-0.46F, 0.1159F, -0.30086F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-21.59341F, -12.60837F, -45.69252F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-120.7755F, -5.21988F, -2.02064F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(-98.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-93.27F, -1.79F, -1.15F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-93.27419F, -1.79323F, -1.15048F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-93.55693F, -22.3224F, 3.64383F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-95.75F, -2.42F, 5.97F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.25F, KeyframeAnimations.degreeVec(-98.4029F, -17.39503F, 6.85104F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-101.24523F, -29.87096F, 7.69993F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-101.25F, -29.87F, 7.7F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-88.17772F, -42.09094F, 10.96195F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2083F, KeyframeAnimations.degreeVec(-88.58526F, -17.10045F, 11.7676F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(-88.59F, -17.1F, 11.77F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-46.59531F, -16.13694F, -3.85578F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-24.5317F, -19.0214F, -13.70805F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.degreeVec(-24.5317F, -19.0214F, -13.70805F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.degreeVec(2.41F, -0.65F, -5.01F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(2.41492F, -0.64686F, -5.01363F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_arm",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(1.6667F, KeyframeAnimations.posVec(-0.00677F, -0.76064F, 3.19059F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0512F, -0.76176F, 3.12882F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.4583F, KeyframeAnimations.posVec(0.03F, -0.51F, 2.09F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5F, KeyframeAnimations.posVec(0.03F, -0.51F, 2.09F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.5417F, KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9167F, KeyframeAnimations.posVec(0.03F, -1.28F, -0.07F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.03F, -0.28229F, -0.07133F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"right_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.addAnimation(
			"left_leg",
			new AnimationChannel(
				AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			)
		)
		.build();
}
