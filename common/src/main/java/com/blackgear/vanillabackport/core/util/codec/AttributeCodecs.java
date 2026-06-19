package com.blackgear.vanillabackport.core.util.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

import java.util.UUID;

public class AttributeCodecs {
    private static final Codec<Operation> OPERATION_CODEC = StringRepresentable.fromEnum(OperationParser::values).xmap(OperationParser::getOperation, operation -> {
        for (OperationParser parser : OperationParser.values()) {
            if (parser.getOperation() == operation) return parser;
        }

        throw new IllegalArgumentException("Unknown operation: " + operation);
    });

    private static final MapCodec<AttributeModifier> ATTRIBUTE_MODIFIER_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(AttributeModifier::getName),
        Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
        OPERATION_CODEC.fieldOf("operation").forGetter(AttributeModifier::getOperation)
    ).apply(instance, (name, amount, operation) -> new AttributeModifier(UUID.nameUUIDFromBytes(name.getBytes()), name, amount, operation)));
    public static final Codec<AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = ATTRIBUTE_MODIFIER_MAP_CODEC.codec();

    public static final Codec<Holder<Attribute>> ATTRIBUTE_CODEC = BuiltInRegistries.ATTRIBUTE.holderByNameCodec();

    private enum OperationParser implements StringRepresentable {
        ADDITION("add_value", Operation.ADDITION),
        MULTIPLY_BASE("add_multiplied_base", Operation.MULTIPLY_BASE),
        MULTIPLY_TOTAL("add_multiplied_total", Operation.MULTIPLY_TOTAL);

        private final String name;
        private final Operation operation;

        OperationParser(String name, Operation operation) {
            this.name = name;
            this.operation = operation;
        }

        public Operation getOperation() {
            return this.operation;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}