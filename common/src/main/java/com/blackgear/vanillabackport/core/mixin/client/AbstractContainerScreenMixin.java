package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.common.api.bundle.BundleContents;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {
    @Shadow protected int imageWidth;

    @Unique private static final ResourceLocation BUNDLE_OPEN_BACK = VanillaBackport.resource("textures/item/bundle_open_back.png");
    @Unique private static final ResourceLocation BUNDLE_OPEN_FRONT = VanillaBackport.resource("textures/item/bundle_open_front.png");

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    private void vb$renderBundleContents(GuiGraphics graphics, Slot slot, CallbackInfo ci) {
        ItemStack stack = slot.getItem();
        if (stack.is(ModItemTags.BUNDLES)) {
            ItemStack selectedItem = BundleContents.getSelectedItemStack(stack);

            if (!selectedItem.isEmpty()) {
                ResourceLocation backTexture = BUNDLE_OPEN_BACK;
                ResourceLocation frontTexture = BUNDLE_OPEN_FRONT;

                for (DyeColor color : DyeColor.values()) {
                    if (stack.is(BundleContents.getByColor(color))) {
                        backTexture = VanillaBackport.resource("textures/item/" + color.getName() + "_bundle_open_back.png");
                        frontTexture = VanillaBackport.resource("textures/item/" + color.getName() + "_bundle_open_front.png");
                        break;
                    }
                }

                PoseStack pose = graphics.pose();
                int slotX = slot.x;
                int slotY = slot.y;

                pose.pushPose();
                pose.translate(0.0F, 0.0F, 100.0F);

                graphics.blit(backTexture, slotX, slotY, 0, 0, 16, 16, 16, 16);
                graphics.renderItem(selectedItem, slotX, slotY, slot.x + slot.y * this.imageWidth);

                pose.pushPose();
                pose.translate(0.0F, 0.0F, 200.0F);
                graphics.blit(frontTexture, slotX, slotY, 0, 0, 16, 16, 16, 16);
                graphics.renderItemDecorations(this.font, stack, slotX, slotY);
                pose.popPose();

                pose.popPose();

                ci.cancel();
            }
        }
    }
}