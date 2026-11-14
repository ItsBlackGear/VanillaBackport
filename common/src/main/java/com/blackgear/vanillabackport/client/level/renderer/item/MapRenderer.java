package com.blackgear.vanillabackport.client.level.renderer.item;

import com.blackgear.platform.core.util.config.ConfigBuilder;
import com.blackgear.vanillabackport.common.registries.ModMapDecorationTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.item.component.MapItemColor;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapRenderer extends BackportedItemRenderer {
  private static final ModelResourceLocation MAP_LOCATION
      = getLocationFromPath("map");
  private static final ModelResourceLocation FILLED_MAP_LOCATION
      = getLocationFromPath("filled_map");
  private static final ModelResourceLocation COLORED_MAP_LOCATION
      = getLocationFromPath("colored_map");

  private static final Map<Holder<MapDecorationType>, ModelResourceLocation> EXPLORER_MAP_LOCATIONS = Map.ofEntries(
      Map.entry(MapDecorationTypes.RED_X, getLocationFromPath("buried_treasure_map")),
      Map.entry(MapDecorationTypes.WOODLAND_MANSION, getLocationFromPath("woodland_mansion_map")),
      Map.entry(MapDecorationTypes.OCEAN_MONUMENT, getLocationFromPath("ocean_monument_map")),
      Map.entry(MapDecorationTypes.DESERT_VILLAGE, getLocationFromPath("desert_village_map")),
      Map.entry(MapDecorationTypes.PLAINS_VILLAGE, getLocationFromPath("plains_village_map")),
      Map.entry(MapDecorationTypes.SAVANNA_VILLAGE, getLocationFromPath("savanna_village_map")),
      Map.entry(MapDecorationTypes.SNOWY_VILLAGE, getLocationFromPath("snowy_village_map")),
      Map.entry(MapDecorationTypes.TAIGA_VILLAGE, getLocationFromPath("taiga_village_map")),
      Map.entry(MapDecorationTypes.JUNGLE_TEMPLE, getLocationFromPath("jungle_pyramid_map")),
      Map.entry(MapDecorationTypes.SWAMP_HUT, getLocationFromPath("swamp_hut_map")),
      Map.entry(MapDecorationTypes.TRIAL_CHAMBERS, getLocationFromPath("buried_trial_chambers_map")),
      Map.entry(ModMapDecorationTypes.ABANDONED_CAMP, getLocationFromPath("abandoned_camp_map")),
      Map.entry(ModMapDecorationTypes.ANCIENT_CITY, getLocationFromPath("buried_ancient_city_map")),
      Map.entry(ModMapDecorationTypes.DESERT_PYRAMID, getLocationFromPath("desert_pyramid_map")),
      Map.entry(ModMapDecorationTypes.MINESHAFT, getLocationFromPath("buried_mineshaft_map")),
      Map.entry(ModMapDecorationTypes.OCEAN_RUIN_WARM, getLocationFromPath("warm_ocean_ruins_map"))
  );

  private static ModelResourceLocation getLocationFromPath(String path) {
    return ModelResourceLocation.inventory(VanillaBackport.resource(path));
  }

  public MapRenderer(ConfigBuilder.ConfigValue<Boolean> configValue, Set<Item> items) {
    super(configValue, items);
  }

  @Override
  protected ModelResourceLocation getModelResourceLocation(ItemStack itemStack) {
    if (Items.MAP.equals(itemStack.getItem())) return MAP_LOCATION;

    ModelResourceLocation decorationLocation = getLocationFromMapLocation(itemStack);
    if (decorationLocation != null) return decorationLocation;

    MapItemColor mapColor = itemStack.get(DataComponents.MAP_COLOR);
    if (mapColor != null && !mapColor.equals(MapItemColor.DEFAULT)) return COLORED_MAP_LOCATION;

    return FILLED_MAP_LOCATION;
  }

  @Nullable
  private static ModelResourceLocation getLocationFromMapLocation(ItemStack itemStack) {
    MapDecorations decorations = itemStack.get(DataComponents.MAP_DECORATIONS);
    if (decorations == null) return null;

    MapDecorations.Entry decoration = decorations.decorations().get("+");
    if (decoration == null) return null;

    return EXPLORER_MAP_LOCATIONS.get(decoration.type());
  }

  @Override
  public Set<ModelResourceLocation> registerModels() {
    HashSet<ModelResourceLocation> set = new HashSet<>(EXPLORER_MAP_LOCATIONS.values());
    set.add(MAP_LOCATION);
    set.add(FILLED_MAP_LOCATION);
    set.add(COLORED_MAP_LOCATION);
    return set;
  }
}
