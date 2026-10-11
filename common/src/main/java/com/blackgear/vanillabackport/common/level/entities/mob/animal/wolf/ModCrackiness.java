package com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf;

import net.minecraft.world.item.ItemStack;

public class ModCrackiness {
    public static final ModCrackiness WOLF_ARMOR = new ModCrackiness(0.95F, 0.69F, 0.32F);
    private final float fractionLow;
    private final float fractionMedium;
    private final float fractionHigh;

    private ModCrackiness(float fractionLow, float fractionMedium, float fractionHigh) {
        this.fractionLow = fractionLow;
        this.fractionMedium = fractionMedium;
        this.fractionHigh = fractionHigh;
    }

    public Level byFraction(float fraction) {
        if (fraction < this.fractionHigh) {
            return Level.HIGH;
        } else if (fraction < this.fractionMedium) {
            return Level.MEDIUM;
        } else {
            return fraction < this.fractionLow ? Level.LOW : Level.NONE;
        }
    }

    public Level byDamage(ItemStack stack) {
        return !stack.isDamageableItem() ? Level.NONE : this.byDamage(stack.getDamageValue(), stack.getMaxDamage());
    }

    public Level byDamage(int damage, int durability) {
        return this.byFraction((float) (durability - damage) / (float) durability);
    }

    public enum Level {
        NONE, LOW, MEDIUM, HIGH
    }
}