package com.blackgear.vanillabackport.common.level.components;

import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public record SwingAnimation(SwingAnimationType type, int duration) {
    public static final SwingAnimation DEFAULT = new SwingAnimation(SwingAnimationType.WHACK, 6);

    public static final Codec<SwingAnimation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SwingAnimationType.CODEC.optionalFieldOf("type", DEFAULT.type).forGetter(SwingAnimation::type),
        ExtraCodecs.POSITIVE_INT.optionalFieldOf("duration", DEFAULT.duration).forGetter(SwingAnimation::duration)
    ).apply(instance, SwingAnimation::new));

    public static final StreamCodec<ByteBuf, SwingAnimation> STREAM_CODEC = StreamCodec.composite(
        SwingAnimationType.STREAM_CODEC, SwingAnimation::type,
        ByteBufCodecs.VAR_INT, SwingAnimation::duration,
        SwingAnimation::new
    );
    
    public static SwingAnimation get(ItemStack stack) {
        Object component = stack.get(ModDataComponents.SWING_ANIMATION.get());
        
        if (component == null) return SwingAnimation.DEFAULT;
        if (component instanceof SwingAnimation animation) return animation;
        
        try {
            Class<?> clazz = component.getClass();
            int duration = (int) clazz.getMethod("duration").invoke(component);
            Object rawType = clazz.getMethod("type").invoke(component);
            SwingAnimationType mappedType = SwingAnimationType.WHACK;
            
            if (rawType != null) {
                String typeName = rawType.toString().toLowerCase();
                for (SwingAnimationType type : SwingAnimationType.values()) {
                    if (type.getSerializedName().equalsIgnoreCase(typeName) || type.name().equalsIgnoreCase(typeName)) {
                        mappedType = type;
                        break;
                    }
                }
            }
            
            return new SwingAnimation(mappedType, duration);
        } catch (Exception e) {
            return SwingAnimation.DEFAULT;
        }
    }
}