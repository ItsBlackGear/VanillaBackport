package com.blackgear.vanillabackport.common.level.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class WolfArmorItem extends Item implements DyeableLeatherItem {
    private static final ResourceLocation TEX_FOLDER = new ResourceLocation("textures/entity/wolf/wolf_armor");
    private final ResourceLocation texture;
    private final ResourceLocation overlay;

    public WolfArmorItem(Properties properties) {
        this(TEX_FOLDER.withSuffix(".png"), TEX_FOLDER.withSuffix("_overlay.png"), properties);
    }

    public WolfArmorItem(ResourceLocation texture, ResourceLocation overlayTexture, Properties properties) {
        super(properties);
        this.texture = texture;
        this.overlay = overlayTexture;
    }

    public static int getColorOrDefault(ItemStack stack, int fallback) {
        return stack.getItem() instanceof DyeableLeatherItem item
            ? item.getColor(stack)
            : fallback;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public ResourceLocation getOverlayTexture() {
        return this.overlay;
    }
}