package com.blackgear.vanillabackport.common.api.interactions.mob;

import com.blackgear.platform.common.integration.MobInteraction;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.items.WolfArmorItem;
import com.blackgear.vanillabackport.common.registries.ModCriteriaTriggers;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ShearEquipmentInteraction implements MobInteraction {
    @Override
    public InteractionResult onInteract(Player player, Entity entity, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (entity instanceof Mob target
            && heldItem.is(Items.SHEARS)
            && !player.isSecondaryUseActive()
            && this.canShearEquipment(target, player)
            && this.attemptToShearEquipment(player, hand, heldItem, target)) {
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }

        return InteractionResult.PASS;
    }

    private boolean attemptToShearEquipment(Player player, InteractionHand hand, ItemStack heldItem, Mob target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack equipment = target.getItemBySlot(slot);
            Optional<Equippables> equippable = Arrays.stream(Equippables.values()).filter(equippables -> equippables.isEquippable().test(equipment)).findAny();
            if (equippable.isPresent()
                && this.canShearEquipment(target, player)
                && (!EnchantmentHelper.hasBindingCurse(equipment) || player.isCreative())
            ) {
                heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                target.setItemSlot(slot, ItemStack.EMPTY);
                target.setGuaranteedDrop(slot);
                target.setPersistenceRequired();
                target.gameEvent(GameEvent.SHEAR, player);
                target.playSound(equippable.get().getShearingSound().get());

                if (player instanceof ServerPlayer serverPlayer) {
                    target.spawnAtLocation(equipment, target.getBbHeight() + 0.5F);
                    ModCriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger(serverPlayer, equipment, target);
                }

                return true;
            }
        }

        return false;
    }

    private boolean canShearEquipment(Mob mob, Player player) {
        if (mob instanceof TamableAnimal tamable) {
            return tamable.isOwnedBy(player);
        }

        return !mob.isVehicle();
    }

    public enum Equippables {
        WOLF_ARMOR(stack -> stack.getItem() instanceof WolfArmorItem, ModSoundEvents.ARMOR_UNEQUIP_WOLF),
        GHAST_HARNESS(stack -> stack.is(ModItemTags.HARNESSES), ModSoundEvents.HARNESS_UNEQUIP);

        private final Predicate<ItemStack> isEquippable;
        private final Supplier<SoundEvent> shearingSound;

        Equippables(Predicate<ItemStack> isEquippable, Supplier<SoundEvent> shearingSound) {
            this.isEquippable = isEquippable;
            this.shearingSound = shearingSound;
        }

        public Predicate<ItemStack> isEquippable() {
            return this.isEquippable;
        }

        public Supplier<SoundEvent> getShearingSound() {
            return this.shearingSound;
        }
    }
}