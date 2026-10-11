package com.blackgear.vanillabackport.common.triggers;

import com.blackgear.vanillabackport.common.registries.ModCriteriaTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class SpearMobsTrigger extends SimpleCriterionTrigger<SpearMobsTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }
    
    public void trigger(ServerPlayer player, int number) {
        this.trigger(player, trigger -> trigger.matches(number));
    }
    
    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Integer> count) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("count").forGetter(TriggerInstance::count)
        ).apply(instance, TriggerInstance::new));
        
        public static Criterion<TriggerInstance> spearMobs(int requiredCounts) {
            return ModCriteriaTriggers.SPEAR_MOBS_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(requiredCounts)));
        }
        
        public boolean matches(int requiredCount) {
            return this.count.isEmpty() || requiredCount >= this.count.get();
        }
    }
}