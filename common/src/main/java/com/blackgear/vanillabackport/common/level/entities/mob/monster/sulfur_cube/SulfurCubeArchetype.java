package com.blackgear.vanillabackport.common.level.entities.mob.monster.sulfur_cube;

import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.util.codec.AttributeCodecs;
import com.blackgear.vanillabackport.core.util.codec.AdditionalCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;

public record SulfurCubeArchetype(
    TagKey<Item> items,
    List<AttributeEntry> attributeModifiers,
    boolean buoyant,
    Optional<ExplosionData> explosion,
    Optional<ContactDamage> contactDamage,
    KnockbackModifiers knockbackModifiers,
    SoundSettings soundSettings
) {
    public static final Codec<SulfurCubeArchetype> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.hashedCodec(Registries.ITEM).fieldOf("items").forGetter(SulfurCubeArchetype::items),
        AttributeEntry.CODEC.listOf().fieldOf("attribute_modifiers").forGetter(SulfurCubeArchetype::attributeModifiers),
        Codec.BOOL.fieldOf("buoyant").forGetter(SulfurCubeArchetype::buoyant),
        ExplosionData.CODEC.optionalFieldOf("explosion").forGetter(SulfurCubeArchetype::explosion),
        ContactDamage.CODEC.optionalFieldOf("contact_damage").forGetter(SulfurCubeArchetype::contactDamage),
        KnockbackModifiers.CODEC.fieldOf("knockback_modifiers").forGetter(SulfurCubeArchetype::knockbackModifiers),
            SoundSettings.CODEC.fieldOf("sound_settings").forGetter(SulfurCubeArchetype::soundSettings)
    ).apply(instance, SulfurCubeArchetype::new));

    public static final KnockbackModifiers DEFAULT_KNOCKBACK_MODIFIERS = new KnockbackModifiers(0.33F, 0.06F);
    public static final SoundSettings DEFAULT_SOUND_SETTINGS = new SoundSettings(ModSoundEvents.SULFUR_CUBE_REGULAR_HIT, ModSoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.2F, 0.5F);

    public record AttributeEntry(Holder<Attribute> attribute, AttributeModifier modifier) {
        public static final Codec<AttributeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AttributeCodecs.ATTRIBUTE_CODEC.fieldOf("attribute").forGetter(AttributeEntry::attribute),
            AttributeCodecs.ATTRIBUTE_MODIFIER_CODEC.fieldOf("modifier").forGetter(AttributeEntry::modifier)
        ).apply(instance, AttributeEntry::new));

        public static AttributeEntry add(Holder<Attribute> attribute, double amount, RegistryKey<SulfurCubeArchetype> archetype) {
            return new AttributeEntry(
                attribute,
                new AttributeModifier(
                    archetype.location().getNamespace() + ":" + archetype.location().getPath() + "_add_" + attribute.value().getDescriptionId(),
                    amount,
                    AttributeModifier.Operation.ADDITION
                )
            );
        }

        public static AttributeEntry multiply(Holder<Attribute> attribute, double amount, RegistryKey<SulfurCubeArchetype> archetype) {
            return new AttributeEntry(
                attribute,
                new AttributeModifier(
                    archetype.location().getNamespace() + ":" + archetype.location().getPath() + "_mul_" + attribute.value().getDescriptionId(),
                    amount - 1.0,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
                )
            );
        }
    }

    public record ContactDamage(ResourceKey<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
        public static final Codec<ContactDamage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage_type").forGetter(ContactDamage::damageType),
            AdditionalCodecs.floatProvider(0.0F).fieldOf("amount").forGetter(ContactDamage::amount),
            Codec.BOOL.fieldOf("attribute_to_source").forGetter(ContactDamage::attributeToSource)
        ).apply(instance, ContactDamage::new));
    }

    public record ExplosionData(int power, boolean causesFire, int fuse) {
        public static final Codec<ExplosionData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("power").forGetter(ExplosionData::power),
            Codec.BOOL.fieldOf("causes_fire").forGetter(ExplosionData::causesFire),
            ExtraCodecs.POSITIVE_INT.fieldOf("fuse").forGetter(ExplosionData::fuse)
        ).apply(instance, ExplosionData::new));
    }

    public record KnockbackModifiers(float horizontalPower, float verticalPower) {
        public static final Codec<KnockbackModifiers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("horizontal_power").forGetter(KnockbackModifiers::horizontalPower),
            Codec.FLOAT.fieldOf("vertical_power").forGetter(KnockbackModifiers::verticalPower)
        ).apply(instance, KnockbackModifiers::new));
    }

    public record SoundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float pushSoundImpulseThreshold, float pushSoundCooldown) {
        public static final Codec<SoundSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SoundEvent.CODEC.fieldOf("hit_sound").forGetter(SoundSettings::hitSound),
                SoundEvent.CODEC.fieldOf("push_sound").forGetter(SoundSettings::pushSound),
                Codec.FLOAT.fieldOf("push_sound_impulse_threshold").forGetter(SoundSettings::pushSoundImpulseThreshold),
                Codec.FLOAT.fieldOf("push_sound_cooldown").forGetter(SoundSettings::pushSoundCooldown)
        ).apply(instance, SoundSettings::new));
    }
}