package com.blackgear.vanillabackport.compat.jei;

import com.blackgear.vanillabackport.client.api.tabs.BundledTabSelector;
import com.blackgear.vanillabackport.client.registries.ModCreativeTabs;
import com.blackgear.vanillabackport.core.mixin.access.CreativeModeInventoryScreenAccessor;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VanillaBackportSlotMover implements IGuiContainerHandler<CreativeModeInventoryScreen> {
    private static final List<Rect2i> DEFAULT_EXCLUSION = List.of();
    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(@NotNull CreativeModeInventoryScreen containerScreen) {
        if(CreativeModeInventoryScreenAccessor.getSelectedTab() == ModCreativeTabs.VANILLA_BACKPORT.get()){
            return BundledTabSelector.bootstrap().getExtraAreas();
        }
        return DEFAULT_EXCLUSION;
    }
}
