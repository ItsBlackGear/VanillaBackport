package com.blackgear.vanillabackport.common.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.food.FoodData;

public record FoodPredicate(MinMaxBounds.Ints level, MinMaxBounds.Doubles saturation) {
    public static final FoodPredicate ANY = new FoodPredicate(MinMaxBounds.Ints.ANY, MinMaxBounds.Doubles.ANY);
    public static final Codec<FoodPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(FoodPredicate::level),
        MinMaxBounds.Doubles.CODEC.optionalFieldOf("saturation", MinMaxBounds.Doubles.ANY).forGetter(FoodPredicate::saturation)
    ).apply(instance, FoodPredicate::new));
    
    public boolean matches(FoodData food) {
        return this.level.matches(food.getFoodLevel()) && this.saturation.matches(food.getSaturationLevel());
    }
    
    public static class Builder {
        private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
        private MinMaxBounds.Doubles saturation = MinMaxBounds.Doubles.ANY;
        
        public Builder withLevel(MinMaxBounds.Ints level) {
            this.level = level;
            return this;
        }
        
        public Builder withSaturation(MinMaxBounds.Doubles saturation) {
            this.saturation = saturation;
            return this;
        }
        
        public static Builder food() {
            return new Builder();
        }
        
        public FoodPredicate build() {
            return new FoodPredicate(this.level, this.saturation);
        }
    }
}