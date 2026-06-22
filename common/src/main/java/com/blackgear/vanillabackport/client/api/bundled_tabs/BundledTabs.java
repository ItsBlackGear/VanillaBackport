package com.blackgear.vanillabackport.client.api.bundled_tabs;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class BundledTabs {
    private final Component tooltip;
    private final ItemStack icon;
    private final List<ItemStack> displayItems;
    private final @Nullable BiConsumer<HolderLookup.Provider, Output> populationLogic;
    private boolean populated;
    private @Nullable BundledTabSelector.Tab tab;
    private boolean selected;

    private BundledTabs(
        Component tooltip,
        ItemStack icon,
        List<ItemStack> staticItems,
        @Nullable BiConsumer<HolderLookup.Provider, Output> populationLogic
    ) {
        this.tooltip = tooltip;
        this.icon = icon;
        this.displayItems = staticItems;
        this.populationLogic = populationLogic;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Component getTooltip() {
        return this.tooltip;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public List<ItemStack> getDisplayItems() {
        return Collections.unmodifiableList(this.displayItems);
    }

    public boolean contains(ItemStack stack) {
        return this.displayItems.contains(stack);
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setContentTab(@Nullable BundledTabSelector.Tab tab) {
        this.tab = tab;
    }

    public void setVisible(boolean visible) {
        if (this.tab != null) this.tab.visible = visible;
    }

    public void setY(int y) {
        if (this.tab != null) this.tab.setY(y);
    }

    public void populate(HolderLookup.Provider provider) {
        if (this.populated || this.populationLogic == null) return;
        this.populationLogic.accept(provider, new Output() {
            @Override
            public void accept(ItemLike item) {
                displayItems.add(new ItemStack(item));
            }
            @Override
            public void accept(ItemStack stack) {
                displayItems.add(stack);
            }
        });
        this.populated = true;
    }

    public static class Builder {
        private Component title;
        private ItemStack icon;
        private BiConsumer<HolderLookup.Provider, Output> populationLogic;

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder icon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public Builder displayItems(BiConsumer<HolderLookup.Provider, Output> logic) {
            this.populationLogic = logic;
            return this;
        }

        public BundledTabs build() {
            if (this.title == null) this.title = Component.empty();
            if (this.icon == null) this.icon = ItemStack.EMPTY;
            return new BundledTabs(this.title, this.icon, new ArrayList<>(), this.populationLogic);
        }
    }

    public interface Output {
        void accept(ItemLike item);
        void accept(ItemStack stack);
    }
}