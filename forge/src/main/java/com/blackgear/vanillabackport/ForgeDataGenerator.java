package com.blackgear.vanillabackport;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.data.client.SoundDefinitionGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

// Example Forge Data Generator use case. - Echo2craft.
public class ForgeDataGenerator {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(
                event.includeClient(),
                new SoundDefinitionGenerator(
                        packOutput,
                        VanillaBackport.NAMESPACE,
                        existingFileHelper
                )
        );
    }
}
