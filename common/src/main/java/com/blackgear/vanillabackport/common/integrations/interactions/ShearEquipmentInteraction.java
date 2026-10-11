package com.blackgear.vanillabackport.common.integrations.interactions;

import com.blackgear.platform.common.integration.MobInteraction;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.items.WolfArmorItem;
import com.blackgear.vanillabackport.common.registries.ModCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ShearEquipmentInteraction implements MobInteraction {
    @Override
    public InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (entity instanceof Wolf target
            && heldItem.is(Items.SHEARS)
            && !player.isSecondaryUseActive()
            && target.isOwnedBy(player)
            && tryShear(
            target,
            player,
            hand,
            heldItem,
            target.getItemBySlot(EquipmentSlot.CHEST),
            stack -> stack.getItem() instanceof WolfArmorItem,
            ModSoundEvents.ARMOR_UNEQUIP_WOLF.get(),
            (mob, stack) -> {
                mob.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                mob.setGuaranteedDrop(EquipmentSlot.CHEST);
            }
        )) {
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        
        return InteractionResult.PASS;
    }
    
    public static boolean tryShear(
        Mob target,
        Player player,
        InteractionHand hand,
        ItemStack heldItem,
        ItemStack equipment,
        Predicate<ItemStack> isEquippable,
        SoundEvent shearingSound,
        BiConsumer<Mob, ItemStack> unequipper
    ) {
        if (equipment.isEmpty()
            || !isEquippable.test(equipment)
            || (EnchantmentHelper.hasBindingCurse(equipment) && !player.isCreative())) {
            return false;
        }
        
        heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        unequipper.accept(target, equipment);
        target.setPersistenceRequired();
        target.gameEvent(GameEvent.SHEAR, player);
        target.playSound(shearingSound);
        
        if (player instanceof ServerPlayer serverPlayer) {
            target.spawnAtLocation(equipment, target.getBbHeight() + 0.5F);
            ModCriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger(serverPlayer, equipment, target);
        }
        
        return true;
    }
}