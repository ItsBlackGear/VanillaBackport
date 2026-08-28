package com.blackgear.vanillabackport.common.level.items.enchantment.effects;

import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record PlaySoundEffect(List<Holder<SoundEvent>> soundEvent, FloatProvider volume, FloatProvider pitch) implements EnchantmentEntityEffect {
	public static final MapCodec<PlaySoundEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		AdditionalCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255)).fieldOf("sound").forGetter(PlaySoundEffect::soundEvent),
		FloatProvider.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundEffect::volume),
		FloatProvider.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundEffect::pitch)
	).apply(instance, PlaySoundEffect::new));
	
	@Override
	public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
		if (!entity.isSilent()) {
			RandomSource randomSource = entity.getRandom();
			int index = Mth.clamp(enchantmentLevel - 1, 0, this.soundEvent.size() - 1);
			level.playSound(
				null,
				origin.x(),
				origin.y(),
				origin.z(),
				this.soundEvent.get(index),
				entity.getSoundSource(),
				this.volume.sample(randomSource),
				this.pitch.sample(randomSource)
			);
		}
	}
	
	@Override
	public MapCodec<PlaySoundEffect> codec() {
		return CODEC;
	}
}