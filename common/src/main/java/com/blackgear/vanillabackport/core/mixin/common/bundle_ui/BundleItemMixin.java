package com.blackgear.vanillabackport.core.mixin.common.bundle_ui;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleItemHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
    @Shadow protected abstract void playInsertSound(Entity entity);
    @Shadow protected abstract void playRemoveOneSound(Entity entity);

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void vb$overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.overrideStackedOnOther(stack, slot, action, player, this::playInsertSound, this::playRemoveOneSound));
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void vb$overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.overrideOtherStackedOnMe(stack, other, slot, action, player, access, this::playInsertSound, this::playRemoveOneSound));
        }
    }
    
    @Inject(method = "dropContents", at = @At("HEAD"), cancellable = true)
    private static void vb$dropContents(ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.dropContents(stack, player));
        }
    }

    @Inject(method = "getBarColor", at = @At("HEAD"), cancellable = true)
    private void vb$getBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            cir.setReturnValue(BundleItemHandler.getBarColor(stack));
        }
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void vb$appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (BundleFeatures.onBundleUpdate()) ci.cancel();
    }
}