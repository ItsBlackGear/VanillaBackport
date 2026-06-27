package com.blackgear.vanillabackport.core.mixin.common.bundle_ui;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleItemHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
    @Shadow protected abstract void playInsertSound(Entity entity);
    @Shadow protected abstract void playRemoveOneSound(Entity entity);
    
    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void vb$onOverrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.overrideStackedOnOther(stack, slot, action, player, this::playInsertSound, this::playRemoveOneSound));
        }
    }
    
    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void vb$onOverrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.overrideOtherStackedOnMe(stack, other, slot, action, player, slotAccess, this::playInsertSound, this::playRemoveOneSound));
        }
    }
    
    @Inject(method = "dropContents", at = @At("HEAD"), cancellable = true)
    private static void vb$dropContents(ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.dropContents(stack, player));
        }
    }
    
    @Inject(method = "getBarColor", at = @At("HEAD"), cancellable = true)
    private void vb$onGetBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.getBarColor(stack));
        }
    }
    
    @Inject(method = "getBarWidth", at = @At("HEAD"), cancellable = true)
    private void vb$onGetBarWidth(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.getBarWidth(stack));
        }
    }
    
    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    private void vb$onGetTooltipImage(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.getTooltipImage(stack));
        }
    }
}