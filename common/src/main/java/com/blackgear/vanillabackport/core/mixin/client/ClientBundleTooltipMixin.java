package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.common.api.bundle.BundleContents;
import com.blackgear.vanillabackport.common.api.bundle.BundleSelectionTooltip;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientBundleTooltip.class)
public class ClientBundleTooltipMixin implements ClientTooltipComponent {
    @Unique private static final ResourceLocation BUNDLE_PROGRESS_BAR_TEXTURE = VanillaBackport.resource("textures/container/bundle/bundle_progressbar.png");
    @Unique private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE = VanillaBackport.resource("textures/container/bundle/slot_highlight_back.png");
    @Unique private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE = VanillaBackport.resource("textures/container/bundle/slot_highlight_front.png");
    @Unique private static final ResourceLocation BUNDLE_SLOT_BACKGROUND_TEXTURE = VanillaBackport.resource("textures/container/bundle/slot_background.png");
    @Unique private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.minecraft.bundle.empty.description");
    @Unique private static final Component BUNDLE_FULL = Component.translatable("item.minecraft.bundle.full");
    @Unique private static final Component BUNDLE_EMPTY = Component.translatable("item.minecraft.bundle.empty");

    @Shadow @Final private NonNullList<ItemStack> items;
    @Shadow @Final private int weight;
    @Unique private int selectedItem;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(BundleTooltip tooltip, CallbackInfo ci) {
        this.selectedItem = tooltip instanceof BundleSelectionTooltip selection ? selection.getSelectedItem() : -1;
    }

    @Unique
    private int getSelectedItem() {
        return this.selectedItem;
    }

    @Unique
    public boolean hasSelectedItem() {
        return this.selectedItem != -1;
    }

    @Override
    public int getHeight() {
        return this.items.isEmpty() ? 39 : this.backgroundHeight();
    }

    @Override
    public int getWidth(Font font) {
        return 96;
    }

    @Unique
    private int backgroundHeight() {
        return this.itemGridHeight() + 13 + 8;
    }

    @Unique
    private int itemGridHeight() {
        return this.gridSizeY() * 24;
    }

    @Unique
    private int getContentXOffset(int width) {
        return (width - 96) / 2;
    }

    @Unique
    private int gridSizeY() {
        return Mth.positiveCeilDiv(this.slotCount(), 4);
    }

    @Unique
    private int slotCount() {
        return Math.min(12, this.items.size());
    }

    @Inject(method = "renderImage", at = @At("HEAD"), cancellable = true)
    private void vb$onRenderImage(Font font, int x, int y, GuiGraphics graphics, CallbackInfo ci) {
        if (this.items.isEmpty()) {
            this.renderEmptyBundleTooltip(font, x, y, this.getWidth(font), graphics);
        } else {
            this.renderBundleWithItemsTooltip(font, x, y, this.getWidth(font), graphics);
        }

        ci.cancel();
    }

