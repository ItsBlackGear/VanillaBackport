package com.blackgear.vanillabackport.core.mixin.client.map_rendering;

import com.blackgear.vanillabackport.client.api.modules.map_rendering.SpecialMapDecorationTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Debug(export = true)
@Mixin(MapItemSavedData.class)
public abstract class MapItemSavedDataMixin {
  @Shadow
  @Final
  public byte scale;

  @Shadow
  @Final
  public int centerX;

  @Shadow
  @Final
  public int centerZ;

  @Shadow
  @Final
  public ResourceKey<Level> dimension;

  @Shadow
  protected abstract void removeDecoration(String identifier);

  @Shadow
  @Final
  private boolean unlimitedTracking;

  @Shadow
  @Final
  Map<String, MapDecoration> decorations;

  @Shadow
  private int trackedDecorationCount;

  @Shadow
  protected abstract void setDecorationsDirty();

  @Inject(method = "addDecoration", at = @At("HEAD"), cancellable = true)
  private void interceptAddingDecoration(Holder<MapDecorationType> decorationType, LevelAccessor level, String id,
                 double x, double z, double yRot, Component displayName, CallbackInfo ci) {
    if (!VanillaBackport.CLIENT_CONFIG.hasModernMapPlayerMarkerTextures.get()) return;

    int i = 1 << this.scale;
    float f = (float) (x - this.centerX) / i;
    float g = (float) (z - this.centerZ) / i;
    byte b = (byte) (f * 2 + 0.5);
    byte c = (byte) (g * 2.0F + 0.5);
    int j = 63;
    byte d = 0;

    if (SpecialMapDecorationTypes.PLAYER_TYPES.contains(decorationType)) {
      yRot += yRot < 0 ? -8 : 8;
      d = (byte) (yRot * 16 / 360);
      if (this.dimension == Level.NETHER && level != null) {
        int k = (int) (level.getLevelData().getDayTime() / 10L);
        d = (byte) ((k * k * 34187121 + k * 121) >> 15 & 15);
      }
    }
    else {
      d = 8;
    }

    if (!(f >= -63 && g >= -63 && f <= 63 && g <= 63)) {
      if (!decorationType.is(MapDecorationTypes.PLAYER)) {
        this.removeDecoration(id);
        return;
      }

      int k = 320;
      if (Math.abs(f) < 320 && Math.abs(g) < 320) {
        decorationType = MapDecorationTypes.PLAYER_OFF_MAP;
      } else {
        if (!this.unlimitedTracking) {
          this.removeDecoration(id);
          return;
        }

        decorationType = MapDecorationTypes.PLAYER_OFF_LIMITS;
      }

      if (f <= -63) {
        b = -128;
      }

      if (g <= -63) {
        c = -128;
      }

      if (f >= 63) {
        b = 127;
      }

      if (g >= 63) {
        c = 127;
      }
    }

    MapDecoration mapDecoration = new MapDecoration(decorationType, b, c, d, Optional.ofNullable(displayName));
    MapDecoration mapDecoration2 = this.decorations.put(id, mapDecoration);
    if (!mapDecoration.equals(mapDecoration2)) {
      if (mapDecoration2 != null && mapDecoration2.type().value().trackCount()) {
        --this.trackedDecorationCount;
      }

      if (decorationType.value().trackCount()) {
        ++this.trackedDecorationCount;
      }

      this.setDecorationsDirty();
    }

    ci.cancel();
  }
}
