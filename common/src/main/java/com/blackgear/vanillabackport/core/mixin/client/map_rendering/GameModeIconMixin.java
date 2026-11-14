package com.blackgear.vanillabackport.core.mixin.client.map_rendering;

import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(GameModeSwitcherScreen.GameModeIcon.class)
public class GameModeIconMixin {
  @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
  private static ItemStack modifyIcon(ItemStack original) {
    return original.getItem().equals(Items.MAP)
        ? vanillabackport$createAdventureModeIcon()
        : original;
  }

  @Unique
  private static ItemStack vanillabackport$createAdventureModeIcon() {
    ItemStack itemStack = new ItemStack(Items.FILLED_MAP);
    MapDecorations.Entry decoration = new MapDecorations.Entry(MapDecorationTypes.RED_X, 0, 0, 0);
    MapDecorations decorations = new MapDecorations(Map.of("+", decoration));
    itemStack.set(DataComponents.MAP_DECORATIONS, decorations);
    return itemStack;
  }
}
