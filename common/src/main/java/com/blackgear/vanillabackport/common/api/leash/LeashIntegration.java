package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.platform.common.integration.MobInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeashIntegration implements MobInteraction {
    @Override
    public InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!entity.level().isClientSide() && player.isSecondaryUseActive() && entity instanceof Leashable leashable && leashable.vb$canBeLeashed(player) && entity.isAlive()) {
            if (!(entity instanceof LivingEntity living && living.isBaby())) {
                List<Leashable> nearbyMobs = Leashable.vb$leashableInArea(entity, l -> l.vb$getLeashHolder() == player);

                if (!nearbyMobs.isEmpty()) {
                    boolean attachedAny = false;

                    for (Leashable target : nearbyMobs) {
                        if (target.vb$canHaveALeashAttachedTo(entity)) {
                            target.vb$setLeashedTo(entity, true);
                            attachedAny = true;
                        }
                    }

                    if (attachedAny) {
                        entity.level().gameEvent(GameEvent.ENTITY_INTERACT, entity.blockPosition(), GameEvent.Context.of(player));
                        entity.playSound(SoundEvents.LEASH_KNOT_PLACE);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        // Shear off all leash connections
        if (stack.is(Items.SHEARS) && this.shearOffAllLeashConnections(entity, player)) {
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            return InteractionResult.SUCCESS;
        }

        if (entity.isAlive() && entity instanceof Leashable leashable) {
            // Drop leash
            if (leashable.vb$getLeashHolder() == player) {
                if (!entity.level().isClientSide()) {
                    leashable.vb$dropLeash(true, !player.isCreative());
                    entity.level().gameEvent(GameEvent.ENTITY_INTERACT, entity.position(), GameEvent.Context.of(player));
                    entity.playSound(SoundEvents.LEASH_KNOT_BREAK);
                }

                return InteractionResult.SUCCESS;
            }

            // Attach a new leash
            if (stack.is(Items.LEAD) && !(leashable.vb$getLeashHolder() instanceof Player)) {
                if (!entity.level().isClientSide() && leashable.vb$canHaveALeashAttachedTo(player)) {
                    if (leashable.vb$isLeashed()) {
                        leashable.vb$dropLeash(true, true);
                    }

                    leashable.vb$setLeashedTo(player, true);
                    entity.playSound(SoundEvents.LEASH_KNOT_PLACE);

                    if (!player.isCreative()) stack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean shearOffAllLeashConnections(Entity entity, Player player) {
        boolean sheared = dropAllLeashConnections(entity, player);
        if (sheared && entity.level() instanceof ServerLevel server) {
            server.playSound(null, entity.blockPosition(), SoundEvents.SHEEP_SHEAR, player != null ? player.getSoundSource() : entity.getSoundSource());
        }

        return sheared;
    }

    public static boolean dropAllLeashConnections(Entity entity, @Nullable Player player) {
        List<Leashable> leashed = Leashable.vb$leashableLeashedTo(entity);
        boolean dropConnections = !leashed.isEmpty();

        if (entity instanceof Leashable leashable && leashable.vb$isLeashed()) {
            leashable.vb$dropLeash(true, true);
            dropConnections = true;
        }

        for (Leashable leashable : leashed) {
            leashable.vb$dropLeash(true, true);
        }

        if (dropConnections) {
            entity.gameEvent(GameEvent.SHEAR, player);
            return true;
        } else {
            return false;
        }
    }
}