package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.core.util.Utilities.MthUtils;
import net.minecraft.util.Mth;

public class Ease {
    public static float inBack(float x) {
        return Mth.square(x) * (2.70158F * x - 1.70158F);
    }
    
    public static float inBounce(float x) {
        return 1.0F - outBounce(1.0F - x);
    }
    
    public static float inCubic(float x) {
        return MthUtils.cube(x);
    }
    
    public static float inElastic(float x) {
        if (x == 0.0F) {
            return 0.0F;
        } else if (x == 1.0F) {
            return 1.0F;
        } else {
            return (float)(-Math.pow(2.0, 10.0 * x - 10.0) * Math.sin((x * 10.0 - 10.75) * (float) (Math.PI * 2.0 / 3.0)));
        }
    }
    
    public static float inExpo(float x) {
        return x == 0.0F ? 0.0F : (float)Math.pow(2.0, 10.0 * x - 10.0);
    }
    
    public static float inQuart(float x) {
        return Mth.square(Mth.square(x));
    }
    
    public static float inQuint(float x) {
        return Mth.square(Mth.square(x)) * x;
    }
    
    public static float inSine(float x) {
        return 1.0F - Mth.cos(x * Mth.HALF_PI);
    }
    
    public static float inOutBounce(float x) {
        return x < 0.5F ? (1.0F - outBounce(1.0F - 2.0F * x)) / 2.0F : (1.0F + outBounce(2.0F * x - 1.0F)) / 2.0F;
    }
    
    public static float inOutCirc(float x) {
        return x < 0.5F ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * x, 2.0))) / 2.0) : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * x + 2.0, 2.0)) + 1.0) / 2.0);
    }
    
    public static float inOutCubic(float x) {
        return x < 0.5F ? 4.0F * MthUtils.cube(x) : (float)(1.0 - Math.pow(-2.0 * x + 2.0, 3.0) / 2.0);
    }
    
    public static float inOutQuad(float x) {
        return x < 0.5F ? 2.0F * Mth.square(x) : (float)(1.0 - Math.pow(-2.0 * x + 2.0, 2.0) / 2.0);
    }
    
    public static float inOutQuart(float x) {
        return x < 0.5F ? 8.0F * Mth.square(Mth.square(x)) : (float)(1.0 - Math.pow(-2.0 * x + 2.0, 4.0) / 2.0);
    }
    
    public static float inOutQuint(float x) {
        return x < 0.5 ? 16.0F * x * x * x * x * x : (float)(1.0 - Math.pow(-2.0 * x + 2.0, 5.0) / 2.0);
    }
    
    public static float outBounce(float x) {
        if (x < 0.36363637F) {
            return 7.5625F * Mth.square(x);
        } else if (x < 0.72727275F) {
            return 7.5625F * Mth.square(x - 0.54545456F) + 0.75F;
        } else {
            return x < 0.9090909090909091 ? 7.5625F * Mth.square(x - 0.8181818F) + 0.9375F : 7.5625F * Mth.square(x - 0.95454544F) + 0.984375F;
        }
    }
    
    public static float outElastic(float x) {
        if (x == 0.0F) {
            return 0.0F;
        } else {
            return x == 1.0F ? 1.0F : (float)(Math.pow(2.0, -10.0 * x) * Math.sin((x * 10.0 - 0.75) * (float) (Math.PI * 2.0 / 3.0)) + 1.0);
        }
    }
    
    public static float outExpo(float x) {
        return x == 1.0F ? 1.0F : 1.0F - (float)Math.pow(2.0, -10.0 * x);
    }
    
    public static float outQuad(float x) {
        return 1.0F - Mth.square(1.0F - x);
    }
    
    public static float outQuint(float x) {
        return 1.0F - (float)Math.pow(1.0 - x, 5.0);
    }
    
    public static float outSine(float x) {
        return Mth.sin(x * Mth.HALF_PI);
    }
    
    public static float inOutSine(float x) {
        return -(Mth.cos(Mth.PI * x) - 1.0F) / 2.0F;
    }
    
    public static float outBack(float x) {
        return 1.0F + 2.70158F * MthUtils.cube(x - 1.0F) + 1.70158F * Mth.square(x - 1.0F);
    }
    
    public static float outQuart(float x) {
        return 1.0F - Mth.square(Mth.square(1.0F - x));
    }
    
    public static float outCubic(float x) {
        return 1.0F - MthUtils.cube(1.0F - x);
    }
    
    public static float inOutExpo(float x) {
        if (x < 0.5F) {
            return x == 0.0F ? 0.0F : (float)(Math.pow(2.0, 20.0 * x - 10.0) / 2.0);
        } else {
            return x == 1.0F ? 1.0F : (float)((2.0 - Math.pow(2.0, -20.0 * x + 10.0)) / 2.0);
        }
    }
    
    public static float inQuad(float x) {
        return x * x;
    }
    
    public static float outCirc(float x) {
        return (float)Math.sqrt(1.0F - Mth.square(x - 1.0F));
    }
    
    public static float inOutElastic(float x) {
        if (x == 0.0F) {
            return 0.0F;
        } else if (x == 1.0F) {
            return 1.0F;
        } else {
            double sin = Math.sin((20.0 * x - 11.125) * Mth.PI * 4.0F / 9.0F);
            return x < 0.5F ? (float)(-(Math.pow(2.0, 20.0 * x - 10.0) * sin) / 2.0) : (float)(Math.pow(2.0, -20.0 * x + 10.0) * sin / 2.0 + 1.0);
        }
    }
    
    public static float inCirc(float x) {
        return (float) (-Math.sqrt(1.0F - x * x)) + 1.0F;
    }
    
    public static float inOutBack(float x) {
        if (x < 0.5F) {
            return 4.0F * x * x * (7.189819F * x - 2.5949094F) / 2.0F;
        } else {
            float dt = 2.0F * x - 2.0F;
            return (dt * dt * (3.5949094F * dt + 2.5949094F) + 2.0F) / 2.0F;
        }
    }
}