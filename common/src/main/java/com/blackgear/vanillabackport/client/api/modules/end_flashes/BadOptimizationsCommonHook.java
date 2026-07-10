package com.blackgear.vanillabackport.client.api.modules.end_flashes;

import com.blackgear.vanillabackport.client.api.modules.end_flashes.EndFlashRenderer;
// import com.mojang.logging.LogUtils;
// import org.slf4j.Logger;
import java.util.function.BooleanSupplier;

public class BadOptimizationsCommonHook implements BooleanSupplier {
    //private static final Logger LOGGER = LogUtils.getLogger();
  // Class must have a public empty constructor!
  public BadOptimizationsCommonHook() {}

  // This method will be called up to once per tick
  // If it returns true, the lightmap/skycolor will update immediately
  @Override
  public boolean getAsBoolean() {
    // if (EndFlashRenderer.needsLightmapUpdate()){
    //     LOGGER.info("Updating End Flash!");
    // }
    return (EndFlashRenderer.needsLightmapUpdate());
  }
}