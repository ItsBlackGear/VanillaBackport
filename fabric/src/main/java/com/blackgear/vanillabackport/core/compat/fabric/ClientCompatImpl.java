package com.blackgear.vanillabackport.core.compat.fabric;

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
public class ClientCompatImpl {
    public static boolean getNMLActiveRemodel(LivingEntity entity) {
        return false;
    }
    
    public static boolean hasQuarkCowTexture(Cow cow) {
        return false;
    }
    
    public static boolean hasQuarkPigTexture(Pig pig) {
        return false;
    }
    
    public static boolean hasQuarkChickenTexture(Chicken chicken) {
        return false;
    }
    
    public static boolean hasQuarkFrogTexture(Frog frog) {
        return false;
    }
}