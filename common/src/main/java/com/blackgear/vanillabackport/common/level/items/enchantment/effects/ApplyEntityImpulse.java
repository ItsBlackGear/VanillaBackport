package com.blackgear.vanillabackport.common.level.items.enchantment.effects;

import com.blackgear.vanillabackport.core.mixin.common.access.PlayerAccessor;
import com.blackgear.vanillabackport.core.util.Utilities.VectorUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record ApplyEntityImpulse(Vec3 direction, Vec3 coordinateScale, LevelBasedValue magnitude) implements EnchantmentEntityEffect {
	public static final MapCodec<ApplyEntityImpulse> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Vec3.CODEC.fieldOf("direction").forGetter(ApplyEntityImpulse::direction),
		Vec3.CODEC.fieldOf("coordinate_scale").forGetter(ApplyEntityImpulse::coordinateScale),
		LevelBasedValue.CODEC.fieldOf("magnitude").forGetter(ApplyEntityImpulse::magnitude)
	).apply(instance, ApplyEntityImpulse::new));
	
	@Override
	public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
		Vec3 look = entity.getLookAngle();
		Vec3 direction = VectorUtils.addLocalCoordinates(look, this.direction).multiply(this.coordinateScale).scale(this.magnitude.calculate(enchantmentLevel));
		entity.addDeltaMovement(direction);
		entity.hurtMarked = true;
		entity.hasImpulse = true;
		if (entity instanceof Player player) {
			PlayerAccessor accessor = (PlayerAccessor) player;
			accessor.setCurrentImpulseContextResetGraceTime(Math.max(accessor.getCurrentImpulseContextResetGraceTime(), 10));
		}
	}
	
	@Override
	public MapCodec<? extends EnchantmentEntityEffect> codec() {
		return CODEC;
	}
}