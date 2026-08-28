package com.blackgear.vanillabackport.common.level.components;

import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record UseEffects(
    boolean canSprint,
    boolean interactVibrations,
    float speedMultiplier
) {
    public static final UseEffects DEFAULT = new UseEffects(false, true, 0.2F);
    public static final Codec<UseEffects> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("can_sprint", DEFAULT.canSprint).forGetter(UseEffects::canSprint),
        Codec.BOOL.optionalFieldOf("interact_vibrations", DEFAULT.interactVibrations).forGetter(UseEffects::interactVibrations),
        Codec.floatRange(0.0F, 1.0F).optionalFieldOf("speed_multiplier", DEFAULT.speedMultiplier).forGetter(UseEffects::speedMultiplier)
    ).apply(instance, UseEffects::new));
    public static final StreamCodec<ByteBuf, UseEffects> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, UseEffects::canSprint,
        ByteBufCodecs.BOOL, UseEffects::interactVibrations,
        ByteBufCodecs.FLOAT, UseEffects::speedMultiplier,
        UseEffects::new
    );
    
    public static UseEffects get(ItemStack stack) {
        Object component = stack.get(ModDataComponents.USE_EFFECTS.get());
        
        if (component == null) return DEFAULT;
        if (component instanceof UseEffects effects) return effects;
        
        try {
            Class<?> clazz = component.getClass();
            
            boolean canSprint = (boolean) clazz.getMethod("canSprint").invoke(component);
            boolean interactVibrations = (boolean) clazz.getMethod("interactVibrations").invoke(component);
            float speedMultiplier = (float) clazz.getMethod("speedMultiplier").invoke(component);
            
            return new UseEffects(canSprint, interactVibrations, speedMultiplier);
        } catch (Exception e) {
            return DEFAULT;
        }
    }
}