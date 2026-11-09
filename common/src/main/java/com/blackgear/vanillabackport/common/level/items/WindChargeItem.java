package com.blackgear.vanillabackport.common.level.items;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.WindCharge;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WindChargeItem extends Item {
    public WindChargeItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            WindCharge charge = new WindCharge(player, level, player.position().x, player.getEyePosition().y, player.position().z);
            charge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(charge);
        }
        
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.WIND_CHARGE_THROW.get(), SoundSource.BLOCKS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        ItemStack itemStack = player.getItemInHand(usedHand);
        player.getCooldowns().addCooldown(this, 10);
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) itemStack.shrink(1);
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}