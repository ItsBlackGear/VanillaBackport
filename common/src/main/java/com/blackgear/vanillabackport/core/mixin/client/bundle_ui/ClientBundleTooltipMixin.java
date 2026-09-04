package com.blackgear.vanillabackport.core.mixin.client.bundle_ui;

import com.blackgear.vanillabackport.client.api.modules.bundle_ui.BundleTooltipHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientBundleTooltip.class)
public class ClientBundleTooltipMixin implements ClientTooltipComponent {
    @Shadow @Final private NonNullList<ItemStack> items;
    @Shadow @Final private int weight;
    
    @Unique private BundleTooltipHandler vb$handler;
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(BundleTooltip tooltip, CallbackInfo ci) {
        this.vb$handler = new BundleTooltipHandler(this.items, this.weight);
        this.vb$handler.init(tooltip);
    }
    
    @Override
    public int getHeight() {
        return this.vb$handler.getHeight();
    }
    
    @Override
    public int getWidth(Font font) {
        return this.vb$handler.getWidth();
    }
    
    @Inject(method = "renderImage", at = @At("HEAD"), cancellable = true)
    private void vb$onRenderImage(Font font, int x, int y, GuiGraphics graphics, CallbackInfo ci) {
        if (this.vb$handler.renderImage(font, x, y, graphics)) ci.cancel();
    }
}