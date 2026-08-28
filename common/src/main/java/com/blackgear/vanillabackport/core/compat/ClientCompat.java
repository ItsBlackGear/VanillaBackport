package com.blackgear.vanillabackport.core.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;

@Environment(EnvType.CLIENT)
public class ClientCompat {
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
}