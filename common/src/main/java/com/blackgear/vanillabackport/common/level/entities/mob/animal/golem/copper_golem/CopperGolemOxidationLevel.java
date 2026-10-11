package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public record CopperGolemOxidationLevel(
	SoundEvent spinHeadSound,
	SoundEvent hurtSound,
	SoundEvent deathSound,
	SoundEvent stepSound,
	ResourceLocation texture,
	ResourceLocation eyeTexture
) {}