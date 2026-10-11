package com.blackgear.vanillabackport.core.compat.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;

@Environment(EnvType.CLIENT)
public class ClientCompatImpl {
    public static boolean hasQuarkCowTexture(Cow cow) {
        return false;
    }
    
    public static boolean hasQuarkPigTexture(Pig pig) {
        return false;
    }
    
    public static boolean hasQuarkChickenTexture(Chicken chicken) {
        return false;
    }
}