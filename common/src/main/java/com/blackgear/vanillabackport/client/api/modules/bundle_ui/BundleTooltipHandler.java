package com.blackgear.vanillabackport.client.api.modules.bundle_ui;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.common.api.modules.bundle_ui.ModernBundle;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;

public class BundleTooltipHandler {
    private static final ResourceLocation PROGRESSBAR_BORDER_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_border");
    private static final ResourceLocation PROGRESSBAR_FILL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
    private static final ResourceLocation PROGRESSBAR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_full");
    private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE = ResourceLocation.withDefaultNamespace("textures/container/bundle/slot_highlight_back.png");
    private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE = ResourceLocation.withDefaultNamespace("textures/container/bundle/slot_highlight_front.png");
    private static final ResourceLocation BUNDLE_SLOT_BACKGROUND_TEXTURE = ResourceLocation.withDefaultNamespace("textures/container/bundle/slot_background.png");
    
    private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.minecraft.bundle.empty.description");
    private static final Component BUNDLE_FULL = Component.translatable("item.minecraft.bundle.full");
    private static final Component BUNDLE_EMPTY = Component.translatable("item.minecraft.bundle.empty");
    
    private final BundleContents contents;
    private int selectedItem;
    
    public BundleTooltipHandler(BundleContents contents) {
        this.contents = contents;
    }
    
    public void init(BundleContents contents) {
        this.selectedItem = ((ModernBundle) (Object) contents).getSelectedItem();
    }
    
    private int getSelectedItem() {
        return this.selectedItem;
    }
    
    private boolean hasSelectedItem() {
        return this.selectedItem != -1;
    }
    
    public int getHeight() {
        return this.contents.isEmpty() ? 39 : this.backgroundHeight();
    }
    
    public int getWidth() {
        return gridSizeX() * 24;
    }
    
    private int backgroundHeight() {
        return this.itemGridHeight() + 13 + 8;
    }
    
    private int itemGridHeight() {
        return this.gridSizeY() * 24;
    }
    
    private int getContentXOffset(int width) {
        return (width - getWidth()) / 2;
    }

    private int gridSizeX() {
        return VanillaBackport.CLIENT_CONFIG.endlessBundleUi.get()
            ? Math.max(4, Mth.ceil(Math.sqrt(this.contents.size())))
            : 4;
    }

    private int gridSizeY() {
        return Mth.positiveCeilDiv(this.slotCount(), gridSizeX());
    }
    
    private int slotCount() {
        return VanillaBackport.CLIENT_CONFIG.endlessBundleUi.get()
            ? this.contents.size()
            : Math.min(12, this.contents.size());
    }
    
    public boolean renderImage(Font font, int x, int y, GuiGraphics graphics) {
        if (!BundleFeatures.onBundleUpdate()) return false;
        
        if (this.contents.isEmpty()) {
            this.renderEmptyBundleTooltip(font, x, y, this.getWidth(), graphics);
        } else {
            this.renderBundleWithItemsTooltip(font, x, y, this.getWidth(), graphics);
        }
        
        return true;
    }
    
