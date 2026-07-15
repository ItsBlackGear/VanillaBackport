package com.blackgear.vanillabackport.common.level.item.spear;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ItemUseAnimations;
import com.blackgear.vanillabackport.common.level.components.*;
import com.blackgear.vanillabackport.common.level.components.KineticWeapon.Condition;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SpearItem extends TieredItem {
    public SpearItem(
        Tier tier,
        float attackDuration,
        float damageMultiplier,
        float delay,
        float dismountTime,
        float dismountThreshold,
        float knockbackTime,
        float knockbackThreshold,
        float damageTime,
        float damageThreshold,
        Properties properties
    ) {
        super(
            tier,
            properties.durability(tier.getUses())
                .component(ModDataComponents.KINETIC_WEAPON.get(),
                    new KineticWeapon(
                        10,
                        (int) (delay * 20.0F),
                        Condition.ofAttackerSpeed((int) (dismountTime * 20.0F), dismountThreshold),
                        Condition.ofAttackerSpeed((int) (knockbackTime * 20.0F), knockbackThreshold),
                        Condition.ofRelativeSpeed((int) (damageTime * 20.0F), damageThreshold),
                        0.38F,
                        damageMultiplier,
                        Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_USE : ModSoundEvents.SPEAR_USE),
                        Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_HIT : ModSoundEvents.SPEAR_HIT)
                    ))
                .component(ModDataComponents.PIERCING_WEAPON.get(),
                    new PiercingWeapon(
                        true,
                        false,
                        Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_ATTACK : ModSoundEvents.SPEAR_ATTACK),
                        Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_HIT : ModSoundEvents.SPEAR_HIT)
                    ))
                .component(ModDataComponents.ATTACK_RANGE.get(),
                    new AttackRange(
                        2.0F,
                        4.5F,
                        2.0F,
                        6.5F,
                        0.125F,
                        0.5F
                    ))
                .component(ModDataComponents.MINIMUM_ATTACK_CHARGE.get(), 1.0F)
                .component(ModDataComponents.SWING_ANIMATION.get(),
                    new SwingAnimation(SwingAnimationType.STAB, (int) (attackDuration * 20.0F)))
                .component(ModDataComponents.USE_EFFECTS.get(),
                    new UseEffects(true, false, 1.0F))
                .attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 0.0F + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 1.0F / attackDuration - 4.0, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                    .build()
                )
        );
    }
    
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        KineticWeapon kineticWeapon = stack.get(ModDataComponents.KINETIC_WEAPON.get());
        if (kineticWeapon != null && !level.isClientSide()) {
            kineticWeapon.damageEntities(stack, remainingUseDuration, livingEntity, livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack heldItem = player.getItemInHand(usedHand);
        KineticWeapon kineticWeapon = heldItem.get(ModDataComponents.KINETIC_WEAPON.get());
        if (kineticWeapon != null) {
            player.startUsingItem(usedHand);
            kineticWeapon.makeSound(player);
            return InteractionResultHolder.consume(heldItem);
        }
        
        return InteractionResultHolder.pass(heldItem);
    }
    
    @Override
    public int getEnchantmentValue() {
        return this.getTier().getEnchantmentValue();
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return ItemUseAnimations.REAL_SPEAR.get();
    }
    
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
    
    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }
    
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.isCreative())
            stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
    
    public static SwingAnimation getSwingAnimation(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SWING_ANIMATION.get(), SwingAnimation.DEFAULT);
    }
}