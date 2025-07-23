package com.blackgear.vanillabackport.core.mixin.common.items;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.bundle.BundleContents;
import com.blackgear.vanillabackport.common.api.bundle.BundleSelectionTooltip;
import com.blackgear.vanillabackport.core.util.ColorUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
    @Shadow protected abstract void playInsertSound(Entity entity);
    @Shadow protected abstract void playRemoveOneSound(Entity entity);

    @Unique private static final int FULL_BAR_COLOR = ColorUtils.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    @Unique private static final int BAR_COLOR = ColorUtils.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);

    @Inject(
        method = "overrideStackedOnOther",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$onOverrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!BundleContents.onBundleUpdate()) return;

        ItemStack itemInSlot = slot.getItem();
        if (action == ClickAction.PRIMARY && !itemInSlot.isEmpty()) {
            if (BundleContents.tryTransfer(stack, slot, player) > 0) {
                this.playInsertSound(player);
            } else {
                this.playInsertFailSound(player);
            }

            this.broadcastChangesOnContainerMenu(player);
            cir.setReturnValue(true);
        } else if (action == ClickAction.SECONDARY && itemInSlot.isEmpty()) {
            ItemStack removed = BundleContents.removeOne(stack);
            if (removed != null) {
                ItemStack inserted = slot.safeInsert(removed);
                if (inserted.getCount() > 0) {
                    BundleContents.tryInsert(stack, inserted);
                } else {
                    this.playRemoveOneSound(player);
                }
            }

            this.broadcastChangesOnContainerMenu(player);
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "overrideOtherStackedOnMe",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$onOverrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> cir) {
        if (!BundleContents.onBundleUpdate()) return;

        if (action == ClickAction.PRIMARY && other.isEmpty()) {
            BundleContents.toggleSelectedItem(stack, -1);
            cir.setReturnValue(false);
        } else {
            if (action == ClickAction.PRIMARY && !other.isEmpty()) {
                if (slot.allowModification(player) && BundleContents.tryInsert(stack, other) > 0) {
                    this.playInsertSound(player);
                } else {
                    this.playInsertFailSound(player);
                }

                this.broadcastChangesOnContainerMenu(player);
                cir.setReturnValue(true);
            } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
                if (slot.allowModification(player)) {
                    ItemStack removed = BundleContents.removeOne(stack);
                    if (removed != null) {
                        this.playRemoveOneSound(player);
                        slotAccess.set(removed);
                    }
                }

                this.broadcastChangesOnContainerMenu(player);
                cir.setReturnValue(true);
            } else {
                BundleContents.toggleSelectedItem(stack, -1);
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
        method = "dropContents",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void vb$dropContents(ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!BundleContents.onBundleUpdate()) return;

        Optional<ItemStack> taken = removeOneFromBundle(stack);
        if (taken.isPresent()) {
            player.drop(taken.get(), true);
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private static Optional<ItemStack> removeOneFromBundle(ItemStack stack) {
        ItemStack removed = BundleContents.removeOne(stack);
        if (removed != null) {
            return Optional.of(removed);
        } else {
            return Optional.empty();
        }
    }

    @Inject(
        method = "getBarColor",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$onGetBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!BundleContents.onBundleUpdate()) return;

        cir.setReturnValue(BundleContents.getContentWeight(stack) >= BundleContents.MAX_WEIGHT ? FULL_BAR_COLOR : BAR_COLOR);
    }

    @Inject(
        method = "getBarWidth",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$onGetBarWidth(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!BundleContents.onBundleUpdate()) return;

        int weight = BundleContents.getContentWeight(stack);
        cir.setReturnValue(Math.min(1 + ((weight * 12) / BundleContents.MAX_WEIGHT), 13));
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void vb$onAppendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
        if (!BundleContents.onBundleUpdate()) return;

        ci.cancel();
    }

    @Inject(
        method = "getTooltipImage",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$onGetTooltipImage(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (!BundleContents.onBundleUpdate()) return;

        NonNullList<ItemStack> items = NonNullList.create();
        BundleContents.getContents(stack).forEach(items::add);
        cir.setReturnValue(Optional.of(new BundleSelectionTooltip(items, BundleContents.getContentWeight(stack), BundleContents.getSelectedItem(stack))));
    }

    @Unique
    private void playInsertFailSound(Entity entity) {
        entity.playSound(ModSoundEvents.BUNDLE_INSERT_FAIL.get(), 1.0F, 1.0F);
    }

    @Unique
    private void broadcastChangesOnContainerMenu(Player player) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu != null && menu.stillValid(player)) {
            menu.slotsChanged(player.getInventory());
        }
    }
}