package com.blackgear.vanillabackport.client.resources;

import com.blackgear.vanillabackport.client.level.color.DryFoliageColor;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.LegacyStuffWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class DryFoliageColorReloadListener extends SimplePreparableReloadListener<int[]> {
    private static final ResourceLocation LOCATION = VanillaBackport.vanilla("textures/colormap/dry_foliage.png");
    public static final DryFoliageColorReloadListener INSTANCE = new DryFoliageColorReloadListener();

    @Override
    protected int[] prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        try {
            return LegacyStuffWrapper.getPixels(resourceManager, LOCATION);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load dry foliage color texture", exception);
        }
    }

    @Override
    protected void apply(int[] object, ResourceManager resourceManager, ProfilerFiller profiler) {
        DryFoliageColor.init(object);
    }
}