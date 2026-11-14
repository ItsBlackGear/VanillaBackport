package com.blackgear.vanillabackport.core.mixin.client.map_rendering;

import com.blackgear.vanillabackport.client.api.modules.map_rendering.SpecialMapDecorationTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(MapDecoration.class)
public abstract class MapDecorationMixin {
  @Shadow
  public abstract Holder<MapDecorationType> type();

  @Inject(method = "getSpriteLocation", at = @At("HEAD"), cancellable = true)
  private void modifySpriteLocation(CallbackInfoReturnable<ResourceLocation> cir) {
    if (VanillaBackport.CLIENT_CONFIG.hasModernMapPlayerMarkerTextures.get()
        && SpecialMapDecorationTypes.PLAYER_TYPES.contains(type())) {
      cir.setReturnValue(VanillaBackport.resource(type().value().assetId().getPath()));
    }
  }
}
