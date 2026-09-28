package com.blackgear.vanillabackport.common.level.items.enchantment;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.GraceTimeWeaponHolder;
import com.blackgear.vanillabackport.common.registries.items.ModEnchantments;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class EnchantmentUtils {
    private static final List<Supplier<SoundEvent>> LUNGE_SOUNDS = List.of(ModSoundEvents.LUNGE_1, ModSoundEvents.LUNGE_2, ModSoundEvents.LUNGE_3);
    
    public static int getLungeLevel(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LUNGE.get(), stack);
    }
    
    public static void doPostPiercingAttack(ServerLevel level, LivingEntity user) {
        if (!(user instanceof ServerPlayer player)) return;
        
        ItemStack weapon = user.getMainHandItem();
        int lungeLevel = getLungeLevel(weapon);
        if (lungeLevel <= 0) return;
        
        if (user.getVehicle() == null && !user.isFallFlying() && !player.getAbilities().flying && !user.isInWater()) {
            boolean canLunge = player.gameMode.getGameModeForPlayer() == GameType.CREATIVE || player.getFoodData().getFoodLevel() >= 7;
            
            if (!canLunge) return;
            
            Vec3 look = player.getLookAngle();
            Vec3 direction = look.with(Direction.Axis.Y, 0.0)
                .normalize()
                .scale(0.458F * (float) lungeLevel);
            
            player.addDeltaMovement(direction);
            player.hurtMarked = true;
            player.hasImpulse = true;
            
            GraceTimeWeaponHolder.of(player).applyPostImpulseGraceTime(10);
            weapon.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            
            player.causeFoodExhaustion(3.0F * (float) lungeLevel);
            
            if (!player.isSilent()) {
                int soundIndex = Math.min(lungeLevel - 1, LUNGE_SOUNDS.size() - 1);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), LUNGE_SOUNDS.get(soundIndex).get(), player.getSoundSource(), 1.0F, 1.0F);
            }
        }
    }
}