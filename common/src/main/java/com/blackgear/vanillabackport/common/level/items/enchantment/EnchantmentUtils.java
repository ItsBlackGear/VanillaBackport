package com.blackgear.vanillabackport.common.level.items.enchantment;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.GraceTimeWeaponHolder;
import com.blackgear.vanillabackport.common.registries.items.ModEnchantments;
import com.blackgear.vanillabackport.core.util.Utilities.VectorUtils;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EnchantmentUtils {
    private static final List<SoundEvent> LUNGE_SOUNDS = List.of(ModSoundEvents.LUNGE_1.get(), ModSoundEvents.LUNGE_2.get(), ModSoundEvents.LUNGE_3.get());
    
    public static int getLungeLevel(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LUNGE.get(), stack);
    }
    
    public static boolean hasLunge(ItemStack stack) {
        return getLungeLevel(stack) > 0;
    }
    
    public static void doPostPiercingAttack(ServerLevel level, LivingEntity user) {
        if (!(user instanceof ServerPlayer player)) return;
        
        ItemStack weapon = user.getMainHandItem();
        if (!hasLunge(weapon)) return;
        
        if (user.getVehicle() == null && !user.isFallFlying() && !player.getAbilities().flying && !user.isInWater()) {
            if (player.gameMode.getGameModeForPlayer() == GameType.CREATIVE || MinMaxBounds.Ints.atLeast(Mth.floor(6.0F) + 1).matches(player.getFoodData().getFoodLevel())) {
                Vec3 look = player.getLookAngle();
                Vec3 direction = VectorUtils.addLocalCoordinates(look, new Vec3(0.0, 0.0, 1.0)).multiply(new Vec3(1.0, 0.0, 1.0)).scale(perLevel(weapon, 0.458F));
                player.addDeltaMovement(direction);
                player.hurtMarked = true;
                player.hasImpulse = true;
                GraceTimeWeaponHolder.of(player).applyPostImpulseGraceTime(10);
                weapon.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                player.causeFoodExhaustion(perLevel(weapon, 3));
                if (!player.isSilent()) {
                    int index = Mth.clamp(getLungeLevel(weapon) - 1, 0, LUNGE_SOUNDS.size() - 1);
                    level.playSound(null, player.position().x, player.position().y, player.position().z, LUNGE_SOUNDS.get(index), player.getSoundSource(), 1.0F, 1.0F);
                }
            }
        }
    }
    
    private static float perLevel(ItemStack stack, float level) {
        return level + level * (getLungeLevel(stack) - 1);
    }
}