    private void renderEmptyBundleTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        this.drawEmptyBundleDescriptionText(x + this.getContentXOffset(width), y, font, graphics);
        this.drawProgressBar(x + this.getContentXOffset(width), y + this.getEmptyBundleDescriptionTextHeight(font) + 4, font, graphics);
    }
    
    private void renderBundleWithItemsTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        boolean maxDisplay = this.contents.size() > 12;
        List<ItemStack> stacks = this.getShownItems(((ModernBundle) (Object) this.contents).getNumberOfItemsToShow());
        int xOffset = x + this.getContentXOffset(width) + getWidth();
        int yOffset = y + this.gridSizeY() * 24;
        int index = 1;
        
        for (int row = 1; row <= this.gridSizeY(); row++) {
            for (int column = 1; column <= gridSizeX(); column++) {
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
    
    private List<ItemStack> getShownItems(int max) {
        int size = Math.min(this.contents.size(), max);
        return this.contents.itemCopyStream().limit(size).toList();
    }
    
    private boolean shouldRenderSurplusText(boolean maxDisplay, int column, int row) {
        return !VanillaBackport.CLIENT_CONFIG.endlessBundleUi.get() && (maxDisplay && column * row == 1);
    }
    
    private boolean shouldRenderItemSlot(List<ItemStack> items, int itemIndex) {
        return items.size() >= itemIndex;
    }
    
    private int getAmountOfHiddenItems(List<ItemStack> items) {
        return this.contents.itemCopyStream().skip(items.size()).mapToInt(ItemStack::getCount).sum();
    }
    
    private void renderSlot(int index, int x, int y, List<ItemStack> stacks, int seed, Font font, GuiGraphics graphics) {
        int itemIndex = stacks.size() - index;
        boolean isSelected = itemIndex == this.getSelectedItem();
        ItemStack stack = stacks.get(itemIndex);
        
        if (isSelected) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            graphics.blit(BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE, x, y, 0, 0, 0, 24, 24, 24, 24);
            RenderSystem.disableBlend();
        } else {
            graphics.blit(BUNDLE_SLOT_BACKGROUND_TEXTURE, x, y, 0, 0, 0, 24, 24, 24, 24);
        }
        
        graphics.renderItem(stack, x + 4, y + 4, seed);
        graphics.renderItemDecorations(font, stack, x + 4, y + 4);
        
        if (isSelected) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            graphics.blit(BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE, x, y, 0, 0, 0, 24, 24, 24, 24);
            RenderSystem.disableBlend();
        }
    }
    
    private void renderCount(int x, int y, int value, Font font, GuiGraphics graphics) {
        graphics.drawCenteredString(font, "+" + value, x + 12, y + 10, -1);
    }
    
    private void drawSelectedItemTooltip(Font font, GuiGraphics graphics, int x, int y, int width) {
        if (!this.hasSelectedItem()) return;
        
        ItemStack stack = this.contents.getItemUnsafe(this.getSelectedItem());
        MutableComponent component = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().color());
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            component.withStyle(ChatFormatting.ITALIC);
        }
        
        int textWidth = font.width(component.getVisualOrderText());
        int xOffset = x + (width / 2) - 12;
        graphics.renderTooltip(font, component, xOffset - (textWidth / 2), y - 15);
    }
    
    private void drawProgressBar(int x, int y, Font textRenderer, GuiGraphics graphics) {
        graphics.blitSprite(this.getProgressBarTexture(), x + 1, y, this.getProgressBarFill(), 13);
        graphics.blitSprite(PROGRESSBAR_BORDER_SPRITE, x, y, getWidth(), 13);
        
        Component component = this.getProgressBarFillText();
        if (component != null) {
            graphics.drawCenteredString(textRenderer, component, x + (getWidth() / 2), y + 3, -1);
        }
    }
    
    private void drawEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphics graphics) {
        graphics.drawWordWrap(font, BUNDLE_EMPTY_DESCRIPTION, x, y, getWidth(), -5592406);
    }
    
    private int getEmptyBundleDescriptionTextHeight(Font font) {
        return font.split(BUNDLE_EMPTY_DESCRIPTION, getWidth()).size() * 9;
    }
    
    private int getProgressBarFill() {
        return Mth.clamp(Mth.mulAndTruncate(this.contents.weight(), getWidth() - 2), 0, getWidth() - 2);
    }
    
    private ResourceLocation getProgressBarTexture() {
        return this.contents.weight().compareTo(Fraction.ONE) >= 0 ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE;
    }
    
    private Component getProgressBarFillText() {
        if (this.contents.isEmpty()) return BUNDLE_EMPTY;
        return this.contents.weight().compareTo(Fraction.ONE) >= 0 ? BUNDLE_FULL : null;
    }
}