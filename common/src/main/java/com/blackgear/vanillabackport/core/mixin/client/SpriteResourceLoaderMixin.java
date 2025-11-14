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

                    // Tricky Trials trim patterns. - Echo2craft.
                    List<ResourceLocation> trimPatternSet = List.of(
                            VanillaBackport.vanilla("trims/models/armor/bolt"),
                            VanillaBackport.vanilla("trims/models/armor/bolt_leggings"),
                            VanillaBackport.vanilla("trims/models/armor/flow"),
                            VanillaBackport.vanilla("trims/models/armor/flow_leggings")
                    );

                    if (manager.getResource(new ResourceLocation(resin.getNamespace(), "textures/" + resin.getPath() + ".png")).isPresent()) {
                        Map<String, ResourceLocation> map = new HashMap<>(permutations.getPermutations());
                        map.put("resin", resin);
                        permutations.setPermutations(map);
                    } else {
                        VanillaBackport.LOGGER.warn("Resin palette texture not found at: {}", resin);
                    }

                    // Adding trim patterns. - Echo2craft.
                    for (ResourceLocation pattern : trimPatternSet){
                        if (manager.getResource(new ResourceLocation(pattern.getNamespace(), "textures/" + pattern.getPath() + ".png")).isPresent()){
                            // Cast permutations.getTextures() to a new ArrayList to avoid ImmutableList error.
                            List<ResourceLocation> textures = new java.util.ArrayList<>(permutations.getTextures().stream().toList());
                            textures.add(pattern);
                            permutations.setTextures(textures);
                        } else {
                            VanillaBackport.LOGGER.warn("Trim pattern texture not found at: {}", pattern);
                        }
                    }
                }
            }
        }
    }

    @Accessor("sources")
    abstract List<SpriteSource> getSources();

    @Mixin(PalettedPermutations.class)
    private interface PalettedPermutationsAccessor {
        // Apparently, List<ResourceLocation> textures value is an ImmutableList.
        @Accessor List<ResourceLocation> getTextures();
        // Apparently, List<ResourceLocation> textures value is an ImmutableList. Cannot be set unless class cast is used.
        @Accessor("textures") @Mutable void setTextures(List<ResourceLocation> textures);

        @Accessor Map<String, ResourceLocation> getPermutations();
        @Accessor("permutations") @Mutable void setPermutations(Map<String, ResourceLocation> permutations);

        @Accessor ResourceLocation getPaletteKey();
    }
}