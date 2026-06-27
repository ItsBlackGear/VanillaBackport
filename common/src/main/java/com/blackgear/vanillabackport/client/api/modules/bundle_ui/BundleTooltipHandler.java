package com.blackgear.vanillabackport.client.api.modules.bundle_ui;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleSelectionTooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BundleTooltipHandler {
    private static final ResourceLocation BUNDLE_PROGRESS_BAR_TEXTURE = new ResourceLocation("textures/container/bundle/bundle_progressbar.png");
    private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE = new ResourceLocation("textures/container/bundle/slot_highlight_back.png");
    private static final ResourceLocation BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE = new ResourceLocation("textures/container/bundle/slot_highlight_front.png");
    private static final ResourceLocation BUNDLE_SLOT_BACKGROUND_TEXTURE = new ResourceLocation("textures/container/bundle/slot_background.png");
    
    private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.minecraft.bundle.empty.description");
    private static final Component BUNDLE_FULL = Component.translatable("item.minecraft.bundle.full");
    private static final Component BUNDLE_EMPTY = Component.translatable("item.minecraft.bundle.empty");
    
    private final NonNullList<ItemStack> items;
    private final int weight;
    private int selectedItem;
    
    public BundleTooltipHandler(NonNullList<ItemStack> items, int weight) {
        this.items = items;
        this.weight = weight;
    }
    
    public void init(BundleTooltip tooltip) {
        this.selectedItem = tooltip instanceof BundleSelectionTooltip selection ? selection.getSelectedItem() : -1;
    }
    
    public int getSelectedItem() {
        return this.selectedItem;
    }
    
    public boolean hasSelectedItem() {
        return this.selectedItem != -1;
    }
    
    public int getWidth(Font font) {
        return 96;
    }
    
    public int getHeight() {
        return this.items.isEmpty() ? 39 : this.backgroundHeight();
    }
    
    private int backgroundHeight() {
        return this.itemGridHeight() + 13 + 8;
    }
    
    private int itemGridHeight() {
        return this.gridSizeY() * 24;
    }
    
    private int getContentXOffset(int width) {
        return (width - 96) / 2;
    }
    
    private int gridSizeY() {
        return Mth.positiveCeilDiv(this.slotCount(), 4);
    }
    
    public int slotCount() {
        return Math.min(12, this.items.size());
    }
    
    public boolean renderImage(Font font, int x, int y, GuiGraphics graphics) {
        if (!BundleFeatures.onBundleUpdate()) return false;
        
        if (this.items.isEmpty()) {
            this.renderEmptyBundleTooltip(font, x, y, this.getWidth(font), graphics);
        } else {
            this.renderBundleWithItemsTooltip(font, x, y, this.getWidth(font), graphics);
        }
        
        return true;
    }
    
    private void renderEmptyBundleTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        this.drawEmptyBundleDescriptionText(x + this.getContentXOffset(width), y, font, graphics);
        this.drawProgressBar(x + this.getContentXOffset(width), y + this.getEmptyBundleDescriptionTextHeight(font) + 4, font, graphics);
    }
    
    private void renderBundleWithItemsTooltip(Font font, int x, int y, int width, GuiGraphics graphics) {
        boolean maxDisplay = this.items.size() > 12;
        List<ItemStack> stacks = this.getShownItems(BundleFeatures.getItemsToShow(this.items));
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
    
    private List<ItemStack> getShownItems(int max) {
        int size = Math.min(this.items.size(), max);
        return this.items.subList(0, size);
    }
    
    private boolean shouldRenderSurplusText(boolean maxDisplay, int column, int row) {
        return maxDisplay && column * row == 1;
    }
    
    private boolean shouldRenderItemSlot(List<ItemStack> items, int itemIndex) {
        return items.size() >= itemIndex;
    }
    
    private int getAmountOfHiddenItems(List<ItemStack> items) {
        return this.items.stream().skip(items.size()).mapToInt(ItemStack::getCount).sum();
    }
    
    private void renderSlot(int index, int x, int y, List<ItemStack> stacks, int seed, Font font, GuiGraphics graphics) {
        int itemIndex = stacks.size() - index;
        boolean hasSelectedItem = itemIndex == this.getSelectedItem();
        ItemStack stack = stacks.get(itemIndex);
        
        if (hasSelectedItem) {
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

        ItemStack stack = this.items.get(this.getSelectedItem());
        MutableComponent component = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().color);
        if (stack.hasCustomHoverName()) {
            component.withStyle(ChatFormatting.ITALIC);
        }
        
        int textWidth = font.width(component.getVisualOrderText());
        int xOffset = x + width / 2 - 12;
        graphics.renderTooltip(font, component, xOffset - textWidth / 2, y - 15);
    }
    
    private void drawProgressBar(int x, int y, Font textRenderer, GuiGraphics graphics) {
        int progress = this.getProgressBarFill();
        boolean isFull = this.weight >= 64;
        if (progress > 0) {
            graphics.blitNineSliced(BUNDLE_PROGRESS_BAR_TEXTURE, x + 1, y, progress > 1 ? progress : 2, 13, 2, 6, 6, isFull ? 6 : 0, 12);
        }
        
        graphics.blitNineSliced(BUNDLE_PROGRESS_BAR_TEXTURE, x, y, 96, 13, 2, 12, 12, 0, 0);
        
        Component fillText = this.getProgressBarFillText();
        if (fillText != null) {
            graphics.drawCenteredString(textRenderer, fillText, x + 48, y + 3, -1);
        }
    }
    
    private void drawEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphics graphics) {
        graphics.drawWordWrap(font, BUNDLE_EMPTY_DESCRIPTION, x, y, 96, -5592406);
    }
    
    private int getEmptyBundleDescriptionTextHeight(Font font) {
        return font.split(BUNDLE_EMPTY_DESCRIPTION, 96).size() * 9;
    }
    
    private int getProgressBarFill() {
        return Mth.clamp((this.weight * 94) / 64, 0, 94);
    }
    
    @Nullable
    private Component getProgressBarFillText() {
        if (this.items.isEmpty()) return BUNDLE_EMPTY;
        return this.weight >= 64 ? BUNDLE_FULL : null;
    }
}