package com.blackgear.vanillabackport.common.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SpearMobsTrigger extends SimpleCriterionTrigger<SpearMobsTrigger.TriggerInstance> {
    static final ResourceLocation ID = new ResourceLocation("spear_mobs");
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
        return new TriggerInstance(player, json.getAsJsonPrimitive("count").getAsInt());
    }
    
    public void trigger(ServerPlayer player, int count) {
        this.trigger(player, instance -> instance.matches(count));
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final int count;
        
        public TriggerInstance(ContextAwarePredicate player, int count) {
            super(SpearMobsTrigger.ID, player);
            this.count = count;
        }
        
        public static TriggerInstance spearMobs(int requiredCounts) {
            return new TriggerInstance(ContextAwarePredicate.ANY, requiredCounts);
        }
        
        public boolean matches(int requiredCount) {
            return this.count == 0 || requiredCount >= this.count;
        }
        
        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject object = super.serializeToJson(context);
            object.addProperty("count", this.count);
            return object;
        }
    }
}