    @Unique
    private void renderEmptyBundleTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        this.drawEmptyBundleDescriptionText(x + this.getContentXOffset(width), y, font, graphics);
        this.drawProgressBar(x + this.getContentXOffset(width), y + this.getEmptyBundleDescriptionTextHeight(font) + 4, font, graphics);
    }

    @Unique
    private void renderBundleWithItemsTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        boolean maxDisplay = this.items.size() > 12;
        List<ItemStack> stacks = this.getShownItems(BundleContents.getItemsToShow(this.items));
        int xOffset = x + this.getContentXOffset(width) + 96;
        int yOffset = y + this.gridSizeY() * 24;
        int index = 1;

        for (int row = 1; row <= this.gridSizeY(); row++) {
            for (int column = 1; column <= 4; column++) {
                int slotX = xOffset - column * 24;
                int slotY = yOffset - row * 24;
                if (this.shouldRenderSurplusText(maxDisplay, column, row)) {
                    this.renderCount(slotX, slotY, this.getAmountOfHiddenItems(stacks), font, graphics);
                } else if (this.shouldRenderItemSlot(stacks, index)) {
                    this.renderSlot(index, slotX, slotY, stacks, index, font, graphics);
                    index++;
                }
            }
        }

        this.drawSelectedItemTooltip(font, graphics, x, y, width);
        this.drawProgressBar(x + this.getContentXOffset(width), y + this.itemGridHeight() + 4, font, graphics);
    }

    @Unique
    private List<ItemStack> getShownItems(int max) {
        int size = Math.min(this.items.size(), max);
        return this.items.stream().toList().subList(0, size);
    }

    @Unique
    private boolean shouldRenderSurplusText(boolean maxDisplay, int column, int row) {
        return maxDisplay && column * row == 1;
    }

    @Unique
    private boolean shouldRenderItemSlot(List<ItemStack> items, int itemIndex) {
        return items.size() >= itemIndex;
    }

    @Unique
    private int getAmountOfHiddenItems(List<ItemStack> items) {
        return this.items.stream().skip(items.size()).mapToInt(ItemStack::getCount).sum();
    }

    @Unique
    private void renderSlot(int index, int x, int y, List<ItemStack> stacks, int seed, Font font, GuiGraphics graphics) {
        int itemIndex = stacks.size() - index;
        boolean hasSelectedItem = itemIndex == this.getSelectedItem();
        ItemStack stack = stacks.get(itemIndex);

        if (hasSelectedItem) {
            // Add transparency to the highlight back texture
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            graphics.blit(BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE, x, y, 0, 0, 0, 24, 24, 24, 24);
            RenderSystem.disableBlend();
        } else {
            graphics.blit(BUNDLE_SLOT_BACKGROUND_TEXTURE, x, y, 0, 0, 0, 24, 24, 24, 24);
        }

        graphics.renderItem(stack, x + 4, y + 4, seed);
        graphics.renderItemDecorations(font, stack, x + 4, y + 4);

        if (hasSelectedItem) {
            // Add transparency to the highlight front texture
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            graphics.blit(BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE, x, y, 0, 0, 0, 24, 24, 24, 24);
            RenderSystem.disableBlend();
        }
    }

    @Unique
    private void renderCount(int x, int y, int value, Font font, GuiGraphics graphics) {
        graphics.drawCenteredString(font, "+" + value, x + 12, y + 10, -1);
    }

    @Unique
    private void drawSelectedItemTooltip(Font font, GuiGraphics graphics, int x, int y, int width) {
        if (this.hasSelectedItem()) {
            ItemStack stack = this.items.get(this.getSelectedItem());
            MutableComponent component = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().color);
            if (stack.hasCustomHoverName()) {
                component.withStyle(ChatFormatting.ITALIC);
            }

            int textWidth = font.width(component.getVisualOrderText());
            int xOffset = x + width / 2 - 12;
            graphics.renderTooltip(font, component, xOffset - textWidth / 2, y - 15);
        }
    }

    @Unique
    private void drawProgressBar(int x, int y, Font textRenderer, GuiGraphics graphics) {
        int progress = this.getProgressBarFill();
        boolean isFull = this.weight >= 64;
        if (progress > 0) {
            // Render progress bar fill
            graphics.blitNineSliced(
                BUNDLE_PROGRESS_BAR_TEXTURE,
                x + 1, y, progress > 1 ? progress : 2, 13,
                2,
                6, 6,
                isFull ? 6 : 0, 12
            );
        }

        // Render progress bar border
        graphics.blitNineSliced(
            BUNDLE_PROGRESS_BAR_TEXTURE,
            x, y,
            96, 13,
            2,
            12, 12,
            0, 0
        );

        Component fillText = this.getProgressBarFillText();
        if (fillText != null)
            graphics.drawCenteredString(textRenderer, fillText, x + 48, y + 3, -1);
    }

    @Unique
    private void drawEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphics graphics) {
        graphics.drawWordWrap(font, BUNDLE_EMPTY_DESCRIPTION, x, y, 96, -5592406);
    }

    @Unique
    private int getEmptyBundleDescriptionTextHeight(Font font) {
        return font.split(BUNDLE_EMPTY_DESCRIPTION, 96).size() * 9;
    }

    @Unique
    private int getProgressBarFill() {
        return Mth.clamp((this.weight * 94) / 64, 0, 94);
    }

    @Unique @Nullable
    private Component getProgressBarFillText() {
        if (this.items.isEmpty()) {
            return BUNDLE_EMPTY;
        } else {
            return this.weight >= 64 ? BUNDLE_FULL : null;
        }
    }
}