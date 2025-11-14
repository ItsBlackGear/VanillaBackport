package com.blackgear.vanillabackport.core.forge;

import com.blackgear.platform.core.Environment;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.forge.registries.VanillaBackportForgeItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VanillaBackport.MOD_ID)
public final class VanillaBackportForge {
    public VanillaBackportForge() {
        VanillaBackport.bootstrap();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);

        // Class to handle decorated pot update, forge platform. - Echo2craft.
        VanillaBackportForgeItems.register(bus);
        // Add Datagen for forge loader, mainly for Sound Definition for now. Not functioning right now. - Echo2craft.
        // bus.addListener(ForgeDataGenerator::gatherData);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        if (Environment.hasModLoaded("terrablender")) {
            VanillaBackportTerrablender.onTerraBlenderInitialized();
        }
    }
}