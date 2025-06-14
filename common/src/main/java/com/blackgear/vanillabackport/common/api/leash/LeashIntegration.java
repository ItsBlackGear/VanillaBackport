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
        if (!entity.level().isClientSide() && player.isSecondaryUseActive() && entity instanceof Leashable leashable && leashable.canBeLeashed(player) && entity.isAlive()) {
            if (!(entity instanceof LivingEntity living && living.isBaby())) {
                List<Leashable> nearbyMobs = Leashable.leashableInArea(entity, l -> l.getLeashHolder() == player);

                if (!nearbyMobs.isEmpty()) {
                    boolean attachedAny = false;

                    for (Leashable target : nearbyMobs) {
                        if (target.canHaveALeashAttachedTo(entity)) {
                            target.setLeashedTo(entity, true);
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
            if (leashable.getLeashHolder() == player) {
                if (!entity.level().isClientSide()) {
                    leashable.dropLeash(true, !player.isCreative());
                    entity.level().gameEvent(GameEvent.ENTITY_INTERACT, entity.position(), GameEvent.Context.of(player));
                    entity.playSound(SoundEvents.LEASH_KNOT_BREAK);
                }

                return InteractionResult.SUCCESS;
            }

            // Attach a new leash
            if (stack.is(Items.LEAD) && !(leashable.getLeashHolder() instanceof Player)) {
                if (!entity.level().isClientSide() && leashable.canHaveALeashAttachedTo(player)) {
                    if (leashable.isLeashed()) {
                        leashable.dropLeash(true, true);
                    }

                    leashable.setLeashedTo(player, true);
                    entity.playSound(SoundEvents.LEASH_KNOT_PLACE);

                    if (!player.isCreative()) stack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean shearOffAllLeashConnections(Entity entity, Player player) {
        boolean sheared = this.dropAllLeashConnections(entity, player);
        if (sheared && entity.level() instanceof ServerLevel server) {
            server.playSound(null, entity.blockPosition(), SoundEvents.SHEEP_SHEAR, player != null ? player.getSoundSource() : entity.getSoundSource());
        }

        return sheared;
    }

    private boolean dropAllLeashConnections(Entity entity, @Nullable Player player) {
        List<Leashable> leashed = Leashable.leashableLeashedTo(entity);
        boolean anyDroppedConnections = !leashed.isEmpty();
        if (entity instanceof Leashable leashable && leashable.isLeashed()) {
            leashable.dropLeash(true, true);
            anyDroppedConnections = true;
        }

        for (Leashable leashable : leashed) {
            leashable.dropLeash(true, true);
        }

        if (anyDroppedConnections) {
            entity.gameEvent(GameEvent.SHEAR, player);
            return true;
        }

        return false;
    }
}