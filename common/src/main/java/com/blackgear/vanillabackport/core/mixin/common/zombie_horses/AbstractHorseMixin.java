package com.blackgear.vanillabackport.core.mixin.common.zombie_horses;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {
    @Shadow protected abstract void eating();
    
    @Inject(method = "handleEating", at = @At("HEAD"), cancellable = true)
    private void vb$handleEating(Player player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.is(Items.RED_MUSHROOM)) return;
        
        AbstractHorse self = (AbstractHorse) (Object) this;
        boolean used = false;
        float heal = 3.0F;
        int temper = 3;
        
        if (self.getHealth() < self.getMaxHealth()) {
            self.heal(heal);
            used = true;
        }
        
        if ((used || !self.isTamed()) && self.getTemper() < self.getMaxTemper()) {
            used = true;
            if (!self.level().isClientSide) {
                self.modifyTemper(temper);
            }
        }
        
        if (used) {
            this.eating();
            self.gameEvent(GameEvent.EAT);
        }
        
        cir.setReturnValue(used);
    }
}