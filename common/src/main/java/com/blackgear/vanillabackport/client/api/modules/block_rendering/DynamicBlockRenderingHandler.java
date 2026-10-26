package com.blackgear.vanillabackport.client.api.modules.block_rendering;

import com.blackgear.platform.core.util.config.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import java.util.*;

/**
 * The DynamicBlockRenderingHandler checks every two seconds if a config value that determines
 * a block model has changed. If that happened, chunks get rerendered to account for the new model.
 * Watched config values should be defined in {@link #WATCHED_CONFIG_VALUES}
 */
@Environment(EnvType.CLIENT)
public class DynamicBlockRenderingHandler {
  public static final DynamicBlockRenderingHandler INSTANCE = new DynamicBlockRenderingHandler();
  public static final List<ConfigBuilder.ConfigValue<?>> WATCHED_CONFIG_VALUES = new ArrayList<>();
  
  public int ticksUntilNextCheck = 0;
  public Map<ConfigBuilder.ConfigValue<?>, Object> lastConfigValues = new HashMap<>();
  
  public static void register(ConfigBuilder.ConfigValue<?> value) {
    WATCHED_CONFIG_VALUES.add(value);
  }
  
  public void tick() {
    if (ticksUntilNextCheck > 0) {
      ticksUntilNextCheck--;
      return;
    }

    ticksUntilNextCheck = 40;

    boolean hasChanged = false;
    for (ConfigBuilder.ConfigValue<?> watchedConfigValue : WATCHED_CONFIG_VALUES) {
      Object lastValue = lastConfigValues.get(watchedConfigValue);
      Object currentValue = watchedConfigValue.get();
      if ((lastValue == null && currentValue != null) || (lastValue != null && !lastValue.equals(currentValue))) {
        hasChanged = true;
        lastConfigValues.put(watchedConfigValue, currentValue);
      }
    }

    if (hasChanged) {
      Minecraft.getInstance().levelRenderer.allChanged();
    }
  }
}
