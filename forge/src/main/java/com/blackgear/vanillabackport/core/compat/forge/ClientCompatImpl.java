package com.blackgear.vanillabackport.core.compat.forge;

import com.blackgear.vanillabackport.core.ModChecker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.violetmoon.quark.content.client.module.VariantAnimalTexturesModule;

@OnlyIn(Dist.CLIENT)
public class ClientCompatImpl {
    public static boolean hasQuarkCowTexture(Cow cow) {
        if (!ModChecker.QUARK) return false;
        ResourceLocation texture = VariantAnimalTexturesModule.Client.getCowTexture(cow);
        return texture != null && texture.getPath().contains("shiny");
    }
    
    public static boolean hasQuarkPigTexture(Pig pig) {
        if (!ModChecker.QUARK) return false;
        ResourceLocation texture = VariantAnimalTexturesModule.Client.getPigTexture(pig);
        return texture != null && texture.getPath().contains("shiny");
    }
    
    public static boolean hasQuarkChickenTexture(Chicken chicken) {
        if (!ModChecker.QUARK) return false;
        ResourceLocation texture = VariantAnimalTexturesModule.Client.getChickenTexture(chicken);
        return texture != null && texture.getPath().contains("shiny");
    }
}