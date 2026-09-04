package com.blackgear.vanillabackport.core.mixin.common.bundle_ui;

import com.blackgear.vanillabackport.common.api.modules.bundle_ui.ModernBundle;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BundleContents.class)
public abstract class BundleContentsMixin implements ModernBundle {
    @Shadow public abstract int size();

    @Unique private int selectedItem = -1;

    @Override
    public void setSelectedItem(int index) {
        this.selectedItem = index;
    }

    @Override
    public int getSelectedItem() {
        return this.selectedItem;
    }

    @Override
    public int getNumberOfItemsToShow() {
        int contents = this.size();
        if (VanillaBackport.CLIENT_CONFIG.endlessBundleUi.get()) {
            return contents;
        }

        int maxDisplay = contents > 12 ? 11 : 12;
        int remainder = contents % 4;
        int padding = remainder == 0 ? 0 : 4 - remainder;
        return Math.min(contents, maxDisplay - padding);
    }
}