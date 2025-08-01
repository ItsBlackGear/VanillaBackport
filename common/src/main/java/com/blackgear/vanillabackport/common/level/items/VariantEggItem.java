package com.blackgear.vanillabackport.common.level.items;

import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.projectile.VariantThrownEgg;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VariantEggItem extends EggItem {
    private final ResourceKey<ChickenVariant> variant;

    public VariantEggItem(ResourceKey<ChickenVariant> variant, Item.Properties properties) {
        super(properties);
        this.variant = variant;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        level.playSound(
            null,
            player.getX(), player.getY(), player.getZ(),
            SoundEvents.EGG_THROW, SoundSource.PLAYERS,
            0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (!level.isClientSide) {
            VariantThrownEgg thrownEgg = new VariantThrownEgg(level, player, stack, this.variant);
            thrownEgg.setItem(stack);
            thrownEgg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownEgg);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}