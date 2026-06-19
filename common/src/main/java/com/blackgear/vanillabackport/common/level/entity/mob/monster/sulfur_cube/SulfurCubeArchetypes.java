package com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCubeArchetype.AttributeEntry;
import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.common.registries.entities.ModDamageTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCubeArchetype.*;

public class SulfurCubeArchetypes {
    public static final BuiltInCoreRegistry<SulfurCubeArchetype> REGISTRIES = BuiltInCoreRegistry.create(ModRegistries.SULFUR_CUBE_ARCHETYPES.get(), VanillaBackport.NAMESPACE);

    public static final ResourceKey<SulfurCubeArchetype> REGULAR = register("regular",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR,
        archetype(1.0F, 0.5F, 0.3F, 0.1F),
        true,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_REGULAR_HIT, ModSoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.2F, 0.5F));
    public static final ResourceKey<SulfurCubeArchetype> BOUNCY = register("bouncy",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY,
        archetype(2.0F, 0.9F, 0.3F, 0.01F),
        true,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.105F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_BOUNCY_HIT, ModSoundEvents.SULFUR_CUBE_BOUNCY_PUSH, 0.3F, 0.7F));
    public static final ResourceKey<SulfurCubeArchetype> SLOW_BOUNCY = register("slow_bouncy",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY,
        archetype(-0.4F, 0.6F, 0.3F, 0.05F),
        false,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.24F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_SLOW_BOUNCY_HIT, ModSoundEvents.SULFUR_CUBE_SLOW_BOUNCY_PUSH, 0.05F, 0.5F));
    public static final ResourceKey<SulfurCubeArchetype> SLOW_FLAT = register("slow_flat",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT,
        archetype(-0.5F, 0.4F, 0.4F, 0.1F),
        false,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.105F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_SLOW_FLAT_HIT, ModSoundEvents.SULFUR_CUBE_SLOW_FLAT_PUSH, 0.03F, 0.9F));
    public static final ResourceKey<SulfurCubeArchetype> FAST_FLAT = register("fast_flat",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT,
        archetype(2.0F, 0.5F, 0.2F, 0.01F),
        false,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.9125F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_FAST_FLAT_HIT, ModSoundEvents.SULFUR_CUBE_FAST_FLAT_PUSH, 0.03F, 0.9F));
    public static final ResourceKey<SulfurCubeArchetype> LIGHT = register("light",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT,
        archetype(1.0F, 1.0F, 0.3F, 1.8F),
        true,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.18F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_LIGHT_HIT, ModSoundEvents.SULFUR_CUBE_LIGHT_PUSH, 0.2F, 0.7F));
    public static final ResourceKey<SulfurCubeArchetype> FAST_SLIDING = register("fast_sliding",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING,
        archetype(-0.5F, 0.1F, 0.05F, 0.01F),
        false,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.6625F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_FAST_SLIDING_HIT, ModSoundEvents.SULFUR_CUBE_FAST_SLIDING_PUSH, 0.05F, 1.0F));
    public static final ResourceKey<SulfurCubeArchetype> SLOW_SLIDING = register("slow_sliding",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING,
        archetype(-0.8F, 0.1F, 0.05F, 0.01F),
        false,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_SLOW_SLIDING_HIT, ModSoundEvents.SULFUR_CUBE_SLOW_SLIDING_PUSH, 0.02F, 1.0F));
    public static final ResourceKey<SulfurCubeArchetype> STICKY = register("sticky",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_STICKY,
        archetype(2.0F, 0.0F, 2.0F, 0.01F),
        false,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_STICKY_HIT, ModSoundEvents.SULFUR_CUBE_STICKY_PUSH, 0.05F, 0.5F));
    public static final ResourceKey<SulfurCubeArchetype> HIGH_RESISTANCE = register("high_resistance",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE,
        archetype(-0.7F, 0.2F, 1.0F, 0.01F),
        false,
        Optional.empty(),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_HIGH_RESISTANCE_HIT, ModSoundEvents.SULFUR_CUBE_HIGH_RESISTANCE_PUSH, 0.03F, 0.7F));
    public static final ResourceKey<SulfurCubeArchetype> EXPLOSIVE = register("explosive",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE,
        archetype(1.0F, 0.5F, 0.3F, 0.3F),
        true,
        Optional.of(new ExplosionData(3, false, 120)),
        Optional.empty(),
        knockbackHitScale(0.4125F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_EXPLOSIVE_HIT, ModSoundEvents.SULFUR_CUBE_EXPLOSIVE_PUSH, 0.1F,0.7F));
    public static final ResourceKey<SulfurCubeArchetype> HOT = register("hot",
        ModItemTags.SULFUR_CUBE_ARCHETYPE_HOT,
        archetype(1.0F, 0.5F, 0.3F, 0.1F),
        true,
        Optional.empty(),
        Optional.of(contactDamage(ModDamageTypes.SULFUR_CUBE_HOT, ConstantFloat.of(1.0F), false)),
        knockbackHitScale(0.4125F, 0.09F),
        soundSettings(ModSoundEvents.SULFUR_CUBE_HOT_HIT, ModSoundEvents.SULFUR_CUBE_HOT_PUSH, 0.2F,0.7F));

    private static Function<ResourceKey<SulfurCubeArchetype>, AttributeEntry> add(Holder<Attribute> attribute, double amount) {
        return key -> AttributeEntry.add(attribute, amount, key);
    }

    private static Function<ResourceKey<SulfurCubeArchetype>, AttributeEntry> multiply(Holder<Attribute> attribute, double amount) {
        return key -> AttributeEntry.multiply(attribute, amount, key);
    }

    private static List<Function<ResourceKey<SulfurCubeArchetype>, AttributeEntry>> archetype(
        float speed,
        float bounce,
        float friction,
        float drag
    ) {
        return List.of(
            add(holder(Attributes.KNOCKBACK_RESISTANCE), -speed),
            add(holder(ModAttributes.BOUNCINESS), bounce),
            multiply(holder(ModAttributes.FRICTION_MODIFIER), friction),
            multiply(holder(ModAttributes.AIR_DRAG_MODIFIER), drag)
        );
    }

    private static ContactDamage contactDamage(ResourceKey<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
        return new ContactDamage(damageType, amount, attributeToSource);
    }

    private static KnockbackModifiers knockbackHitScale(float horizontalPower, float verticalPower) {
        return new KnockbackModifiers(horizontalPower, verticalPower);
    }

    private static SoundSettings soundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float threshold, float cooldown) {
        return new SoundSettings(hitSound, pushSound, threshold, cooldown);
    }

    private static Holder<Attribute> holder(Attribute attribute) {
        return Holder.direct(attribute);
    }

    private static ResourceKey<SulfurCubeArchetype> register(
        String name,
        TagKey<Item> blocks,
        List<Function<ResourceKey<SulfurCubeArchetype>, AttributeEntry>> modifiers,
        boolean floats,
        Optional<ExplosionData> maxFuse,
        Optional<ContactDamage> contactDamage,
        KnockbackModifiers knockbackModifiers,
        SoundSettings soundSettings
    ) {
        return REGISTRIES.resource(
            name,
            key ->
            new SulfurCubeArchetype(
                blocks,
                modifiers.stream().map(archetype -> archetype.apply(key)).toList(),
                floats,
                maxFuse,
                contactDamage,
                knockbackModifiers,
                soundSettings
            )
        );
    }
}