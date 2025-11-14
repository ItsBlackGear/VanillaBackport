package com.blackgear.vanillabackport.compat.emi;

import com.blackgear.vanillabackport.compat.jei.VanillaBackportSlotMover;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

@EmiEntrypoint
public class VanillaBackportEMI implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addExclusionArea(CreativeModeInventoryScreen.class, (pCreativeScreen, consumer) -> {
            var slot = (new VanillaBackportSlotMover()).getGuiExtraAreas(pCreativeScreen);
            slot.forEach( rect2i -> consumer.accept(new Bounds(rect2i.getX(), rect2i.getY(), rect2i.getWidth(), rect2i.getHeight())));
        });
    }
}
