package com.blackgear.vanillabackport.common.api.interactions.mob;

import com.blackgear.platform.common.integration.MobInteraction;
import com.blackgear.vanillabackport.common.api.leash.Leashable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeashIntegration implements MobInteraction {
    @Override
    public InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        Level level = entity.level();
        if (!level.isClientSide()
            && player.isSecondaryUseActive()
            && entity instanceof Leashable leashable
            && leashable.vb$canBeLeashed(player)
            && entity.isAlive()
            && !(entity instanceof LivingEntity living && living.isBaby())) {
            List<Leashable> mobsToLeash = Leashable.vb$leashableInArea(entity, l -> l.vb$getLeashHolder() == player);

            if (!mobsToLeash.isEmpty()) {
                boolean anyLeashed = false;

                for (Leashable mob : mobsToLeash) {
                    if (mob.vb$canHaveALeashAttachedTo(entity)) {
                        mob.vb$setLeashedTo(entity, true);
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

        // Shear off all leash connections
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(Items.SHEARS) && this.shearOffAllLeashConnections(entity, player)) {
            heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (entity.isAlive() && entity instanceof Leashable leashable) {
            // Drop leash
            if (leashable.vb$getLeashHolder() == player) {
                if (!level.isClientSide()) {
                    leashable.vb$dropLeash(true, !player.isCreative());
                    level.gameEvent(GameEvent.ENTITY_INTERACT, entity.position(), GameEvent.Context.of(player));
                    entity.playSound(SoundEvents.LEASH_KNOT_BREAK);
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            // Attach a new leash
            if (heldItem.is(Items.LEAD) && !(leashable.vb$getLeashHolder() instanceof Player)) {
                if (level.isClientSide()) {
                    return InteractionResult.CONSUME;
                }

                if (leashable.vb$canHaveALeashAttachedTo(player)) {
                    if (leashable.vb$isLeashed()) leashable.vb$dropLeash();
                    leashable.vb$setLeashedTo(player, true);
                    entity.playSound(SoundEvents.LEASH_KNOT_PLACE);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private boolean shearOffAllLeashConnections(Entity entity, Player player) {
        boolean dropped = dropAllLeashConnections(entity, player);
        if (dropped && entity.level() instanceof ServerLevel server) {
            server.playSound(null, entity.blockPosition(), SoundEvents.SHEEP_SHEAR, player != null ? player.getSoundSource() : entity.getSoundSource());
        }

        return dropped;
    }

    public static boolean dropAllLeashConnections(Entity entity, @Nullable Player player) {
        List<Leashable> leashables = Leashable.vb$leashableLeashedTo(entity);
        boolean dropped = !leashables.isEmpty();

        if (entity instanceof Leashable leashable && leashable.vb$isLeashed()) {
            leashable.vb$dropLeash();
            dropped = true;
        }

        for (Leashable leashable : leashables) {
            leashable.vb$dropLeash();
        }

        if (dropped) {
            entity.gameEvent(GameEvent.SHEAR, player);
            return true;
        } else {
            return false;
        }
    }
}