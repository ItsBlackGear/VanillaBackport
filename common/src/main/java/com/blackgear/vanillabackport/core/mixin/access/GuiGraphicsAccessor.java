package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Invoker
    void callBlitNineSlicedSprite(TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice nineSlice, int x, int y, int blitOffset, int width, int height);
}
