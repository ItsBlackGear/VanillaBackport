package com.blackgear.vanillabackport.core.mixin.client.tab_scrolling;

import com.blackgear.vanillabackport.client.registries.ModCreativeTabs;
import com.blackgear.vanillabackport.core.mixin.client.access.CreativeModeInventoryScreenAccessor;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    /**
     * Stops the inventory from scrolling if your mouse is hovering the Bundled Tabs
     */
    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) (Object) this;
        if (CreativeModeInventoryScreenAccessor.getSelectedTab() != ModCreativeTabs.VANILLA_BACKPORT.get()) return;

        if (mouseX >= screen.leftPos - 30 && mouseY >= screen.topPos + 2 && mouseX <= screen.leftPos && mouseY <= screen.topPos + 122) {
            cir.setReturnValue(false);
        }
    }
}