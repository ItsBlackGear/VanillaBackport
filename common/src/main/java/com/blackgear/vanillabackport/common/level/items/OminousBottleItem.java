package com.blackgear.vanillabackport.common.level.items;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OminousBottleItem extends Item {
    public static final int EFFECT_DURATION = 120000;
    public static final int MIN_AMPLIFIER = 0;
    public static final int MAX_AMPLIFIER = 4;
    public static final String OMINOUS_BOTTLE_AMPLIFIER_TAG_NAME = "ominous_bottle_amplifier";
    public OminousBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 32;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity.hasEffect(MobEffects.BAD_OMEN) && getAmplifier(pStack) < MAX_AMPLIFIER) {
            MobEffectInstance effect = pLivingEntity.getEffect(MobEffects.BAD_OMEN);
            if(effect != null){
                pLivingEntity.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, EFFECT_DURATION, effect.getAmplifier() + 1, false, false, true));
                // pLivingEntity.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 12000, pLivingEntity.getEffect(MobEffects.BAD_OMEN).getAmplifier() + 1));
            }
            else {
                pLivingEntity.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, EFFECT_DURATION, getAmplifier(pStack) + 1, false, false, true));
            }
        } else {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, EFFECT_DURATION, getAmplifier(pStack), false, false, true));
            // pLivingEntity.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 12000, 0));
        }
        pLivingEntity.playSound(ModSoundEvents.OMINOUS_BOTTLE_DISPOSE.get());
        if (pLivingEntity instanceof Player player) {
            if (!player.isCreative()) {
                pStack.shrink(1);
            }
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        PotionUtils.addPotionTooltip(List.of(new MobEffectInstance(MobEffects.BAD_OMEN, EFFECT_DURATION, getAmplifier(pStack), false, false, true)), pTooltipComponents, 1.0f);
    }

    public static int getAmplifier(ItemStack pOminousBottle) {
        if(pOminousBottle.hasTag() && pOminousBottle.getOrCreateTag().contains(OMINOUS_BOTTLE_AMPLIFIER_TAG_NAME)){
            return pOminousBottle.getOrCreateTag().getInt(OMINOUS_BOTTLE_AMPLIFIER_TAG_NAME);
        }
        return MIN_AMPLIFIER;

        // Component code, but the code was unfinished so ignore that option.
        // return OminousBottleAmplifierComponent.DEFAULT.load(pOminousBottle.getOrCreateTag()).value();

        // Tag name mapping system, currently it is not needed and it is also not polished enough to be used.
        // With TTOminousBottleConstant.OMINOUS_BOTTLE_AMPLIFIER_NEW_TAG_NAME = "ominous_bottle_amplifier"
        // and TTOminousBottleConstant.OMINOUS_BOTTLE_AMPLIFIER_OLD_TAG_NAME = "OminousBottleAmplifier"
        // (I made that old tag name up based on old tag naming system pre 1.21.)
        // (After 1.21, all tag names are written in lowercase with underscores as word separators.)

        /*if(TTTagHelper.hasTag(vTag, TTOminousBottleConstant.OMINOUS_BOTTLE_AMPLIFIER_NEW_TAG_NAME, TTOminousBottleConstant.OMINOUS_BOTTLE_AMPLIFIER_OLD_TAG_NAME)) {
            return TTTagHelper.getInt(vTag, TTOminousBottleConstant.OMINOUS_BOTTLE_AMPLIFIER_NEW_TAG_NAME, TTOminousBottleConstant.OMINOUS_BOTTLE_AMPLIFIER_OLD_TAG_NAME,0);
        }*/
    }
}
