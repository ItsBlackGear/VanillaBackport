package com.blackgear.vanillabackport.client.api.bundled_tabs;

import com.blackgear.platform.client.event.screen.HudInteractions;
import com.blackgear.platform.client.event.screen.HudRendering;
import com.blackgear.platform.client.event.screen.api.ScreenAccess;
import com.blackgear.platform.core.util.event.CancellableResult;
import com.blackgear.vanillabackport.client.registries.ModBundledTabs;
import com.blackgear.vanillabackport.client.registries.ModCreativeTabs;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.client.access.CreativeModeInventoryScreenAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public class BundledTabSelector {
    private static final ResourceLocation SELECTOR_BAR = VanillaBackport.resource("textures/gui/bundled_tabs/interface.png");
    private static final int VISIBLE_CATEGORIES = 5;

    private static BundledTabSelector instance;

    public static BundledTabSelector bootstrap() {
        if (instance == null) instance = new BundledTabSelector();
        return instance;
    }

    private int guiLeft;
    private int guiTop;
    private int scroll;

    private AbstractWidget scrollUpButton;
    private AbstractWidget scrollDownButton;

    private List<BundledTabs> bundles = null;
    private CreativeModeTab lastTab;
    private int itemCount;

    private BundledTabSelector() {
        HudRendering.POST_INITIALIZE.register(this::init);
        HudRendering.RENDER_BACKGROUND.register(this::renderBackground);
        HudRendering.CLOSE_CONTAINER.register(this::onClose);
        HudInteractions.SCROLLING_POST.register(this::onScroll);
    }

    private CancellableResult onScroll(Minecraft client, Screen screen, double mouseX, double mouseY, double scrollX, double scrollY) {
        CreativeModeTab tab = CreativeModeInventoryScreenAccessor.getSelectedTab();

        if (this.isValidTab(tab)) {
            if (mouseX >= this.guiLeft - 30 && mouseY >= this.guiTop + 2 && mouseX <= this.guiLeft && mouseY <= this.guiTop + 122) {
                if (!(scrollY < 0)) {
                    if (this.scroll > 0) this.scroll--;
                } else {
                    if (this.scroll < this.getMaxScroll()) this.scroll++;
                }
            }
            this.updateWidgets();
        }

        return CancellableResult.PASS;
    }

    private void init(Minecraft minecraft, Screen screen, ScreenAccess access) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            if (this.bundles == null) this.bundles = new ArrayList<>(ModBundledTabs.getTabs());
            this.guiLeft = creativeScreen.leftPos;
            this.guiTop = creativeScreen.topPos;
            this.injectWidgets(creativeScreen, access::addRenderableWidget);
            this.itemCount = ModCreativeTabs.VANILLA_BACKPORT.get().getDisplayItems().size();
        }
    }

    private void renderBackground(Minecraft minecraft, AbstractContainerScreen<?> screen, GuiGraphics graphics, int mouseX, int mouseY, DeltaTracker timer) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab tab = CreativeModeInventoryScreenAccessor.getSelectedTab();
            graphics.pose().pushPose();
            graphics.pose().translate(0.0, 0.0, 0.0);

            if (this.isValidTab(tab)) {
                graphics.blit(SELECTOR_BAR, this.guiLeft - 30, this.guiTop + 2, 0, 0, 30, 120);
                if (this.hasSelectedBundle() && creativeScreen.getMenu().items.size() == this.itemCount) {
                    this.bundles.forEach(BundledTabs::deselect);
                }
            }

            if (this.lastTab != tab) {
                this.onSwitchCreativeTab(tab, creativeScreen);
                this.lastTab = tab;
            }

            graphics.pose().popPose();
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

    private boolean hasSelectedBundle() {
        return this.bundles != null && this.bundles.stream().anyMatch(BundledTabs::isSelected);
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

        this.scrollUpButton = new ScrollButton(this.guiLeft - 24, this.guiTop + 6, 32, b -> {
            if (this.scroll > 0) this.scroll--;
            this.updateWidgets();
        });
        this.scrollDownButton = new ScrollButton(this.guiLeft - 24, this.guiTop + 108, 52, b -> {
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
        Set<ItemStack> seen = new HashSet<>();
        LinkedHashSet<ItemStack> display = new LinkedHashSet<>();
        boolean hasSelection = this.hasSelectedBundle();

        ModCreativeTabs.VANILLA_BACKPORT.get().getDisplayItems().forEach(stack -> {
            if (!hasSelection) {
                if (seen.add(stack)) display.add(stack.copy());
            } else {
                this.bundles.stream()
                    .filter(BundledTabs::isSelected)
                    .filter(bundle -> bundle.contains(stack))
                    .findFirst()
                    .ifPresent(bundle -> {
                        if (seen.add(stack)) display.add(stack.copy());
                    });
            }
        });

        NonNullList<ItemStack> items = screen.getMenu().items;
        items.clear();
        items.addAll(display);
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
            this.setTooltip(Tooltip.create(bundle.getTooltip()));
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.pose().pushPose();
            graphics.pose().translate(0.0, 0.0, 20.0);
            this.renderSelected(graphics);
            graphics.renderItem(this.bundle.getIcon(), this.getX(), this.getY());
            graphics.pose().popPose();
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
                graphics.pose().translate(0.0, 0.0, 20.0);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                graphics.blit(SELECTOR_BAR, this.getX(), this.getY(), 32, 44, 16, 16);
                RenderSystem.disableBlend();
                graphics.pose().popPose();
            }
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
            graphics.pose().pushPose();
            graphics.pose().translate(0.0, 0.0, 20.0);
            graphics.blit(SELECTOR_BAR, this.getX(), this.getY(), this.uOffset, textureY, 18, 11);
            graphics.pose().popPose();
        }
    }
}