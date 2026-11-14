package com.blackgear.vanillabackport.client.api.modules.map_rendering;

import net.minecraft.core.Holder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

import java.util.Set;

public class SpecialMapDecorationTypes {
  public static final Set<Holder<MapDecorationType>> PLAYER_TYPES = Set.of(
      MapDecorationTypes.PLAYER,
      MapDecorationTypes.PLAYER_OFF_LIMITS,
      MapDecorationTypes.PLAYER_OFF_MAP
  );
}
