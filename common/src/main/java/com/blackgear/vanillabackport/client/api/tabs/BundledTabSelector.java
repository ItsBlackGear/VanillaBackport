package com.blackgear.vanillabackport.client.api.tabs;

import com.blackgear.platform.client.event.screen.HudRendering;
import com.blackgear.platform.client.event.screen.api.ScreenAccess;
import com.blackgear.vanillabackport.client.registries.ModBundledTabs;
import com.blackgear.vanillabackport.client.registries.ModCreativeTabs;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.access.CreativeModeInventoryScreenAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public class BundledTabSelector {
    public static final BundledTabSelector INSTANCE = new BundledTabSelector();

    private static final ResourceLocation SELECTOR_BAR = VanillaBackport.resource("textures/gui/bundled_tabs/interface.png");
    private static final int VISIBLE_CATEGORIES = 5;

    private int guiLeft;
    private int guiTop;
    private int scroll;

    private AbstractWidget scrollUpButton;
    private AbstractWidget scrollDownButton;

    private List<BundledTabs> bundles;
    private CreativeModeTab lastTab;

    public void bootstrap() {
        List<BundledTabs> bundles = ModBundledTabs.getFilters();
        Collections.reverse(bundles);
        this.bundles = bundles;

        HudRendering.POST_INITIALIZE.register(this::init);
        HudRendering.RENDER_BACKGROUND.register(this::renderBackground);
        HudRendering.CLOSE_CONTAINER.register(this::onClose);
    }

    private void init(Minecraft minecraft, Screen screen, ScreenAccess access) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            this.guiLeft = creativeScreen.leftPos;
            this.guiTop = creativeScreen.topPos;
            this.injectWidgets(creativeScreen, access::addRenderableWidget);
        }
    }

    private void renderBackground(Minecraft minecraft, AbstractContainerScreen<?> screen, GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab tab = CreativeModeInventoryScreenAccessor.getSelectedTab();

            if (this.isValidTab(tab)) {
                graphics.blit(SELECTOR_BAR, this.guiLeft - 30, this.guiTop + 2, 0, 0, 30, 120);
            }

            if (this.lastTab != tab) {
                this.onSwitchCreativeTab(tab, creativeScreen);
                this.lastTab = tab;
            }
        }
    }

    private void onClose(Minecraft minecraft, Screen screen) {
        if (screen instanceof CreativeModeInventoryScreen) {
            this.scrollUpButton = null;
            this.scrollDownButton = null;

            this.bundles.forEach(bundle -> {
                bundle.setContentTab(null);
                bundle.deselect();
            });
        }
    }

    private void injectWidgets(CreativeModeInventoryScreen screen, Consumer<AbstractWidget> widgets) {
        this.bundles.forEach(category -> {
            Tab tab = new Tab(this.guiLeft - 23, this.guiTop + 7, category, button -> {
                if (category.isSelected()) {
                    category.deselect();
                } else {
                    this.bundles.forEach(BundledTabs::deselect);
                    category.select();
                }
                this.updateItems(screen);
            });

            tab.visible = false;
            widgets.accept(tab);
        });

        this.scrollUpButton = new ScrollButton(this.guiLeft - 24, this.guiTop + 6, 32, button -> {
            if (this.scroll > 0) this.scroll--;
            this.updateWidgets();
        });
        this.scrollDownButton = new ScrollButton(this.guiLeft - 24, this.guiTop + 108, 52, button -> {
            if (this.scroll < this.getMaxScroll()) this.scroll++;
            this.updateWidgets();
        });

        widgets.accept(this.scrollUpButton);
        widgets.accept(this.scrollDownButton);

        this.updateWidgets();
        this.onSwitchCreativeTab(CreativeModeInventoryScreenAccessor.getSelectedTab(), screen);
    }

    private int getMaxScroll() {
        return Math.max(0, this.bundles.size() - VISIBLE_CATEGORIES);
    }

    private void updateItems(CreativeModeInventoryScreen screen) {
        Set<ItemStack> seenItems = new HashSet<>();
        LinkedHashSet<ItemStack> displayItems = new LinkedHashSet<>();

        boolean hasSelected = this.bundles.stream().anyMatch(BundledTabs::isSelected);

        ModCreativeTabs.VANILLA_BACKPORT.get().getDisplayItems().forEach(stack -> {
            if (!hasSelected) {
                if (!seenItems.contains(stack)) {
                    displayItems.add(stack.copy());
                    seenItems.add(stack);
                }
            } else {
                this.bundles.stream()
                    .filter(BundledTabs::isSelected)
                    .forEach(bundle -> {
                        if (!seenItems.contains(stack) && bundle.contains(stack)) {
                            displayItems.add(stack.copy());
                            seenItems.add(stack);
                        }
                    });
            }
        });

        NonNullList<ItemStack> items = screen.getMenu().items;
        items.clear();
        items.addAll(displayItems);
        screen.getMenu().scrollTo(0);
    }

    private void updateWidgets() {
        this.bundles.forEach(bundle -> bundle.setVisible(false));

        for (int i = this.scroll; i < this.scroll + VISIBLE_CATEGORIES && i < this.bundles.size(); i++) {
            BundledTabs bundle = this.bundles.get(i);
            bundle.setY(this.guiTop + 18 * (i - this.scroll) + 18);
            bundle.setVisible(true);
        }

        boolean isValidTab = this.isValidTab(CreativeModeInventoryScreenAccessor.getSelectedTab());

        this.scrollUpButton.visible = isValidTab && this.scroll > 0;
        this.scrollDownButton.visible = isValidTab && this.scroll < this.getMaxScroll();
    }

    private void onSwitchCreativeTab(CreativeModeTab tab, CreativeModeInventoryScreen screen) {
        if (this.isValidTab(tab)) {
            this.updateWidgets();
            this.updateItems(screen);
        } else {
            this.scrollUpButton.visible = false;
            this.scrollDownButton.visible = false;
            this.bundles.forEach(bundle -> bundle.setVisible(false));
        }
    }

    private boolean isValidTab(CreativeModeTab tab) {
        return tab == ModCreativeTabs.VANILLA_BACKPORT.get();
    }

    public static class Tab extends Button {
        private final BundledTabs bundle;

        protected Tab(int x, int y, BundledTabs bundle, OnPress onPress) {
            super(x, y, 16, 16, Component.empty(), onPress, DEFAULT_NARRATION);
            this.bundle = bundle;
            bundle.setContentTab(this);
            this.setTooltip(Tooltip.create(bundle.tooltip));
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            this.renderSelected(graphics);
            graphics.renderItem(this.bundle.getIcon(), this.getX(), this.getY());
            this.renderHighlight(graphics);
        }

        private void renderSelected(GuiGraphics graphics) {
            if (this.bundle.isSelected()) {
                graphics.blit(SELECTOR_BAR, this.getX() - 7, this.getY() - 1, 36, 24, 30, 19);
            }
        }

        private void renderHighlight(GuiGraphics graphics) {
            if (this.isHovered() && !this.bundle.isSelected()) {
                graphics.pose().pushPose();
                graphics.pose().translate(0.0, 0.0, 200.0);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                graphics.blit(SELECTOR_BAR, this.getX(), this.getY(), 32, 44, 16, 16);
                RenderSystem.disableBlend();
                graphics.pose().popPose();
            }
        }

        @Override
        protected ClientTooltipPositioner createTooltipPositioner() {
            return DefaultTooltipPositioner.INSTANCE;
        }
    }

    public static class ScrollButton extends Button {
        private final int uOffset;

        public ScrollButton(int x, int y, int uOffset, OnPress onPress) {
            super(x, y, 18, 20, Component.empty(), onPress, DEFAULT_NARRATION);
            this.uOffset = uOffset;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int textureY = this.isHovered ? 12 : 0;
            graphics.blit(SELECTOR_BAR, this.getX(), this.getY(), this.uOffset, textureY, 18, 11);
        }
    }
}