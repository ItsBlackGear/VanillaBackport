package com.blackgear.vanillabackport.client.api.modules.bundle_ui;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.EnumMap;
import java.util.Map;

public final class BundleRenderHandler {
    private static final ResourceLocation DEFAULT_BACK = ResourceLocation.withDefaultNamespace("textures/item/bundle_open_back.png");
    private static final ResourceLocation DEFAULT_FRONT = ResourceLocation.withDefaultNamespace("textures/item/bundle_open_front.png");

    private static final Map<DyeColor, ResourceLocation> BACK_TEXTURES = new EnumMap<>(DyeColor.class);
    private static final Map<DyeColor, ResourceLocation> FRONT_TEXTURES = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            BACK_TEXTURES.put(color, ResourceLocation.withDefaultNamespace("textures/item/" + color.getName() + "_bundle_open_back.png"));
            FRONT_TEXTURES.put(color, ResourceLocation.withDefaultNamespace("textures/item/" + color.getName() + "_bundle_open_front.png"));
        }
    }

    public static boolean renderBundleContents(GuiGraphics graphics, Font font, Slot slot, int imageWidth) {
        if (!BundleFeatures.onBundleUpdate()) return false;

        ItemStack stack = slot.getItem();
        if (!stack.is(ModItemTags.BUNDLES)) return false;

        ItemStack selectedItem = BundleFeatures.getSelectedItemStack(stack);
        if (selectedItem.isEmpty()) return false;

        ResourceLocation backTexture = DEFAULT_BACK;
        ResourceLocation frontTexture = DEFAULT_FRONT;

        if (!stack.is(Items.BUNDLE)) {
            for (Map.Entry<DyeColor, ResourceLocation> entry : BACK_TEXTURES.entrySet()) {
                if (stack.is(BundleFeatures.getByColor(entry.getKey()))) {
                    backTexture = entry.getValue();
                    frontTexture = FRONT_TEXTURES.get(entry.getKey());
                    break;
                }
            }
        }

        int slotX = slot.x;
        int slotY = slot.y;
        PoseStack pose = graphics.pose();

        pose.pushPose();
        pose.translate(0.0F, 0.0F, 100.0F);

        graphics.blit(backTexture, slotX, slotY, 0, 0, 16, 16, 16, 16);
        graphics.renderItem(selectedItem, slotX, slotY, slotX + slotY * imageWidth);

        pose.pushPose();
        pose.translate(0.0F, 0.0F, 200.0F);
        graphics.blit(frontTexture, slotX, slotY, 0, 0, 16, 16, 16, 16);
        graphics.renderItemDecorations(font, stack, slotX, slotY);
        pose.popPose();

        pose.popPose();
        return true;
    }
}