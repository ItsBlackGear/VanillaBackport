package com.blackgear.vanillabackport.core.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.frog.Frog;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ClientCompat {
    @ExpectPlatform
    public static boolean getNMLActiveRemodel(LivingEntity entity) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean hasQuarkCowTexture(Cow cow) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean hasQuarkPigTexture(Pig pig) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean hasQuarkChickenTexture(Chicken chicken) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean hasQuarkFrogTexture(Frog frog) {
        throw new AssertionError();
    }
}