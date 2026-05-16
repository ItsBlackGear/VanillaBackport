package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.platform.common.integration.MobInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class LeashIntegration implements MobInteraction {
    @Override
    public InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        Level level = entity.level();

        if (!level.isClientSide()
            && player.isSecondaryUseActive()
            && entity instanceof Leashable leashable
            && leashable.canBeLeashed()
            && entity.isAlive()
            && !(entity instanceof LivingEntity living && living.isBaby())) {

            List<Leashable> mobsToLeash = LeashPhysics.leashableInArea(entity, l -> l.getLeashHolder() == player);
            if (!mobsToLeash.isEmpty()) {
                boolean anyLeashed = false;

                for (Leashable target : mobsToLeash) {
                    if (LeashPhysics.canAttachLeash(target, entity)) {
                        target.setLeashedTo(entity, true);
                        anyLeashed = true;
                    }
                }

                if (anyLeashed) {
                    level.gameEvent(GameEvent.ENTITY_INTERACT, entity.blockPosition(), GameEvent.Context.of(player));
                    entity.playSound(SoundEvents.LEASH_KNOT_PLACE);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(Items.SHEARS) && shearAllConnections(entity, player)) {
            heldItem.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            return InteractionResult.SUCCESS;
        }

        if (entity.isAlive() && entity instanceof Leashable leashable) {
            if (leashable.getLeashHolder() == player) {
                if (!level.isClientSide()) {
                    leashable.dropLeash(true, !player.isCreative());
                    entity.gameEvent(GameEvent.ENTITY_INTERACT, player);
                    entity.playSound(SoundEvents.LEASH_KNOT_BREAK);
                }

                return InteractionResult.SUCCESS;
            }

            if (heldItem.is(Items.LEAD) && !(leashable.getLeashHolder() instanceof Player)) {
                if (!level.isClientSide() && LeashPhysics.canAttachLeash(leashable, player)) {
                    if (leashable.isLeashed()) {
                        leashable.dropLeash(true, true);
                    }

                    leashable.setLeashedTo(player, true);
                    entity.playSound(SoundEvents.LEASH_KNOT_PLACE);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private static boolean shearAllConnections(Entity entity, Player player) {
        boolean dropped = LeashPhysics.dropAllLeashConnections(entity, player);
        if (dropped && entity.level() instanceof ServerLevel server) {
            server.playSound(null, entity.blockPosition(), SoundEvents.SHEEP_SHEAR, player != null ? player.getSoundSource() : entity.getSoundSource(), 1.0F, 1.0F);
        }

        return dropped;
    }
}