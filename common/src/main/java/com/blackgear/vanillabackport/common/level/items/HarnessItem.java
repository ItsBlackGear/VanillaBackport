package com.blackgear.vanillabackport.common.level.items;

import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

public class HarnessItem extends Item {
    public HarnessItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (target instanceof HappyGhast ghast && target.isAlive()) {
            if (!ghast.isHarnessed() && ghast.canBeHarnessed()) {
                if (!player.level().isClientSide) {
                    ghast.equipHarness();
                    ghast.setItemSlot(EquipmentSlot.CHEST, new ItemStack(this));
                    target.level().gameEvent(target, GameEvent.EQUIP, target.position());
                    stack.shrink(1);
                }

                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}