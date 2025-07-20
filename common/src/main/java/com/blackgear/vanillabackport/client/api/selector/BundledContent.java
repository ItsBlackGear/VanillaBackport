package com.blackgear.vanillabackport.client.api.selector;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BundledContent {
    public final Component tooltip;
    private final ItemStack icon;
    private final List<ItemStack> displayItems;
    private @Nullable BundledContentSelector.Tab tab;
    private boolean selected = false;

    private BundledContent(Component tooltip, ItemStack icon, List<ItemStack> displayItems) {
        this.tooltip = tooltip;
        this.icon = icon;
        this.displayItems = displayItems;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public boolean contains(ItemStack stack) {
        return this.displayItems.contains(stack);
    }

    public List<ItemStack> getDisplayItems() {
        return this.displayItems;
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

    public void setContentTab(@Nullable BundledContentSelector.Tab tab) {
        this.tab = tab;
    }

    public void setVisible(boolean visible) {
        if (this.tab != null) {
            this.tab.visible = visible;
        }
    }

    public void setY(int y) {
        if (this.tab != null) {
            this.tab.setY(y);
        }
    }

    public static class Builder {
        private Component title;
        private ItemStack icon;
        private final List<ItemStack> displayItems = new ArrayList<>();

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder icon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public Builder displayItems(BiConsumer<HolderLookup.Provider, Output> consumer) {
            Minecraft minecraft = Minecraft.getInstance();
            assert minecraft.player != null;
            HolderLookup.Provider provider = minecraft.player.level().registryAccess();

            Output output = new Output() {
                @Override
                public void accept(ItemLike item) {
                    displayItems.add(new ItemStack(item));
                }

                @Override
                public void accept(ItemStack stack) {
                    displayItems.add(stack);
                }
            };

            consumer.accept(provider, output);
            return this;
        }

        public BundledContent build() {
            if (this.title == null) this.title = Component.empty();
            if (this.icon == null) this.icon = ItemStack.EMPTY;
            return new BundledContent(this.title, this.icon, new ArrayList<>(this.displayItems));
        }
    }

    public interface Output {
        void accept(ItemLike item);

        void accept(ItemStack stack);
    }
}