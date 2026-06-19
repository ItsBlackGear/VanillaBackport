package com.blackgear.vanillabackport.common.api.integration.interactions;

import com.blackgear.platform.common.integration.MobInteraction;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WolfArmorInteraction implements MobInteraction {
    @Override
    public InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (entity instanceof Wolf wolf) {
            if (!wolf.level().isClientSide) {
                if (wolf.isTame() && wolf.isOwnedBy(player)) {
                    ItemStack armor = wolf.getItemBySlot(EquipmentSlot.CHEST);

                    if (stack.is(ModItems.WOLF_ARMOR.get())
                        && armor.isEmpty()
                        && !wolf.isBaby()
                    ) {
                        wolf.setItemSlot(EquipmentSlot.CHEST, stack.copyWithCount(1));
                        wolf.playSound(ModSoundEvents.ARMOR_EQUIP_WOLF.get());
                        if (!player.getAbilities().instabuild) stack.shrink(1);
                        player.swing(hand);
                        return InteractionResult.SUCCESS;
                    }

                    if (wolf.isInSittingPose()
                        && armor.is(ModItems.WOLF_ARMOR.get())
                        && armor.isDamaged()
                        && stack.is(ModItems.ARMADILLO_SCUTE.get())
                    ) {
                        if (!player.getAbilities().instabuild) stack.shrink(1);
                        wolf.playSound(ModSoundEvents.WOLF_ARMOR_REPAIR.get());
                        int repairUnit = (int) (armor.getMaxDamage() * 0.125F);
                        armor.setDamageValue(Math.max(0, armor.getDamageValue() - repairUnit));
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }
}
