package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.common.api.bundle.BundleFeatures;
import com.blackgear.vanillabackport.common.api.bundle.IBundle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
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
public abstract class ClientBundleTooltipMixin implements ClientTooltipComponent {
    @Shadow @Final private BundleContents contents;

    @Unique private static final ResourceLocation PROGRESSBAR_BORDER_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_border");
    @Unique private static final ResourceLocation PROGRESSBAR_FILL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
    @Unique private static final ResourceLocation PROGRESSBAR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_full");
    @Unique private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE = ResourceLocation.withDefaultNamespace("textures/container/bundle/slot_highlight_back.png");
    @Unique private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE = ResourceLocation.withDefaultNamespace("textures/container/bundle/slot_highlight_front.png");
    @Unique private static final ResourceLocation BUNDLE_SLOT_BACKGROUND_TEXTURE = ResourceLocation.withDefaultNamespace("textures/container/bundle/slot_background.png");
    @Unique private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.minecraft.bundle.empty.description");
    @Unique private static final Component BUNDLE_FULL = Component.translatable("item.minecraft.bundle.full");
    @Unique private static final Component BUNDLE_EMPTY = Component.translatable("item.minecraft.bundle.empty");

    @Unique private int selectedItem;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$onInit(BundleContents contents, CallbackInfo ci) {
        this.selectedItem = ((IBundle)(Object)contents).getSelectedItem();
    }

    @Unique
    private int getSelectedItem() {
        return this.selectedItem;
    }

    @Unique
    private boolean hasSelectedItem() {
        return this.selectedItem != -1;
    }

    @Override
    public int getHeight() {
        return this.contents.isEmpty() ? 39 : this.backgroundHeight();
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
        return Math.min(12, this.contents.size());
    }

    @Inject(method = "renderImage", at = @At("HEAD"), cancellable = true)
    private void vb$onRenderImage(Font font, int x, int y, GuiGraphics graphics, CallbackInfo ci) {
        if (BundleFeatures.onBundleUpdate()) {
            ci.cancel();

            if (this.contents.isEmpty()) {
                this.renderEmptyBundleTooltip(font, x, y, this.getWidth(font), graphics);
            } else {
                this.renderBundleWithItemsTooltip(font, x, y, this.getWidth(font), graphics);
            }
        }
    }

    @Unique
    private void renderEmptyBundleTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        this.drawEmptyBundleDescriptionText(x + this.getContentXOffset(width), y, font, graphics);
        this.drawProgressBar(x + this.getContentXOffset(width), y + this.getEmptyBundleDescriptionTextHeight(font) + 4, font, graphics);
    }

    @Unique
    private void renderBundleWithItemsTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        boolean maxDisplay = this.contents.size() > 12;
        List<ItemStack> stacks = this.getShownItems(((IBundle)(Object)this.contents).getNumberOfItemsToShow());
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
        int size = Math.min(this.contents.size(), max);
        return this.contents.itemCopyStream().toList().subList(0, size);
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
        return this.contents.itemCopyStream().skip(items.size()).mapToInt(ItemStack::getCount).sum();
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
            ItemStack stack = this.contents.getItemUnsafe(this.getSelectedItem());
            MutableComponent component = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().color());
            if (stack.has(DataComponents.CUSTOM_NAME)) component.withStyle(ChatFormatting.ITALIC);

            int textWidth = font.width(component.getVisualOrderText());
            int xOffset = x + width / 2 - 12;
            graphics.renderTooltip(font, component, xOffset - textWidth / 2, y - 15);
        }
    }

    @Unique
    private void drawProgressBar(int x, int y, Font textRenderer, GuiGraphics graphics) {
        graphics.blitSprite(this.getProgressBarTexture(), x + 1, y, this.getProgressBarFill(), 13);
        graphics.blitSprite(PROGRESSBAR_BORDER_SPRITE, x, y, 96, 13);
        Component component = this.getProgressBarFillText();
        if (component != null) graphics.drawCenteredString(textRenderer, component, x + 48, y + 3, -1);
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
        return Mth.clamp(Mth.mulAndTruncate(this.contents.weight(), 94), 0, 94);
    }

    @Unique
    private ResourceLocation getProgressBarTexture() {
        return this.contents.weight().compareTo(Fraction.ONE) >= 0 ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE;
    }

    @Unique @Nullable
    private Component getProgressBarFillText() {
        if (this.contents.isEmpty()) {
            return BUNDLE_EMPTY;
        } else {
            return this.contents.weight().compareTo(Fraction.ONE) >= 0 ? BUNDLE_FULL : null;
        }
    }
}