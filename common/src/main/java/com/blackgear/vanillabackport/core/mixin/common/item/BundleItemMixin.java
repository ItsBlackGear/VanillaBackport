package com.blackgear.vanillabackport.core.mixin.common.item;

import com.blackgear.vanillabackport.common.api.bundle.BundleFeatures;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
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

    @Unique private static final int FULL_BAR_COLOR = FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    @Unique private static final int BAR_COLOR = FastColor.ARGB32.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void vb$overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!BundleFeatures.onBundleUpdate()) return;

        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) {
            cir.setReturnValue(false);
        } else {
            ItemStack itemInSlot = slot.getItem();
            BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
            if (action == ClickAction.PRIMARY && !itemInSlot.isEmpty()) {
                if (mutable.tryTransfer(slot, player) > 0) {
                    this.playInsertSound(player);
                } else {
                    BundleFeatures.playInsertFailSound(player);
                }

                stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable()); // ✅ FIXED!
                BundleFeatures.broadcastChangesOnContainerMenu(player);
                cir.setReturnValue(true);
            } else if (action == ClickAction.SECONDARY && itemInSlot.isEmpty()) {
                ItemStack removed = mutable.removeOne();
                if (removed != null) {
                    ItemStack insert = slot.safeInsert(removed);
                    if (insert.getCount() > 0) {
                        mutable.tryInsert(insert);
                    } else {
                        this.playRemoveOneSound(player);
                    }
                }

                stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
                BundleFeatures.broadcastChangesOnContainerMenu(player);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void vb$overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        if (!BundleFeatures.onBundleUpdate()) return;

        if (action == ClickAction.PRIMARY && other.isEmpty()) {
            BundleFeatures.toggleSelectedItem(stack, -1);
            cir.setReturnValue(false);
        } else {
            BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (contents == null) {
                cir.setReturnValue(false);
            } else {
                BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
                if (action == ClickAction.PRIMARY && !other.isEmpty()) {
                    if (slot.allowModification(player) && mutable.tryInsert(other) > 0) {
                        this.playInsertSound(player);
                    } else {
                        BundleFeatures.playInsertFailSound(player);
                    }

                    stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
                    BundleFeatures.broadcastChangesOnContainerMenu(player);
                    cir.setReturnValue(true);
                } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
                    if (slot.allowModification(player)) {
                        ItemStack removed = mutable.removeOne();
                        if (removed != null) {
                            this.playRemoveOneSound(player);
                            access.set(removed);
                        }
                    }

                    stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
                    BundleFeatures.broadcastChangesOnContainerMenu(player);
                    cir.setReturnValue(true);
                } else {
                    BundleFeatures.toggleSelectedItem(stack, -1);
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "getBarColor", at = @At("HEAD"), cancellable = true)
    private void vb$getBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!BundleFeatures.onBundleUpdate()) return;

        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        cir.setReturnValue(contents.weight().compareTo(Fraction.ONE) >= 0 ? FULL_BAR_COLOR : BAR_COLOR);
    }

    @Inject(method = "dropContents", at = @At("HEAD"), cancellable = true)
    private static void vb$dropContents(ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!BundleFeatures.onBundleUpdate()) return;

        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents != null && !contents.isEmpty()) {
            Optional<ItemStack> optional = BundleFeatures.removeOneItemFromBundle(stack, player, contents);
            if (optional.isPresent()) {
                player.drop(optional.get(), true);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        } else {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void vb$appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (BundleFeatures.onBundleUpdate()) ci.cancel();
    }
}