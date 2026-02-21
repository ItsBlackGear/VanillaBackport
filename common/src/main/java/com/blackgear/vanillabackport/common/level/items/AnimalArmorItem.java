package com.blackgear.vanillabackport.common.level.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class AnimalArmorItem extends Item {
    private final AnimalMaterial material;
    private final BodyType bodyType;

    public AnimalArmorItem(AnimalMaterial material, BodyType type, int durability, Item.Properties properties) {
        super(properties.stacksTo(1).durability(durability));
        this.material = material;
        this.bodyType = type;
    }

    public static enum BodyType {
        CANINE(new EntityType[]{EntityType.WOLF});

        public final HolderSet<EntityType<?>> allowedEntities;

        private BodyType(EntityType... entityTypes) {
            this.allowedEntities = HolderSet.direct(EntityType::builtInRegistryHolder, entityTypes);
        }
    }

    public AnimalMaterial getMaterial() {
        return this.material;
    }

    public HolderSet<EntityType<?>> getAllowedEntities() {
        return this.bodyType.allowedEntities;
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.CHEST) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ARMOR, new AttributeModifier(
                    UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295"),
                    "Armor modifier",
                    material.getDefense(),
                    AttributeModifier.Operation.ADDITION
            ));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.material.getRepairIngredient().test(repair);
    }
}
