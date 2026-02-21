package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.items.AnimalArmorItem;
import com.blackgear.vanillabackport.core.util.MobUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin({Animal.class})
public abstract class AnimalMixin {
    @Unique
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    @Unique
    private static final UUID ARMOR_TOUGHNESS_MODIFIER_UUID = UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC");
    @Unique
    private static final UUID KNOCKBACK_RESISTANCE_MODIFIER_UUID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");

    public AnimalMixin() {
    }

    @Inject(
            method = {"mobInteract"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Animal entity = (Animal)(Object)this;
        ItemStack stack = player.getItemInHand(hand);

        if (MobUtils.canWearArmor(entity)) {
            if (this.isEquippable(stack) && entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && !entity.isBaby()) {
                this.setArmorEquipment(stack.copyWithCount(1));
                Item var7 = stack.getItem();
                if (var7 instanceof AnimalArmorItem) {
                    AnimalArmorItem animalArmorItem = (AnimalArmorItem)var7;
                    entity.playSound(animalArmorItem.getMaterial().getEquipSound());
                }

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
            } else if (this.shearable() && (stack.getItem() instanceof ShearsItem && entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AnimalArmorItem && (!EnchantmentHelper.hasBindingCurse(entity.getItemBySlot(EquipmentSlot.CHEST)) || player.isCreative()))) {
                stack.hurtAndBreak(1, player, (a) -> {
                    a.broadcastBreakEvent(hand);
                });
                entity.playSound(ModSoundEvents.ARMOR_UNEQUIP_WOLF.get());
                ItemStack armor = entity.getItemBySlot(EquipmentSlot.CHEST);
                this.setArmorEquipment(ItemStack.EMPTY);
                entity.spawnAtLocation(armor);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Unique
    private boolean isEquippable(ItemStack stack) {
        Animal entity = (Animal)(Object)this;
        Item var4 = stack.getItem();
        boolean var10000;
        if (var4 instanceof AnimalArmorItem animalArmorItem) {
            if (animalArmorItem.getAllowedEntities().contains(entity.getType().builtInRegistryHolder())) {
                var10000 = true;
                return var10000;
            }
        }

        var10000 = false;
        return var10000;
    }

    @Unique
    private boolean shearable() {
        Animal var2 = (Animal)(Object)this;
        boolean var10000;
        if (var2 instanceof Shearable shearable) {
            if (shearable.readyForShearing()) {
                var10000 = false;
                return var10000;
            }
        }

        var10000 = true;
        return var10000;
    }

    @Unique
    private boolean tamable(Player player) {
        Animal var3 = (Animal)(Object)this;
        boolean var10000;
        if (var3 instanceof TamableAnimal tamable) {
            if (!tamable.isTame() || !tamable.isOwnedBy(player)) {
                var10000 = false;
                return var10000;
            }
        }

        var10000 = true;
        return var10000;
    }

    @Unique
    private void setArmorEquipment(ItemStack stack) {
        Animal entity = (Animal)(Object)this;
        if (!entity.level().isClientSide()) {
            entity.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            entity.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(ARMOR_TOUGHNESS_MODIFIER_UUID);
            entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(KNOCKBACK_RESISTANCE_MODIFIER_UUID);
            entity.setDropChance(EquipmentSlot.CHEST, 0.0F);
            Item var4 = stack.getItem();
            if (var4 instanceof AnimalArmorItem) {
                AnimalArmorItem animalArmorItem = (AnimalArmorItem)var4;
                entity.setItemSlot(EquipmentSlot.CHEST, stack);
                if (animalArmorItem.getMaterial().getDefense() != 0) {
                    entity.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Animal armor bonus", (double)animalArmorItem.getMaterial().getDefense(), AttributeModifier.Operation.ADDITION));
                }

                if (animalArmorItem.getMaterial().getToughness() != 0.0F) {
                    entity.getAttribute(Attributes.ARMOR_TOUGHNESS).addPermanentModifier(new AttributeModifier(ARMOR_TOUGHNESS_MODIFIER_UUID, "Animal toughness", (double)animalArmorItem.getMaterial().getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
                }

                if (animalArmorItem.getMaterial().getKnockbackResistance() != 0.0F) {
                    entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier(KNOCKBACK_RESISTANCE_MODIFIER_UUID, "Animal knockback resistance", (double)animalArmorItem.getMaterial().getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
                }

                entity.setGuaranteedDrop(EquipmentSlot.CHEST);
            }
        }
    }
}
