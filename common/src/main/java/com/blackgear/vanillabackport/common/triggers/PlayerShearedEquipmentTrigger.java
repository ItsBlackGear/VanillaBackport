package com.blackgear.vanillabackport.common.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerShearedEquipmentTrigger extends SimpleCriterionTrigger<PlayerShearedEquipmentTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation("player_sheared_equipment");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	protected PlayerShearedEquipmentTrigger.TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
		ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
		ContextAwarePredicate entity = EntityPredicate.fromJson(json, "entity", context);
		return new PlayerShearedEquipmentTrigger.TriggerInstance(player, item, entity);
	}

	public void trigger(ServerPlayer player, ItemStack item, Entity entity) {
		LootContext lootContext = EntityPredicate.createContext(player, entity);
		this.trigger(player, instance -> instance.matches(item, lootContext));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate item;
		private final ContextAwarePredicate entity;

		public TriggerInstance(ContextAwarePredicate player, ItemPredicate item, ContextAwarePredicate entity) {
			super(PlayerShearedEquipmentTrigger.ID, player);
			this.item = item;
			this.entity = entity;
		}

		public static PlayerShearedEquipmentTrigger.TriggerInstance equipmentSheared(ContextAwarePredicate player, ItemPredicate.Builder item, ContextAwarePredicate entity) {
			return new PlayerShearedEquipmentTrigger.TriggerInstance(player, item.build(), entity);
		}

		public static PlayerShearedEquipmentTrigger.TriggerInstance equipmentSheared(ItemPredicate.Builder item, ContextAwarePredicate entity) {
			return equipmentSheared(ContextAwarePredicate.ANY, item, entity);
		}

		public boolean matches(ItemStack item, LootContext lootContext) {
			return this.item.matches(item) && this.entity.matches(lootContext);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject object = super.serializeToJson(context);
			object.add("item", this.item.serializeToJson());
			object.add("entity", this.entity.toJson(context));
			return object;
		}
	}
}
