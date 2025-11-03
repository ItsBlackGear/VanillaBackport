package com.blackgear.vanillabackport.client.api.tabs;

import com.blackgear.platform.client.event.screen.HudRendering;
import com.blackgear.platform.client.event.screen.api.ScreenAccess;
import com.blackgear.vanillabackport.client.registries.ModBundledTabs;
import com.blackgear.vanillabackport.client.registries.ModCreativeTabs;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.access.CreativeModeInventoryScreenAccessor;
import com.google.common.collect.ImmutableList;
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
import net.minecraft.client.renderer.Rect2i;
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
        if (instance == null) {
            instance = new BundledTabSelector();
        }

        return instance;
    }

    private int guiLeft;
    private int guiTop;
    private int scroll;

    private AbstractWidget scrollUpButton;
    private AbstractWidget scrollDownButton;

    private List<BundledTabs> bundles = null;
    // Since we don't use the last tab contents, maybe it is better to just use boolean check? - Echo2craft.
    private CreativeModeTab lastTab;
    // Check if a bundle tab is selected. - Echo2craft.
    private boolean isBundleTabSelected;
    // store amount of all Vanilla Backport items, use for checking current tab - Echo2craft.
    private int modItemsAmount;

    // For JEI compat, in development right now. - Echo2craft.
    private List<Rect2i> extraAreas = Collections.emptyList();
    private static final int BUNDLED_TABS_WIDTH = 30;
    private static final int BUNDLED_TABS_HEIGHT = 120;
    private int bundledTabsLastXPos = Integer.MIN_VALUE;
    private int bundledTabsLastYPos = Integer.MIN_VALUE;

    private BundledTabSelector() {
        HudRendering.POST_INITIALIZE.register(this::init);
        HudRendering.RENDER_BACKGROUND.register(this::renderBackground);
        HudRendering.CLOSE_CONTAINER.register(this::onClose);
    }

    private void init(Minecraft minecraft, Screen screen, ScreenAccess access) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            if (this.bundles == null) {
                List<BundledTabs> bundles = ModBundledTabs.getFilters();
                Collections.reverse(bundles);
                this.bundles = bundles;
            }
            this.guiLeft = creativeScreen.leftPos;
            this.guiTop = creativeScreen.topPos;
            this.injectWidgets(creativeScreen, access::addRenderableWidget);
            modItemsAmount = ModCreativeTabs.VANILLA_BACKPORT.get().getDisplayItems().size();

            // JEI integration - Echo2craft.
            // It's being redefined many times, I'm seeking a better way to check this better. - Echo2craft.
            // extraAreas = ImmutableList.of(new Rect2i(this.guiLeft - 30, this.guiTop + 2, BUNDLED_TABS_WIDTH, BUNDLED_TABS_HEIGHT));

            int curXPos = this.guiLeft - 30;
            int curYPos = this.guiTop + 2;

            // 1. Check if the screen position has changed or if it's the first time
            if (curXPos != this.bundledTabsLastXPos || curYPos != this.bundledTabsLastYPos) {

                // 2. Update the last known position
                this.bundledTabsLastXPos = curXPos;
                this.bundledTabsLastYPos = curYPos;

                // 3. Recalculate extraAreas based on the current position
                extraAreas = ImmutableList.of(
                        new Rect2i(
                                curXPos, // The final X coordinate of the area
                                curYPos, // The final Y coordinate of the area
                                BUNDLED_TABS_WIDTH,
                                BUNDLED_TABS_HEIGHT
                        )
                );
            }
        }
    }

    private void renderBackground(Minecraft minecraft, AbstractContainerScreen<?> screen, GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab tab = CreativeModeInventoryScreenAccessor.getSelectedTab();

            if (this.isValidTab(tab)) {
                graphics.blit(SELECTOR_BAR, bundledTabsLastXPos, bundledTabsLastYPos, 0, 0, BUNDLED_TABS_WIDTH, BUNDLED_TABS_HEIGHT);
                // graphics.renderOutline(bundledTabsLastXPos, bundledTabsLastYPos, BUNDLED_TABS_WIDTH, BUNDLED_TABS_HEIGHT, 0xff000000);

                // Below is the code to handle user clicking Vanilla Backport tab button again to view all items, on the same tab.
                // Checking if there is any bundle tab being selected, deselect it right away to ensure visual consistency, I think.
                // Check if a bundle tab is being selected and the last tab must be this tab - Echo2craft.
                if(isBundleTabSelected && this.lastTab == tab){
                    // Check if displayed items are all Vanilla Backport items. - Echo2craft.
                    if(creativeScreen.getMenu().items.size() == modItemsAmount){
                        // Deselect all bundle tabs as user view all items, not the selected bundle tab items. - Echo2craft.
                        this.bundles.forEach(BundledTabs::deselect);
                        // Avoid triggering the same function.
                        isBundleTabSelected = false;
                    }
                }
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
                    // Simple check - Echo2craft.
                    isBundleTabSelected = false;
                } else {
                    this.bundles.forEach(BundledTabs::deselect);
                    category.select();
                    // - Echo2craft.
                    isBundleTabSelected = true;
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

    // For JEI compat, in development. - Echo2craft.
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

    /*protected void debugExtraAreas(GuiGraphics graphics) {
        for (Rect2i area : getExtraAreas()) {
            graphics.fill(area.getX() + area.getWidth(), area.getY() + area.getHeight(), area.getX(), area.getY(),
                    0xD3D3D3D3);
        }
    }*/

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