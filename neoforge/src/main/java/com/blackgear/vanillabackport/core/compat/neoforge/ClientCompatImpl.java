package com.blackgear.vanillabackport.core.compat.neoforge;

import com.blackgear.vanillabackport.core.ModChecker;
import dev.tazer.mixed_litter.RemodelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.frog.Frog;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.violetmoon.quark.content.client.module.VariantAnimalTexturesModule;
import org.violetmoon.quark.content.client.module.VariantAnimalTexturesModule.Client;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ClientCompatImpl {
    public static boolean getNMLActiveRemodel(LivingEntity entity) {
        if (!ModChecker.MIXED_LITTER) return false;
        return RemodelRegistry.remodelActive(entity.getType());
    }
    
    public static boolean hasQuarkCowTexture(Cow cow) {
        if (!ModChecker.QUARK) return false;
        ResourceLocation texture = Client.getCowTexture(cow);
        return texture != null && texture.getPath().contains("shiny");
    }
    
    public static boolean hasQuarkPigTexture(Pig pig) {
        if (!ModChecker.QUARK) return false;
        ResourceLocation texture = Client.getPigTexture(pig);
        return texture != null && texture.getPath().contains("shiny");
    }
    
    public static boolean hasQuarkChickenTexture(Chicken chicken) {
        if (!ModChecker.QUARK) return false;
        ResourceLocation texture = Client.getChickenTexture(chicken);
        return texture != null && texture.getPath().contains("shiny");
    }
    
    public static boolean hasQuarkFrogTexture(Frog frog) {
        if (!ModChecker.QUARK) return false;
        ResourceLocation texture = Client.getFrogTexture(frog);
        return texture != null && texture.getPath().contains("shiny");
    }
}