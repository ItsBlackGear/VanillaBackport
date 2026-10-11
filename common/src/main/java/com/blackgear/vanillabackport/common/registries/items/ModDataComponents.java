package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.level.components.*;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.EitherHolder;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final CoreRegistry<DataComponentType<?>> REGISTRIES = CoreRegistry.create(BuiltInRegistries.DATA_COMPONENT_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<DataComponentType<RegistryKey<ChickenVariant>>> CHICKEN_VARIANT = register("chicken/variant",
        builder ->
            builder.persistent(ResourceLocation.CODEC.xmap(RegistryKey::of, RegistryKey::location))
                .networkSynchronized(ResourceLocation.STREAM_CODEC.map(RegistryKey::of, RegistryKey::location)));
    
    public static final Supplier<DataComponentType<AttackRange>> ATTACK_RANGE = register("attack_range",
        builder -> builder.persistent(AttackRange.CODEC)
            .networkSynchronized(AttackRange.STREAM_CODEC)
            .cacheEncoding());
    
    public static final Supplier<DataComponentType<KineticWeapon>> KINETIC_WEAPON = register("kinetic_weapon",
        builder -> builder.persistent(KineticWeapon.CODEC)
            .networkSynchronized(KineticWeapon.STREAM_CODEC)
            .cacheEncoding());
    
    public static final Supplier<DataComponentType<PiercingWeapon>> PIERCING_WEAPON = register("piercing_weapon",
        builder -> builder.persistent(PiercingWeapon.CODEC)
            .networkSynchronized(PiercingWeapon.STREAM_CODEC)
            .cacheEncoding());
    
    public static final Supplier<DataComponentType<UseEffects>> USE_EFFECTS = register("use_effects",
        builder -> builder.persistent(UseEffects.CODEC)
            .networkSynchronized(UseEffects.STREAM_CODEC));
    
    public static final Supplier<DataComponentType<EitherHolder<DamageType>>> DAMAGE_TYPE = register("damage_type",
        builder -> builder.persistent(EitherHolder.codec(Registries.DAMAGE_TYPE, DamageType.CODEC))
            .networkSynchronized(EitherHolder.streamCodec(Registries.DAMAGE_TYPE, DamageType.STREAM_CODEC))
            .cacheEncoding());
    
    public static final Supplier<DataComponentType<Float>> MINIMUM_ATTACK_CHARGE = register("minimum_attack_range",
        builder -> builder.persistent(AdditionalCodecs.floatRange(0.0F, 1.0F))
            .networkSynchronized(ByteBufCodecs.FLOAT));
    
    public static final Supplier<DataComponentType<SwingAnimation>> SWING_ANIMATION = register("swing_animation",
        builder -> builder.persistent(SwingAnimation.CODEC)
            .networkSynchronized(SwingAnimation.STREAM_CODEC));
    
    public static <T> Supplier<DataComponentType<T>> register(String key, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return REGISTRIES.register(key, () -> operator.apply(DataComponentType.builder()).build());
    }
}