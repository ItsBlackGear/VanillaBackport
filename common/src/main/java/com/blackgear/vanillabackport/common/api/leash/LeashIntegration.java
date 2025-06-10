package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.platform.common.integration.MobInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class LeashIntegration implements MobInteraction {
    @Override
    public InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!entity.level().isClientSide() && player.isSecondaryUseActive() && entity instanceof Mob mob && mob.canBeLeashed(player) && entity.isAlive()) {
            if (!mob.isBaby()) {
                List<Leashable> nearbyMobs = this.leashableInArea(mob, l -> l.getLeashHolder() == player);

                if (!nearbyMobs.isEmpty()) {
                    boolean attachedAny = false;

                    for (Leashable target : nearbyMobs) {
                        if (this.canHaveALeashAttachedTo(target, mob)) {
                            target.setLeashedTo(mob, true);
                            attachedAny = true;
                        }
                    }

                    if (attachedAny) {
                        mob.level().gameEvent(GameEvent.ENTITY_INTERACT, mob.position(), GameEvent.Context.of(player));
                        mob.playSound(SoundEvents.LEASH_KNOT_PLACE);
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
                if (!entity.level().isClientSide() && this.canHaveALeashAttachedTo(leashable, player)) {
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

    private boolean canHaveALeashAttachedTo(Leashable source, Entity target) {
        if (source == target) return false;

        return source.leashDistanceTo(target) > source.leashSnapDistance() || source.canBeLeashed(target instanceof Player player ? player : null);
    }

    private List<Leashable> leashableInArea(Entity entity, Predicate<Leashable> filter) {
        return this.leashableInArea(entity.level(), entity.getBoundingBox().getCenter(), filter);
    }

    private List<Leashable> leashableInArea(Level level, Vec3 pos, Predicate<Leashable> filter) {
        AABB area = AABB.ofSize(pos, 32.0, 32.0, 32.0);
        return level.getEntitiesOfClass(Entity.class, area, entity -> entity instanceof Leashable leashable && filter.test(leashable))
            .stream()
            .map(Leashable.class::cast)
            .toList();
    }

    private boolean shearOffAllLeashConnections(Entity entity, Player player) {
        boolean sheared = this.dropAllLeashConnections(entity, player);

        if (sheared && entity.level() instanceof ServerLevel server) {
            server.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.SHEEP_SHEAR,
                player != null ? player.getSoundSource() : entity.getSoundSource());
        }

        return sheared;
    }

    private boolean dropAllLeashConnections(Entity entity, @Nullable Player player) {
        List<Leashable> leashed = this.leashableInArea(entity, mob -> mob.getLeashHolder() == entity);
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