package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(SpriteResourceLoader.class)
public abstract class SpriteResourceLoaderMixin {
    @Inject(
        method = "load",
        at = @At("RETURN")
    )
    private static void vb$handleArmorTrims(ResourceManager manager, ResourceLocation location, CallbackInfoReturnable<SpriteResourceLoader> cir) {
        if (location.equals(new ResourceLocation("armor_trims"))) {
            for (SpriteSource source : ((SpriteResourceLoaderMixin) (Object) cir.getReturnValue()).getSources()) {
                if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().equals(new ResourceLocation("trims/color_palettes/trim_palette"))) {
                    ResourceLocation resin = VanillaBackport.vanilla("trims/color_palettes/resin");

                    if (manager.getResource(new ResourceLocation(resin.getNamespace(), "textures/" + resin.getPath() + ".png")).isPresent()) {
                        Map<String, ResourceLocation> map = new HashMap<>(permutations.getPermutations());
                        map.put("resin", resin);
                        permutations.setPermutations(map);
                    } else {
                        VanillaBackport.LOGGER.warn("Resin palette texture not found at: {}", resin);
                    }
                }
            }
        }
    }

    @Accessor("sources")
    abstract List<SpriteSource> getSources();

    @Mixin(PalettedPermutations.class)
    private interface PalettedPermutationsAccessor {
        @Accessor List<ResourceLocation> getTextures();
        @Accessor("textures") @Mutable void setTextures(List<ResourceLocation> textures);

        @Accessor Map<String, ResourceLocation> getPermutations();
        @Accessor("permutations") @Mutable void setPermutations(Map<String, ResourceLocation> permutations);

        @Accessor ResourceLocation getPaletteKey();
    }
}