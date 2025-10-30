package com.blackgear.vanillabackport.compat.jei;

import com.blackgear.vanillabackport.core.VanillaBackport;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

// Allow Creative Bundled Tabs to adapt to JEI UI - Echo2craft.
@JeiPlugin
public class VanillaBackportJEI implements IModPlugin {
    private static final ResourceLocation ID = VanillaBackport.resource("jei_plugin");

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // registration.addGuiContainerHandler(CreativeModeInventoryScreen.class, new SlotMover());
        // registration.addGuiScreenHandler();
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }
}
