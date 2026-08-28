package com.blackgear.vanillabackport.common.level.items.spear;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ItemUseAnimations;
import com.blackgear.vanillabackport.common.level.items.spear.KineticWeapon.Condition;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SpearItem extends TieredItem implements Vanishable {
    private final KineticWeapon kineticWeapon;
    private final PiercingWeapon piercingWeapon;
    private final AttackRange attackRange;
    private final SwingAnimation swingAnimation;
    private final UseEffects useEffects;
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    
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
        super(tier, properties);
        this.kineticWeapon = new KineticWeapon(
            10,
            (int) (delay * 20.0F),
            Condition.ofAttackerSpeed((int) (dismountTime * 20.0F), dismountThreshold),
            Condition.ofAttackerSpeed((int) (knockbackTime * 20.0F), knockbackThreshold),
            Condition.ofRelativeSpeed((int) (damageTime * 20.0F), damageThreshold),
            0.38F,
            damageMultiplier,
            Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_USE : ModSoundEvents.SPEAR_USE),
            Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_HIT : ModSoundEvents.SPEAR_HIT)
        );
        this.piercingWeapon = new PiercingWeapon(
            true,
            false,
            Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_ATTACK : ModSoundEvents.SPEAR_ATTACK),
            Optional.of(tier == Tiers.WOOD ? ModSoundEvents.SPEAR_WOOD_HIT : ModSoundEvents.SPEAR_HIT)
        );
        this.attackRange = new AttackRange(
            2.0F,
            4.5F,
            2.0F,
            6.5F,
            0.125F,
            0.5F
        );
        this.swingAnimation = new SwingAnimation(SwingAnimationType.STAB, (int) (attackDuration * 20.0F));
        this.useEffects = new UseEffects(true, false, 1.0F);
        this.attackDamage = tier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 1.0F / attackDuration - 4.0, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }
    
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide()) {
            this.kineticWeapon.damageEntities(stack, remainingUseDuration, livingEntity, livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack heldItem = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        this.kineticWeapon.makeSound(player);
        return InteractionResultHolder.consume(heldItem);
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
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    
    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }
    
    public float getAttackDamage() {
        return this.attackDamage;
    }
    
    public KineticWeapon getKineticWeapon() {
        return this.kineticWeapon;
    }
    
    public PiercingWeapon getPiercingWeapon() {
        return this.piercingWeapon;
    }
    
    public AttackRange getAttackRange() {
        return this.attackRange;
    }
    
    public float getMinimumAttackCharge() {
        return 1.0F;
    }
    
    public SwingAnimation getSwingAnimation() {
        return this.swingAnimation;
    }
    
    public UseEffects getUseEffects() {
        return this.useEffects;
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}