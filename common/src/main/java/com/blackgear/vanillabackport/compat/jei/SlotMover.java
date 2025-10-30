package com.blackgear.vanillabackport.compat.jei;

import com.blackgear.vanillabackport.client.api.tabs.BundledTabSelector;
import mezz.jei.api.gui.handlers.IGlobalGuiHandler;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class SlotMover implements IGuiContainerHandler<CreativeModeInventoryScreen> {
    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(@NotNull CreativeModeInventoryScreen containerScreen) {
        return BundledTabSelector.bootstrap().getExtraAreas();
    }
}
