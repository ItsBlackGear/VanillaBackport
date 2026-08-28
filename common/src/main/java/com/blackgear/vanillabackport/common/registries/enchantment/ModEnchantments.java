package com.blackgear.vanillabackport.common.registries.enchantment;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.items.enchantment.effects.ApplyEntityImpulse;
import com.blackgear.vanillabackport.common.level.items.enchantment.effects.ApplyExhaustion;
import com.blackgear.vanillabackport.common.level.items.enchantment.effects.PlaySoundEffect;
import com.blackgear.vanillabackport.common.predicates.FoodPredicate;
import com.blackgear.vanillabackport.common.predicates.PlayerPredicate;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AllOf;
import net.minecraft.world.item.enchantment.effects.DamageItem;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ModEnchantments {
    public static final BootstrapRegistrar<Enchantment> REGISTRIES = BootstrapRegistrar.create(Registries.ENCHANTMENT, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<Enchantment> LUNGE = REGISTRIES.resource("lunge", (context, key) -> {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
            Enchantment.definition(
                items.getOrThrow(ModItemTags.LUNGE_ENCHANTABLE),
                5,
                3,
                Enchantment.dynamicCost(5, 8),
                Enchantment.dynamicCost(25, 8),
                2,
                EquipmentSlotGroup.HAND
            )
        )
        .withEffect(
            ModEnchantmentEffectComponents.POST_PIERCING_ATTACK.get(),
            AllOf.entityEffects(
                new DamageItem(new LevelBasedValue.Constant(1.0F)),
                new ApplyExhaustion(LevelBasedValue.perLevel(4.0F)),
                new ApplyEntityImpulse(new Vec3(0.0, 0.0, 1.0), new Vec3(1.0, 0.0, 1.0), LevelBasedValue.perLevel(0.458F)),
                new PlaySoundEffect(List.of(ModSoundEvents.LUNGE_1, ModSoundEvents.LUNGE_2, ModSoundEvents.LUNGE_3), ConstantFloat.of(1.0F), ConstantFloat.of(1.0F))
            ),
            AllOfCondition.allOf(
                InvertedLootItemCondition.invert(
                    LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity()))
                ),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsFlying(false))
                ),
                AnyOfCondition.anyOf(
                    InvertedLootItemCondition.invert(
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().build())
                        )
                    ),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().setGameType(GameTypePredicate.of(GameType.CREATIVE)).build())
                    ),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().setFood(FoodPredicate.Builder.food().withLevel(MinMaxBounds.Ints.atLeast(Mth.floor(6.0F) + 1)).build()).build())
                    )
                )
            )
        ).build(key);
    });
}